package org.omg.uml.diagraminterchange;

/**
 * GraphElement object instance interface.
 */
public interface GraphElement extends org.omg.uml.diagraminterchange.DiagramElement {
    /**
     * Returns the value of attribute position.
     * @return Value of attribute position.
     */
    public org.omg.uml.diagraminterchange.Point getPosition();
    /**
     * Sets the value of position attribute. See {@link #getPosition} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setPosition(org.omg.uml.diagraminterchange.Point newValue);
    /**
     * Returns the value of reference semanticModel.
     * @return Value of reference semanticModel.
     */
    public org.omg.uml.diagraminterchange.SemanticModelBridge getSemanticModel();
    /**
     * Sets the value of reference semanticModel. See {@link #getSemanticModel} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setSemanticModel(org.omg.uml.diagraminterchange.SemanticModelBridge newValue);
    /**
     * Returns the value of reference link.
     * @return Value of reference link.
     */
    public java.util.Collection getLink();
    /**
     * Returns the value of reference contained.
     * @return Value of reference contained.
     */
    public java.util.List getContained();
    /**
     * Returns the value of reference anchorage.
     * @return Value of reference anchorage.
     */
    public java.util.Collection getAnchorage();
}
