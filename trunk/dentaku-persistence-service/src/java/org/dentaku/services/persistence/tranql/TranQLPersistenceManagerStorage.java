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

import org.dentaku.services.persistence.Entity;
import org.dentaku.services.persistence.PersistenceException;
import org.dentaku.services.persistence.TranQLPersistenceFactory;
import org.dentaku.services.persistence.AbstractPersistenceManagerStorage;
import org.tranql.field.FieldAccessor;
import org.tranql.field.FieldTransform;
import org.tranql.ql.Query;
import org.tranql.ql.QueryBuilder;
import org.tranql.ql.QueryException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class TranQLPersistenceManagerStorage extends AbstractPersistenceManagerStorage {
    protected org.tranql.schema.Schema schema;

    public void initialize() throws Exception {
        super.initialize();

    }

    public Object load(Class theClass, Serializable id) throws PersistenceException {
        QueryBuilder qb = QueryBuilder.buildSelectById(((TranQLPersistenceFactory)factories.get(theClass.getName())).getEntity(), false);
        Query query = new Query(new FieldTransform[]{new FieldAccessor(0, theClass)}, null);
//        query.addChild(new Select(false).addChild(theClass.)
        return doQuery(query);
    }

    private Object doQuery(Query query) throws PersistenceException {
        try {
            return schema.getCommandFactory().createQuery(query).execute(null,null,null);
        } catch (QueryException e) {
            throw new PersistenceException(e);
        }
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
