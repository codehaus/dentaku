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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.dentaku.services.metadata.validator.ValidatingVisitor;
import org.dentaku.services.metadata.validator.VisitorException;
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
        TaggedValue taggedValue = getTaggedValue(tagName, true);
        String result = null;
        if (taggedValue != null) {
            Collection dataValue = taggedValue.getDataValue();
            if (!dataValue.isEmpty()) {
                result = dataValue.iterator().next().toString();
            }
        }
        return result;
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

    /**
     * This returns all tags that have the same name.  Can tags actually be defined with the same name twice?  This is JavaDoc style...
     *
     * @param name      Name of tag
     * @param canonical Use fully qualified tag name
     * @return Collection of TaggedValues that match
     */
    public Collection getTaggedValuesForName(final String name, final boolean canonical) {
        return CollectionUtils.select(getTaggedValue(), new Predicate() {
            public boolean evaluate(Object o) {
                String thisName;
                if (canonical) {
                    thisName = ((TaggedValueImpl) o).getFullyQualifiedName();
                } else {
                    thisName = ((TaggedValueImpl) o).getName();
                }
                return thisName.equals(name);
            }
        });
    }

    /**
     * @deprecated
     */
    public Collection getTaggedValuesForName(final String name) {
        return getTaggedValuesForName(name, false);
    }

    /**
     * @deprecated
     */
    public Collection getTaggedValues() {
        return getTaggedValue();
    }

    /**
     * Returns the first tagged value for this name by calling <code>getTaggedValuesForName</code>, ignoring others
     *
     * @param name      Name of tag
     * @param canonical
     * @return
     */
    public TaggedValue getTaggedValue(String name, boolean canonical) {
        Collection tv = getTaggedValuesForName(name, canonical);
        if (!tv.isEmpty()) {
            return (TaggedValue) tv.iterator().next();
        } else {
            return null;
        }
    }

    public TaggedValue getTaggedValue(String name) {
        return getTaggedValue(name, false);
    }

    /**
     * @param taggedValues ignored
     * @deprecated
     */
    public String findTagValue(Collection taggedValues, String tagName) {
        return getTaggedValue(tagName, true).getDataValue().iterator().next().toString();
    }

    public void accept(ValidatingVisitor visitor, Object context) throws VisitorException {
        visitor.visit(this, context);
    }

    public String toString() {
        return getFullyQualifiedName();
    }
}
