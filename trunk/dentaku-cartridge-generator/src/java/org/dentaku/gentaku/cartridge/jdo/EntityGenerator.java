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
package org.dentaku.gentaku.cartridge.jdo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.dentaku.gentaku.cartridge.GenerationException;
import org.dentaku.gentaku.cartridge.GeneratorSupport;
import org.netbeans.jmiimpl.omg.uml.foundation.core.AssociationEndImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.modelmanagement.ModelImpl;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.modelmanagement.UmlPackage;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class fills out stubs for the Generator interfaces needed for JDO generation.  The standard modeling strategy is that we
 * assume things about a setup that uses entities, for instance that we are going to have two different classes, one which is the
 * the generated base class and one that is the user implemented subclass with method implementations on it.  Among other things
 * that we do here, we patch up the model to give an accurate representation of what it actually looks like.  Therefore, we would
 * probably want to have this plugin listed (and run) before any other plugins that require accurate entity information.
 */
public class EntityGenerator extends GeneratorSupport {
    // add the items that we implicitly generate for Entity objects
    public void preProcessModel(ModelImpl model) throws GenerationException {
        CorePackage core = ((org.omg.uml.UmlPackage) model.refOutermostPackage()).getCore();
        Stereotype classifierStereotype = (Stereotype) CollectionUtils.find(core.getStereotype().refAllOfType(), new Predicate() {
            public boolean evaluate(Object object) {
                return ((ModelElementImpl) object).getName().equals("Entity");
            }
        });

        ClassifierImpl odspEntity = findUmlClass(core, model, "org.dentaku.services.persistence", "Entity", true);
        ClassifierImpl javaUtilList = findUmlClass(core, model, "java.util", "List", true);

        Collection extendedElements = new LinkedList(core.getAStereotypeExtendedElement().getExtendedElement(classifierStereotype));

        // pass 1: set up the classes and do the fields while we are here
        for (Iterator elemIterator = extendedElements.iterator(); elemIterator.hasNext();) {
            ModelElementImpl modelElement = (ModelElementImpl) elemIterator.next();

            if (!(modelElement instanceof UmlClass)) {
                throw new GenerationException("modelElement must be a UmlClass (is a " + modelElement.getClass().getName() + ")");
            }

            ClassifierImpl classifier = (ClassifierImpl) modelElement;

            createGeneralization(core, odspEntity, classifier);

            // update our superclass structure to accurately reflect what we are generating
            String entityName = classifier.getName();
            classifier.setName(entityName + "Base");
            ClassifierImpl subclass = (ClassifierImpl) findUmlClass(core, model, ((ModelElementImpl) classifier.getNamespace()).getFullyQualifiedName(), entityName, true);
            subclass.getStereotype().add(classifierStereotype);
            createTaggedValue(core, subclass, findStereotypeTagdef(classifierStereotype, "class.name"), "class.name", "${parent.name}");
            createTaggedValue(core, subclass, findStereotypeTagdef(classifierStereotype, "class.persistence-capable-superclass"), "class.persistence-capable-superclass", ((ModelElementImpl) classifier).getFullyQualifiedName());
            createTaggedValue(core, subclass, null, "gentaku.generate", "false");

            // handle the fields
            Collection attributes = CollectionUtils.select(classifier.getFeature(), new InstanceofPredicate(Attribute.class));
            for (Iterator attIterator = attributes.iterator(); attIterator.hasNext();) {
                Attribute attribute = (Attribute) attIterator.next();
                TaggedValue taggedValue = createTaggedValue(core, attribute, findStereotypeTagdef(classifierStereotype, "field.name"), "field.name", "${parent.name}");
                attribute.getTaggedValue().add(taggedValue);
            }

            // move the relations down to the subclass
            Collection links = new LinkedList(classifier.getAssociationLinks());
            for (Iterator moveIterator = links.iterator(); moveIterator.hasNext();) {
                AssociationEnd associationEnd = (AssociationEnd) moveIterator.next();
                associationEnd.setParticipant(subclass);
            }

        }

        // pass 2: do the relation fields
        for (Iterator elemIterator = extendedElements.iterator(); elemIterator.hasNext();) {
            ModelElementImpl modelElement = (ModelElementImpl) elemIterator.next();
            ClassifierImpl classifier = (ClassifierImpl) modelElement;
            String fullyQualifiedName = classifier.getFullyQualifiedName();
            String subclassName = fullyQualifiedName.substring(0, fullyQualifiedName.lastIndexOf("Base"));
            ClassifierImpl subclass = findUmlClass(core, model, subclassName, false);
            // handle the associations
            for (Iterator assIter = subclass.getTargetEnds().iterator(); assIter.hasNext();) {
                AssociationEndImpl end = (AssociationEndImpl) assIter.next();
                if (end.isNavigable()) {
                    ClassifierImpl endClass = (ClassifierImpl) end.getParticipant();
                    Attribute newAttr = core.getAttribute().createAttribute();
                    if (end.getName() != null) {
                        newAttr.setName(end.getName());
                    } else {
                        String name = endClass.getName();
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        newAttr.setName(name);
                    }

                    createTaggedValue(core, newAttr, findStereotypeTagdef(classifierStereotype, "field.name"), "field.name", "${parent.name}");
                    if (end.getMultiplicity() != null) {
                        MultiplicityRange multiplicityRange = (MultiplicityRange) end.getMultiplicity().getRange().iterator().next();
                        int lower = multiplicityRange.getLower();
                        int upper = multiplicityRange.getUpper();
                        if (upper - lower > 1 || upper == -1) {
                            createTaggedValue(core, newAttr, findStereotypeTagdef(classifierStereotype, "collection.element-type"), "collection.element-type", endClass.getFullyQualifiedName());
                            createTaggedValue(core, newAttr, findStereotypeTagdef(classifierStereotype, "join"), "join", "");
                            core.getATypedFeatureType().add(newAttr, javaUtilList);
                        } else {
                            core.getATypedFeatureType().add(newAttr, endClass);
                        }
                    } else {
                        // if no multiplicity, assume 1
                        System.out.println("warning - no multiplicty found: " + ((ClassifierImpl) end.getParticipant()).getFullyQualifiedName() + "<->" + ((ClassifierImpl) ((AssociationEndImpl) end).getOtherEnd().getParticipant()).getFullyQualifiedName());
                        core.getATypedFeatureType().add(newAttr, endClass);
                    }
                    core.getAOwnerFeature().add(classifier, newAttr);
                }
            }
        }
    }

