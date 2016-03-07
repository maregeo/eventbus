/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qc.eventbus.spring;

import org.springframework.beans.BeansException;

/**
 *
 * @author tino
 */
public class SubscriberException extends BeansException {
    public SubscriberException(String msg) {
        super(msg);
    }
    
    public SubscriberException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
