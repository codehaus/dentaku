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

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.NoOp;
import org.tranql.field.Row;
import org.tranql.ql.QueryBinding;
import org.tranql.ql.QueryException;
import org.tranql.query.ResultHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class InstantiatingObjectResultHandler implements ResultHandler {
    private static Map enhancedClasses = new HashMap();
    final QueryBinding[] resultTransform;
    private ClassLoader classLoader;

    public InstantiatingObjectResultHandler(QueryBinding[] resultTransform, ClassLoader classLoader) {
        this.resultTransform = resultTransform;
        this.classLoader = classLoader;
    }

    public Object fetched(Row row, Object arg) throws QueryException {
        Object o = null;
        Map results = new HashMap();
        for (int i = 0; i < resultTransform.length; i++) {
            QueryBinding queryBinding = resultTransform[i];
            Object value = row.get(i);
            o = getResultInstance(queryBinding, results);
            if (queryBinding.isAttributeBinding()) {
                setValue(queryBinding, o, value);
            } else if (queryBinding.isFKAttributeBinding()) {
                setProxy(queryBinding, o, value);
            } else {
                throw new QueryException("unknown binding");
            }
        }
        Collection values = results.values();
        if (values.size() > 1) {
            o = values.toArray(new Object[values.size()]);
        } else {
            o = values.iterator().next();
        }
        return o;
    }

    private Object getResultInstance(QueryBinding binding, Map results) throws QueryException {
        String name = binding.getEntity().getPhysicalName();
        Object result = results.get(name);
        if (result == null) {
            result = createEnhancedInstance(name);
            results.put(name, result);
        }
        return result;
    }

    private Object createEnhancedInstance(String name) throws QueryException {
        Object result;
        Class enhanced = (Class) enhancedClasses.get(name);
        try {
            if (enhanced == null) {
                Class superclass = Class.forName(name);
                Enhancer enhancer = new Enhancer();
                enhancer.setClassLoader(classLoader);
                enhancer.setSuperclass(superclass);
                enhancer.setCallbackFilter(new ReferenceCallbackFilter(superclass));
                enhancer.setCallbackTypes(new Class[]{NoOp.class, LazyLoader.class});
                enhancer.setUseFactory(false);
                enhanced = enhancer.createClass();
                enhancedClasses.put(name, enhanced);
            }
            result = enhanced.newInstance();
        } catch (Exception e) {
            throw new QueryException(e);
        }
        return result;
    }

    private void setProxy(QueryBinding queryBinding, Object o, Object value) throws QueryException {
        Object relation = createEnhancedInstance(queryBinding.getFKAttribute().getType().getName());
        setValue(queryBinding.getFKAttribute().getName(), relation, value);
        setValue(queryBinding, o, relation);
    }

    private void setValue(QueryBinding queryBinding, Object o, Object value) throws QueryException {
        setValue(queryBinding.getAttribute().getName(), o, value);
    }

    private void setValue(String name, Object o, Object value) throws QueryException {
        try {
            java.lang.reflect.Field f = o.getClass().getSuperclass().getSuperclass().getDeclaredField(name);
            boolean access = f.isAccessible();
            f.setAccessible(true);
            f.set(o, value);
            f.setAccessible(access);
        } catch (Exception e) {
            throw new QueryException(e);
        }
    }
}
