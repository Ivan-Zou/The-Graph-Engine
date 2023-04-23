package graphengine.algorithms;

import graphengine.Graph;

import java.util.LinkedList;

public class IdentifySTC {
    /**
     * Identifies the vertices that satisfies and violates STC in the graph
     *
     * @param graph         the graph to identify STC on
     * @return  string list array containing a list of vertices satisfying STC and list of vertices violating STC
     */
    public static LinkedList<String>[] identifySTC(Graph graph) {
        LinkedList<String>[] results = new LinkedList[2];
        LinkedList<String> satisfiesSTC = new LinkedList<>();
        LinkedList<String> violatesSTC = new LinkedList<>();

        for (String vertex : graph.getVertices()) {
            // if the vertex satisfies STC, add it to the satisfying list
            if (identifySTCForVertex(graph, vertex)) {
                satisfiesSTC.add(vertex);
                // otherwise, add it to the violating list
            } else {
                violatesSTC.add(vertex);
            }
        }
        results[0] = satisfiesSTC;
        results[1] = violatesSTC;
        return results;
    }

    /**
     * Checks if a single vertex satisfies or violates STC
     *
     * @param graph         the graph
     * @param vertex        the vertex to check STC
     * @return true, if the vertex satisfies STC, false otherwise
     */
    private static boolean identifySTCForVertex(Graph graph, String vertex) {
        for (String neighbor1 : graph.getVertex(vertex).getNeighbors()) {
            char tieStrength1 = graph.getTieStrength(vertex, neighbor1);
            for (String neighbor2 : graph.getVertex(vertex).getNeighbors()) {
                // only compare different neighbors
                if (!neighbor1.equals(neighbor2)) {
                    char tieStrength2 = graph.getTieStrength(vertex, neighbor2);
                    // if the two tie strengths of the two neighbors are both strong
                    if (tieStrength1 == 'S' && tieStrength2 == 'S') {
                        // then check if there are any edges between the two neighbors
                        boolean existEdge = graph.hasDirectedEdge(neighbor1, neighbor2) ||
                                graph.hasDirectedEdge(neighbor2, neighbor1);
                        // if the edge does not exist, then STC is violated so return false
                        if (!existEdge) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
