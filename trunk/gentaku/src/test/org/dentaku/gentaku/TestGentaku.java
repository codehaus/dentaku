/*
 * TestGentaku.java
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
package org.dentaku.gentaku;

import junit.framework.TestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.generama.MetadataProvider;
import org.generama.tests.SinkWriterMapper;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;

public class TestGentaku extends TestCase {
    public void testGentaku() {
        Gentaku gt = new Gentaku(SinkWriterMapper.class);
        MutablePicoContainer pico = new DefaultPicoContainer();
        gt.composeContainer(pico, null);

        JMIUMLMetadataProvider mp = (JMIUMLMetadataProvider) pico.getComponentInstanceOfType(MetadataProvider.class);
        assertNotNull(mp);
    }
}
