/*
 * EJBConnectorBean.java
 * Copyright 2002-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dentaku.foundation.connector;

import org.codehaus.plexus.PlexusContainer;
import org.dentaku.foundation.event.AbstractEvent;
import org.dentaku.foundation.pipeline.Pipeline;
import org.dentaku.services.container.ContainerManager;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;

/**
 * @ejb:bean type="Stateful" name="EJBConnector" jndi-name="connector/bill2/EJBConnector"
 * @ejb:interface extends="javax.ejb.EJBObject, org.dentaku.foundation.connector.Connector"
 *                local-extends="javax.ejb.EJBLocalObject, org.dentaku.foundation.connector.Connector"
 * @ connector:permission unchecked="true"
 * @clover ///CLOVER:OFF
 */
public abstract class EJBConnectorBean implements Connector, SessionBean {
    protected Connector connector;
    private ContainerManager cm;

    /**
     * @ejb.create-method
     * @throws EJBException
     */
    public void ejbCreate() throws javax.ejb.CreateException {
        try {
            cm = ContainerManager.getContainerManager(this.getClass().getResource("ConnectorConfig.xml"));
            connector = (DirectConnector)cm.lookup(Connector.ROLE);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    public void ejbRemove() throws EJBException {
        try {
            cm.dispose();
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    public void fireEvent(AbstractEvent event) throws Exception {
        connector.fireEvent(event);
    }

    public Pipeline getPipeline() {
        return connector.getPipeline();
    }
}
