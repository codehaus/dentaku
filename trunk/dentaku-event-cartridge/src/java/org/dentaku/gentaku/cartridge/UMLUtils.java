/*
 * UMLUtils.java
 * Copyright 2002-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dentaku.gentaku.cartridge;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.apache.commons.collections.functors.NotPredicate;
import org.dentaku.gentaku.cartridge.event.graph.GraphProcessor;
import org.dentaku.gentaku.cartridge.event.graph.JMIUMLIterator;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.generama.Plugin;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.SimpleState;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.StructuralFeature;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @deprecated
 */
public class UMLUtils {
    protected JMIUMLMetadataProvider mp = null;
    protected Plugin plugin = null;
    protected HashMap stereotypeCache;
    protected DbMappingTable typeMappings;
    private static UMLUtils instance;

    public static UMLUtils getInstance(JMIUMLMetadataProvider jmiumlMetadataProvider, Plugin plugin) {
        if (instance == null) {
            instance = new UMLUtils(jmiumlMetadataProvider, plugin);
        }
        return instance;
    }

    /**
     * @deprecated
     */
    public UMLUtils(JMIUMLMetadataProvider mp, Plugin plugin) {
        this.mp = mp;
        this.plugin = plugin;
        stereotypeCache = new HashMap();
        typeMappings = new DbMappingTable();
    }

