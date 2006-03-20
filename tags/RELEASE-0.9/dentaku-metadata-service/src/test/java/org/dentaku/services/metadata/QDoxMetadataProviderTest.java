/*
 * QDoxMetadataProviderTest.java
 * Copyright 2004-2004 Bill2, Inc.
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
package org.dentaku.services.metadata;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.codehaus.plexus.embed.Embedder;
import org.generama.QDoxCapableMetadataProvider;

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.JavaClass;

public class QDoxMetadataProviderTest extends TestCase {
    private Embedder e;
    private Collection metadata;

    protected void setUp() throws Exception {
        e = new Embedder();
        String className = getClass().getName();
        String name = className.substring(className.lastIndexOf(".") + 1) + ".xml";
        URL resource = getClass().getResource(name);
        e.setConfiguration(resource);
        e.start();
        QDoxCapableMetadataProvider ms = (QDoxCapableMetadataProvider)e.lookup(QDoxCapableMetadataProvider.ROLE);
        metadata = ms.getMetadata();
    }

    public void testFindRoot() throws Exception {
        for (Iterator it = metadata.iterator(); it.hasNext();) {
            AbstractJavaEntity elem = (AbstractJavaEntity) it.next();
            if (elem instanceof JavaClass && elem.getName().equals("Root")) {
                return;
            }
        }
        fail("root element not found");
    }
}
