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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.dentaku.services.metadata.RepositoryException;
import org.dentaku.services.metadata.nbmdr.MagicDrawRepositoryReader;
import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Operation;

abstract public class ClassifierImpl extends ModelElementImpl implements Classifier {
    private final static String PRIMARY_KEY = "PrimaryKey";

    protected ClassifierImpl(StorableObject storable) {
        super(storable);
    }

    /**
     * Gets the attributes attribute of the ClassifierProxy object
     *
     * @return The attributes value
     */
    public Collection getAttributes() {
        Collection features = new FilteredCollection(getFeature()) {
            protected boolean accept(Object object) {
                return object instanceof Attribute;
            }
        };
        return features;
    }

    /**
     * Gets the associationLinks attribute of the ClassifierProxy object
     *
     * @return The associationLinks value
     */
    public Collection getAssociationLinks() {
        return getCore().getAParticipantAssociation().getAssociation(this);
    }

    private CorePackage getCore() {
        return ((UmlPackage)repository().getExtent(MagicDrawRepositoryReader.MODEL_NAME)).getCore();
    }

    /**
     * Gets the operations of the specified Classifier object.
     *
     * @param object Classifier object
     * @return Collection of org.omg.uml.foundation.core.Operation
     */
    public Collection getOperations() {
        Collection features = new FilteredCollection(getFeature()) {
            protected boolean accept(Object object) {
                return object instanceof Operation;
            }
        };
        return features;
    }

    /**
     * Returns the collection of interfaces implemented by the given
     * Classifier object.
     *
     * @param object Class
     * @return Collection of Interfaces
     */
    public Collection getAbstractions() {
        Collection clientDependencies = getCore().getAClientClientDependency().getClientDependency(this);
        return new FilteredCollection(clientDependencies) {
            public boolean add(Object object) {
                Abstraction abstraction = (Abstraction) object;
                return super.add(abstraction.getSupplier().iterator().next());
            }

            protected boolean accept(Object object) {
                return object instanceof Abstraction;
            }
        };
    }

    public Object getJavaGeneralization() {
        Iterator i = getGeneralization().iterator();
        if (i.hasNext()) {
            Generalization generalization = (Generalization) i.next();
            return generalization.getParent();
        }
        return null;
    }

    protected static abstract class FilteredCollection extends Vector {
        /**
         * Constructor for the FilterCollection object
         *
         * @param c Description of the Parameter
         */
        public FilteredCollection(Collection c) {
            for (Iterator i = c.iterator(); i.hasNext();) {
                Object object = i.next();
                if (accept(object)) {
                    add(object);
                }
            }
        }

        /**
         * Description of the Method
         *
         * @param object Description of the Parameter
         * @return Description of the Return Value
         */
        protected abstract boolean accept(Object object);
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
                    return (Attribute) attribute;
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

    public Collection getTargetEnds() throws RepositoryException {
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
}
