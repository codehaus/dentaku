package org.omg.uml.diagraminterchange;

/**
 * Diagram class proxy interface.
 */
public interface DiagramClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Diagram createDiagram();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param position 
     * @param size 
     * @param name 
     * @param zoom 
     * @param viewport 
     * @return The created instance object.
     */
    public Diagram createDiagram(boolean isVisible, org.omg.uml.diagraminterchange.Point position, org.omg.uml.diagraminterchange.Dimension size, java.lang.String name, double zoom, org.omg.uml.diagraminterchange.Point viewport);
}
