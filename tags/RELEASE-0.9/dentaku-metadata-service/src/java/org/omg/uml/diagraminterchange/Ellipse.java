package org.omg.uml.diagraminterchange;

/**
 * Ellipse object instance interface.
 */
public interface Ellipse extends org.omg.uml.diagraminterchange.GraphicPrimitive {
    /**
     * Returns the value of attribute center.
     * @return Value of attribute center.
     */
    public org.omg.uml.diagraminterchange.Point getCenter();
    /**
     * Sets the value of center attribute. See {@link #getCenter} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setCenter(org.omg.uml.diagraminterchange.Point newValue);
    /**
     * Returns the value of attribute radiusX.
     * @return Value of attribute radiusX.
     */
    public double getRadiusX();
    /**
     * Sets the value of radiusX attribute. See {@link #getRadiusX} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setRadiusX(double newValue);
    /**
     * Returns the value of attribute radiusY.
     * @return Value of attribute radiusY.
     */
    public double getRadiusY();
    /**
     * Sets the value of radiusY attribute. See {@link #getRadiusY} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setRadiusY(double newValue);
    /**
     * Returns the value of attribute rotation.
     * @return Value of attribute rotation.
     */
    public double getRotation();
    /**
     * Sets the value of rotation attribute. See {@link #getRotation} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setRotation(double newValue);
    /**
     * Returns the value of attribute startAngle.
     * @return Value of attribute startAngle.
     */
    public double getStartAngle();
    /**
     * Sets the value of startAngle attribute. See {@link #getStartAngle} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setStartAngle(double newValue);
    /**
     * Returns the value of attribute endAngle.
     * @return Value of attribute endAngle.
     */
    public double getEndAngle();
    /**
     * Sets the value of endAngle attribute. See {@link #getEndAngle} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setEndAngle(double newValue);
}
