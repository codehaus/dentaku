package org.dentaku.services.metadata.jmi.core;


/**
 * Defines Dentaku's extensions for <code>org.omg.uml.foundation.core.AssociationEnd</code>
 * interface.
 * 
 * @see org.omg.uml.foundation.core.AssociationEnd
 */
public interface AssociationEnd extends org.omg.uml.foundation.core.AssociationEnd {

    public abstract boolean isOne2Many();

    public abstract boolean isMany2Many();

    public abstract boolean isOne2One();

    public abstract boolean isMany2One();

    public abstract org.omg.uml.foundation.core.AssociationEnd getOtherEnd();

    public abstract org.omg.uml.foundation.core.AssociationEnd getSource();

    public abstract org.omg.uml.foundation.core.AssociationEnd getTarget();

    public abstract String getRoleName();

    public abstract boolean isClass();

}
