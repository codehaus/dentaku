/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata.proxy;

import org.omg.uml.foundation.core.Attribute;
import org.dentaku.services.metadata.UMLStaticHelper;


/**
 * dynamic proxy for an Attribute: dynamically supports the UMLAttribute, 
 * and org.omg.uml.foundation.core.Attribute interfaces.
 *
 *@author  <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class PAttribute
	extends PModelElement
	implements UMLAttribute
{
	private UMLStaticHelper scriptHelper;


	/**
	 *  Description of the Method
	 *
	 *@param  classifier    Description of the Parameter
	 *@param  scriptHelper  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	public static Attribute newInstance(
		UMLStaticHelper scriptHelper,
		Attribute attribute)
	{
		Class[] interfaces = new Class[]
			{
			UMLAttribute.class,
			Attribute.class
			};

		return (Attribute)java.lang.reflect.Proxy.newProxyInstance(
			attribute.getClass().getClassLoader(),
			interfaces,
			new PAttribute(attribute, scriptHelper));
	}


	
	private PAttribute(
		Attribute attribute,
		UMLStaticHelper scriptHelper)
	{
		super(attribute,scriptHelper);
	}


	public Object getId()
	{
		return modelElement;
	}

}

