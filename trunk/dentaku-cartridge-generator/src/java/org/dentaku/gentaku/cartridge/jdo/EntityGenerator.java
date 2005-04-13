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
import org.dentaku.services.metadata.Utils;
import org.netbeans.jmiimpl.omg.uml.foundation.core.AssociationEndImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.modelmanagement.ModelImpl;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.modelmanagement.UmlPackage;
import org.dom4j.Document;

import java.util.ArrayList;
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
    public void touchupOutputDocument(Document outputDocument) throws GenerationException {
        outputDocument.addDocType("jdo", "-//Sun Microsystems, Inc.//DTD Java Data Objects Metadata 2.0//EN", "http://java.sun.com/dtd/jdo_2_0.dtd");
    }

    // add the items that we implicitly generate for Entity objects
    public void preProcessModel(ModelImpl model) throws GenerationException {
        final org.omg.uml.UmlPackage umlPackage = (org.omg.uml.UmlPackage)model.refOutermostPackage();
        CorePackage core = ((org.omg.uml.UmlPackage) umlPackage).getCore();
        Stereotype classifierStereotype = (Stereotype) CollectionUtils.find(core.getStereotype().refAllOfType(), new Predicate() {
            public boolean evaluate(Object object) {
                return ((ModelElementImpl) object).getName().equals("Entity");
            }
        });

        UmlPackage jdoPackage = Utils.findUmlPackage(umlPackage, "org.dentaku.gentaku.jdo", false);

        ClassifierImpl odspEntity = Utils.findUmlClass(umlPackage, "org.dentaku.services.persistence", "Entity", true);
        ClassifierImpl javaUtilList = Utils.findUmlClass(umlPackage, "java.util", "List", true);

        Collection extendedElements = new LinkedList(core.getAStereotypeExtendedElement().getExtendedElement(classifierStereotype));

        // pass 1: set up the classes and do the fields while we are here
        for (Iterator elemIterator = extendedElements.iterator(); elemIterator.hasNext();) {
            ModelElementImpl modelElement = (ModelElementImpl) elemIterator.next();

            if (!(modelElement instanceof UmlClass)) {
                throw new GenerationException("modelElement must be a UmlClass (is a " + modelElement.getClass().getName() + ")");
            }

            ClassifierImpl classifier = (ClassifierImpl) modelElement;
            Utils.createTaggedValue(core, classifier, findTagdef(jdoPackage.getOwnedElement(), "inheritance.strategy"), "no-table");

            createGeneralization(core, odspEntity, classifier);

            // update our superclass structure to accurately reflect what we are generating
            String entityName = classifier.getName();
            classifier.setName(entityName + "Base");
            ClassifierImpl subclass = (ClassifierImpl) Utils.findUmlClass(umlPackage, ((ModelElementImpl) classifier.getNamespace()).getFullyQualifiedName(), entityName, true);
            subclass.getStereotype().add(classifierStereotype);
            Utils.createTaggedValue(core, subclass, findTagdef(jdoPackage.getOwnedElement(), "class.name"), "${parent.name}");
            Utils.createTaggedValue(core, subclass, findTagdef(jdoPackage.getOwnedElement(), "class.persistence-capable-superclass"), ((ModelElementImpl) classifier).getFullyQualifiedName());
            Utils.createTaggedValue(core, subclass, "gentaku.generate", "false");

            // handle the fields
            Collection attributes = CollectionUtils.select(classifier.getFeature(), new InstanceofPredicate(Attribute.class));
            for (Iterator attIterator = attributes.iterator(); attIterator.hasNext();) {
                Attribute attribute = (Attribute) attIterator.next();
                TaggedValue taggedValue = Utils.createTaggedValue(core, attribute, findTagdef(jdoPackage.getOwnedElement(), "field.name"), "${parent.name}");
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
            ClassifierImpl subclass = findUmlClass(umlPackage, subclassName, false);
            // handle the associations
            for (Iterator assIter = subclass.getTargetEnds().iterator(); assIter.hasNext();) {
                AssociationEndImpl end = (AssociationEndImpl) assIter.next();
                if (end.isNavigable() && !excludedAssocs(end)) {
                    ClassifierImpl endClass = (ClassifierImpl) end.getParticipant();
                    Attribute newAttr = core.getAttribute().createAttribute();
                    if (end.getName() != null) {
                        newAttr.setName(end.getName());
                    } else {
                        String name = endClass.getName();
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        newAttr.setName(name);
                    }

                    Utils.createTaggedValue(core, newAttr, findTagdef(jdoPackage.getOwnedElement(), "field.name"), "${parent.name}");
                    if (end.getMultiplicity() != null) {
                        if (isCollection(end)) {
                            Utils.createTaggedValue(core, newAttr, findTagdef(jdoPackage.getOwnedElement(), "collection.element-type"), endClass.getFullyQualifiedName());

                            AssociationEndImpl otherEnd = end.getOtherEnd();
                            if (!isCollection(otherEnd) && otherEnd.isNavigable()) {
                                String name = otherEnd.getName();
                                if (name == null) {
                                    name = otherEnd.getParticipant().getName();
                                    name = name.substring(0,1).toLowerCase() + name.substring(1);
                                }
                                Utils.createTaggedValue(core, newAttr, findTagdef(jdoPackage.getOwnedElement(), "field.mapped-by"), name);
                            }

                            core.getATypedFeatureType().add(newAttr, javaUtilList);
                        } else {
                            core.getATypedFeatureType().add(newAttr, endClass);
                        }
                    } else {
                        // if no multiplicity, assume
                        System.out.println("warning - no multiplicty found: " + ((ClassifierImpl) end.getParticipant()).getFullyQualifiedName() + "<->" + ((ClassifierImpl) ((AssociationEndImpl) end).getOtherEnd().getParticipant()).getFullyQualifiedName());
                        core.getATypedFeatureType().add(newAttr, endClass);
                    }
                    core.getAOwnerFeature().add(classifier, newAttr);
                }
            }
        }
    }

    private boolean isCollection(AssociationEndImpl end) {
        MultiplicityRange multiplicityRange = (MultiplicityRange) end.getMultiplicity().getRange().iterator().next();
        int lower = multiplicityRange.getLower();
        int upper = multiplicityRange.getUpper();
        boolean result = false;
        if (upper - lower > 1 || upper == -1) {
            result = true;
        }
        return result;
    }

    /**
	 * @param end
	 * @return
	 */
	private boolean excludedAssocs(AssociationEndImpl end) {
        UmlAssociation anAssoc = ((AssociationEnd) end).getAssociation();
        Collection stereotypesForAssoc = anAssoc.getStereotype();
		ArrayList sterotypesForAssocList = new ArrayList(stereotypesForAssoc);
		for (Iterator ie = sterotypesForAssocList.iterator(); ie.hasNext();) {
            Stereotype stereo = (Stereotype)ie.next();
            if(stereo.getName().equals("View") ||
            		stereo.getName().equals("Validates") ||
					stereo.getName().equals("Imports")) {
            	return true;
            }
		}
		return false;
	}

	private void createGeneralization(CorePackage core, ClassifierImpl parent, ClassifierImpl child) {
        Generalization g = core.getGeneralization().createGeneralization();
        g.setChild(child);
        g.setParent(parent);
    }

    private ClassifierImpl findUmlClass(org.omg.uml.UmlPackage umlPackage, String fqn, boolean create) {
        int indexOf = fqn.lastIndexOf(".");
        if (indexOf == -1) {
            return Utils.findUmlClass(umlPackage, "", fqn, true);
        } else {
            String entityName = fqn.substring(indexOf + 1);
            String pkgName = fqn.substring(0, indexOf);
            return Utils.findUmlClass(umlPackage, pkgName, entityName, create);
        }
    }

    private TagDefinition findTagdef(Collection definedTag, final String key) {
        TagDefinition tag = (TagDefinition) CollectionUtils.find(definedTag, new Predicate() {
            public boolean evaluate(Object object) {
                return          object instanceof TagDefinition
                                && ((TagDefinition) object).getName() != null
                                && ((TagDefinition) object).getName().equals(key);
            }
        });
        return tag;
    }
}
