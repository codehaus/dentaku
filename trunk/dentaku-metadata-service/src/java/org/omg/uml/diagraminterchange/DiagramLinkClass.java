package org.omg.uml.diagraminterchange;

/**
 * DiagramLink class proxy interface.
 */
public interface DiagramLinkClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public DiagramLink createDiagramLink();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param zoom 
     * @param viewport 
     * @return The created instance object.
     */
    public DiagramLink createDiagramLink(double zoom, org.omg.uml.diagraminterchange.Point viewport);
}
