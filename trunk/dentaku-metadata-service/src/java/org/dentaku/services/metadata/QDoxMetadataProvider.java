/*
 * QDoxMetadataProvider.java
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

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.ClassLibrary;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaClassCache;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.ModelBuilder;
import com.thoughtworks.qdox.parser.structs.ClassDef;
import com.thoughtworks.qdox.parser.structs.FieldDef;
import com.thoughtworks.qdox.parser.structs.MethodDef;
import com.thoughtworks.qdox.parser.structs.TagDef;
import org.generama.GeneramaException;
import org.generama.QDoxCapableMetadataProvider;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.OperationImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QDoxMetadataProvider extends JMIMetadataProviderBase implements QDoxCapableMetadataProvider, JavaClassCache {
    private ClassLibrary classLibrary = new ClassLibrary(this);
    private Collection metadata;
    private Map classes;
    private Map qdoxJMIMap;

    public QDoxMetadataProvider() {
    }

    public QDoxMetadataProvider(RepositoryReader reader) {
        super(reader);
    }

    public Collection getMetadata() throws GeneramaException {
        setupMetadata();
        return classes.values();
    }

    public Collection getJMIMetadata() {
        setupMetadata();
        return metadata;
    }

    private void setupMetadata() {
        try {
            if (classes == null) {
                metadata = getModel().getCore().getModelElement().refAllOfType();
                classLibrary.addDefaultLoader();
                Collection c = getModel().getCore().getClassifier().refAllOfType();
                classes = new HashMap(c.size());
                qdoxJMIMap = new HashMap(c.size());
                for (Iterator ot = c.iterator(); ot.hasNext();) {
                    Classifier classifier = (Classifier) ot.next();
                    JavaClass qdoxClass = createJMIClass(classifier);
                    classes.put(classifier.getName(), qdoxClass);
                    qdoxJMIMap.put(qdoxClass, classifier);
                }
            }
        } catch (Exception e) {
            throw new GeneramaException("problem loading metadata", e);
        }
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

    private void addClass(JavaClass cls) {
        classes.put(cls.getFullyQualifiedName(), cls);
        cls.setJavaClassCache(this);
    }

    public JavaClass getClassByName(String name) {
        if (name == null) {
            return null;
        }
        JavaClass result = (JavaClass) classes.get(name);
        if (result == null) {
            // Try to make a binary class out of it
            result = null; //createBinaryClass(name);
            if (result != null) {
                addClass(result);
            } else {
                result = createUnknownClass(name);
            }
        }
        return result;
    }

    /**
     * Returns all the classes found in all the sources, including inner classes
     * and "extra" classes (multiple outer classes defined in the same source file).
     *
     * @return all the classes found in all the sources.
     * @since 1.3
     */
    public JavaClass[] getClasses() {
//        Set resultSet = new HashSet();
//        JavaSource[] javaSources = getSources();
//        for (int i = 0; i < javaSources.length; i++) {
//            JavaSource javaSource = javaSources[i];
//            addClassesRecursive(javaSource, resultSet);
//        }
        JavaClass[] result = (JavaClass[]) classes.values().toArray(new JavaClass[classes.size()]);
        return result;
    }


    private JavaClass createUnknownClass(String name) {
        ModelBuilder unknownBuilder = new ModelBuilder(classLibrary, docletTagFactory);
        ClassDef classDef = new ClassDef();
        classDef.name = name;
        unknownBuilder.beginClass(classDef);
        unknownBuilder.endClass();
        JavaSource unknownSource = unknownBuilder.getSource();
        JavaClass result = unknownSource.getClasses()[0];
        return result;
    }

    private JavaClass createJMIClass(Classifier classifier) {
        // Create a new builder and mimic the behaviour of the parser.
        // We're getting all the information we need via reflection instead.
        ModelBuilder binaryBuilder = new ModelBuilder(classLibrary, docletTagFactory);

        // Set the package name and class name
        String packageName = super.getOriginalPackageName(classifier);
        binaryBuilder.addPackage(packageName);

        ClassDef classDef = new ClassDef();
        classDef.name = classifier.getName();

        // Set the extended class and interfaces.
        Collection interfaces = ((ClassifierImpl)classifier).getAbstractions();
        if (classifier instanceof Interface) {
            // It's an interface
            classDef.isInterface = true;
            for (Iterator it = interfaces.iterator(); it.hasNext();) {
                Classifier anInterface = (Classifier) it.next();
                classDef.extendz.add(anInterface.getName());
            }
        } else {
            // It's a class
            for (Iterator it = interfaces.iterator(); it.hasNext();) {
                Classifier anInterface = (Classifier) it.next();
                classDef.implementz.add(anInterface.getName());
            }
            Classifier superclass = (Classifier) ((ClassifierImpl) classifier).getJavaGeneralization();
            if (superclass != null) {
                classDef.extendz.add(superclass.getName());
            }
        }

//        addModifiers(classDef.modifiers, clazz.getModifiers());

        // add tags
        addTags(classifier, binaryBuilder);

        binaryBuilder.beginClass(classDef);

        // add the methods
        Collection methods = ((ClassifierImpl) classifier).getOperations();
        for (Iterator it = methods.iterator(); it.hasNext();) {
            Operation pOperation = (Operation) it.next();
            // Ignore methods defined in superclasses
            if (((Operation) pOperation).getOwner() == classifier) {
                addMethodOrConstructor(pOperation, binaryBuilder);
            }
        }

        Collection fields = ((ClassifierImpl) classifier).getAttributes();
        for (Iterator it = fields.iterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            if (attribute.getOwner() == classifier) {
                addField(attribute, binaryBuilder);
            }
        }

        binaryBuilder.endClass();
        JavaSource binarySource = binaryBuilder.getSource();
        // There is always only one class in a "binary" source.
        JavaClass result = binarySource.getClasses()[0];

        result.setJavaClassCache(this);
        return result;

    }

    private void addStereotype(Stereotype pClassifier, ModelBuilder binaryBuilder) {
        for (Iterator it = pClassifier.getTaggedValue().iterator(); it.hasNext();) {
            TaggedValue taggedValue = (TaggedValue)it.next();
            binaryBuilder.addJavaDocTag(new TagDef(taggedValue.getName(), ((TaggedValueImpl)taggedValue).getValue()));
        }
    }

    private void addTags(ModelElement classifier, ModelBuilder binaryBuilder) {
        Collection comments = classifier.getComment();
        String comment = "";
        for (Iterator it = comments.iterator(); it.hasNext();) {
            comment += ((Comment) it.next()).getBody();
        }

        // this is an important call to make first, modifies state of the builder
        binaryBuilder.addJavaDoc(comment);

        // add stereotype tags first. allowing for overrides
        for (Iterator it = ((ModelElement) classifier).getStereotype().iterator(); it.hasNext();) {
            Stereotype stereotype = (Stereotype) it.next();
            addStereotype(stereotype, binaryBuilder);
        }

        for (Iterator it = ((ModelElementImpl) classifier).getTaggedValues().iterator(); it.hasNext();) {
            TaggedValueImpl taggedValue = (TaggedValueImpl) it.next();
            if (taggedValue.getName().startsWith("@")) {
                binaryBuilder.addJavaDocTag(new TagDef(taggedValue.getName().substring(1), taggedValue.getValue()));
            }
        }
    }

    private void addField(Attribute field, ModelBuilder binaryBuilder) {
        FieldDef fieldDef = new FieldDef();
        Classifier fieldType = field.getType();
        fieldDef.name = field.getName();
        fieldDef.type = fieldType.getName();
//        fieldDef.dimensions = getDimension(fieldType);
        // add tags
        addTags(field, binaryBuilder);
        binaryBuilder.addField(fieldDef);
    }

    private void addMethodOrConstructor(Operation operation, ModelBuilder binaryBuilder) {
        MethodDef methodDef = new MethodDef();
        // The name of constructors are qualified. Need to strip it.
        // This will work for regular methods too, since -1 + 1 = 0
        int lastDot = operation.getName().lastIndexOf('.');
        methodDef.name = operation.getName().substring(lastDot + 1);

//        addModifiers(methodDef.modifiers, operation.getModifiers());
        Classifier[] exceptions;
        Classifier[] parameterTypes;
        if (((OperationImpl)operation).getTaggedValue("Constructor") != null) {
            methodDef.constructor = true;

            exceptions = ((OperationImpl) operation).getExceptionTypes();
            parameterTypes = ((OperationImpl) operation).getParameterTypes();
        } else {
            methodDef.constructor = false;

            // For some stupid reason, these methods are not defined in Member,
            // but in both Method and Construcotr.
            exceptions = ((OperationImpl) operation).getExceptionTypes();
            parameterTypes = ((OperationImpl) operation).getParameterTypes();

            methodDef.returns = ((OperationImpl) operation).getReturnTypeName();
//            methodDef.dimensions = getDimension(returnType);

        }
        for (int j = 0; j < exceptions.length; j++) {
            Classifier exception = exceptions[j];
            methodDef.exceptions.add(exception.getName());
        }
        for (int j = 0; j < parameterTypes.length; j++) {
            FieldDef param = new FieldDef();
            Classifier parameterType = parameterTypes[j];
            param.name = "p" + j;
            param.type = parameterType.getName();
//            param.dimensions = getDimension(parameterType);
            methodDef.params.add(param);
        }
        // add tags
        addTags(operation, binaryBuilder);
        binaryBuilder.addMethod(methodDef);

    }

    public String getOriginalFileName(Object metadata) {
        String result;
        if (metadata instanceof JavaClass) {
            JavaClass javaClass = (JavaClass) metadata;
            result = javaClass.getName() + ".java";
        } else {
            result = super.getOriginalFileName(metadata);
        }
        return result;
    }

    public String getOriginalPackageName(Object metadata) {
        if (metadata instanceof JavaClass) {
            JavaClass javaClass = (JavaClass) metadata;
            return javaClass.getPackage();
        } else {
            return super.getOriginalPackageName(metadata);
        }
    }
}
