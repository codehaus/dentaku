/*
 * DirectConnector.java
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
package org.dentaku.foundation.connector;

import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.dentaku.services.container.ContainerManager;
import org.dentaku.services.exception.DentakuException;

public class DirectConnector extends ConnectorBase {
    private static DirectConnector ourInstance;
    private static ContainerManager containerManager;

    public static DirectConnector getInstance() throws DentakuException {
        if (ourInstance == null) {
            try {
                containerManager = ContainerManager.getContainerManager(ConnectorBase.class.getResource("ConnectorConfig.xml"));
                ourInstance = (DirectConnector)containerManager.lookup(Connector.ROLE);
            } catch (ComponentLookupException e) {
                // how to test this?
                throw new DentakuException(e);
            }
        }
        return ourInstance;
    }

    public DirectConnector() {
    }
}
