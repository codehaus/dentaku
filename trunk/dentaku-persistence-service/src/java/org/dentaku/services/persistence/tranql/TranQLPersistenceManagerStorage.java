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
package org.dentaku.services.persistence.tranql;

import org.dentaku.services.persistence.AbstractPersistenceManagerStorage;
import org.dentaku.services.persistence.Entity;
import org.dentaku.services.persistence.PersistenceException;
import org.dentaku.services.persistence.TranQLPersistenceFactory;
import org.tranql.field.FieldAccessor;
import org.tranql.field.Row;
import org.tranql.ql.Query;
import org.tranql.ql.QueryBuilder;
import org.tranql.ql.QueryException;
import org.tranql.query.CommandTransform;
import org.tranql.query.ObjectResultHandler;
import org.tranql.query.QueryCommand;
import org.tranql.query.SchemaMapper;
import org.tranql.sql.SQLSchema;
import org.tranql.sql.jdbc.JDBCQueryCommand;
import org.tranql.sql.sql92.SQL92Schema;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TranQLPersistenceManagerStorage extends AbstractPersistenceManagerStorage {
    protected Schema schema;
    protected SQLSchema sqlSchema;
    private CommandTransform mapper;

    public void initialize() throws Exception {
        super.initialize();

        // set up SQL schema
        Context context = new InitialContext();
        sqlSchema = new SQL92Schema("local", (DataSource) context.lookup(TranQLPersistenceFactory.dataSourceName));

        // set up local schema
        schema = new Schema();
        for (Iterator it = factories.values().iterator(); it.hasNext();) {
            TranQLPersistenceFactory factory = (TranQLPersistenceFactory) it.next();
            schema.addEntity(factory.getEntity());
            sqlSchema.addTable(factory.getTable());
        }
        mapper = new SchemaMapper(sqlSchema);

    }

    public Object load(Class theClass, Serializable id) throws PersistenceException {
        TranQLPersistenceFactory tranQLPersistenceFactory = ((TranQLPersistenceFactory) factories.get(theClass.getName() + "Factory"));
        QueryBuilder qb = QueryBuilder.buildSelectById(tranQLPersistenceFactory.getEntity(), false);

//        Query query = new Query(new FieldTransform[]{new FieldAccessor(0, theClass)}, null);
//        query.addChild(new Select(false).addChild(theClass.)
        Object result;
        try {
            QueryCommand command = mapper.transform(new DentakuQueryCommand(qb.getQuery()));
            System.out.println("Query = " + ((JDBCQueryCommand) command).getSQLText());
            result = command.execute(new ObjectResultHandler(new FieldAccessor(0, theClass)), new Row(new Object[]{id}), null);
        } catch (QueryException e) {
            throw new PersistenceException(e);
        }
        return result;
    }

    public void saveOrUpdate(Entity object) throws PersistenceException {
    }

    public void delete(Entity object) throws PersistenceException {
    }

    public List find(String query, Object[] values, Class[] types) throws PersistenceException {
        return null;
    }

    public Collection filter(Collection c, String filter) throws PersistenceException {
        return null;
    }

    public void refresh(Object o) throws PersistenceException {
    }

    public void beginTrans(boolean write) {
    }

    public void endTrans() {
    }

    public void endTrans(boolean somethingUnknown) {
    }
}
