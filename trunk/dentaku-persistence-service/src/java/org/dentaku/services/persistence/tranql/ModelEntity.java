/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.tranql;

import org.tranql.schema.Entity;
import org.tranql.schema.Attribute;
import org.tranql.schema.AssociationEnd;
import org.tranql.schema.Association;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class ModelEntity implements Entity {
    private final String schemaName;
    private final String ejbName;

    private final List pkFields = new ArrayList();
    private final List cmpFields = new ArrayList();
    private final Map attributeMap = new HashMap();

    private final List cmrFields = new ArrayList();
    private final Map cmrFieldsByName = new HashMap();

    public ModelEntity(String schemaName, String ejbName) {
        this.schemaName = schemaName;
        this.ejbName = ejbName;
    }

    public String getName() {
        return schemaName;
    }

    public String getPhysicalName() {
        return ejbName;
    }

    public Attribute getAttribute(String name) {
        return (Attribute) attributeMap.get(name);
    }

    public List getAttributes() {
        return Collections.unmodifiableList(cmpFields);
    }

    public AssociationEnd getAssociationEnd(String name) {
        return (AssociationEnd) cmrFieldsByName.get(name);
    }

    public List getAssociationEnds() {
        return Collections.unmodifiableList(cmrFields);
    }

    public List getPrimaryKeyFields() {
        return Collections.unmodifiableList(pkFields);
    }

    public void addField(Attribute field) {
        cmpFields.add(field);
        attributeMap.put(field.getName(), field);
        if (field.isIdentity()) {
            pkFields.add(field);
        }
    }

    public void addCMRField(Relation field) {
        cmrFieldsByName.put(field.getName(), field);
        cmrFields.add(field);
    }

    public void addRelation(Association relation) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
