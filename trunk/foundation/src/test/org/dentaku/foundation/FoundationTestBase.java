/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.foundation;

import junit.framework.TestCase;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.embed.Embedder;
import org.dentaku.foundation.event.AbstractEvent;
import org.dentaku.foundation.output.OutputContext;
import org.dentaku.foundation.connector.ConnectorBase;
import org.dentaku.foundation.connector.DirectConnector;
import org.dentaku.foundation.connector.Connector;
import org.dentaku.services.container.ContainerManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public abstract class FoundationTestBase extends TestCase {
    /**
     * Default container test classloader.
     */
    private ClassLoader classLoader;
    /**
     * Default Container.
     */
    protected Embedder container;
    protected String configFile = "DefaultTestConfig.xml";

    protected void setUp() throws Exception {
        container = new Embedder();
        URL resource = getClass().getResource(configFile);
        container.setConfiguration(resource);
        container.start();
    }

    public void tearDown() throws Exception {
        if (container != null) {
            container.stop();
            container = null;
        }
    }

    public class DummyEvent extends AbstractEvent {
        public DummyEvent() {
        }

        public DummyEvent(OutputContext response) {
            super(response);
        }

        public boolean execute(Object o) throws Exception {
            return true;
        }
    }
}
