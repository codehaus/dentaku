/*
 * PipelineTest.java
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
package org.dentaku.foundation;

import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.dentaku.foundation.connector.Connector;
import org.dentaku.foundation.connector.DirectConnector;
import org.dentaku.services.container.ContainerManager;
import org.dentaku.services.exception.DentakuException;

import java.util.List;

public class DirectConnectorTest extends FoundationTestBase {
    protected void setUp() throws Exception {

        super.setUp();
    }

    public void testConnectorSingleton() throws Exception {
        Connector component = DirectConnector.getInstance();
        // check for same instance
        assertEquals(component, DirectConnector.getInstance());
        assertEquals(component, DirectConnector.getInstance());
    }

    public void testValvesPopulated() throws Exception {
        Connector component = (Connector) container.lookup(Connector.ROLE);
        List l = component.getPipeline().getValves();
        assertFalse(l.isEmpty());
    }

    public void testPipeline() throws Exception {
        Connector component = (Connector) container.lookup(Connector.ROLE);
        DummyEvent event = new DummyEvent();
        component.fireEvent(event);
    }

    public void testContainerNotFound() throws Exception {
        try {
            tearDown();
            ContainerManager cm = ContainerManager.getContainerManager(this.getClass().getResource("EmptyConfig.xml"));
            Connector component = (Connector) cm.getContainer().lookup(Connector.ROLE);
            fail("Expected ComponentLookupException");
        } catch (ComponentLookupException e) { }
    }

//    public void testConnectorNotFound() throws Exception {
//        try {
//            tearDown();
//            DirectConnector.getContainerManager(DirectConnectorTest.class.getResourceAsStream("EmptyConfig.xml"));
//            DirectConnector.getInstance();
//            fail("Expected DentakuException");
//        } catch (DentakuException e) { }
//    }
}