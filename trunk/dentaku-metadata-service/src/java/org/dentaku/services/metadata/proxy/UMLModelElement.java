/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata.proxy;

import java.util.Collection;

/**
 * defines those methods missing from the ModelElement in the UML 1.4 schema that are
 * needed by the UML2EJB based code generation scripts.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public interface UMLModelElement {
    public Collection getTaggedValues();

    public Object getId();
}