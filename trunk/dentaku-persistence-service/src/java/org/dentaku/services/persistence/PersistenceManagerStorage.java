/*
 * PersistenceManagerStorage.java
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
import org.dentaku.services.persistence.tranql.ModelEntity;
import org.tranql.schema.Attribute;
import org.tranql.schema.Association;
import org.tranql.ejb.Relationship;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A PersistenceManagerStorage is the lookup component for multiple PersistenceEngines.  This is pluggable,
 * but little need to replace it is foreseen.  PersistenceEngines understand what classes they are
 * responsible for, and when they are added to the manager, they populate the ability for a client
 * to recover a factory for a given class.  The factory is responsible for the creation and read of objects,
 * but the manager is responsible for update and delete.
 */
public interface PersistenceManagerStorage {
    public static final String ROLE = PersistenceManagerStorage.class.getName();

    SessionProvider getSessionProvider() throws PersistenceException;
    PersistenceFactory getPersistenceFactory(String name) throws PersistenceException;
    Object load(Class theClass, Serializable id) throws PersistenceException;
    void saveOrUpdate(Entity object) throws PersistenceException;
    void delete(Entity object) throws PersistenceException;
    List find(String query, Object[] values, Class[] types) throws PersistenceException;
    Collection filter(Collection c, String filter) throws PersistenceException;
    void refresh(Object o) throws PersistenceException;
    void releaseSession() throws PersistenceException;
    void rollback();

    void beginTrans(boolean write);

    void endTrans();
    void endTrans(boolean somethingUnknown);

    ModelEntity createEntity(String name, Class clazz);
    Attribute createField(String s, Class aClass, boolean b);
    Association createRelation(Class r1, Class r2);
}
