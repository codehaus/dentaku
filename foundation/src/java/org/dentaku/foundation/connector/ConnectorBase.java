/*
 * ConnectorBase.java
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

import org.dentaku.foundation.event.AbstractEvent;
import org.dentaku.foundation.pipeline.Pipeline;

/**
 * Classes extending ConnectorBase are designed to be the entry point into the Dentaku architecture and
 * are generally used by so called "front controllers".  The container is instantiated and maintained here.
 * This allows compatibility with front controllers that are not IOC aware.
 */
public abstract class ConnectorBase implements Connector {
    /**
     * Default Container.
     */
    protected Pipeline pipeline;
    protected Connector connector;

    public Pipeline getPipeline() {
        return pipeline;
    }

    /**
     * @param event
     * @throws Exception
     * @ejb.interface-method
     */
    public void fireEvent(AbstractEvent event) throws Exception {
        pipeline.execute(event);
    }
}