    private void createGeneralization(CorePackage core, ClassifierImpl parent, ClassifierImpl child) {
        Generalization g = core.getGeneralization().createGeneralization();
        g.setChild(child);
        g.setParent(parent);
    }

    private ClassifierImpl findUmlClass(CorePackage core, ModelImpl m, String fqn, boolean create) {
        int indexOf = fqn.lastIndexOf(".");
        if (indexOf == -1) {
            return findUmlClass(core, m, "", fqn, true);
        } else {
            String entityName = fqn.substring(indexOf + 1);
            String pkgName = fqn.substring(0, indexOf);
            return findUmlClass(core, m, pkgName, entityName, create);
        }
    }

    private ClassifierImpl findUmlClass(CorePackage core, ModelImpl m, String pkgName, final String entityName, boolean create) {
        // set up our superclass package structure
        UmlPackage newPackage = m.getChildPackage(pkgName, create);

        ClassifierImpl result = (ClassifierImpl) CollectionUtils.find(newPackage.getOwnedElement(), new Predicate() {
            public boolean evaluate(Object object) {
                return (object instanceof UmlClass && ((UmlClass) object).getName().equals(entityName));
            }
        });
        // create the entity
        if (result == null) {
            result = (ClassifierImpl) core.getUmlClass().createUmlClass();
            result.setName(entityName);
            core.getANamespaceOwnedElement().add(newPackage, result);
        }
        return result;
    }

    private TaggedValue createTaggedValue(CorePackage core, ModelElement owner, TagDefinition tagdefType, final String key, String value) {
        TaggedValue taggedValue = core.getTaggedValue().createTaggedValue();
        taggedValue.setName(key);
        taggedValue.getDataValue().add(value);

        if (tagdefType == null) {
            tagdefType = core.getTagDefinition().createTagDefinition();
            tagdefType.setTagType("String");
        }
        taggedValue.setType(tagdefType);

        core.getAModelElementTaggedValue().add(owner, taggedValue);
        return taggedValue;
    }

    private TagDefinition findStereotypeTagdef(Stereotype stereotype, final String key) {
        TagDefinition definedTag = (TagDefinition) CollectionUtils.find(stereotype.getDefinedTag(), new Predicate() {
            public boolean evaluate(Object object) {
                return ((TagDefinition) object).getName().equals(key);
            }
        });
        return definedTag;
    }
}
