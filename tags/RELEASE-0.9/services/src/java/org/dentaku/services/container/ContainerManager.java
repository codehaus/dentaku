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
import org.codehaus.plexus.embed.Embedder;
import org.dentaku.services.exception.DentakuException;

import java.io.IOException;
import java.net.URL;

/**
 * This is all kind of wacked out, but basically it's that way to make it testable with external container insertion
 */
public class ContainerManager {
    protected Embedder container;
    private static ContainerManager instance;

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

    public Embedder getContainer() {
        return container;
    }

    public Object lookup(String key) throws ComponentLookupException {
        return container.lookup(key);
    }

    public void dispose() throws Exception {
        container.stop();
        instance = null;
    }

    public static ContainerManager getContainerManager(URL configurationStream) throws DentakuException {
        ContainerManager containerManager = new ContainerManager();
//        containerManager.add(new InputStreamReader(configurationStream));
        if (containerManager.container == null) {
            Embedder container = new Embedder();
            try {
                container.setConfiguration(configurationStream);
                container.start();
            } catch (Exception e) {
                throw new DentakuException(e);
            }
            containerManager.container = container;
        }
        return containerManager;
    }
}
