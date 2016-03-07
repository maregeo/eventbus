package com.qc.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qc.eventbus.context.EventBusContext;
import com.qc.eventbus.spring.InvalidSubscriberException;
import com.qc.eventbus.spring.SubscriberException;

/**
 *
 *
 * @author Tino
 *
 */
public class EventBus implements EventBusContext {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());
    
    protected Map<Class, List<Subscriber>> subscribers = new HashMap<Class, List<Subscriber>>();
    protected AsyncPublishProvider asyncPublishProvider;

    public void init() {
        if (asyncPublishProvider == null) {
            asyncPublishProvider = new AsyncPublishProvider();
            asyncPublishProvider.init(this);
        }
    }

    public synchronized void addSubscriber(Subscriber target) {
        Method method = target.method;
        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 1) {
            throw new InvalidSubscriberException("[eventbus] subscriber only support one parameter ");
        }
        if (paramTypes.length == 0 && target.event == null) {
            throw new InvalidSubscriberException("[eventbus] subscriber should set event when has no event parameter");
        }

        Class key = (target.event != null) ? target.event : paramTypes[0];
        List<Subscriber> values = subscribers.get(key);
        if (values == null) {
            values = (List<Subscriber>) Collections.synchronizedList(new ArrayList<Subscriber>());
            subscribers.put(key, values);
        }
        values.add(target);
        _logger.info("[eventbus] add subscriber [" + target.method.toGenericString() + "]");
    }

    public void publish(Object event) {
        List<Subscriber> subscriberList = subscribers.get(event.getClass());
        if (subscriberList == null || subscriberList.isEmpty()) {
            return;
        }
        for (Subscriber subscriber : subscriberList) {
            doPublishing(subscriber, event);
        }
    }

    protected void doPublishing(Subscriber subscriber, Object event) {
        if (subscriber.isAsync) {
            _logger.debug("subscriber async " + subscriber.method.toString() + ",event " + event.toString());
            asyncPublishProvider.publish(subscriber, event);
            return;
        }

        try {
            _logger.debug("subscriber call " + subscriber.method.toString() + ",event " + event.toString());
            subscriber.invoke(event);
        } catch (Exception e) {
            throw new SubscriberException("Failed to invoke subscriber:" + "subscriber call " + subscriber.method.toString() + ",event " + event.toString(), e);
        }
    }

    public void destroy() {
        if (asyncPublishProvider != null) {
            asyncPublishProvider.destroy();
        }
        _logger.info("eventbus shutdown success!");
    }
}
