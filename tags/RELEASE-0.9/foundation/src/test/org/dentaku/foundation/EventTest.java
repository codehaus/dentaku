/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.foundation;

import org.codehaus.plexus.PlexusContainer;
import org.dentaku.foundation.event.AbstractEvent;
import org.dentaku.foundation.output.StrutsOutputContext;

import java.io.InputStream;

public class EventTest extends FoundationTestBase {
    private PlexusContainer container;
    private InputStream configurationStream;
    private ClassLoader classLoader;

    public void testAbstractEvent() throws Exception {
        // create an event with an output context
        StrutsOutputContext response = new StrutsOutputContext();
        AbstractEvent event = new DummyEvent(response);

        // check that it's there
        assertEquals(event.getOutputContext(), response);

        // clear it, check that it's null
        event.setOutputContext(null);
        assertNull(event.getOutputContext());
    }

}
