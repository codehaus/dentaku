/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata.proxy;

import java.util.Collection;

/**
 * defines those methods missing from Classifier in the UML 1.4 schema that are
 * needed by the UML2EJB based code generation scripts.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public interface UMLClassifier
        extends UMLModelElement {
    /**
     * Gets the attributes attribute of the UMLClassifier object
     *
     * @return The attributes value
     */
    Collection getAttributes();

    /**
     * Gets the dependencies attribute of the UMLClassifier object
     *
     * @return The dependencies value
     */
    Collection getDependencies();

    /**
     * Gets the operations attribute of the UMLClassifier object
     *
     * @return Collection
     */
    Collection getOperations();

    /**
     * Gets the associationLinks attribute of the UMLClassifier object
     *
     * @return The associationLinks value
     */
    Collection getAssociationLinks();

    /**
     * Gets the package attribute of the UMLClassifier object
     *
     * @return The package value
     */
    Object getPackage();

}

