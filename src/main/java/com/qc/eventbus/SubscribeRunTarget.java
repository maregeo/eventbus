package com.qc.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscribeRunTarget implements Runnable {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());
    
    Subscriber subscriber;
    Object event;

    public SubscribeRunTarget(Subscriber subscriber, Object event) {
        this.subscriber = subscriber;
        this.event = event;
    }

    @Override
    public void run() {
        try {
            subscriber.invoke(event);
        } catch (Exception e) {
            _logger.error("[eventbus] invoke subscriber fail ", e);
        }
    }
}
