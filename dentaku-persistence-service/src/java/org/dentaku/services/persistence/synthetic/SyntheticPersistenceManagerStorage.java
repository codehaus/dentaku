/*
 * SyntheticPersistenceManagerStorage.java
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
package org.dentaku.services.persistence.synthetic;

import org.dentaku.services.persistence.AbstractPersistenceManagerStorage;
import org.dentaku.services.persistence.Entity;
import org.dentaku.services.persistence.PersistenceException;
import org.dentaku.services.persistence.tranql.ModelEntity;
import org.tranql.schema.Attribute;
import org.tranql.schema.Association;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * This class returns fake data that can be used by components that are being unit
 * tested.
 *
 * Future directions for this are moving the collection generation out of the generated class and into
 * something that is generated at runtime from JMI-based information.
 */
public class SyntheticPersistenceManagerStorage extends AbstractPersistenceManagerStorage {

    public Object load(Class theClass, Serializable id) throws PersistenceException {
        long key = ((Long)id).longValue();
        try {
            if (key == 0) {
                Method m = theClass.getMethod("getTestClassZero", null);
                return m.invoke(null, null);
            } else if (key == 1) {
                Method m = theClass.getMethod("getTestClassOne", null);
                return m.invoke(null, null);
            } else {
                List l = getMany(theClass);
                return l.get(2);
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    private List getMany(Class theClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = theClass.getMethod("getTestClassMany", null);
        List l = (List)m.invoke(null, null);
        return l;
    }

    public void saveOrUpdate(Entity object) throws PersistenceException {
        // todo: what should we do about inserts?  the references are sometimes circular...
    }

    public void delete(Entity object) throws PersistenceException {
        // todo: what should we do about deletes?
    }

    public List find(String query, Object[] values, Class[] types) throws PersistenceException {
        String tok[] = query.split(" ");
        String className = null;
        for (int i = 0; i < tok.length; i++) {
            String s = tok[i];
            if (s.equals("class")) {
                className = tok[i+1];
                try {
                    return getMany(Class.forName(className));
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public Collection filter(Collection c, String filter) throws PersistenceException {
        // sufficient?
        return c;
    }

    public void refresh(Object o) throws PersistenceException {
        // do nothing
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
