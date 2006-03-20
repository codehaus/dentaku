package org.omg.uml.diagraminterchange;

/**
 * Image class proxy interface.
 */
public interface ImageClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Image createImage();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param uri 
     * @param mimeType 
     * @return The created instance object.
     */
    public Image createImage(boolean isVisible, java.lang.String uri, java.lang.String mimeType);
}
