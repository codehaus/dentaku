/*
 * PersistenceServiceTestBase.java
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
package org.dentaku.services;

import junit.framework.TestCase;
import org.axiondb.jdbc.AxionDataSource;
import org.codehaus.plexus.embed.Embedder;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.Connection;

public abstract class PersistenceServiceTestBase extends TestCase {
    protected Embedder container;
    protected DataSource ds;

    protected void setUp() throws Exception {
        super.setUp();

        container = new Embedder();
        String className = getClass().getName();
        String name = className.substring(className.lastIndexOf(".") + 1) + ".xml";
        URL resource = getClass().getResource(name);
        container.setConfiguration(resource);
        container.start();

        ds = new AxionDataSource("jdbc:axiondb:testdb");
        Connection c = ds.getConnection();

//        container.lookupList(
//        c.createStatement().execute("create table "
   }

    protected void tearDown() throws Exception {
        Connection c = ds.getConnection();
        c.createStatement().execute("SHUTDOWN");
        container.stop();
        super.tearDown();
    }

}
