/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata.proxy;

import org.omg.uml.foundation.core.ModelElement;

/**
 * defines those methods missing from Dependency in the UML 1.4 schema that are
 * needed by the UML2EJB based code generation scripts.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public interface UMLDependency
        extends UMLModelElement {
    public Object getId();

    public ModelElement getTargetType();
}
