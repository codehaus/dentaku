/*
 * DefaultPipeline.java
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
package org.dentaku.foundation.pipeline;

import org.dentaku.foundation.event.AbstractEvent;

import java.util.Iterator;
import java.util.List;

public class DefaultPipeline implements Pipeline {
    private List valves;

    public List getValves() {
        return valves;
    }

    public void execute(AbstractEvent event) throws Exception {
        boolean valveSuccess = true;
        for (Iterator it = valves.iterator(); valveSuccess && it.hasNext();) {
            Valve valve = (Valve) it.next();
            valveSuccess = valve.execute(event);
        }
    }
}
