/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata.proxy;

import java.util.Collection;

import org.omg.uml.foundation.datatypes.VisibilityKind;

/**
 * defines those methods missing from the Operation in the UML 1.4 schema that are 
 * needed by the UML2EJB based code generation scripts.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public interface UMLOperation
	extends UMLModelElement
{
	public VisibilityKind getVisibility();
    public Collection getParameters();
    public Object getType();
}
