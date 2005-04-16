/*
 * ClassifierImpl.java
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
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.dentaku.services.metadata.nbmdr.MagicDrawRepositoryReader;
import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.Classifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

abstract public class ClassifierImpl extends ModelElementImpl implements Classifier {
    private final static String PRIMARY_KEY = "PrimaryKey";

    protected ClassifierImpl(StorableObject storable) {
        super(storable);
    }

    public Collection getAttributes() {
        return CollectionUtils.select(getFeature(), new InstanceofPredicate(Attribute.class));
    }

    public Collection getAssociationLinks() {
        return getCore().getAParticipantAssociation().getAssociation(this);
    }

    private CorePackage getCore() {
        return ((UmlPackage)repository().getExtent(MagicDrawRepositoryReader.MODEL_NAME)).getCore();
    }

    public Collection getOperations() {
        return CollectionUtils.select(getFeature(), new InstanceofPredicate(Operation.class));
    }

    public Collection getAbstractions() {
        // todo there's gotta be a better way to do this than iterating the list three times...
        Collection clientDependencies = getCore().getAClientClientDependency().getClientDependency(this);
        Collection collection = CollectionUtils.select(clientDependencies, new InstanceofPredicate(Abstraction.class));
        return CollectionUtils.collect(collection, new Transformer() {
            public Object transform(Object object) {
                return ((Abstraction) object).getSupplier().iterator().next();
            }
        });
    }

    /**
     * This is improved, but todo STILL BROKEN
     * @return
     */
    public GeneralizableElement getSuperclass() {
        Iterator i = getGeneralization().iterator();
        if (i.hasNext()) {
            GeneralizableElement parent = ((Generalization) i.next()).getParent();
            if (!parent.isAbstract()) {
                return parent;
            }
        }
        return null;
    }

    /**
     * Gets the primaryKeyAttribute attribute
     *
     * @param object Description of the Parameter
     * @return The primaryKeyAttribute value
     */
    public Attribute getPrimaryKeyAttribute() {
        ClassifierImpl current = this;
        while (current != null) {
            Collection attributes = current.getAttributes();
            for (Iterator i = attributes.iterator(); i.hasNext();) {
                AttributeImpl attribute = (AttributeImpl)i.next();
                if (attribute.getStereotypeNames().contains(PRIMARY_KEY)) {
                    return attribute;
                }
            }
            Iterator it = current.getGeneralization().iterator();
            if (it.hasNext()) {
                current = (ClassifierImpl)((Generalization)it.next()).getParent();
            } else {
                current = null;
            }
        }
        return null;
    }

    public Collection getTargetEnds() {
        Collection links = getCore().getAParticipantAssociation().getAssociation(this);
        ArrayList result = new ArrayList();
        for (Iterator it = links.iterator(); it.hasNext();) {
            Object end = it.next();
            if (end instanceof AssociationEnd) {
                Collection ends = ((AssociationEnd) end).getAssociation().getConnection();
                for (Iterator i = ends.iterator(); i.hasNext();) {
                    AssociationEnd ae = (AssociationEnd) i.next();
                    if (!end.equals(ae)) {
                        result.add(ae);
                    }
                }
            }
        }
        return result;
    }
    
    public Collection getAllAssocClassOfType(String AssocStereoName, String ClassSteroName) {
    	ArrayList result = new ArrayList();
		Collection classAssociations = this.getAssociationLinks();
		ArrayList classAssociationsList = new ArrayList(classAssociations);
        for (Iterator it = classAssociationsList.iterator(); it.hasNext();) {
            Object end = it.next();
            if (end instanceof AssociationEnd) {
            	// Find class of the entity we are building the action for
                UmlAssociation assocRefAssoc = ((AssociationEnd) end).getAssociation();
                Collection sterotypesForAssoc = assocRefAssoc.getStereotype();
        		ArrayList sterotypesForAssocList = new ArrayList(sterotypesForAssoc);
        		for (Iterator ie = sterotypesForAssocList.iterator(); ie.hasNext();) {
                    Stereotype stero = (Stereotype)ie.next();
                    if(stero.getName().equals(AssocStereoName)) {
                    	// now we know it is the right type get the other end
                    	AssociationEndImpl otherEnd = (AssociationEndImpl) ((AssociationEndImpl)end).getTarget();
                    	// 
                    	if(otherEnd.isClass()) {
                    		ClassifierImpl aClassifier = (ClassifierImpl)otherEnd.getParticipant();
                    		if(aClassifier.getStereotypeNames().contains(ClassSteroName))
                    			result.add(aClassifier);
                    	}
                    }
        		}
            }
        }
    	return result;
    }
}
