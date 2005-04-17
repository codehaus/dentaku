/*
 *  MuleConnectorTest.java
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
package org.dentaku.foundation;

import org.mule.MuleServer;
import org.mule.config.ConfigurationBuilder;
import org.mule.config.ReaderResource;
import org.mule.config.builders.MuleXmlConfigurationBuilder;
import org.mule.extras.client.MuleClient;
import org.mule.umo.UMOMessage;

import java.io.Reader;
import java.io.StringReader;
import java.io.InputStreamReader;

public class MuleConnectorTest extends junit.framework.TestCase {
    public MuleServer server;

    protected void setUp() throws Exception {
        server = new MuleServer();
        System.setProperty("org.mule.xml.validate", "false");
        ConfigurationBuilder builder = new MuleXmlConfigurationBuilder();
        MuleServer.setConfigBuilder(builder);
        ReaderResource readerResource = new ReaderResource("whatever", new InputStreamReader(getClass().getResourceAsStream("MuleConnectorConfig.xml")));
        builder.configure(new ReaderResource[] { readerResource });
        server.start(false);
    }

    protected void tearDown() throws Exception {
    }

    public void testSomething() throws Exception {
        MuleClient client = new MuleClient();
        UMOMessage result = client.send("vm://localhost/test.queue",
                new FooWhich("string1", "string2"), null);

    }

    public class FooWhich {
        private String string1;
        private String string2;

        public FooWhich(String string1, String string2) {
            this.string1 = string1;
            this.string2 = string2;
        }

        public String toString() {
            return "[" + string1 + "," + string2 + "]";
        }
    }
}
