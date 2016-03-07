package com.qc.eventbus.spring;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.qc.eventbus.AsyncPublishProvider;
import com.qc.eventbus.Subscriber;

public class SpringAsyncPublishProvider extends AsyncPublishProvider {

    @Override
    public void publish(Subscriber subscriber, Object event) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            super.publish(subscriber, event);
        } else {
            TransactionSynchronizationManager.registerSynchronization(new EventBusTransactionSynchronization(pool,
                    subscriber, event));
        }
    }
}
