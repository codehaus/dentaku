/*
 * JMIUMLIterator.java
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
package org.dentaku.gentaku.cartridge.event.graph;

import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class JMIUMLIterator implements GraphIterator {
    public Collection nextEdges(Object vertex) {
        Collection outgoingTrans = ((StateVertex) vertex).getOutgoing();
        ArrayList result = new ArrayList(outgoingTrans.size());
        for (Iterator it = outgoingTrans.iterator(); it.hasNext();) {
            result.add(it.next());
        }
        return result;
    }

    public Collection prevEdges(Object vertex) {
        Collection outgoingTrans = ((StateVertex) vertex).getIncoming();
        ArrayList result = new ArrayList(outgoingTrans.size());
        for (Iterator it = outgoingTrans.iterator(); it.hasNext();) {
            result.add(it.next());
        }
        return result;
    }

    // get the target of an edge
    public Object getSource(Object edge) {
        return ((Transition) edge).getSource();
    }

    public Object getTarget(Object edge) {
        return ((Transition) edge).getTarget();
    }
}

