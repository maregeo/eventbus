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
	private static final long serialVersionUID = -2304039992227412811L;

	public InvalidSubscriberException(String msg) {
        super(msg);
    }
}
