package org.omg.uml.diagraminterchange;

/**
 * Ellipse class proxy interface.
 */
public interface EllipseClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Ellipse createEllipse();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param center 
     * @param radiusX 
     * @param radiusY 
     * @param rotation 
     * @param startAngle 
     * @param endAngle 
     * @return The created instance object.
     */
    public Ellipse createEllipse(boolean isVisible, org.omg.uml.diagraminterchange.Point center, double radiusX, double radiusY, double rotation, double startAngle, double endAngle);
}
