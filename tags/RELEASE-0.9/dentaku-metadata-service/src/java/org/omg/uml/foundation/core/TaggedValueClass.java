package org.omg.uml.foundation.core;

/**
 * TaggedValue class proxy interface.
 */
public interface TaggedValueClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public TaggedValue createTaggedValue();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param dataValue 
     * @return The created instance object.
     */
    public TaggedValue createTaggedValue(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, java.util.Collection dataValue);
}
