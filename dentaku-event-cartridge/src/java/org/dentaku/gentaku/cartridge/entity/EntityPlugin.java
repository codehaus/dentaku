/*
 * EntityImplPlugin.java
 * Copyright 2002-2004 Bill2, Inc.
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
package org.dentaku.gentaku.cartridge.entity;

import org.generama.MetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.dentaku.gentaku.cartridge.JavaPluginBase;

public class EntityPlugin extends JavaPluginBase {
    public EntityPlugin(VelocityTemplateEngine templateEngine, MetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        getStereotypes().add("Entity");
        setCreateonly(true);
        setMultioutput(true);
    }
}
