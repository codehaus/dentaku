/*
 * ModelElementImpl.java
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

import org.netbeans.mdr.handlers.InstanceHandler;
import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.modelmanagement.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

abstract public class ModelElementImpl extends InstanceHandler implements ModelElement {
    private static Map stereotypeCache = new HashMap();

    public ModelElementImpl(StorableObject storable) {
        super(storable);
    }

    public String getPackageName() {
        String packageName = "";

        for (ModelElement namespace = getNamespace(); (namespace instanceof org.omg.uml.modelmanagement.UmlPackage) && !(namespace instanceof Model); namespace = namespace.getNamespace()) {
            packageName = "".equals(packageName) ? namespace.getName() : namespace.getName() + "." + packageName;
        }

        return packageName;
    }

    public String getFullyQualifiedName() {
        String fullName = getName();

        if (isPrimitiveType(fullName)) {
            return fullName;
        }

        String packageName = getPackageName();
        fullName = "".equals(packageName) ? fullName : packageName + "." + fullName;

        return fullName;
    }

    private boolean isPrimitiveType(String name) {
        return ("void".equals(name) || "char".equals(name) || "byte".equals(name) || "short".equals(name) || "int".equals(name) || "long".equals(name) || "float".equals(name) || "double".equals(name) || "boolean".equals(name));
    }

    public String findTagValue(String tagName) {
        return findTagValue(getTaggedValue(), tagName);
    }

    /**
     * Searches a collection of tag values for one with a particular
     * name
     *
     * @param taggedValues of taggedValues
     * @param tagName      name of tag for which to search
     * @return value of tag, null if tag not found
     */
    public String findTagValue(Collection taggedValues, String tagName) {
        for (Iterator i = taggedValues.iterator(); i.hasNext();) {
            TaggedValueImpl taggedValue = (TaggedValueImpl) i.next();
            String tgvName = taggedValue.getFullyQualifiedName();
            if (tagName.equals(tgvName)) {
                Iterator it = taggedValue.getDataValue().iterator();
                if (it.hasNext()) {
                    return it.next().toString();
                }
                return null;
            }
        }
        return null;
    }

    public Collection getStereotypeNames() {
        Collection names = (Collection) stereotypeCache.get(this);
        if (names == null) {
            names = new ArrayList();
            Collection stereotypes = getStereotype();
            for (Iterator i = stereotypes.iterator(); i.hasNext();) {
                ModelElement stereotype = (ModelElement) i.next();
                names.add(stereotype.getName());
            }
            stereotypeCache.put(this, names);
        }
        return names;
    }

    public TaggedValue getTaggedValue(String name) {
        for (Iterator it = getTaggedValues().iterator(); it.hasNext();) {
            TaggedValue taggedValue = (TaggedValue) it.next();
            if (taggedValue.getName().equalsIgnoreCase(name)) {
                return taggedValue;
            }
        }
        return null;
    }

    public Collection getTaggedValues() {
        Collection taggedValues = getTaggedValue();
        Collection taggedValueProxies = new Vector();
        for (Iterator i = taggedValues.iterator(); i.hasNext();) {
            TaggedValue taggedValue = (TaggedValue) i.next();
            taggedValueProxies.add(taggedValue);
        }
        return taggedValueProxies;
    }



}
