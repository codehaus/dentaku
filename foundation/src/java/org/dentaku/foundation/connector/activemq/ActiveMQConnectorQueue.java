/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.dentaku.foundation.connector.activemq;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Startable;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.dentaku.foundation.event.AbstractEvent;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class ActiveMQConnectorQueue extends ActiveMQConnectorSupport implements MessageListener, Startable, LogEnabled {
    public MessageConsumer consumer;
    private Logger logger;

    public void start() throws Exception {
        Destination destination = getDestination();

        consumer = session.createConsumer(destination);
        consumer.setMessageListener(this);
    }

    public void stop() throws Exception {
    }

    public void onMessage(Message message) {
        try {
            AbstractEvent event = (AbstractEvent) ((ObjectMessage) message).getObject();
            pipeline.execute(event);
        } catch (Exception e) {
            logger.error("error processing message", e);
        }
    }

    public void enableLogging(Logger logger) {
        this.logger = logger;
    }
}
