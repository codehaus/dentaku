/*
 * HibernatePersistenceManager.java
 * Copyright 2002-2004 Bill2, Inc.
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
import org.dentaku.services.persistence.ModelEntity;
import org.dentaku.services.persistence.PersistenceException;

import java.io.Serializable;
import java.util.List;
import java.util.Collection;

public class HibernatePersistenceManager extends AbstractPersistenceManager {
    public HibernatePersistenceManager() {

    }

    public void saveOrUpdate(ModelEntity object) throws PersistenceException {
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

    public void delete(ModelEntity object) throws PersistenceException {
        try {
            sessionProvider.getSession().delete(object);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
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
}
