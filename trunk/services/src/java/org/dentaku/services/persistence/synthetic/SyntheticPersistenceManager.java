/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.synthetic;

import net.sf.hibernate.type.Type;
import org.dentaku.services.persistence.AbstractPersistenceManager;
import org.dentaku.services.persistence.ModelEntity;
import org.dentaku.services.persistence.PersistenceException;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * This class returns fake data that can be used by components that are being unit
 * tested.
 *
 * Future directions for this are moving the collection generation out of the generated class and into
 * something that is generated at runtime from JMI-based information.
 */
public class SyntheticPersistenceManager extends AbstractPersistenceManager {

    public Object load(Class theClass, Serializable id) throws PersistenceException {
        long key = ((Long)id).longValue() % 3;
        try {
            if (key == 0) {
                Method m = theClass.getMethod("getTestClassZero", null);
                return m.invoke(null, null);
            } else if (key == 1) {
                Method m = theClass.getMethod("getTestClassOne", null);
                return m.invoke(null, null);
            } else {
                Method m = theClass.getMethod("getTestClassMany", null);
                List l = (List)m.invoke(null, null);
                return l.get(2);
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public void saveOrUpdate(ModelEntity object) throws PersistenceException {
        // todo: what should we do about inserts?  the references are sometimes circular...
    }

    public void delete(ModelEntity object) throws PersistenceException {
        // todo: what should we do about deletes?
    }

    public List find(String query, Object value, Type type) throws PersistenceException {
        return find(query, new Object[] {value}, new Type[] {type}); 
    }

    public List find(String query, Object[] values, Type[] types) throws PersistenceException {
        // todo: this is definitely insufficient
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection filter(Collection c, String filter) throws PersistenceException {
        // sufficient?
        return c;
    }

    public void refresh(Object o) throws PersistenceException {
        // do nothing
    }

}
