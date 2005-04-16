/*
 *    ParameterImpl.java
 *
 *    Copyright 2002, Bill2, Inc. All rights reserved.
 *
 *    This program contains the confidential trade secret
 *    information of Bill2, Inc.  Use, disclosure, or
 *    copying without written consent is strictly prohibited.
 *
 *    @author topping
 *    @version $Revision$
 */
package org.netbeans.jmiimpl.omg.uml.foundation.core;

import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

public abstract class ParameterImpl extends ModelElementImpl implements Parameter {

    protected ParameterImpl(StorableObject storable) {
        super(storable);
    }

    public boolean isReturnParam() {
        return getKind() != null && getKind().equals(ParameterDirectionKindEnum.PDK_RETURN);
    }
}
