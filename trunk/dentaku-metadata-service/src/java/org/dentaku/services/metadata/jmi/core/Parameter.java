package org.dentaku.services.metadata.jmi.core;

/**
 * Defines Dentaku's extensions for <code>org.omg.uml.foundation.core.Parameter</code> interface.
 * 
 * @see org.omg.uml.foundation.core.Parameter
 */
public interface Parameter extends org.omg.uml.foundation.core.Parameter {

    public abstract boolean isReturnParam();

}
