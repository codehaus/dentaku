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

import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.generama.MetadataProvider;
import org.generama.tests.SinkWriterMapper;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.xdoclet.JavaSourceProvider;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.io.Reader;
import java.io.StringReader;

public class GentakuTest extends TestCase {
    public static class TestJavaSourceProvider implements JavaSourceProvider {
        public Collection getURLs() {
            Reader one = new StringReader("" +
                            "public class One{}"
            );
            Reader two = new StringReader("" +
                            "public class Two{}"
            );
            Reader three = new StringReader("" +
                            "public class Three{}"
            );
            List result = new ArrayList();
            result.add(one);
            result.add(two);
            result.add(three);
            return result;
        }

        public String getEncoding() {
            return null;
        }
    }

    public void testGentaku() {
        Gentaku gt = new Gentaku(TestJavaSourceProvider.class, SinkWriterMapper.class);
        MutablePicoContainer pico = new DefaultPicoContainer();
        gt.composeContainer(pico, null);

        QDoxMetadataProvider mp = (QDoxMetadataProvider) pico.getComponentInstanceOfType(MetadataProvider.class);
        assertNotNull(mp);
    }
    
}