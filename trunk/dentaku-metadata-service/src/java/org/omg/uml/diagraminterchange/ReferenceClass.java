package org.omg.uml.diagraminterchange;

/**
 * Reference class proxy interface.
 */
public interface ReferenceClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Reference createReference();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param isIndividualPresentation 
     * @return The created instance object.
     */
    public Reference createReference(boolean isVisible, boolean isIndividualPresentation);
}
