package graphengine.algorithms;

import graphengine.Graph;
import graphengine.Vertex;

public class DFS {
    /**
     * Returns the graph of the DFS forest
     *
     * @param graph         input graph to run DFS on
     * @param source        the source vertex
     * @return Graph of DFS forest
     */
    public static Graph runDFS(Graph graph, String source) {
        Graph forest = new Graph();
        // run DFS on the source vertex
        dfsVisit(graph, forest, source);

        // iterate through all vertices and run DFS on undiscovered vertices
        for (String vertex : graph.getVertices()) {
            if (!graph.getVertex(vertex).isDiscovered()) {
                dfsVisit(graph, forest, vertex);
            }
        }
        return forest;
    }

    /**
     * Recursive method to go deep into a vertex's neighbor and assign start and
     * finish times to the vertices accordingly
     *
     * @param input     input graph
     * @param output    the graph to store the DFS forest
     * @param source    the source vertex
     */
    private static void dfsVisit(Graph input, Graph output, String source) {
        output.incrementTime();
        output.addVertex(source);
        output.getVertex(source).setStartTime(output.getCurrTime());

        Vertex currVertex = input.getVertex(source);
        currVertex.setDiscovered(true);

        for (String neighbor : currVertex.getNeighbors()) {
            if (!input.getVertex(neighbor).isDiscovered()) {
                output.addDirectedEdge(source, neighbor, null);
                dfsVisit(input, output, neighbor);
            }
        }
        output.incrementTime();
        output.getVertex(source).setFinishTime(output.getCurrTime());
    }
}
