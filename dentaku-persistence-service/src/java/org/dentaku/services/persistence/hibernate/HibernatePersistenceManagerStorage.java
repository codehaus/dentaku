/*
 * HibernatePersistenceManagerStorage.java
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
package org.dentaku.services.persistence.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;
import org.dentaku.services.persistence.Entity;
import org.dentaku.services.persistence.PersistenceException;
import org.dentaku.services.persistence.AbstractPersistenceManagerStorage;
import org.dentaku.services.persistence.tranql.ModelEntity;
import org.tranql.schema.Attribute;
import org.tranql.schema.Association;

import java.io.Serializable;
import java.util.List;
import java.util.Collection;

public class HibernatePersistenceManagerStorage extends AbstractPersistenceManagerStorage {
    public HibernatePersistenceManagerStorage() {

    }

    public void saveOrUpdate(Entity object) throws PersistenceException {
        try {
            if (object.getId() != null) {
                sessionProvider.getSession().saveOrUpdate(object);
            } else {
                Object pk = sessionProvider.getSession().save(object);
                object.setId(pk);
            }
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public void delete(Entity object) throws PersistenceException {
        try {
            sessionProvider.getSession().delete(object);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public List find(String query, Object[] values, Class[] types) throws PersistenceException {
        return null;
    }

    public Object load(Class theClass, Serializable id) throws PersistenceException {
        try {
            return sessionProvider.getSession().load(theClass, id);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public List find(String query, Object value, Type type) throws PersistenceException {
        try {
            return sessionProvider.getSession().find(query, value, type);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public List find(String query, Object[] values, Type[] types) throws PersistenceException {
        try {
            return sessionProvider.getSession().find(query, values, types);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public Collection filter(Collection c, String filter) throws PersistenceException {
        try {
            return sessionProvider.getSession().filter(c, filter);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public void refresh(Object o) throws PersistenceException {
        try {
            sessionProvider.getSession().refresh(o);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public void beginTrans(boolean write) {
    }

    public void endTrans() {
    }

    public void endTrans(boolean somethingUnknown) {
    }

    public ModelEntity createEntity(String name, Class clazz) {
        return null;
    }

    public Attribute createField(String s, Class aClass, boolean b) {
        return null;
    }

    public Association createRelation(Class r1, Class r2) {
        return null;
    }
}