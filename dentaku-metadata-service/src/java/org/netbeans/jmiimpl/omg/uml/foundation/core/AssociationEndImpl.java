/*
 * AssociationEndImpl.java
 * Copyright 2004-2004 Bill2, Inc.
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
package org.netbeans.jmiimpl.omg.uml.foundation.core;

import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;

import java.util.Collection;
import java.util.Iterator;

abstract public class AssociationEndImpl extends ModelElementImpl implements AssociationEnd {
    protected AssociationEndImpl(StorableObject storable) {
        super(storable);
    }

    public boolean isOne2Many() {
        boolean thisMany = isMany(this);
        boolean thatMany = isMany(getOtherEnd());
        return !thisMany && thatMany;
    }

    public boolean isMany2Many() {
        boolean thisMany = isMany(this);
        boolean thatMany = isMany(getOtherEnd());
        return thisMany && thatMany;
    }

    public boolean isOne2One() {
        boolean thisMany = isMany(this);
        boolean thatMany = isMany(getOtherEnd());
        return !thisMany && !thatMany;
    }

    public boolean isMany2One() {
        return isMany(this) && !isMany(getOtherEnd());
    }

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

    protected AssociationEnd getOtherEnd() {
        Collection ends = getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext();) {
            AssociationEnd ae = (AssociationEnd) i.next();
            if (!this.equals(ae)) {
                return ae;
            }
        }
        return null;
    }

    public AssociationEnd getSource() {
        return this;
    }

    public AssociationEnd getTarget() {
        return getOtherEnd();
    }

    public String getRoleName() {
        String roleName = getName();
        if ((roleName == null) || (roleName.length() == 0)) {
            String s = getParticipant().getName();
            if (s != null && s.length() > 0) {
                s = s.substring(0, 1).toLowerCase() + s.substring(1);
            }
            roleName = s;
        }
        return roleName;
    }

    public boolean isClass() {
        return getParticipant() instanceof UmlClass;
    }
}
