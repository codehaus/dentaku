/*
 * TestPlugin.java
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
package org.dentaku.gentaku;

import org.generama.*;

import java.io.IOException;

/**
 * 
 */
public class TestPlugin extends Plugin {
    public boolean wasExecuted = false;

    public TestPlugin(TemplateEngine templateEngine, MetadataProvider metadataProvider, WriterMapper writerMapper, boolean wasExecuted) {
        super(templateEngine, metadataProvider, writerMapper);
        this.wasExecuted = wasExecuted;
    }

    public void execute() throws IOException, GeneramaException {
        wasExecuted = true;
    }
}
