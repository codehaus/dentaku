package org.dentaku.services.metadata.jmi.modelmanagement;

/**
 * Defines Dentaku's extensions for <code>org.omg.uml.modelmanagement.UmlPackage</code> interface.
 * 
 * @see org.omg.uml.modelmanagement.UmlPackage
 */
public interface UmlPackage extends org.omg.uml.modelmanagement.UmlPackage {

    public abstract org.omg.uml.modelmanagement.UmlPackage getChildPackage(String name, boolean create);

}
