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
public class InvalidSubscriberException extends BeansException {
    public InvalidSubscriberException(String msg) {
        super(msg);
    }
}
