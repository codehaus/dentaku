/*
 * JMIMetadataProviderBase.java
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
package org.dentaku.services.metadata;

import com.thoughtworks.qdox.model.DocletTagFactory;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.modelmanagement.Model;
import org.xdoclet.ConfigurableDocletTagFactory;

public abstract class JMIMetadataProviderBase implements JMICapableMetadataProvider {
    public static boolean booted;

    protected RepositoryReader reader;
    protected UmlPackage model;
    final ConfigurableDocletTagFactory docletTagFactory = new ConfigurableDocletTagFactory();

    // injected parameters

    protected JMIMetadataProviderBase() {
    }

    public JMIMetadataProviderBase(RepositoryReader reader) {
        this.reader = reader;
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        try {
            getModel();
        } catch (RepositoryException e) {
            throw new RuntimeException("Couldn't parse UML", e);
        } 
    }

    public String getOriginalFileName(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return "";
        }
        String result = null;
        ModelElement modelElement = (ModelElement) object;
        if (object instanceof TaggedValue) {
            result = ((TaggedValue) object).getName();

            // sometimes the tag name is on the TagDefinition
            if ((result == null) && (((TaggedValue) object).getType() != null)) {
                result = ((TaggedValue) object).getType().getName();

                // sometimes it is the TagType
                if (result == null) {
                    result = ((TaggedValue) object).getType().getTagType();
                }
            }
        } else {
            result = modelElement.getName();
        }
        return result + ".java";
    }

    public String getOriginalPackageName(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return "";
        }
        ModelElement modelElement = (ModelElement) object;
        String packageName = "";
        for (ModelElement namespace = modelElement.getNamespace(); (namespace instanceof org.omg.uml.modelmanagement.UmlPackage) && !(namespace instanceof Model); namespace = namespace.getNamespace()) {
            packageName = "".equals(packageName) ? namespace.getName() : namespace.getName() + "." + packageName;
        }
        return packageName;
    }

    public UmlPackage getModel() throws RepositoryException {
        if (model == null) {
            if (model == null) {
                model = reader.getModel();
            }
        }
        return model;
    }

    public DocletTagFactory getDocletTagFactory() {
        return docletTagFactory;
    }

}
