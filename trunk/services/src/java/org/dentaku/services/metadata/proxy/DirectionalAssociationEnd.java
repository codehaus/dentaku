package org.dentaku.services.metadata.proxy;


import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.dentaku.services.metadata.UMLStaticHelper;

import java.util.Collection;
import java.util.Iterator;

/**
 * Implements operations for querying an assocation from the perspective
 * of one of the assocation ends.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class DirectionalAssociationEnd
{
	private UMLStaticHelper scriptHelper;
    protected AssociationEnd associationEnd;

    public DirectionalAssociationEnd(
        UMLStaticHelper scriptHelper,
		AssociationEnd associationEnd)
	{
        this.associationEnd = associationEnd;
        this.scriptHelper = scriptHelper;
	}

	public AssociationEnd getSource()
	{
		return PAssociationEnd.newInstance(scriptHelper,associationEnd);
	}


	public AssociationEnd getTarget()
	{
		return PAssociationEnd.newInstance(scriptHelper,getOtherEnd());
	}

    /**
     * get the name of the association
     */
    public String getName()
    {
        return associationEnd.getAssociation().getName();
    }

    /**
     * get a string that can be used to uniquely id this association
     */
    public String getId()
    {
        return associationEnd.getAssociation().refMofId();
    }

    public boolean isOne2Many()
    {
        boolean thisMany = isMany(associationEnd);
        boolean thatMany = isMany(getOtherEnd());
        return !thisMany && thatMany;
    }

    public boolean isMany2Many()
    {
        boolean thisMany = isMany(associationEnd);
        boolean thatMany = isMany(getOtherEnd());
        return thisMany && thatMany;
    }

    public boolean isOne2One()
    {
        boolean thisMany = isMany(associationEnd);
        boolean thatMany = isMany(getOtherEnd());
        return !thisMany && !thatMany;
    }

    public boolean isMany2One()
    {
        return isMany(associationEnd) && !isMany(getOtherEnd());
    }

    static protected boolean isMany(AssociationEnd ae)
    {
        Multiplicity multiplicity = ae.getMultiplicity();
        if (multiplicity == null)
        {
            return false;  // no multiplicity means multiplicity==1
        }
        Collection ranges = multiplicity.getRange();

        for (Iterator i = ranges.iterator(); i.hasNext() ; )
        {
            MultiplicityRange range = (MultiplicityRange)i.next();
            if ( range.getUpper() > 1 )
            {
                return true;
            }

            int rangeSize = range.getUpper() - range.getLower();
            if (rangeSize < 0)
            {
                return true;
            }

        }

        return false;
    }

    protected AssociationEnd getOtherEnd()
    {
        AssociationEnd otherEnd;

        Collection ends = associationEnd.getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext(); )
        {
            AssociationEnd ae = (AssociationEnd)i.next();
            if (!associationEnd.equals(ae))
            {
                return ae;
            }
        }

        return null;
    }

}
