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
import org.dentaku.services.container.ContainerManager;
import org.dentaku.services.container.DentakuPlexusContainer;
import org.axiondb.jdbc.AxionDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

public abstract class PersistenceServiceTestBase extends TestCase {
    protected DentakuPlexusContainer container;
    protected DataSource ds;

    protected void setUp() throws Exception {
        super.setUp();
        ds = new AxionDataSource("jdbc:axiondb:testdb");

        ContainerManager instance = ContainerManager.getInstance();
        setupConfiguration(instance);
        instance.setup();
        container = instance.getContainer();
   }

    protected void tearDown() throws Exception {
        Connection c = ds.getConnection();
        c.createStatement().execute("SHUTDOWN");
        super.tearDown();

        container.dispose();
        container = null;
    }

    protected void setupConfiguration(ContainerManager instance) {
        String className = this.getClass().getName();
        String filename = className.substring(className.lastIndexOf(".") + 1) + ".xml";
        InputStream configurationStream = this.getClass().getResourceAsStream(filename);
        instance.add(new InputStreamReader(configurationStream));
    }
}
