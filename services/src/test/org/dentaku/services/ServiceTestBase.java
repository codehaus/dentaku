/*
 * BaseServiceTest.java
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
package org.dentaku.services;

import junit.framework.TestCase;
import org.dentaku.services.container.ContainerManager;
import org.dentaku.services.container.DentakuPlexusContainer;

import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ServiceTestBase extends TestCase {
    protected DentakuPlexusContainer container;

    protected void setUp() throws Exception {

        ContainerManager instance = ContainerManager.getInstance();
        setupConfiguration(instance);
        instance.setup();
        container = instance.getContainer();
    }

    protected void setupConfiguration(ContainerManager instance) {
        String className = this.getClass().getName();
        String filename = className.substring(className.lastIndexOf(".") + 1) + ".xml";
        InputStream configurationStream = this.getClass().getResourceAsStream(filename);
        instance.add(new InputStreamReader(configurationStream));
    }

    public void tearDown() throws Exception {
        container.dispose();
        container = null;
    }
}
