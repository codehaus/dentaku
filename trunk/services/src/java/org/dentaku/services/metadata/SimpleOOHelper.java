/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata;

import org.dentaku.gentaku.cartridge.DbMappingTable;
import org.dentaku.services.metadata.proxy.PModel;
import org.dentaku.services.metadata.proxy.PClassifier;
import org.dentaku.services.metadata.proxy.DirectionalAssociationEnd;
import org.generama.defaults.JavaGeneratingPlugin;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * This script helper simulates the old-style UML2EJB SimpleOO based
 * script helper, and is provided mostly for backward compatability
 * with the UML2EJB code generation scripts - it would be nice to
 * deprecate it at some point, but at present there is no plan to do so.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class SimpleOOHelper extends UMLStaticHelper {
    protected DbMappingTable typeMappings;
    private static SimpleOOHelper instance;
    protected HashMap stereotypeCache;
    private JavaGeneratingPlugin plugin;
    private final static String PRIMARY_KEY = "PrimaryKey";
    private final static String ENTITY_BEAN = "Entity";

    public static SimpleOOHelper getInstance(org.omg.uml.UmlPackage model, JavaGeneratingPlugin plugin) {
        if (instance == null) {
            instance = new SimpleOOHelper(plugin);
            instance.setModel(model);
        }
        return instance;
    }

    public SimpleOOHelper(JavaGeneratingPlugin plugin) {
        this.plugin = plugin;
        stereotypeCache = new HashMap();
        typeMappings = new DbMappingTable();
    }

    public Object getModel() {
        return PModel.newInstance(this, this.model);
    }

    public Collection getModelElements() {
        Collection elements = new Vector();
        for (Iterator i = super.getModelElements().iterator();
             i.hasNext();
                ) {
            Object o = i.next();
            if (o instanceof Classifier) {
                o = PClassifier.newInstance(this, (Classifier) o);
            }
            elements.add(o);
        }
        return elements;
    }

    public DirectionalAssociationEnd getAssociationData(Object object) {
        if ((object == null) || !(object instanceof AssociationEnd)) {
            return null;
        }
        AssociationEnd ae = (AssociationEnd) object;
        return new DirectionalAssociationEnd(this, ae);
    }

    /**
     * <p>Formats an HTML String as a collection of paragraphs.
     * Each paragraph has a getLines() method that returns a collection
     * of Strings.</p>
     * 
     * @param string the String to be formatted
     * @return Collection the collection of paragraphs found.
     */
//    public Collection formatHTMLStringAsParagraphs(String string)
//    {
//        try
//        {
//            return new HTMLAnalyzer().htmlToParagraphs(string);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * Gets the primaryKeyAttribute attribute of the UMLScriptHelper object
     *
     * @param object Description of the Parameter
     * @return The primaryKeyAttribute value
     */
    public Attribute getPrimaryKeyAttribute(Object object) {
        Object current = object;
        while (current != null) {
            Collection attributes = getAttributes(current);
            for (Iterator i = attributes.iterator(); i.hasNext();) {
                Object attribute = i.next();
                if (getStereotypeNames(attribute).contains(PRIMARY_KEY)) {
                    return (Attribute) attribute;
                }
            }
            current = ((Generalization) ((Classifier) current).getGeneralization().iterator().next()).getParent();
        }
        return null;
    }

    /**
     * Returns a string indicating whether the Bean is
     * a local or remotely accessable bean.
     * 
     * @param object Bean class
     * @return String 'local' or 'remote'
     */
    public String getEjbRefViewType(Object object) {
        if (ENTITY_BEAN.equals(getStereotype(object))) {
            return "local";
        }
        return "remote";
    }

    /**
     * Returns a string representing the name of the
     * home interface for the Bean.
     *
     * @param object bean class
     * @return string homeInterfaceName
     */
    public String getHomeInterfaceName(Object object) {
        if (getStereotypeNames(object).contains(ENTITY_BEAN)) {
            return getName(object) + "LocalHome";
        }
        return getName(object) + "Home";
    }

    /**
     * Returns a string representing the component name
     * for the Bean. It does not append the 'local' suffix
     * any more (deprecated).
     *
     * @param object
     * @return String
     */
    public String getComponentInterfaceName(Object object) {
        return getName(object);
    }

    /**
     * Returns a list of attributes for a class. The list is
     * useful for generating method signatures for constructors and/or
     * generating code for calling such a constructor
     *
     * @param object        class object
     * @param withTypeNames should attribute types appear in the list
     * @param includePK     should primary key be included in the list
     * @return String representation of attribute list
     */
    public String getAttributesAsList(Object object,
                                      boolean withTypeNames,
                                      boolean includePK) {
        StringBuffer sb = new StringBuffer();
        String separator = "";
        sb.append("(");
        for (Iterator it = getAttributes(object).iterator(); it.hasNext();) {
            Attribute a = (Attribute) it.next();

            // check if attribute is the PK of this class
            // and include it only if includePK is true.
            if (includePK || !getStereotypeNames(a).contains(PRIMARY_KEY)) {
                sb.append(separator);
                if (withTypeNames) {
                    String typeName = findFullyQualifiedName(a.getType());
                    sb.append(typeName);
                    sb.append(" ");
                    sb.append(a.getName());
                } else {
                    sb.append("get");
                    sb.append(upperCaseFirstLetter(a.getName()));
                    sb.append("()");
                }
                separator = ", ";
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * <p>Builds a call to the operation with a list of parameters.
     * that can easily be used from the Velocity script.</p>
     * <p/>
     * <p>This is good to generate business delegates.
     * See feature request #638931.</p>
     *
     * @param o the operation
     * @return String the complete call to the operation
     */
    public String getOperationCall(Object object) {
        if ((object == null) || !(object instanceof Operation)) {
            return null;
        }
        Operation o = (Operation) object;
        StringBuffer sb = new StringBuffer();
        sb.append(o.getName());
        sb.append("(");
        sb.append(getOperationParameterNames(o));
        sb.append(")");
        return sb.toString();
    }

    /**
     * Builds a comma-separated list of parameter names of an operation.
     * 
     * @param o the operation
     * @return String the list of parameter names
     */
    public String getOperationParameterNames(Operation o) {
        StringBuffer sb = new StringBuffer();
        Iterator it = o.getParameter().iterator();
        boolean commaNeeded = false;
        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind())) {
                if (commaNeeded) {
                    sb.append(", ");
                }
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    /**
     * returns a string representation for the Java signature for
     * a given operation. Note: The return type is not included (any more)!
     * 
     * @param model element representing the operation
     * @return String representation of the operation signature
     */
    public String getOperationSignature(Object object) {
        if ((object == null) || !(object instanceof Operation)) {
            return null;
        }
        Operation o = (Operation) object;
        Iterator it = o.getParameter().iterator();
        if (!it.hasNext()) {
            return o.getName() + "()";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(o.getName());
        sb.append("(");
        sb.append(getOperationTypedParameterList(o));
        sb.append(")");
        return sb.toString();
    }

    /**
     * Builds a comma-separated parameter list
     * (type and name of each parameter) of an operation.
     *
     * @param o the operation
     * @return String the parameter list
     */
    public String getOperationTypedParameterList(Operation o) {
        StringBuffer sb = new StringBuffer();
        Iterator it = o.getParameter().iterator();
        boolean commaNeeded = false;
        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind())) {
                String type;
                if (p.getType() == null) {
                    type = "int";
                } else {
                    type = getFullyQualifiedName(p.getType());
                }
                if (commaNeeded) {
                    sb.append(", ");
                }
                sb.append(type);
                sb.append(" ");
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    /**
     * Provided only for backward compatability with old velocity scripts.
     * In truth all model elements can be assigned more than one stereotype.
     *
     * @param object
     * @return String
     */
    public String getStereotype(Object object) {
        Iterator i = getStereotypeNames(object).iterator();
        if (i.hasNext()) {
            String stereotype = (String) i.next();
            return stereotype;
        }
        return "";
    }

    /**
     * Returns the fully qualified name of the given
     * model element.  The fully qualified name includes
     * complete package qualified name of the model element.
     * 
     * @param object model element
     * @return String fully qualified name
     */
    public String findFullyQualifiedName(Object object) {
        return getFullyQualifiedName(object);
    }

    /**
     * <p>Returns the JDBC type for an attribute.  It gets the type
     * from the tag <code>andromda.persistence.JDBCType</code> for this.
     * </p>
     *
     * @param attribute the attribute
     * @return String the string to be used with JDBC
     */
    public String findAttributeJDBCType(Attribute attribute) {
        if (attribute == null)
            return null;
        String value = findTagValue(attribute, "andromda.persistence.JDBCType", true);
        if (null == value) {
            Object type = attribute.getType();
            value = plugin.getDestinationClassname(type);
            if (typeMappings != null) {
                value = typeMappings.getJDBCType(value);
            }
        }
        return value;
    }

    /**
     * <p>Returns the length for the SQL type of an attribute.  It
     * gets the length from the tag
     * <code>andromda.persistence.SQLFieldLength</code>.  This might return "50"
     * for a VARCHAR field or "12,2" for a DECIMAL field.</p>
     *
     * @param attribute the attribute
     * @return String the length of the underlying SQL field
     */
    public String findAttributeSQLFieldLength(Attribute attribute) {
        String value = findTagValue(attribute, "andromda.persistence.SQLFieldLength", true);
        return value;
    }

    /**
     * <p>Returns the SQL type for an attribute.  Normally it gets the
     * type from the tag <code>andromda.persistence.SQLType</code>.  If this tag
     * doesn't exist, it uses {@link #findAttributeSQLFieldLength(Attribute)
     * findAttributeSQLFieldLength()} and combines it's result with the standard
     * SQL type for the attributes type from the type mapping configuration
     * file.</p>
     *
     * @param attribute the attribute
     * @return String the string to be used as SQL type
     */
    public String findAttributeSQLType(Attribute attribute) {
//        String value = findTagValue(attribute, "andromda.persistence.SQLType", true);
//
//        if (null == value)
//        {
//            Object type = attribute.getType();
//            String typeName = findFullyQualifiedName(type);
//            value = this.typeMappings.getSQLType(typeName, findAttributeSQLFieldLength(attribute));
//        }
//        return value;
        return null;
    }

    /**
     * Returns the name of the package that contains the
     * given model element.
     *
     * @param object model element
     * @return fully qualified name of the package
     */
    public String findPackageName(Object object) {
        return getPackageName(object);
    }

    /**
     * Provided only for backward compatability with
     * UML2EJB code generation scripts.  It does nothing
     * in this implementation except return the object that is passed
     * into it.
     *
     * @param object
     * @return Object
     */
    public Object findClassById(Object object) {
        if (object instanceof Classifier) {
            return object;
        }
        return null;
    }

    /**
     * Provided only for backward compatability with UML2EJB
     * code generation scripts.   It does not except
     * return the object that it was passed in.
     * 
     * @param object a model element
     * @return the model element that was passed
     */
    public Object convertToType(Object object) {
        return object;
    }

    /**
     * Transform a type name if necessary.  Initially used by HibernateEntityFactory.vsl.
     *
     * @param type
     * @return
     */
    public String typeTransform(String type) {
        if (type.equals("int")) {
            return "Integer";
        } else if (type.equals("long")) {
            return "Long";
        } else {
            return type;
        }
    }

    public String upperCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }
}
