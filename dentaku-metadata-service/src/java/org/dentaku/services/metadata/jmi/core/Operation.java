package org.dentaku.services.metadata.jmi.core;

import org.omg.uml.foundation.core.Classifier;

/**
 * Defines Dentaku's extensions for <code>org.omg.uml.foundation.core.Operation</code> interface.
 * 
 * @see org.omg.uml.foundation.core.Operation
 */
public interface Operation extends org.omg.uml.foundation.core.Operation {

    public abstract String getTypedParameterList();

    /**
     * Builds a comma-separated parameter list (type and name of each parameter) of an operation.
     * 
     * @param o the operation
     * @return String the parameter list
     */
    public abstract String getSignature();

    public abstract Classifier getReturnType();

    public abstract String getVisibilityName();

    public abstract Classifier[] getExceptionTypes();

    public abstract Classifier[] getParameterTypes();

    public abstract String getReturnTypeName();

}
