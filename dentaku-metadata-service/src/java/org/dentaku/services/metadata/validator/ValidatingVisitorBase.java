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
package org.dentaku.services.metadata.validator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.omg.uml.foundation.core.ModelElement;

public abstract class ValidatingVisitorBase implements ValidatingVisitor {
    final protected Map superclassCache = new HashMap();
    final protected Set methodFilter = new HashSet();

    protected ValidatingVisitorBase() {
        try {
            methodFilter.add(ValidatingVisitorBase.class.getMethod("visit", new Class[]{ModelElement.class, Object.class}));
        } catch (NoSuchMethodException e) {
            throw new InstantiationError("couldn't reflect our own visit method!");
        }
    }

    /**
     * Generic reflective visitor for subclasses
     *
     * @param element Element in the AST being visited
     * @param context Context for accumulating state and whatnot
     * @throws VisitorException
     * @todo need to cache the food chain for repeat calls
     */
    public void visit(ModelElement element, Object context) throws VisitorException {
        Class c = element.getClass();
        ListOrderedSet interfaces = getVisitMethodsForClass(c);
        for (Iterator it = interfaces.iterator(); it.hasNext();) {
            Method m = (Method) it.next();
            try {
                m.invoke(this, new Object[]{element, context});
            } catch (Exception e1) {
                throw new VisitorException(e1);
            }
        }
    }

    private ListOrderedSet getVisitMethodsForClass(Class c) {
        ListOrderedSet result = (ListOrderedSet) superclassCache.get(c);
        if (result == null) {
            result = new ListOrderedSet();
            superclassCache.put(c, result);
            Class current = c;
            while (current != null) {
                Class interfaces[] = current.getInterfaces();
                try {
                    Method method = getClass().getMethod("visit", new Class[]{current, Object.class});
                    if (!methodFilter.contains(method)) {
                        result.add(method);
                    }
                } catch (NoSuchMethodException e) {
                    // do nothing and loop
                }

                for (int i = 0; i < interfaces.length; i++) {
                    Class thisInterface = interfaces[i];
                    result.addAll(getVisitMethodsForClass(thisInterface));
                }

                current = current.getSuperclass();
            }
        }
        return result;
    }
}
