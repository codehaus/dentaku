package org.omg.uml.foundation.core;

/**
 * TagDefinition class proxy interface.
 */
public interface TagDefinitionClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public TagDefinition createTagDefinition();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param tagType 
     * @param multiplicity 
     * @return The created instance object.
     */
    public TagDefinition createTagDefinition(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, java.lang.String tagType, org.omg.uml.foundation.datatypes.Multiplicity multiplicity);
}
