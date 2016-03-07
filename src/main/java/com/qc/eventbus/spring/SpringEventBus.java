package com.qc.eventbus.spring;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.qc.eventbus.EventBus;
import com.qc.eventbus.Subscriber;
import com.qc.eventbus.annotation.Subscribe;

public class SpringEventBus extends EventBus implements BeanPostProcessor, ApplicationContextAware {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext context;

    @Override
    public void init() {
        this.asyncPublishProvider = new SpringAsyncPublishProvider();
        this.asyncPublishProvider.init(this);
        super.init();
    }

    @Override
    public Object postProcessBeforeInitialization(Object obj, String s) throws BeansException {
        return obj;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }

        if (!context.containsBean(beanName) || !context.isSingleton(beanName)) {
            _logger.info("[eventbus] prototype bean is not supported!");
            return bean;
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe != null) {
                Subscriber target = new Subscriber();
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length > 1) {
                    throw new InvalidSubscriberException("[eventbus] subscriber only support one parameter");
                }
                if (paramTypes.length == 0) {
                    throw new InvalidSubscriberException("[eventbus] subscriber should set subject when has no event parameter");
                }
                target.event = paramTypes[0];
                target.object = bean;
                target.isAsync = subscribe.async();
                target.method = method;

                addSubscriber(target);
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationcontext) throws BeansException {
        context = applicationcontext;
    }
}
