/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata;

import junit.framework.TestCase;
import org.codehaus.plexus.embed.Embedder;
import org.generama.MetadataProvider;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Classifier;

import java.util.Collection;
import java.util.Iterator;
import java.net.URL;

public class MetadataServiceTest extends TestCase {
    private Embedder e;
    private Collection metadata;

    protected void setUp() throws Exception {
        e = new Embedder();
        String className = getClass().getName();
        String name = className.substring(className.lastIndexOf(".") + 1) + ".xml";
        URL resource = getClass().getResource(name);
        e.setConfiguration(resource);
        e.start();
        MetadataProvider ms = (MetadataProvider)e.lookup(MetadataProvider.ROLE);
        metadata = ms.getMetadata();
    }

    public void testFindRoot() throws Exception {
        for (Iterator it = metadata.iterator(); it.hasNext();) {
            ModelElement elem = (ModelElement) it.next();
            if (elem instanceof Classifier && elem.getName() == "root") {
                return;
            }
            fail("root element not found");
        }
    }
}
