package org.dentaku.services.metadata.jmi.core;

/**
 * Defines Dentaku's extensions for <code>org.omg.uml.foundation.core.TaggedValue</code>
 * interface.
 * 
 * @see org.omg.uml.foundation.core.TaggedValue
 */
public interface TaggedValue extends org.omg.uml.foundation.core.TaggedValue {

    /**
     * This is rather WRONG because we are just returning all the values, separated by spaces. Not a
     * particularly reasonable behavior....
     * 
     * @return
     */
    public abstract String getValue();

}
