/*
 * GraphResults.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GraphResults {
    private HashMap discoveredVertex;
    private ArrayList finishedVertex;
    private int time;

    // edge states
    private Set treeEdges;
    private Set backEdges;
    private Set forwardEdges;
    private Set crossEdges;
    private Collection roots;

    public GraphResults() {
        discoveredVertex = new HashMap();
        finishedVertex = new ArrayList();
        time = 0;
        treeEdges = new HashSet();
        backEdges = new HashSet();
        forwardEdges = new HashSet();
        crossEdges = new HashSet();
        roots = new ArrayList();
    }

    public HashMap getDiscoveredVertex() {
        return discoveredVertex;
    }

    public void setDiscoveredVertex(HashMap discoveredVertex) {
        this.discoveredVertex = discoveredVertex;
    }

    public ArrayList getFinishedVertex() {
        return finishedVertex;
    }

    public void setFinishedVertex(ArrayList finishedVertex) {
        this.finishedVertex = finishedVertex;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Set getTreeEdges() {
        return treeEdges;
    }

    public void setTreeEdges(Set treeEdges) {
        this.treeEdges = treeEdges;
    }

    public Set getBackEdges() {
        return backEdges;
    }

    public void setBackEdges(Set backEdges) {
        this.backEdges = backEdges;
    }

    public Set getForwardEdges() {
        return forwardEdges;
    }

    public void setForwardEdges(Set forwardEdges) {
        this.forwardEdges = forwardEdges;
    }

    public Set getCrossEdges() {
        return crossEdges;
    }

    public void setCrossEdges(Set crossEdges) {
        this.crossEdges = crossEdges;
    }

    public int getNextTime() {
        return time++;
    }

    public Collection getRoots() {
        return roots;
    }

    public void setRoots(Collection roots) {
        this.roots = roots;
    }
}
