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
import org.dentaku.gentaku.tools.cgen.visitor.LocalDefaultElement;
import org.dom4j.Branch;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlClass;

import java.util.Collection;
import java.util.Iterator;

public class EntityGenerator extends GeneratorSupport {
    // add the items that we implicitly generate for Entity objects
    public void preGenerate(LocalDefaultElement mappingNode, Branch parentOutput, ModelElement modelElement) throws GenerationException {
        if (!(modelElement instanceof UmlClass)) {
            throw new GenerationException("modelElement must be a UmlClass (is a " + modelElement.getClass().getName() + ")");
        }

        ClassifierImpl classifer = (ClassifierImpl) modelElement;
        CorePackage core = ((UmlPackage) modelElement.refOutermostPackage()).getCore();

        // handle the fields
        Collection attributes = CollectionUtils.select(classifer.getFeature(), new InstanceofPredicate(Attribute.class));
        for (Iterator attIterator = attributes.iterator(); attIterator.hasNext();) {
            Attribute attribute = (Attribute) attIterator.next();
            TaggedValue taggedValue = createTaggedValue(core, attribute, modelElement, "field.name", "${parent.name}");
            attribute.getTaggedValue().add(taggedValue);
        }

        // handle the associations
        for (Iterator assIter = classifer.getTargetEnds().iterator(); assIter.hasNext();) {
            AssociationEnd end = (AssociationEnd) assIter.next();
            if (end.isNavigable()) {
                ClassifierImpl endClass = (ClassifierImpl) end.getParticipant();
                Attribute newAttr = core.getAttribute().createAttribute();
                newAttr.setName(endClass.getName());
                newAttr.setType(endClass);

                Collection taggedValue = newAttr.getTaggedValue();
                taggedValue.add(createTaggedValue(core, newAttr, modelElement, "field.name", "${parent.name}"));
                taggedValue.add(createTaggedValue(core, newAttr, modelElement, "collection.element-type", endClass.getFullyQualifiedName()));
                taggedValue.add(createTaggedValue(core, newAttr, modelElement, "join", ""));
                newAttr.setOwner(classifer);
            }
        }
    }

    private TaggedValue createTaggedValue(CorePackage core, Attribute attribute, ModelElement umlClass, final String key, String value) {
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
