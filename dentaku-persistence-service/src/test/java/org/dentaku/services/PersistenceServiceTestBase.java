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
import org.dentaku.services.persistence.TranQLPersistenceFactory;
import org.mockejb.jndi.MockContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;

public abstract class PersistenceServiceTestBase extends TestCase {
    protected Embedder container;
    protected AxionDataSource ds;
    private Context context;

    protected void setUp() throws Exception {
        super.setUp();

        container = new Embedder();
        String className = getClass().getName();
        String name = className.substring(className.lastIndexOf(".") + 1) + ".xml";
        URL resource = getClass().getResource(name);
        container.setConfiguration(resource);
        container.start();

        MockContextFactory.setAsInitial();
        // create the initial context that will be used for binding EJBs
        context = new InitialContext();
        // add to the context
        ds = new AxionDataSource("jdbc:axiondb:testdb");
        context.rebind(TranQLPersistenceFactory.dataSourceName, ds);

        Connection c = ds.getConnection();
        Statement s = c.createStatement();

        s.execute("CREATE TABLE Root(id long, intVal integer, stringVal varchar(50))");
        s.execute("CREATE TABLE Child(id long, intVal integer, stringVal varchar(50), rootFk long)");
        s.execute("INSERT INTO Root VALUES(1, 10, 'hello')");
        s.execute("INSERT INTO Child VALUES(1, 10, 'child1', 1)");
        s.execute("INSERT INTO Child VALUES(2, 20, 'child2', 1)");

        s.close();
        c.close();

        MockContextFactory.setAsInitial();
        // create the initial context that will be used for binding EJBs
        context = new InitialContext();
        // add to the context
        context.rebind(TranQLPersistenceFactory.dataSourceName, ds);
    }

    protected void tearDown() throws Exception {
        // this shutdown does not appear to do anything... if the database is opened again, the old tables are still there
        Connection c = ds.getConnection();
        c.createStatement().execute("SHUTDOWN");
        c.close();
        container.stop();
        super.tearDown();
    }
}
