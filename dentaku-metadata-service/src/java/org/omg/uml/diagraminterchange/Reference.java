package org.omg.uml.diagraminterchange;

/**
 * Reference object instance interface.
 */
public interface Reference extends org.omg.uml.diagraminterchange.DiagramElement {
    /**
     * Returns the value of attribute isIndividualPresentation.
     * @return Value of attribute isIndividualPresentation.
     */
    public boolean isIndividualPresentation();
    /**
     * Sets the value of isIndividualPresentation attribute. See {@link #isIndividualPresentation} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setIndividualPresentation(boolean newValue);
    /**
     * Returns the value of reference referenced.
     * @return Value of reference referenced.
     */
    public org.omg.uml.diagraminterchange.DiagramElement getReferenced();
    /**
     * Sets the value of reference referenced. See {@link #getReferenced} for 
     * description on the reference.
     * @param newValue New value to be set.
     */
    public void setReferenced(org.omg.uml.diagraminterchange.DiagramElement newValue);
}
