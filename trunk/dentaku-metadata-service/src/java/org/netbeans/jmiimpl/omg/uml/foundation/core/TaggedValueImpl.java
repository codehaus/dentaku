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

import java.util.Iterator;

import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.core.TaggedValue;

abstract public class TaggedValueImpl extends ModelElementImpl implements TaggedValue {
    protected TaggedValueImpl(StorableObject storable) {
        super(storable);
    }
    public String getValue() {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = getReferenceValue().iterator(); it.hasNext();) {
            Object o = (Object) it.next();
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(o.toString());
        }
        for (Iterator i = getDataValue().iterator(); i.hasNext();) {
            Object v = i.next();
            if (v.toString().length() > 0) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(v.toString());
            }
        }
        return sb.toString();
    }
}
