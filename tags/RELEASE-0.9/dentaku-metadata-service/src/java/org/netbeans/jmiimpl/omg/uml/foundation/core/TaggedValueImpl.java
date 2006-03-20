/*
 * TaggedValueImpl.java
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
package org.netbeans.jmiimpl.omg.uml.foundation.core;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.VariableResolver;

import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.core.TaggedValue;

abstract public class TaggedValueImpl extends ModelElementImpl implements TaggedValue {
    // todo is this threadsafe???
    static ExpressionEvaluatorImpl ev = new ExpressionEvaluatorImpl();
    
    protected TaggedValueImpl(StorableObject storable) {
        super(storable);
    }

    /**
     * This is rather WRONG because we are just returning all the values, separated by spaces.  Not a particularly reasonable behavior....
     * @return
     */
    public String getValue() {
        StringBuffer sb = new StringBuffer();
        append(sb, getReferenceValue());
        append(sb, getDataValue());
        return sb.toString();
    }

    private void append(StringBuffer sb, Collection contents) {
        for (Iterator it = contents.iterator(); it.hasNext();) {
            Object o = it.next();
            try {
                VariableResolver vr = new VariableResolver() {
                    public Object resolveVariable(String string) {
                        Object result = null;
                        if (string.equals("model")) {
                            result = getModelElement().refOutermostPackage();
                        } else if (string.equals("parent")) {
                            result = getModelElement();
                        }
                        return result;
                    }
                };
                String result = (String)ev.evaluate(o.toString(), String.class, vr, null);
                if (result.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }
                    sb.append(result);
                }
            } catch (ELException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
