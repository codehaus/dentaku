package org.dentaku.services.metadata.jmi.core;

/**
 * Defines Dentaku's extensions for <code>org.omg.uml.foundation.core.Attribute</code> interface.
 * 
 * @see org.omg.uml.foundation.core.Attribute
 */
public interface Attribute extends org.omg.uml.foundation.core.Attribute {

    public abstract String getDatabaseColumnName();

    /**
     * Searches the given class feature (operation or attribute) for the specified tag.
     * 
     * If the follow boolean is set to true then the search will continue from the class feature to
     * the class itself and then up the class hiearchy.
     * 
     * @param tagName name of the tag to search for
     * @param follow <b>true</b> if search should follow inheritance hierarchy
     * @return String value of tag, <b>null</b> if tag not found
     */
    public abstract String findTagValue(String tagName, boolean follow);

    /**
     * Returns the JDBC type for an attribute. It gets the type from the tag
     * <code>andromda.persistence.JDBCType</code> for this.
     * 
     * @return String the string to be used with JDBC
     */
    public abstract String getAttributeJDBCType();

    /**
     * Returns the SQL type for an attribute. Normally it gets the type from the tag
     * <code>andromda.persistence.SQLType</code>. If this tag doesn't exist, it uses
     * {@link #findAttributeSQLFieldLength(AttributeExtension) findAttributeSQLFieldLength()} and
     * combines it's result with the standard SQL type for the attributes type from the type mapping
     * configuration file.
     * 
     * @return String the string to be used as SQL type
     */
    public abstract String getAttributeSQLType();

    /**
     * Returns the length for the SQL type of an attribute. It gets the length from the tag
     * <code>andromda.persistence.SQLFieldLength</code>. This might return "50" for a VARCHAR
     * field or "12,2" for a DECIMAL field.
     * 
     * @param attribute the attribute
     * @return String the length of the underlying SQL field
     */
    public abstract String getAttributeSQLFieldLength();

    public abstract boolean isMany();

    public abstract boolean isOne();

}
