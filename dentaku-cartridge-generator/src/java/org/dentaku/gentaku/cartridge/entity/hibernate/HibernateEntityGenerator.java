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
package org.dentaku.gentaku.cartridge.entity.hibernate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.dentaku.gentaku.cartridge.GenerationException;
import org.dentaku.gentaku.cartridge.entity.EntityGenerator;
import org.dentaku.services.metadata.Utils;
import org.dom4j.Document;
import org.netbeans.jmiimpl.omg.uml.foundation.core.AssociationEndImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.*;
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
public class HibernateEntityGenerator extends EntityGenerator {
    public void touchupOutputDocument(Document outputDocument) throws GenerationException {
        outputDocument.addDocType("hibernate-mapping", "-//Hibernate/Hibernate Mapping DTD 3.0//EN", "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
    }

    protected Stereotype getClassifierStereotype(CorePackage core) {
        Stereotype classifierStereotype = (Stereotype) CollectionUtils.find(core.getStereotype().refAllOfType(), new Predicate() {
            public boolean evaluate(Object object) {
                return ((ModelElementImpl) object).getName().equals("HibernateEntity");
            }
        });
        return classifierStereotype;
    }

    protected UmlPackage getEntityPackage(org.omg.uml.UmlPackage umlPackage) {
        return Utils.findUmlPackage(umlPackage, "org.dentaku.gentaku.hibernate", false);
    }

    protected void setupCollectionEnd(CorePackage core, Attribute newAttr, UmlPackage entityPackage, ClassifierImpl endClass, AssociationEndImpl end, ClassifierImpl javaUtilList) {
        Utils.createTaggedValue(core, newAttr, findTagdef(entityPackage.getOwnedElement(), "collection.element-type"), endClass.getFullyQualifiedName());

        AssociationEndImpl otherEnd = end.getOtherEnd();
        if (!isCollection(otherEnd) && otherEnd.isNavigable()) {
            String name = otherEnd.getName();
            if (name == null) {
                name = otherEnd.getParticipant().getName();
                name = name.substring(0,1).toLowerCase() + name.substring(1);
            }
            Utils.createTaggedValue(core, newAttr, findTagdef(entityPackage.getOwnedElement(), "field.mapped-by"), name);
        }

        core.getATypedFeatureType().add(newAttr, javaUtilList);
    }

    protected void createFieldName(CorePackage core, Attribute newAttr, UmlPackage jdoPackage) {
        Utils.createTaggedValue(core, newAttr, findTagdef(jdoPackage.getOwnedElement(), "field.name"), "${parent.name}");
    }

    protected Collection setupClasses(CorePackage core, Stereotype classifierStereotype, UmlPackage entityPackage, ClassifierImpl odspEntity, org.omg.uml.UmlPackage umlPackage) throws GenerationException {
        Collection extendedElements = new LinkedList(core.getAStereotypeExtendedElement().getExtendedElement(classifierStereotype));

        // pass 1: set up the classes and do the fields while we are here
        for (Iterator elemIterator = extendedElements.iterator(); elemIterator.hasNext();) {
            ModelElementImpl modelElement = (ModelElementImpl) elemIterator.next();

            if (!(modelElement instanceof UmlClass)) {
                throw new GenerationException("modelElement must be a UmlClass (is a " + modelElement.getClass().getName() + ")");
            }

            ClassifierImpl classifier = (ClassifierImpl) modelElement;
            Utils.createTaggedValue(core, classifier, findTagdef(entityPackage.getOwnedElement(), "inheritance.strategy"), "new-table");
            Utils.createTaggedValue(core, classifier, findTagdef(entityPackage.getOwnedElement(), "discriminator.strategy"), "class-name");
            Utils.createTaggedValue(core, classifier, findTagdef(entityPackage.getOwnedElement(), "column.name"), "java_type");

            createGeneralization(core, odspEntity, classifier);

            // update our superclass structure to accurately reflect what we are generating
            String entityName = classifier.getName();
            classifier.setName(entityName + "Base");
            ClassifierImpl subclass = (ClassifierImpl) Utils.findUmlClass(umlPackage, ((ModelElementImpl) classifier.getNamespace()).getFullyQualifiedName(), entityName, true);
            subclass.getStereotype().add(classifierStereotype);
            Utils.createTaggedValue(core, subclass, findTagdef(entityPackage.getOwnedElement(), "class.name"), "${parent.name}");
            Utils.createTaggedValue(core, subclass, findTagdef(entityPackage.getOwnedElement(), "class.persistence-capable-superclass"), ((ModelElementImpl) classifier).getFullyQualifiedName());
            Utils.createTaggedValue(core, subclass, "gentaku.generate", "false");
            Utils.createTaggedValue(core, subclass, findTagdef(entityPackage.getOwnedElement(), "inheritance.strategy"), "superclass-table");

            // handle the fields
            Collection attributes = CollectionUtils.select(classifier.getFeature(), new InstanceofPredicate(Attribute.class));
            for (Iterator attIterator = attributes.iterator(); attIterator.hasNext();) {
                Attribute attribute = (Attribute) attIterator.next();
                TaggedValue taggedValue = Utils.createTaggedValue(core, attribute, findTagdef(entityPackage.getOwnedElement(), "field.name"), "${parent.name}");
                attribute.getTaggedValue().add(taggedValue);
            }

            // move the relations down to the subclass
            Collection links = new LinkedList(classifier.getAssociationLinks());
            for (Iterator moveIterator = links.iterator(); moveIterator.hasNext();) {
                AssociationEnd associationEnd = (AssociationEnd) moveIterator.next();
                associationEnd.setParticipant(subclass);
            }

        }
        return extendedElements;
    }

}
