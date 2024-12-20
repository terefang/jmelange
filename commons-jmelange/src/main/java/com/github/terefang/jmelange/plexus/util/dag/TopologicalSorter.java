package com.github.terefang.jmelange.plexus.util.dag;

/*
 * Copyright The Codehaus Foundation.
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

import com.github.terefang.jmelange.plexus.util.dag.DAG;
import com.github.terefang.jmelange.plexus.util.dag.Vertex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 *
 */
public class TopologicalSorter {

    private static final Integer NOT_VISITED = 0;

    private static final Integer VISITING = 1;

    private static final Integer VISITED = 2;

    /**
     * @param graph the graph
     * @return List of String (vertex labels)
     */
    public static List<String> sort(final com.github.terefang.jmelange.plexus.util.dag.DAG graph) {
        return dfs(graph);
    }

    public static List<String> sort(final com.github.terefang.jmelange.plexus.util.dag.Vertex vertex) {
        // we need to use addFirst method so we will use LinkedList explicitly
        final List<String> retValue = new LinkedList<>();

        dfsVisit(vertex, new HashMap<com.github.terefang.jmelange.plexus.util.dag.Vertex, Integer>(), retValue);

        return retValue;
    }

    private static List<String> dfs(final DAG graph) {
        // we need to use addFirst method so we will use LinkedList explicitly
        final List<String>                                      retValue       = new LinkedList<>();
        final Map<com.github.terefang.jmelange.plexus.util.dag.Vertex, Integer> vertexStateMap = new HashMap<>();

        for (com.github.terefang.jmelange.plexus.util.dag.Vertex vertex : graph.getVertices()) {
            if (isNotVisited(vertex, vertexStateMap)) {
                dfsVisit(vertex, vertexStateMap, retValue);
            }
        }

        return retValue;
    }

    private static boolean isNotVisited(final com.github.terefang.jmelange.plexus.util.dag.Vertex vertex, final Map<com.github.terefang.jmelange.plexus.util.dag.Vertex, Integer> vertexStateMap) {
        final Integer state = vertexStateMap.get(vertex);

        return (state == null) || NOT_VISITED.equals(state);
    }

    private static void dfsVisit(
            final com.github.terefang.jmelange.plexus.util.dag.Vertex vertex, final Map<com.github.terefang.jmelange.plexus.util.dag.Vertex, Integer> vertexStateMap, final List<String> list) {
        vertexStateMap.put(vertex, VISITING);

        for (Vertex v : vertex.getChildren()) {
            if (isNotVisited(v, vertexStateMap)) {
                dfsVisit(v, vertexStateMap, list);
            }
        }

        vertexStateMap.put(vertex, VISITED);

        list.add(vertex.getLabel());
    }
}
