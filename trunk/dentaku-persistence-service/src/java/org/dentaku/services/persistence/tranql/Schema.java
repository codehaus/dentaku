/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.tranql;

import org.tranql.schema.*;
import org.tranql.query.CommandFactory;

import java.util.Map;
import java.util.HashMap;

public class Schema implements org.tranql.schema.Schema {
    private Map entityMap;
    private final CommandFactory commandFactory;

    public Schema() {
        entityMap = new HashMap();
        commandFactory = new DentakuCommandFactory();
    }


    public String getName() {
        return null;
    }

    public String getPhysicalName() {
        return null;
    }

    public Entity getEntity(String name) {
        return null;
    }

    public Map getEntities() {
        return null;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void addEntity(Entity entity) {
        entityMap.put(entity.getName(), entity);
    }
}
