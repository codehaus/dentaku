/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.dentaku.foundation.connector.activemq;

import org.codehaus.activemq.ActiveMQConnectionFactory;
import org.dentaku.foundation.connector.ConnectorBase;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Connection;
import javax.jms.Session;

public class ActiveMQConnectorSupport extends ConnectorBase {
    public Session session;

    protected Destination getDestination() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        Connection connection = connectionFactory.createConnection();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("DentakuConnectorDefault");
        return destination;
    }
}
