/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.tranql;

import org.tranql.schema.AbstractAssociation;
import org.tranql.schema.Entity;

public class Relationship extends AbstractAssociation {
    public Relationship(JoinDefinition joinDefinition) {
        super(joinDefinition);
    }

    public Relationship(Entity mtmEntity, JoinDefinition leftDefinition, JoinDefinition rightDefinition) {
        super(mtmEntity, leftDefinition, rightDefinition);
    }
}