    /**
     * @deprecated
     */
    public Collection getAttributes(Object modelObject) {
        if ((modelObject == null) || !(modelObject instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }
        Collection collection = CollectionUtils.select(((Classifier) modelObject).getFeature(), new InstanceofPredicate(Attribute.class));
        return collection;
    }

    /**
     * @deprecated
     */
    public Collection getOperations(Object modelObject) {
        if ((modelObject == null) || !(modelObject instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }
        Collection collection = CollectionUtils.select(((Classifier) modelObject).getFeature(), new InstanceofPredicate(Operation.class));
        return collection;
    }

    /**
     * @deprecated
     */
    public Collection getTargetEnds(Object object) {
        if ((object == null) || !(object instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }
        Collection links = getAssociationEnds(object);
        ArrayList result = new ArrayList();
        for (Iterator it = links.iterator(); it.hasNext();) {
            Object end = it.next();
            if (end instanceof AssociationEnd) {
                Collection ends = ((AssociationEnd) end).getAssociation().getConnection();
                for (Iterator i = ends.iterator(); i.hasNext();) {
                    AssociationEnd ae = (AssociationEnd) i.next();
                    if (!end.equals(ae)) {
                        result.add(ae);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @deprecated
     */
    public Collection getAssociationEnds(Object object) {
        Collection association = mp.getModel().getCore().getAParticipantAssociation().getAssociation((Classifier) object);
        return association;
    }

    /**
     * @deprecated
     */
    public boolean isClass(Object object) {
        boolean b = (object instanceof UmlClass);
        return b;
    }

    /**
     * @deprecated
     */
    public boolean isActor(Object object) {
        boolean b = (object instanceof Actor);
        return b;
    }

    /**
     * @deprecated
     */
    public String upperCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }

    /**
     * @deprecated
     */
    public Collection getStereotypeNames(Object object) {
        Collection names = (Collection) stereotypeCache.get(object);
        if (names == null) {
            if ((object == null) || !(object instanceof ModelElement)) {
                return Collections.EMPTY_LIST;
            }
            names = new ArrayList();
            Collection stereotypes = ((ModelElement) object).getStereotype();
            for (Iterator i = stereotypes.iterator(); i.hasNext();) {
                ModelElement stereotype = (ModelElement) i.next();
                names.add(stereotype.getName());
            }
            stereotypeCache.put(object, names);
        }
        return names;
    }

    /**
     * @deprecated
     */
    public boolean matchesStereotype(Object object, String stereotype) {
        Collection names = getStereotypeNames(object);
        boolean b = names.contains(stereotype);
        return b;
    }

    /**
     * Gets the primaryKeyAttribute attribute of the UMLScriptHelper object
     *
     * @param object Description of the Parameter
     * @return The primaryKeyAttribute value
     * @deprecated
     */
    public Attribute getPrimaryKeyAttribute(Object object) {
        for (Object current = object; current != null; current = ((Generalization) ((Classifier) current).getGeneralization().iterator().next()).getParent()) {
            Collection attributes = CollectionUtils.select(((Classifier) current).getFeature(), new InstanceofPredicate(Attribute.class));
            for (Iterator i = attributes.iterator(); i.hasNext();) {
                Object attribute = i.next();
                if (getStereotypeNames(attribute).contains("PrimaryKey")) {
                    return (Attribute) attribute;
                }
            }
        }
        return null;
    }

    /**
     * Returns the collection of dependencies for a given model element.
     * <p/>
     * <p>Abstraction/Interface implements dependencies will not be
     * included in this collection.</b>
     *
     * @param object model element
     * @return Collection of org.omg.uml.foundation.core.Dependency
     * @deprecated
     */
    public Collection getDependencies(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return Collections.EMPTY_LIST;
        }
        Collection clientDependencies = mp.getModel().getCore().getAClientClientDependency().getClientDependency((ModelElement) object);
        Collection collection = CollectionUtils.select(clientDependencies, new AndPredicate(new InstanceofPredicate(Dependency.class), new NotPredicate(new InstanceofPredicate(Abstraction.class))));
        return collection;
    }

    /**
     * <p>Converts a string following the Java naming conventions to a
     * database attribute name.  For example convert customerName to
     * CUSTOMER_NAME.</p>
     *
     * @param s         string to convert
     * @param separator character used to separate words
     * @return string converted to database attribute format
     * @deprecated
     */
    public static String toDatabaseAttributeName(String s, String separator) {
        StringBuffer databaseAttributeName = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(lowerCaseFirstLetter(s));
        for (char character = iter.first(); character != CharacterIterator.DONE; character = iter.next()) {
            if (Character.isUpperCase(character)) {
                databaseAttributeName.append(separator);
            }
            character = Character.toUpperCase(character);
            databaseAttributeName.append(character);
        }
        return databaseAttributeName.toString();
    }

    /**
     * <p>Removes the capitalization of a string. That is, it returns
     * "hamburgerStall" when receiving a "HamburgerStall".</p>
     *
     * @param s the input string
     * @return String the output string.
     * @deprecated
     */
    public static String lowerCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toLowerCase() + s.substring(1);
        } else {
            return s;
        }
    }

    /**
     * <p>Returns a consistent name for a relation, independent from
     * the end of the relation one is looking at.</p>
     * <p/>
     * <p>In order to guarantee consistency with relation names, they
     * must appear the same whichever angle (ie entity) that you come
     * from.  For example, if you are at Customer end of a
     * relationship to an Address then your relation may appear with
     * the name Customer-Address.  But if you are in the Address
     * entity looking at the Customer then you will get an error
     * because the relation will be called Address-Customer.  A simple
     * way to guarantee that both ends of the relationship have the
     * same name is merely to use alphabetical ordering.</p>
     *
     * @param roleName       name of role in relation
     * @param targetRoleName name of target role in relation
     * @param separator      character used to separate words
     * @return uniform mapping name (in alphabetical order)
     * @deprecated
     */
    public static String toRelationName(String roleName, String targetRoleName, String separator) {
        if (roleName.compareTo(targetRoleName) <= 0) {
            return (roleName + separator + targetRoleName);
        }
        return (targetRoleName + separator + roleName);
    }

    /**
     * <p>Returns the JDBC type for an attribute.  It gets the type
     * from the tag <code>andromda.persistence.JDBCType</code> for this.
     * </p>
     *
     * @param attribute the attribute
     * @return String the string to be used with JDBC
     * @deprecated
     */
    public String findAttributeJDBCType(Attribute attribute) {
        if (attribute == null)
            return null;
        String value = findTagValue(attribute, "andromda.persistence.JDBCType", true);
        if (null == value) {
            Object type = attribute.getType();
            value = getDestinationFullyQualifiedClassName(type);
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
     * @deprecated
     */
    public String findAttributeSQLFieldLength(Attribute attribute) {
        String value = findTagValue(attribute, "gentaku.persistence.SQLFieldLength", true);
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
        String value = findTagValue(attribute, "gentaku.persistence.SQLColumnType", true);
        if (null == value) {
            Object type = attribute.getType();
            String typeName = getFullyQualifiedName(type);
            value = this.typeMappings.getSQLType(typeName, findAttributeSQLFieldLength(attribute));
        }
        return value;
    }

    /**
     * Returns the collection of taggedValues for a given modelElement
     *
     * @param object model element
     * @return Collection of org.omg.uml.foundation.core.TaggedValue
     * @deprecated
     */
    public Collection getTaggedValues(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return Collections.EMPTY_LIST;
        }
        ModelElement modelElement = (ModelElement) object;
        return modelElement.getTaggedValue();
    }

    /**
     * Searches the given class feature (operation or attribute) for
     * the specified tag.
     * <p/>
     * <p>If the follow boolean is set to true then the search will
     * continue from the class feature to the class itself and then
     * up the class hiearchy.</p>
     *
     * @param feature attribute or operation object
     * @param tagName name of the tag to search for
     * @param follow  <b>true</b> if search should follow inheritance
     *                hierarchy
     * @return String value of tag, <b>null</b> if tag not found
     * @deprecated
     */
    public String findTagValue(StructuralFeature feature, String tagName, boolean follow) {
        if (feature == null)
            return null;
        String value = findTagValue(getTaggedValues(feature), tagName);
        ModelElement element = feature.getType();
        while ((value == null) && (element != null)) {
            value = findTagValue(getTaggedValues(element), tagName);
            element = getGeneralization(element);
        }
        return value;
    }

    /**
     * Searches a collection of tag values for one with a particular
     * name
     *
     * @param taggedValues of taggedValues
     * @param tagName      name of tag for which to search
     * @return value of tag, null if tag not found
     */
    public String findTagValue(Collection taggedValues, String tagName) {
        for (Iterator i = taggedValues.iterator(); i.hasNext();) {
            TaggedValue taggedValue = (TaggedValue) i.next();
            String tgvName = getDestinationFullyQualifiedClassName(taggedValue);
            if (tagName.equals(tgvName)) {
                Iterator it = taggedValue.getDataValue().iterator();
                if (it.hasNext()) {
                    return it.next().toString();
                }
                return null;
            }
        }
        return null;
    }

    /**
     * @deprecated
     */
    public String getName(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return null;
        }
        ModelElement modelElement = (ModelElement) object;
        if (object instanceof TaggedValue) {
            return getTaggedValueName((TaggedValue) object);
        }
        return modelElement.getName();
    }

    /**
     * @deprecated
     */
    private String getTaggedValueName(TaggedValue tgv) {
        String tgvName = tgv.getName();

        // sometimes the tag name is on the TagDefinition
        if ((tgvName == null) && (tgv.getType() != null)) {
            tgvName = tgv.getType().getName();

            // sometimes it is the TagType
            if (tgvName == null) {
                tgvName = tgv.getType().getTagType();
            }
        }
        return tgvName;
    }

    /**
     * Returns the generalization/superclass for the given model generalizable
     * model element (i.e. Class).
     *
     * @param object model element
     * @return GeneralizableElement super class
     * @deprecated
     */
    public GeneralizableElement getGeneralization(Object object) {
        if ((object == null) || !(object instanceof GeneralizableElement)) {
            return null;
        }
        GeneralizableElement element = (GeneralizableElement) object;
        Iterator i = element.getGeneralization().iterator();
        if (i.hasNext()) {
            Generalization generalization = (Generalization) i.next();
            return generalization.getParent();
        }
        return null;
    }

    /**
     * Returns the collection of interfaces implemented by the given
     * Classifier object.
     *
     * @param object  Class
     * @return Collection of Interfaces
     * @deprecated
     */
    public Collection getAbstractions(Object object) {
        if ((object == null) || !(object instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }

        ModelElement modelElement = (ModelElement) object;

        Collection clientDependencies = mp.getModel().getCore().getAClientClientDependency().getClientDependency(modelElement);
        Collection collection = CollectionUtils.select(clientDependencies, new InstanceofPredicate(Abstraction.class));

        return collection;
    }


    /**
     * returns a string representation for the Java signature for
     * a given operation. Note: The return type is not included (any more)!
     *
     * @param model element representing the operation
     * @return String representation of the operation signature
     * @deprecated
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
     * @deprecated
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
                    type = getDestinationFullyQualifiedClassName(p.getType());
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
     * Transform a type name if necessary.  Initially used by HibernateEntityFactory.vsl.
     *
     * @param type
     * @return
     * @deprecated
     */
    public String typeTransform(String type) {
        if (type.equals("int")) {
            return "Integer";
        } else {
            return type;
        }
    }

    /**
     * @deprecated
     */
    public String getSupertypes(Object modelElement) {
        String superTypes = null;
        for (Iterator it = ((Classifier) modelElement).getGeneralization().iterator(); it.hasNext();) {
            Generalization g = (Generalization) it.next();
            String name = getDestinationFullyQualifiedClassName(g.getParent());
            if (superTypes == null) {
                superTypes = name;
            } else {
                superTypes += "," + name;
            }
        }
        return superTypes;
    }

    /**
     * @deprecated
     */
    public String getInterfaces(Object modelElement) {
        String superTypes = null;
        for (Iterator it = getAbstractions(modelElement).iterator(); it.hasNext();) {
            Abstraction abstraction = (Abstraction) it.next();
            String name = getDestinationFullyQualifiedClassName(abstraction.getSupplier().iterator().next());
            if (superTypes == null) {
                superTypes = name;
            } else {
                superTypes += "," + name;
            }
        }
        return superTypes;
    }

    /**
     * @deprecated
     */
    public String getDestinationClassname(Object metadata) {
        String destinationFilename = plugin.getDestinationFilename(metadata);
        return destinationFilename.substring(0, destinationFilename.indexOf('.'));
    }

    /**
     * @deprecated
     */
    public String getDestinationFullyQualifiedClassName(Object metadata) {
        String packageName = plugin.getDestinationPackage(metadata);
        packageName = packageName.equals("") ? "" : packageName + ".";
        return packageName + getDestinationClassname(metadata);
    }

    /**
     * @deprecated
     */
    public String getDatabaseColumnName(Object attribute) {
        if ((attribute == null) || !(attribute instanceof Attribute)) {
            return null;
        }
        String name = findTagValue((Attribute) attribute, "gentaku.persistence.SQLColumnName", true);
        if (name == null) {
            name = ((Attribute) attribute).getName();
        }
        return name;
    }

    /**
     * @deprecated
     */
    public String getDatabaseTableName(Object table) {
        if ((table == null) || !(table instanceof UmlClass)) {
            return null;
        }
        String name = findTagValue(getTaggedValues(table), "gentaku.persistence.SQLTableName");

        if (name == null) {
            name = ((UmlClass)table).getName();
        }
        return name;
    }

    /**
     * @deprecated
     */
    public AssociationEnd getOtherEnd(AssociationEnd associationEnd) {
        Collection ends = associationEnd.getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext();) {
            AssociationEnd ae = (AssociationEnd) i.next();
            if (!associationEnd.equals(ae)) {
                return ae;
            }
        }
        return null;
    }

    /**
     * @deprecated
     */
    public Pseudostate findInitialState(Collection states) {
        for (Iterator it = states.iterator(); it.hasNext();) {
            StateVertex state = (StateVertex) it.next();
            if (state instanceof Pseudostate && ((Pseudostate) state).getKind().equals(PseudostateKindEnum.PK_INITIAL)) {
                return (Pseudostate) state;
            }
        }
        return null;
    }

    /**
     * @deprecated
     */
    public boolean isOne2Many(AssociationEnd associationEnd) {
        return !isMany(associationEnd) && isMany(getOtherEnd(associationEnd));
    }

    /**
     * @deprecated
     */
    public boolean isMany2Many(AssociationEnd associationEnd) {
        return isMany(associationEnd) && isMany(getOtherEnd(associationEnd));
    }

    /**
     * @deprecated
     */
    public boolean isOne2One(AssociationEnd associationEnd) {
        return !isMany(associationEnd) && !isMany(getOtherEnd(associationEnd));
    }

    /**
     * @deprecated
     */
    public boolean isMany2One(AssociationEnd associationEnd) {
        return isMany(associationEnd) && !isMany(getOtherEnd(associationEnd));
    }

    /**
     * @deprecated
     */
    static protected boolean isMany(AssociationEnd ae) {
        Multiplicity multiplicity = ae.getMultiplicity();
        if (multiplicity == null) {
            return false;  // no multiplicity means multiplicity==1
        }
        Collection ranges = multiplicity.getRange();
        for (Iterator i = ranges.iterator(); i.hasNext();) {
            MultiplicityRange range = (MultiplicityRange) i.next();
            if (range.getUpper() > 1) {
                return true;
            }
            int rangeSize = range.getUpper() - range.getLower();
            if (rangeSize < 0) {
                return true;
            }
        }
        return false;
    }

    /**
    * Returns the name of a model element fully qualified by the
    * name of the package that contains it. If the model element
    * is a primitive type it will return the primitive type itself.
    *
    *@param object model element
    *@return fully qualifed name
     * @deprecated
    */
    public String getFullyQualifiedName(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return null;
        }

        ModelElement modelElement = (ModelElement) object;

        String fullName = modelElement.getName();

        if (!isPrimitiveType(fullName)) {
            fullName = getDestinationFullyQualifiedClassName(modelElement);
        }

        return fullName;
    }

    /**
     * @deprecated
     */
    public static boolean isPrimitiveType(String name) {
        return (   "void".equals(name)
                || "char".equals(name)
                || "byte".equals(name)
                || "short".equals(name)
                || "int".equals(name)
                || "long".equals(name)
                || "float".equals(name)
                || "double".equals(name)
                || "boolean".equals(name) );
    }

    /**
     * @deprecated
     */
    public Object findOneItem(Namespace modelElement, Class clazz) throws GenerationException {
        Object result = null;
        for (Iterator it = modelElement.getOwnedElement().iterator(); it.hasNext();) {
            Object o = (Object) it.next();
            if (clazz.isInstance(o)) {
                if (result != null) {
                    throw new GenerationException("multiple use cases exist");
                }
                result = o;
            }

        }
        return result;
    }

    /**
     * @deprecated
     */
    public Object findOneActivityGraph(Namespace modelElement) throws GenerationException {
        return findOneItem(modelElement, ActivityGraph.class);
    }

    /**
     * @deprecated
     */
    public List getWorkflowActions(Object modelElement) throws GenerationException {
        List result = new ArrayList();
        ActivityGraph activityGraph = (ActivityGraph) findOneItem(((Namespace) modelElement), ActivityGraph.class);
        Pseudostate startState = findInitialState(((CompositeState) activityGraph.getTop()).getSubvertex());
        if (startState != null) {
            Collection subvertex = ((CompositeState) activityGraph.getTop()).getSubvertex();
            GraphProcessor gp = new GraphProcessor();
            JMIUMLIterator nav = new JMIUMLIterator();
            gp.validate(subvertex, nav);
            Collection topological = gp.getTopological();
            for (Iterator it = topological.iterator(); it.hasNext();) {
                StateVertex vertex = (StateVertex) it.next();
                if (vertex instanceof SimpleState) {
                    result.add(vertex);
                }
            }
        }
        return result;
    }


}
