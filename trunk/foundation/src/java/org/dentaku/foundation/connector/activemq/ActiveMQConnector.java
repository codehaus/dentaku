/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.dentaku.foundation.connector.activemq;

import org.dentaku.foundation.connector.Connector;
import org.dentaku.foundation.event.AbstractEvent;
import org.dentaku.foundation.pipeline.Pipeline;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Startable;

import javax.jms.ObjectMessage;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.DeliveryMode;

public class ActiveMQConnector extends ActiveMQConnectorSupport implements Connector, Startable {
    protected MessageProducer producer;
    public void fireEvent(AbstractEvent event) throws Exception {
        ObjectMessage message = session.createObjectMessage(event);
        producer.send(message);
    }

    public void start() throws Exception {
        Destination destination = getDestination();

        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
}

    public void stop() throws Exception {
    }
}
