/*
 * Entity.java
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

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import java.util.Collection;

import org.dentaku.services.persistence.hibernate.ThreadLocalSessionProvider;

public abstract class Entity {
    public abstract Object getId();
    public abstract void setId(Object newValue);

    public Collection refresh(Collection o) throws PersistenceException {
//        try {
//            Session sess = ThreadLocalSessionProvider.getThreadLocalSession();
//            sess.refresh(this);
//        } catch (HibernateException e) {
//            throw new PersistenceException(e);
//        }
        return o;
    }

    public Object initialize(Object o) throws PersistenceException {
        return o;
    }
}
