package org.dentaku.services.metadata.jmi.core;

import java.util.Collection;

import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.GeneralizableElement;

/**
 * Defines Dentaku's extensions for <code>org.omg.uml.foundation.core.Classifier</code> interface.
 * 
 * @see org.omg.uml.foundation.core.Classifier
 */
public interface Classifier extends org.omg.uml.foundation.core.Classifier {

    public abstract Collection getAttributes();

    public abstract Collection getAssociationLinks();

    public abstract Collection getOperations();

    public abstract Collection getAbstractions();

    /**
     * This is improved, but todo STILL BROKEN
     * 
     * @return
     */
    public abstract GeneralizableElement getSuperclass();

    /**
     * Gets the primaryKeyAttribute attribute
     * 
     * @param object Description of the Parameter
     * @return The primaryKeyAttribute value
     */
    public abstract Attribute getPrimaryKeyAttribute();

    public abstract Collection getTargetEnds();

    public abstract Collection getAllAssocClassOfType(String AssocStereoName, String ClassSteroName);

}
