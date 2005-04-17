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
package org.dentaku.services.metadata;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.modelmanagement.ModelImpl;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.modelmanagement.UmlPackage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    public static URL checkURL(URL check) {
        URL result = null;
        if (check != null) {
            try {
                InputStream is = check.openStream();
                is.close();
                result = check;
            } catch (IOException e) { }
        }
        return result;
    }

    public static URL checkURL(String check) {
        URL result = null;
        if (check != null) {
            try {
                result = checkURL(new File(check).toURL());
            } catch (MalformedURLException e) { }
        }
        return result;
    }

    public static ClassifierImpl findUmlClass(org.omg.uml.UmlPackage umlPackage, String pkgName, final String entityName, boolean create) {
        // set up our superclass package structure
        ModelImpl model = Utils.getModelRoot(umlPackage);
        UmlPackage newPackage = model.getChildPackage(pkgName, create);

        ClassifierImpl result = (ClassifierImpl) CollectionUtils.find(newPackage.getOwnedElement(), new Predicate() {
            public boolean evaluate(Object object) {
                return (object instanceof UmlClass && ((UmlClass) object).getName().equals(entityName));
            }
        });
        // create the entity
        if (result == null) {
            CorePackage core = umlPackage.getCore();
            result = (ClassifierImpl) core.getUmlClass().createUmlClass();
            result.setName(entityName);
            core.getANamespaceOwnedElement().add(newPackage, result);
        }
        return result;
    }

    public static UmlPackage findUmlPackage(org.omg.uml.UmlPackage umlPackage, String pkgName, boolean create) {
        ModelImpl model = (ModelImpl)Utils.getModelRoot(umlPackage);
        UmlPackage newPackage = model.getChildPackage(pkgName, create);
        return newPackage;
    }

    public static TaggedValue createTaggedValue(CorePackage core, ModelElement owner, final String key, String value) {
        TagDefinition tagdefType = core.getTagDefinition().createTagDefinition();
        tagdefType.setTagType("String");
        TaggedValue taggedValue = createTaggedValue(core, owner, tagdefType, value);
        taggedValue.setName(key);
        return taggedValue;
    }

    public static TaggedValue createTaggedValue(CorePackage core, ModelElement owner, TagDefinition tagdefType, String value) {
        TaggedValue taggedValue = core.getTaggedValue().createTaggedValue();
        taggedValue.setName(tagdefType.getName());
        taggedValue.getDataValue().add(value);

        taggedValue.setType(tagdefType);

        core.getAModelElementTaggedValue().add(owner, taggedValue);
        return taggedValue;
    }

    public static ModelImpl getModelRoot(org.omg.uml.UmlPackage model) {
        ModelImpl m = (ModelImpl) CollectionUtils.find(model.getCore().getNamespace().refAllOfType(), new Predicate() {
            public boolean evaluate(Object o) {
                if (((ModelElementImpl) o).getNamespace() == null) return true; else return false;
            }
        });
        return m;
    }
}
