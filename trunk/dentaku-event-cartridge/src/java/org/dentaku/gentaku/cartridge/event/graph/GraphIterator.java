/*
 * GraphIterator.java
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

import java.util.Collection;

public interface GraphIterator {
    public Collection nextEdges(Object vertex);
    public Collection prevEdges(Object vertex);

    // get the target of an edge
    public Object getSource(Object edge);
    public Object getTarget(Object edge);
}
