package graphengine.algorithms;

import graphengine.Graph;
import graphengine.Vertex;

import java.util.LinkedList;

public class BFS {
    /**
     * Returns the graph of the BFS forest
     *
     * @param graph         input graph to perform BFS on
     * @param source        the source vertex
     * @return Graph of BFS forest
     */
    public static Graph runBFS(Graph graph, String source) {
        Graph forest = new Graph();
        // run BFS on the source vertex
        subBFS(graph, forest, source);

        // iterate through all vertices and run BFS on undiscovered vertices
        for (String vertex : graph.getVertices()) {
            if (!graph.getVertex(vertex).isDiscovered()) {
                subBFS(graph, forest, vertex);
            }
        }
        return forest;
    }

    /**
     * BFS Algorithm on a connected component rooted at the source node
     *
     * @param input     the input graph to run BFS on
     * @param output    the graph to store the BFS forest
     * @param source    the source vertex
     */
    private static void subBFS(Graph input, Graph output, String source) {
        Vertex sourceVertex = input.getVertex(source);

        output.addVertex(source);
        LinkedList<String> queue = new LinkedList<>();
        queue.addLast(source);
        sourceVertex.setDiscovered(true);

        while (!queue.isEmpty()) {
            String currVertex = queue.removeFirst();
            for (String neighbor : input.getVertex(currVertex).getNeighbors()) {
                Vertex neighborVertex = input.getVertex(neighbor);
                if (!neighborVertex.isDiscovered()) {
                    queue.addLast(neighbor);
                    neighborVertex.setDiscovered(true);
                    output.addDirectedEdge(currVertex, neighbor, null);
                }
            }
        }
    }
}
