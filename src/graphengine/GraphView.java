package graphengine;

import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

/**
 * The class used to construct the visual representation of a graph
 */
public class GraphView {
    /**
     * Adds a vertex to the visual representation of the graph
     *
     * @param graph     visual graph
     * @param vertex    the vertex to add
     */
    public static void addVertex(MultiGraph graph, String vertex) {
        try {
            graph.addNode(vertex).addAttribute("ui.label", vertex);
        } catch (IdAlreadyInUseException ignore) {}
    }

    /**
     * Deletes a vertex from the visual representation of the graph
     *
     * @param graph     visual graph
     * @param vertex    the vertex to delete
     */
    public static void deleteVertex(MultiGraph graph, String vertex) {
        graph.removeNode(vertex);
    }

    /**
     * Adds a directed edge to the visual representation of the graph
     *
     * @param graph         visual graph
     * @param vertex1       start vertex
     * @param vertex2       end vertex
     * @param tieStrength   tie strength of the edge
     */
    public static void addDirectedEdge(MultiGraph graph, String vertex1, String vertex2, Character tieStrength) {
        addVertex(graph, vertex1);
        addVertex(graph, vertex2);
        graph.addEdge(vertex1 + "." + vertex2, vertex1, vertex2, true).setAttribute("ui.label", tieStrength);
    }

    /**
     * Deletes a directed edge in the visual representation of the graph
     *
     * @param graph     visual graph
     * @param vertex1   start vertex
     * @param vertex2   end vertex
     */
    public static void deleteDirectedEdge(MultiGraph graph, String vertex1, String vertex2) {
        graph.removeEdge(vertex1 + "." + vertex2);
    }

    /**
     * Adds an undirected edge to the visual representation of the graph
     *
     * @param graph         visual graph
     * @param vertex1       a vertex
     * @param vertex2       a vertex
     * @param tieStrength   tie strength of the edge
     */
    public static void addUndirectedEdge(MultiGraph graph, String vertex1, String vertex2, Character tieStrength) {
        addVertex(graph, vertex1);
        addVertex(graph, vertex2);
        graph.addEdge(vertex1 + "." + vertex2, vertex1, vertex2, true).setAttribute("ui.label", tieStrength);
        graph.addEdge(vertex2 + "." + vertex1, vertex2, vertex1, true).setAttribute("ui.label", tieStrength);
    }

    /**
     * Deletes an undirected edge in the visual representation of the graph
     *
     * @param graph     visual graph
     * @param vertex1   a vertex
     * @param vertex2   a vertex
     */
    public static void deleteUndirectedEdge(MultiGraph graph, String vertex1, String vertex2) {
        graph.removeEdge(vertex1 + "." + vertex2);
        graph.removeEdge(vertex2 + "." + vertex1);
    }

    /**
     * Add a tree edge to the visual representation of the graph
     *
     * @param graph     visual graph
     * @param vertex1   a vertex
     * @param vertex2   a vertex
     */
    public static void addTreeEdge(MultiGraph graph, String vertex1, String vertex2) {
        addVertex(graph, vertex1);
        addVertex(graph, vertex2);
        graph.addEdge(vertex1 + "." + vertex2, vertex1, vertex2, false);
    }

    /**
     * Resets the visual representation of the graph
     *
     * @param graph         visual graph
     * @param styleSheet    style sheet for the visual graph
     */
    public static void reset(MultiGraph graph, String styleSheet) {
        graph.clear();
        graph.addAttribute("ui.stylesheet", styleSheet);
    }

    /**
     * Draws the visual representation of the input forest graph
     *
     * @param forest        the input forest graph
     * @param graphview     visual graph
     * @param algorithm     algorithm used to get forest
     * @param styleSheet    style sheet for the visual graph
     */
    public static void drawForest(Graph forest, MultiGraph graphview, GraphAlgorithm algorithm,
                                  String styleSheet) {
        // reset the visual graph to draw the new graph
        reset(graphview, styleSheet);
        SpriteManager spriteManager = new SpriteManager(graphview);

        for (String vertex : forest.getVertices()) {
            addVertex(graphview, vertex);
            // If the forest is a DFS forest, then draw the start and finish times
            if (algorithm == GraphAlgorithm.DFS) {
                Sprite time = spriteManager.addSprite(vertex + "time");
                Vertex vertexObj = forest.getVertex(vertex);
                time.attachToNode(vertex);
                time.setPosition(StyleConstants.Units.PX, Constants.START_FINISH_X, Constants.START_FINISH_Y, Constants.START_FINISH_Z);
                time.setAttribute("ui.label", vertexObj.getStartTime() + "/" + vertexObj.getFinishTime());
            }

            for (String neighbor : forest.getVertex(vertex).getNeighbors()) {
                addVertex(graphview, neighbor);
                addTreeEdge(graphview, vertex, neighbor);
            }
        }
    }
}
