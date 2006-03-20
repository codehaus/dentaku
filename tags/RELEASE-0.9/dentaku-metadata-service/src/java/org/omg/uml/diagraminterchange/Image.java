package org.omg.uml.diagraminterchange;

/**
 * Image object instance interface.
 */
public interface Image extends org.omg.uml.diagraminterchange.LeafElement {
    /**
     * Returns the value of attribute uri.
     * @return Value of attribute uri.
     */
    public java.lang.String getUri();
    /**
     * Sets the value of uri attribute. See {@link #getUri} for description on 
     * the attribute.
     * @param newValue New value to be set.
     */
    public void setUri(java.lang.String newValue);
    /**
     * Returns the value of attribute mimeType.
     * @return Value of attribute mimeType.
     */
    public java.lang.String getMimeType();
    /**
     * Sets the value of mimeType attribute. See {@link #getMimeType} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setMimeType(java.lang.String newValue);
}
