/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package example.web;

import servletunit.struts.MockStrutsTestCase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.mockejb.jndi.MockContextFactory;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.dentaku.services.container.DentakuPlexusContainer;
import org.dentaku.services.container.ContainerManager;

public class TestBase extends MockStrutsTestCase {
    /**
     * Configuration stream to use for default container test.
     */
    protected InputStream configurationStream;
    /**
     * Default container test classloader.
     */
    /**
     * Default Container.
     */
    protected DentakuPlexusContainer container;

    public void setUp() throws Exception {
        super.setUp();
//        setContextDirectory(new File("target/bsp-web"));
        // set up jndi
        MockContextFactory.setAsInitial();

        // set up plexus
        configurationStream = TestBase.class.getResourceAsStream("TestContainerConfiguration.xml");
        assertNotNull(configurationStream);
        container = new DentakuPlexusContainer();
        container.setConfigurationResource(new InputStreamReader(configurationStream));
        container.initialize();
        container.start();
        ContainerManager.getInstance().setup(container);

        // set up log4j
        ConsoleAppender consoleAppender = new ConsoleAppender(new SimpleLayout());
        consoleAppender.setWriter(new PrintWriter(System.out));
        Logger.getRoot().addAppender(consoleAppender);
        Logger.getRoot().setLevel(Level.ERROR);

//        // set up "login"
//        getMockRequest().setRemoteUser("hstaffjin");
//        getMockRequest().setUserRole("jc_staff");
//        SFRealm.setupUserContext(getRequest(), "hstaffjin");
    }
}