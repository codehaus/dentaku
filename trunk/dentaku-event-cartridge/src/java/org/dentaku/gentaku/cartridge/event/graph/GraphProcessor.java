/*
 * GraphProcessor.java
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

public class GraphProcessor {
    private GraphResults dfsResults = null;
    private GraphResults sccResults = null;
    private ArrayList topological = null;

    public GraphResults scc(Collection verticies, GraphIterator nav) {
        init(verticies, nav);
        return sccResults;
    }

    public GraphResults dfs(Collection verticies, GraphIterator nav) {
        GraphResults results = new GraphResults();
        for (Iterator it = verticies.iterator(); it.hasNext();) {
            Object vertex = (Object) it.next();
            if (!results.getDiscoveredVertex().containsKey(vertex)) {
                Collection roots = results.getRoots();
                if (roots != null) {
                    roots.add(vertex);
                }
                dfsVisit(results, vertex, nav);
            }
        }
        return results;
    }

    private ArrayList topologicalSort(GraphResults resultSet1) {
        Object[] finished = resultSet1.getFinishedVertex().toArray();
        ArrayList topological = new ArrayList(finished.length);
        for (int i = finished.length - 1; i >= 0; i--) {
            topological.add(finished[i]);
        }
        return topological;
    }

    private void dfsVisit(GraphResults results, Object vertex, GraphIterator nav) {
        results.getDiscoveredVertex().put(vertex, new Integer(results.getNextTime()));

        Collection nextEdges = nav.nextEdges(vertex);
        for (Iterator i = nextEdges.iterator(); i.hasNext();) {
            Object nextEdge = i.next();
            Object nextVertex = nav.getTarget(nextEdge);
            System.out.println("dfsVisit: thisVertex="+vertex.toString()+"; nextEdge="+getEdgeType(results, nextEdge)+"; nextVertex="+nextVertex.toString());
            classifyEdge(results, nav, nextEdge);
            if (!results.getDiscoveredVertex().containsKey(nextVertex)) {
                dfsVisit(results, nextVertex, nav);
            }
        }
        results.getFinishedVertex().add(vertex);
    }

    private void classifyEdge(GraphResults results, GraphIterator nav, Object edge) {
        Object source = nav.getSource(edge);
        Object target = nav.getTarget(edge);
        if (results.getFinishedVertex().contains(target)) {
            // black, forward or cross edge, distinguish by time
            int sourceTime = ((Integer) results.getDiscoveredVertex().get(source)).intValue();
            int targetTime = ((Integer) results.getDiscoveredVertex().get(target)).intValue();
            if (sourceTime < targetTime) {
                System.out.println("classifyEdge - FORWARD: source="+source.toString()+"; dest="+target.toString());
                results.getForwardEdges().add(edge);
            } else {
                System.out.println("classifyEdge - CROSS: source="+source.toString()+"; dest="+target.toString());
                results.getCrossEdges().add(edge);
            }
        } else {
            if (results.getDiscoveredVertex().containsKey(target)) {
                // gray, back edge
                System.out.println("classifyEdge - BACK: source="+source.toString()+"; dest="+target.toString());
                results.getBackEdges().add(edge);
            } else {
                // white, tree edge
                System.out.println("classifyEdge - TREE: source="+source.toString()+"; dest="+target.toString());
                results.getTreeEdges().add(edge);
            }
        }
    }

    private String getEdgeType(GraphResults results, Object edge) {
        if (results.getBackEdges().contains(edge)) {
            return "back";
        } else if (results.getForwardEdges().contains(edge)) {
            return "forward";
        } else if (results.getCrossEdges().contains(edge)) {
            return "cross";
        } else if (results.getTreeEdges().contains(edge)) {
            return "tree";
        }
        return "unknown";
    }

    /**
     * Fill out the graph, validating it along the way.
     * This is tricky.  Be sure that the graph has only the exact number of verticies (stray verticies will cause
     * problems with nodes on the front), and that there is only a single path.  This needs to be made much more robust.
     * @param verticies
     * @param nav
     * @throws GraphException
     */
    public void validate(Collection verticies, GraphIterator nav) throws GraphException {
        // do the basics
        init(verticies, nav);

        // TEST 1: nodes are have only one path
        StateVertex lastVertex = null;
        Iterator it = topological.iterator();
        lastVertex = (StateVertex) it.next();
        while (it.hasNext()) {
            StateVertex thisVertex = (StateVertex) it.next();
            Collection outgoing = lastVertex.getOutgoing();
            if (!(outgoing.size() == 1
                    && ((Transition)lastVertex.getOutgoing().iterator().next()).getTarget() == thisVertex)) {
                throw new GraphException("graph is not a single path");
            }
            lastVertex = thisVertex;
        }
    }

    public void init(Collection verticies, GraphIterator nav) {
        dfsResults = dfs(verticies, nav);
        topological = topologicalSort(dfsResults);
        sccResults = dfs(topological, new ReverseGraphIterator(nav));
    }

    public GraphResults getDfsResults() {
        return dfsResults;
    }

    public GraphResults getSccResults() {
        return sccResults;
    }

    public ArrayList getTopological() {
        return topological;
    }
}

