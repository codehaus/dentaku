/*
 * AbstractPersistenceManagerStorage.java
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

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.dentaku.services.persistence.hibernate.SessionProvider;
import org.dentaku.services.persistence.tranql.ModelEntity;
import org.dentaku.services.persistence.tranql.Field;
import org.tranql.schema.Attribute;
import org.tranql.schema.Association;
import org.tranql.ejb.Relationship;

import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

public abstract class AbstractPersistenceManagerStorage implements PersistenceManagerStorage, Initializable {
    protected SessionProvider sessionProvider = null;
    protected Map factories = null;

    public PersistenceFactory getPersistenceFactory(String name) throws PersistenceException {
        PersistenceFactory persistenceFactory = (PersistenceFactory) factories.get(name);
        return persistenceFactory;
    }

    public SessionProvider getSessionProvider() {
        return sessionProvider;
    }

    public void releaseSession() throws PersistenceException {
        sessionProvider.releaseSession();
    }

    public void rollback() {
        sessionProvider.rollback();
    }

    public void initialize() throws Exception {
        for (Iterator it = factories.values().iterator(); it.hasNext();) {
            PersistenceFactory factory = (PersistenceFactory) it.next();
            factory.setupEntities(this);
        }
        for (Iterator it = factories.values().iterator(); it.hasNext();) {
            PersistenceFactory factory = (PersistenceFactory) it.next();
            factory.setupRelations(this);
        }
    }

    public ModelEntity createEntity(String name, Class clazz) {
        return new ModelEntity(name, name);
    }

    public Attribute createField(String name, Class clazz, boolean pk) {
        return new Field(name, name, clazz, pk);
    }

    public Association createRelation(Class r1, Class r2) {
        org.tranql.schema.Entity entity1 = ((PersistenceFactory)factories.get(r1.getName()+"Factory")).getEntity();
        org.tranql.schema.Entity entity2 = ((PersistenceFactory)factories.get(r2.getName() + "Factory")).getEntity();

        return new Relationship(new Association.JoinDefinition(entity1, entity2, new LinkedHashMap()));
    }
}
