package org.omg.uml.diagraminterchange;

/**
 * Uml1SemanticModelBridge object instance interface.
 */
public interface Uml1SemanticModelBridge extends org.omg.uml.diagraminterchange.SemanticModelBridge {
    /**
     * Returns the value of reference element.
     * @return Value of reference element.
     */
    public org.omg.uml.foundation.core.Element getElement();
    /**
     * Sets the value of reference element. See {@link #getElement} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setElement(org.omg.uml.foundation.core.Element newValue);
}
