package org.dentaku.services.metadata.jmi.core;

import java.util.Collection;

import org.dentaku.services.metadata.validator.ValidatingVisitor;
import org.dentaku.services.metadata.validator.VisitorException;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * Defines Dentaku's extensions for <code>org.omg.uml.foundation.core.ModelElement</code>
 * interface.
 * 
 * @see org.omg.uml.foundation.core.ModelElement
 */
public interface ModelElement extends org.omg.uml.foundation.core.ModelElement {

    public abstract String getPackageName();

    public abstract String getFullyQualifiedName();

    public abstract String findTagValue(String tagName);

    public abstract Collection getStereotypeNames();

    /**
     * This returns all tags that have the same name. Can tags actually be defined with the same
     * name twice? This is JavaDoc style...
     * 
     * @param name Name of tag
     * @param canonical Use fully qualified tag name
     * @return Collection of TaggedValues that match
     */
    public abstract Collection getTaggedValuesForName(final String name, final boolean canonical);

    /**
     * @deprecated
     */
    public abstract Collection getTaggedValuesForName(final String name);

    /**
     * @deprecated
     */
    public abstract Collection getTaggedValues();

    /**
     * Returns the first tagged value for this name by calling <code>getTaggedValuesForName</code>,
     * ignoring others
     * 
     * @param name Name of tag
     * @param canonical
     * @return
     */
    public abstract TaggedValue getTaggedValue(String name, boolean canonical);

    public abstract TaggedValue getTaggedValue(String name);

    /**
     * @param taggedValues ignored
     * @deprecated
     */
    public abstract String findTagValue(Collection taggedValues, String tagName);

    public abstract void accept(ValidatingVisitor visitor, Object context) throws VisitorException;

    public abstract String toString();

}
