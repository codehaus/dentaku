package org.omg.uml.diagraminterchange;

/**
 * SemanticModelBridge object instance interface.
 */
public interface SemanticModelBridge extends javax.jmi.reflect.RefObject {
    /**
     * Returns the value of attribute presentation.
     * @return Value of attribute presentation.
     */
    public java.lang.String getPresentation();
    /**
     * Sets the value of presentation attribute. See {@link #getPresentation} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setPresentation(java.lang.String newValue);
    /**
     * Returns the value of reference graphElement.
     * @return Value of reference graphElement.
     */
    public org.omg.uml.diagraminterchange.GraphElement getGraphElement();
    /**
     * Sets the value of reference graphElement. See {@link #getGraphElement} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setGraphElement(org.omg.uml.diagraminterchange.GraphElement newValue);
    /**
     * Returns the value of reference diagram.
     * @return Value of reference diagram.
     */
    public org.omg.uml.diagraminterchange.Diagram getDiagram();
    /**
     * Sets the value of reference diagram. See {@link #getDiagram} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setDiagram(org.omg.uml.diagraminterchange.Diagram newValue);
}
