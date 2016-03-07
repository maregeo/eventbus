package com.qc.eventbus.spring;

import com.qc.eventbus.context.EventBusContext;

public class SpringEventBusContext {

    private static EventBusContext context;

    public static EventBusContext getContext() {
        return context;
    }

    public static void setContext(EventBusContext context) {
        SpringEventBusContext.context = context;
    }
}
