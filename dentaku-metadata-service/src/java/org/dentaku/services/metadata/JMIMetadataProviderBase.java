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

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import org.generama.ConfigurableDocletTagFactory;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;
import org.xdoclet.JavaSourceProvider;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class JMIMetadataProviderBase implements JMICapableMetadataProvider,
        org.codehaus.plexus.personality.plexus.lifecycle.phase.Startable,
        org.picocontainer.Startable {

    protected RepositoryReader reader;
    protected UmlPackage umlPackage;
    protected final ConfigurableDocletTagFactory docletTagFactory = new ConfigurableDocletTagFactory();
    protected JavaSourceProvider fileProvider = null;
    protected Map classes = new HashMap();
    protected Map qdoxJMIMap = new HashMap();

    // injected parameters

    protected JMIMetadataProviderBase() {
    }

    public JMIMetadataProviderBase(RepositoryReader reader) {
        this.reader = reader;
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
    }

    public JMIMetadataProviderBase(RepositoryReader reader, JavaSourceProvider fileProvider) {
        this(reader);
        this.fileProvider = fileProvider;
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
        return umlPackage;
    }

    public ConfigurableDocletTagFactory getDocletTagFactory() {
        return docletTagFactory;
    }

    public void start() {
        try {
            umlPackage = reader.getModel();
        } catch (RepositoryException e) {
            throw new RuntimeException("couldn't load JMI metadata", e);
        }

        if (fileProvider != null) {
            JavaDocBuilder builder = null;
            try {
                builder = new JavaDocBuilder(docletTagFactory);
                builder.setEncoding(fileProvider.getEncoding());
                Collection urls = fileProvider.getURLs();
                for (Iterator iterator = urls.iterator(); iterator.hasNext();) {
                    URL next = (URL) iterator.next();
                    builder.addSource(next);
                }
            } catch (Exception e) {
                throw new RuntimeException("couldn't load QDox metadata", e);
            }

            addClassesToJMI(builder);
        }
    }

    private void addClassesToJMI(JavaDocBuilder builder) {
        JavaClass[] classes = builder.getClasses();
        for (int i = 0; i < classes.length; i++) {
            JavaClass qdoxClass = classes[i];
            ClassifierImpl jmiClass = findClass(qdoxClass);
            handleFields(qdoxClass, jmiClass);
            handleMethods(qdoxClass, jmiClass);

            jmiClass.setVisibility(getVisibility(qdoxClass.getModifiers()));

            Type superClass = qdoxClass.getSuperClass();
            if (superClass != null) {
                ClassifierImpl superclass = findClass(superClass.getJavaClass());
                createGeneralization(superclass, jmiClass);
            }

            Type[] interfaces = qdoxClass.getImplements();
            for (int j = 0; j < interfaces.length; j++) {
                Type anInterface = interfaces[j];
                createGeneralization(findClass(anInterface.getJavaClass()), jmiClass);
            }

            createTags(qdoxClass, jmiClass);
            if (this.classes.containsKey(qdoxClass.getFullyQualifiedName())) {
                System.err.println("WARNING - Overwriting JMI metadata for "+ qdoxClass.getFullyQualifiedName()+" with metadata from Java sources!");
            }
            this.classes.put(qdoxClass.getFullyQualifiedName(), qdoxClass);
            qdoxJMIMap.put(qdoxClass, jmiClass);
        }
    }

    private void createGeneralization(ClassifierImpl superclass, ClassifierImpl cls) {
        Generalization g = umlPackage.getCore().getGeneralization().createGeneralization();
        g.setParent(superclass);
        g.setChild(cls);
    }

    private void createTags(AbstractJavaEntity aClass, ModelElement cls) {
        DocletTag[] tags = aClass.getTags();
        for (int j = 0; j < tags.length; j++) {
            DocletTag tag = tags[j];
            Utils.createTaggedValue(umlPackage.getCore(), cls, null, tag.getName(), tag.getValue());
        }
    }

    private void handleMethods(JavaClass aClass, ClassifierImpl cls) {
        JavaMethod[] methods = aClass.getMethods();
        for (int j = 0; j < methods.length; j++) {
            JavaMethod javaMethod = methods[j];
            Operation o = umlPackage.getCore().getOperation().createOperation();

            o.setName(javaMethod.getName());
            o.setVisibility(getVisibility(javaMethod.getModifiers()));

            JavaParameter[] params = javaMethod.getParameters();
            for (int k = 0; k < params.length; k++) {
                JavaParameter param = params[k];
                addParameterToOperation(o, param.getName(), param.getType(), ParameterDirectionKindEnum.PDK_IN);
            }
            addParameterToOperation(o, null, javaMethod.getReturns(), ParameterDirectionKindEnum.PDK_RETURN);

            createTags(javaMethod, o);

            cls.getOperations().add(o);
        }
    }

    private void addParameterToOperation(Operation o, String name, Type type, ParameterDirectionKindEnum direction) {
        Parameter p = umlPackage.getCore().getParameter().createParameter();
        if (name != null) {
            p.setName(name);
        }
        p.setType(findClass(type.getJavaClass()));
        p.setKind(direction);

        o.getParameter().add(p);
    }

    private void handleFields(JavaClass aClass, ClassifierImpl cls) {
        JavaField[] fields = aClass.getFields();
        for (int j = 0; j < fields.length; j++) {
            JavaField field = fields[j];

            Attribute attribute = umlPackage.getCore().getAttribute().createAttribute();

            attribute.setName(field.getName());
            attribute.setVisibility(getVisibility(field.getModifiers()));
            JavaClass javaClass = field.getType().getJavaClass();
            attribute.setType(findClass(javaClass));

            createTags(field, attribute);

            cls.getAttributes().add(attribute);
        }
    }

    private ClassifierImpl findClass(JavaClass javaClass) {
        return Utils.findUmlClass(umlPackage, javaClass.getPackage(), javaClass.getName(), true);
    }

    private VisibilityKind getVisibility(String[] modifiers) {

        for (int i = 0; i < modifiers.length; i++) {
            String modifier = modifiers[i];
            if (modifier.equals("public")) {
                // we found our token, do something with it
                return VisibilityKindEnum.VK_PUBLIC;
            } else if (modifier.equals("protected")) {
                return VisibilityKindEnum.VK_PROTECTED;
            } else if (modifier.equals("private")) {
                return VisibilityKindEnum.VK_PRIVATE;
            }
        }
        return VisibilityKindEnum.VK_PACKAGE;
    }

    public void stop() {
    }

    /**
     * This method is used to map to other known types
     * @param object
     * @return
     */
    public Classifier mapObjectToClassifier(Object object) {
        if (object instanceof AbstractJavaEntity) {
            return (Classifier)qdoxJMIMap.get(object);
        } else {
            throw new IllegalArgumentException("Unknown type to map");
        }
    }
}
