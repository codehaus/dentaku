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
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.dentaku.gentaku.cartridge.GenerationException;
import org.dentaku.gentaku.cartridge.GeneratorSupport;
import org.dentaku.gentaku.tools.cgen.visitor.LocalDefaultElement;
import org.dom4j.Branch;
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

public class EntityGenerator extends GeneratorSupport {

    // add the items that we implicitly generate for Entity objects
    public void preGenerate(LocalDefaultElement mappingNode, Branch parentOutput, ModelElement modelElement) throws GenerationException {
        if (!(modelElement instanceof UmlClass)) {
            throw new GenerationException("modelElement must be a UmlClass (is a " + modelElement.getClass().getName() + ")");
        }

        ClassifierImpl classifer = (ClassifierImpl) modelElement;
        CorePackage core = ((CorePackage) modelElement.refImmediatePackage());

        // get the model root (there can be only one...)
        ModelImpl m = (ModelImpl) CollectionUtils.find(core.getNamespace().refAllOfType(), new Predicate() {
            public boolean evaluate(Object o) {
                if (((ModelElementImpl) o).getNamespace() == null) return true; else return false;
            }
        });

        // set up the supertype
        UmlClass newClass = findUmlClass(m, core, "org.dentaku.services.persistence", "Entity");
        Generalization g = core.getGeneralization().createGeneralization();
        g.setChild(classifer);
        g.setParent(newClass);

        // handle the fields
        Collection attributes = CollectionUtils.select(classifer.getFeature(), new InstanceofPredicate(Attribute.class));
        for (Iterator attIterator = attributes.iterator(); attIterator.hasNext();) {
            Attribute attribute = (Attribute) attIterator.next();
            TaggedValue taggedValue = createTaggedValue(core, attribute, modelElement, "field.name", "${parent.name}");
            attribute.getTaggedValue().add(taggedValue);
        }

        UmlClass javaUtilList = findUmlClass(m, core, "java.util", "List");

        // handle the associations
        for (Iterator assIter = classifer.getTargetEnds().iterator(); assIter.hasNext();) {
            AssociationEnd end = (AssociationEnd) assIter.next();
            if (end.isNavigable()) {
                ClassifierImpl endClass = (ClassifierImpl) end.getParticipant();
                Attribute newAttr = core.getAttribute().createAttribute();
                if (end.getName() != null) {
                    newAttr.setName(end.getName());
                } else {
                    newAttr.setName(endClass.getName());
                }

                createTaggedValue(core, newAttr, modelElement, "field.name", "${parent.name}");
                if (end.getMultiplicity() != null) {
                    MultiplicityRange multiplicityRange = (MultiplicityRange) end.getMultiplicity().getRange().iterator().next();
                    int lower = multiplicityRange.getLower();
                    int upper = multiplicityRange.getUpper();
                    if (upper - lower > 1 || upper == -1) {
                        newAttr.setType(javaUtilList);
                        createTaggedValue(core, newAttr, modelElement, "collection.element-type", endClass.getFullyQualifiedName());
                        createTaggedValue(core, newAttr, modelElement, "join", "");
                    } else {
                        newAttr.setType(endClass);
                    }
                }
                newAttr.setOwner(classifer);
            }
        }
    }

    private UmlClass findUmlClass(ModelImpl m, CorePackage core, String pkgName, final String entityName) {
        // set up our superclass package structure
        UmlPackage newPackage = m.getChildPackage(pkgName, true);

        UmlClass result = (UmlClass)CollectionUtils.find(newPackage.getOwnedElement(), new AndPredicate(new EqualPredicate(entityName), new InstanceofPredicate(UmlClass.class)));
        // create the entity
        if (result == null) {
            result = core.getUmlClass().createUmlClass();
            result.setName(entityName);
            core.getANamespaceOwnedElement().add(newPackage, result);
        }
        return result;
    }

    private TaggedValue createTaggedValue(CorePackage core, ModelElement attribute, ModelElement umlClass, final String key, String value) {
        TaggedValue taggedValue = core.getTaggedValue().createTaggedValue();
        taggedValue.setModelElement(attribute);
        taggedValue.setName(key);
        Stereotype s = (Stereotype) umlClass.getStereotype().iterator().next();
        TagDefinition definedTag = (TagDefinition) CollectionUtils.find(s.getDefinedTag(), new Predicate() {
            public boolean evaluate(Object object) {
                return ((TagDefinition) object).getName().equals(key);
            }
        });
        taggedValue.getDataValue().add(value);
        taggedValue.setType(definedTag);
        return taggedValue;
    }
}
