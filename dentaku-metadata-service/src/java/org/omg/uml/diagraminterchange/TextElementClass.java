package org.omg.uml.diagraminterchange;

/**
 * TextElement class proxy interface.
 */
public interface TextElementClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public TextElement createTextElement();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param text 
     * @return The created instance object.
     */
    public TextElement createTextElement(boolean isVisible, java.lang.String text);
}
