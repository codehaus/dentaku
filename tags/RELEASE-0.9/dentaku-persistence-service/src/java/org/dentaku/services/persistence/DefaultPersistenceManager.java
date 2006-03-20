/*
 * DefaultPersistenceManager.java
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
package org.dentaku.services.persistence;

import org.dentaku.services.persistence.hibernate.SessionProvider;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DefaultPersistenceManager extends AbstractPersistenceManager {
    private Map factories;

    public PersistenceFactory getPersistenceFactory(String name) {
        return (PersistenceFactory)factories.get(name);
    }

    public void saveOrUpdate(Entity object) throws PersistenceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void delete(Entity object) throws PersistenceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public SessionProvider getSessionProvider() {
        return null;
    }

    public Object load(Class theClass, Serializable id) throws PersistenceException {
        return null;
    }

    public List find(String query, Object[] values, Class[] types) throws PersistenceException {
        return null;
    }

    public Collection filter(Collection c, String filter) throws PersistenceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void releaseSession() throws PersistenceException {
    }

    public void rollback() {
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
