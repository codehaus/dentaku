/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.tranql;

import org.tranql.schema.AssociationEnd;
import org.tranql.schema.Entity;
import org.tranql.schema.Association;

public class Relation implements AssociationEnd {
    private final String name;
    private final Entity ejb;
    private final boolean isSingle;
    private final boolean isCascadeDelete;
    private final Relationship relationship;
    private final boolean isVirtual;

    public Relation(String name, Entity ejb, boolean isSingle, boolean isCascadeDelete, Relationship relationship, boolean isVirtual) {
        this.name = name;
        this.ejb = ejb;
        this.isSingle = isSingle;
        this.isCascadeDelete = isCascadeDelete;
        this.relationship = relationship;
        relationship.addAssociationEnd(this);
        this.isVirtual = isVirtual;
    }

    public String getName() {
        return name;
    }

    public Entity getEntity() {
        return ejb;
    }

    public Association getAssociation() {
        return relationship;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public boolean isMulti() {
        return !isSingle;
    }

    public boolean isCascadeDelete() {
        return isCascadeDelete;
    }

    public boolean isVirtual() {
        return isVirtual;
    }
}
