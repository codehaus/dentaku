/*
 * OperationImpl.java
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

import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

public abstract class OperationImpl extends ModelElementImpl implements Operation {
    protected OperationImpl(StorableObject storable) {
        super(storable);
    }

    public String getTypedParameterList() {
        StringBuffer sb = new StringBuffer();
        Iterator it = getParameter().iterator();
        boolean commaNeeded = false;
        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind())) {
                String type;
                if (p.getType() == null) {
                    type = "int";
                } else {
                    type = ((ModelElementImpl) p.getType()).getFullyQualifiedName();
                }
                if (commaNeeded) {
                    sb.append(", ");
                }
                sb.append(type);
                sb.append(" ");
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();

    }
    
    /**
     * Builds a comma-separated parameter list
     * (type and name of each parameter) of an operation.
     *
     * @param o the operation
     * @return String the parameter list
     */
    public String getSignature() {
        StringBuffer sb = new StringBuffer();
        Iterator it = getParameter().iterator();
        boolean commaNeeded = false;
        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind())) {
                String type;
                if (p.getType() == null) {
                    type = "int";
                } else {
                    type = ((ModelElementImpl) p.getType()).getFullyQualifiedName();
                }
                if (commaNeeded) {
                    sb.append(", ");
                }
                sb.append(type);
                sb.append(" ");
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    public Classifier getReturnType() {
        Collection parms = getParameter();
        for (Iterator i = parms.iterator(); i.hasNext();) {
            Parameter p = (Parameter) i.next();
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind())) {
                return p.getType();
            }
        }
        return null;
    }

    public String getVisibilityName() {
        VisibilityKind visibility = getVisibility();
        if (VisibilityKindEnum.VK_PRIVATE.equals(visibility)) {
            return "private";
        } else if (VisibilityKindEnum.VK_PROTECTED.equals(visibility)) {
            return "protected";
        } else if (VisibilityKindEnum.VK_PUBLIC.equals(visibility)) {
            return "public";
        }
        return "";

    }

    public Classifier[] getExceptionTypes() {
        return new Classifier[0];  //To change body of created methods use File | Settings | File Templates.
    }

    public Classifier[] getParameterTypes() {
        return new Classifier[0];  //To change body of created methods use File | Settings | File Templates.
    }

    public String getReturnTypeName() {
        Classifier returnType = getReturnType();
        return (returnType != null ? returnType.getName() : "void");
    }

}
