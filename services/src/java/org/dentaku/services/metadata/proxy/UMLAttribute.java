/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata.proxy;

/**
 * defines those methods missing from Attribute in the UML 1.4 schema that are 
 * needed by the UML2EJB based code generation scripts.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public interface UMLAttribute
	extends UMLModelElement
{
	public Object getId();
}

