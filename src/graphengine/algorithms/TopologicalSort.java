package graphengine.algorithms;

import graphengine.Graph;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

public class TopologicalSort {

    /**
     * Identifies the topological sorting of the graph
     *
     * @param graph     the graph to topological sort
     * @return a list of vertices in a topological sorting
     * @throws IllegalArgumentException if the graph is not a directed acyclic graph (DAG)
     */
    public static LinkedList<String> runTopologicalSort(Graph graph) {
        if (!isDAG(graph)) {
            throw new IllegalArgumentException("The Graph is Not a Directed Acyclic Graph (DAG)");
        }
        LinkedList<String> sorting = new LinkedList<>();
        Graph dfs = DFS.runDFS(graph, getRandomVertex(graph));
        HashMap<Integer, String> finishTimeToVertex = getFinishTimeToVertexMap(dfs);

        // add the finish times from the map into an array
        Integer[] finishTimes = new Integer[finishTimeToVertex.size()];
        int i = 0;
        for (int finishTime : finishTimeToVertex.keySet()) {
            finishTimes[i] = finishTime;
            i++;
        }
        // sort the finish times array in descending order
        Arrays.sort(finishTimes, Collections.reverseOrder());
        // add the vertices to the topological sorting in order of decreasing finishing time
        for (int finishTime : finishTimes) {
            sorting.addLast(finishTimeToVertex.get(finishTime));
        }
        return sorting;
    }

    /**
     * Checks if the graph is a directed acyclic graph (DAG)
     *
     * @param graph     the graph
     * @return true, if the graph is a DAG, false otherwise
     */
    private static boolean isDAG (Graph graph) {
        // -1 = Fully Discovered | 0 = Undiscovered | 1 = Being Processed
        HashMap<String, Integer> discovery = initDiscoveryMap(graph);
        LinkedList<String> stack = new LinkedList<>();
        // iterate through every vertex in the graph
        for (String vertex : graph.getVertices()) {
            // if the vertex is undiscovered
            if (discovery.get(vertex) == 0) {
                stack.addLast(vertex);
                while (!stack.isEmpty()) {
                    String currVertex = stack.removeLast();
                    // if the current vertex is undiscovered, mark it as discovered and put it back into the stack
                    if (discovery.get(currVertex) == 0) {
                        discovery.replace(currVertex, 1);
                        stack.addLast(currVertex);
                        // iterate through the neighbors of the current vertex
                        for (String neighbor : graph.getVertex(currVertex).getNeighbors()) {
                            // if a neighbor is undiscovered, add it to the stack to be processed later
                            if (discovery.get(neighbor) == 0) {
                                stack.addLast(neighbor);
                                // if the neighbor was being processed then there is a cycle, so return false
                            } else if (discovery.get(neighbor) == 1) {
                                return false;
                            }
                        }
                        // if we came back to a vertex that was being processed, then mark it as fully discovered
                    } else if (discovery.get(currVertex) == 1) {
                        discovery.replace(currVertex, -1);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns a hash map with all the vertices in the graph as keys with each value being 0
     *
     * @param graph     the graph
     * @return hash map
     */
    private static HashMap<String, Integer> initDiscoveryMap(Graph graph) {
        HashMap<String, Integer> discovery = new HashMap<>();
        for (String vertex : graph.getVertices()) {
            discovery.put(vertex, 0);
        }
        return discovery;
    }

    /**
     * Returns a random vertex from the graph
     *
     * @return string of a vertex
     */
    private static String getRandomVertex(Graph graph) {
        int size = graph.getSize();
        int randomInt = new Random().nextInt(size);
        String[] verticesArr = new String[size];
        int i = 0;
        for (String vertex : graph.getVertices()) {
            verticesArr[i] = vertex;
            i++;
        }
        return verticesArr[randomInt];
    }

    /**
     * Returns a hash map that maps the finish time of a vertex to the vertex
     *
     * @param graph     the graph
     * @return hash map
     */
    private static HashMap<Integer, String> getFinishTimeToVertexMap(Graph graph) {
        HashMap<Integer, String> map = new HashMap<>();
        for (String vertex : graph.getVertices()) {
            map.put(graph.getVertex(vertex).getFinishTime(), vertex);
        }
        return map;
    }
}
