package com.qc.eventbus;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qc.eventbus.context.EventBusContext;

public class AsyncPublishProvider {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());
    
    protected int poolSize = 200;
    protected ThreadPoolExecutor pool;
    protected EventBusContext context;

    public void init(EventBusContext context) {
        this.context = context;
        pool = new ThreadPoolExecutor(poolSize, poolSize, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    public void destroy() {
        if (pool == null) {
            return;
        }
        pool.shutdown();
        int size = pool.getQueue().size();
        if (!pool.isTerminated()) {
            _logger.info("[eventbus] pool wait shutdown, thread quene size " + size);
        }
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                break;
            }
        }
        _logger.info("[eventbus] pool shutdown success!");
        pool = null;
    }

    public void publish(Subscriber subscriber, Object event) {
        pool.execute(new SubscribeRunTarget(subscriber, event));
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
