/*
 * ContainerManager.java
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
package org.dentaku.services.container;

import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.dentaku.services.exception.DentakuException;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This is all kind of wacked out, but basically it's that way to make it testable with external container insertion
 */
public class ContainerManager {
    protected DentakuPlexusContainer container;
    private static ContainerManager instance;
    protected List configurationResources = new ArrayList();

    /**
     * Return a reference to the enclosing plexus container.  If this is not possible, throw a
     * <code>ContainerException</code>.
     *
     * @return PlexusContainer -- the container that Dentaku is running from.
     * @throws ContainerException if we cannot find a reference to the container.
     */
    public static ContainerManager getInstance() throws ContainerException {
        if (instance == null) {
            instance = new ContainerManager();
        }
        return instance;
    }

    public static void setInstance(ContainerManager instance) {
        ContainerManager.instance = instance;
    }

    public DentakuPlexusContainer getContainer() {
        return container;
    }

//    public static void setup(InputStream configurationResource) throws ContainerException {
//        InputStreamReader reader = new InputStreamReader(configurationResource);
//        XStream xs = new XStream();
//        List l = (List)xs.fromXML(reader);
//        ArrayList result = new ArrayList();
//        for (Iterator it = l.iterator(); it.hasNext();) {
//            result.add((InputStreamReader) it.next());
//        }
//        setup();
//    }

    /**
     * Build a new plexus container and put it into JNDI
     *
     * @throws ContainerException
     */
    public void setup() throws ContainerException {
        if (container == null) {
            container = new DentakuPlexusContainer();
            try {
                container.setConfigurationResources(configurationResources);
                container.initialize();
                container.start();
            } catch (Exception e) {
                throw new ContainerException(e);
            }
        }
    }

    public void setup(DentakuPlexusContainer c) {
        container = c;
    }

    public void add(InputStreamReader configurationResource) {
        configurationResources.add(configurationResource);
    }

    public Object lookup(String key) throws ComponentLookupException {
        return container.lookup(key);
    }

    public void dispose() {
        container.dispose();
        instance = null;
    }

    public static ContainerManager getContainerManager(InputStream configurationStream) throws DentakuException {
        ContainerManager containerManager = new ContainerManager();
        containerManager.add(new InputStreamReader(configurationStream));
        containerManager.setup();
        return containerManager;
    }

    public void disposeContainerManager() {
        dispose();
    }
}
