/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.foundation;

import org.dentaku.foundation.connector.EJBConnector;
import org.dentaku.foundation.connector.EJBConnectorHome;
import org.dentaku.foundation.connector.EJBConnectorSession;
import org.dentaku.foundation.connector.EJBConnectorUtil;
import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.jndi.MockContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;

public class EJBConnectorTest extends FoundationTestBase {
    // State of this test case. These variables are initialized by setUp method
    private Context context;

    public EJBConnectorTest() {
        configFile = "EJBTestConfig.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();

        /* We need to set MockContextFactory as our JNDI provider.
         * This method sets the necessary system properties.
         */
        MockContextFactory.setAsInitial();

        // create the initial context that will be used for binding EJBs
        context = new InitialContext( );

        // Create an instance of the MockContainer
        MockContainer mockContainer = new MockContainer( context );

        /* Create deployment descriptor of our sample bean.
         * MockEjb uses it instead of XML deployment descriptors
         */
        SessionBeanDescriptor sampleServiceDescriptor =
            new SessionBeanDescriptor( EJBConnectorHome.JNDI_NAME,
            EJBConnectorHome.class, EJBConnector.class, EJBConnectorSession.class );
        // Deploy operation creates Home and binds it to JNDI
        mockContainer.deploy( sampleServiceDescriptor );

    }

    public void testTest() throws Exception {
        EJBConnectorHome home = EJBConnectorUtil.getHome();

        // create the bean
        EJBConnector sampleService = home.create();
        // check the pipeline
        assertTrue(sampleService.getPipeline().getValves().size() > 0);
        // call the method
        sampleService.fireEvent(new DummyEvent());

        sampleService.remove();
    }
}
