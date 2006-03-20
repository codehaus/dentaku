/**
 *
 *  Copyright 2004 Brian Topping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dentaku.gentaku.tools.cgen;

import junit.framework.TestCase;
import org.dentaku.gentaku.tools.cgen.xmi.XMIGenTask;
import org.dentaku.gentaku.tools.cgen.xmi.Module;

import java.io.File;

public class XMIGenTest extends TestCase {
    public XMIGenTask gen;

    protected void setUp() throws Exception {
        super.setUp();
        gen = new XMIGenTask();
        String root = System.getProperty("dentaku.rootdir");
        Module m = new Module();
        m.setMapping(new File(root, "dentaku-cartridge-generator/src/java/org/dentaku/gentaku/cartridge/jdo/xml/mapping.xml").getPath());
        m.setSchema(new File(root, "dentaku-cartridge-generator/src/java/org/dentaku/gentaku/cartridge/jdo/xml/jdo_2_0.xsd").getPath());
        gen.setDestdir("/tmp");
        gen.addModule(m);
    }

    public void testXMIGen() throws Exception {
        gen.execute();
    }
}
