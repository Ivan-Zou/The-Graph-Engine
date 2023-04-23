package graphengine;

import java.util.HashMap;
import java.util.Set;

public class Graph {
    // A Hash Map that maps a vertex label to its corresponding vertex object
    private HashMap<String, Vertex> adjacencyList;

    // tracks the number of vertices in this graph
    private int size;

    // tracks the current time in the DFS traversal
    private int currTime;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.size = 0;
        this.currTime = 0;
    }

    /**
     * Returns the graph represented by an adjacency list
     *
     * @return adjacency list
     */
    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    /**
     * Returns the number of vertices in this graph
     *
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the current time
     *
     * @return currTime
     */
    public int getCurrTime() {
        return currTime;
    }

    /**
     * Increase currTime by 1
     */
    public void incrementTime() {
        currTime++;
    }

    /**
     * Returns the vertex object of the vertex label
     *
     * @param vertex    vertex label
     * @return vertex object
     */
    public Vertex getVertex(String vertex) {
        return adjacencyList.get(vertex);
    }

    /**
     * Checks if the specified vertex is in the graph
     *
     * @param vertex        the vertex to check
     * @return true, if vertex is in the graph, false otherwise
     */
    public boolean hasVertex(String vertex) {
        return adjacencyList.containsKey(vertex);
    }

    /**
     * Adds a vertex to this graph if it is not already in this graph
     *
     * @param vertex the vertex to add
     * @return true, if the vertex was successfully added, false otherwise
     */
    public boolean addVertex(String vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new Vertex());
            size++;
            return true;
        }
        return false;
    }

    /**
     * Deletes a vertex to this graph if it is in this graph
     *
     * @param vertex the vertex to delete
     * @return true, if the vertex was successfully deleted, false if the vertex is not in the graph
     */
    public boolean deleteVertex(String vertex) {
        if (adjacencyList.containsKey(vertex)) {
            adjacencyList.remove(vertex);
            size--;
            for (Vertex currVertex : adjacencyList.values()) {
                currVertex.deleteEdge(vertex);
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if there is a directed edge from one vertex to another vertex
     *
     * @param vertex1       the starting vertex
     * @param vertex2       the ending vertex
     * @return true, if the directed edge exists, false otherwise
     * @throws IllegalArgumentException if a specified vertex is not in the graph
     */
    public boolean hasDirectedEdge(String vertex1, String vertex2) {
        if (adjacencyList.containsKey(vertex1) && adjacencyList.containsKey(vertex2)) {
            return adjacencyList.get(vertex1).hasEdge(vertex2);
        }
        throw new IllegalArgumentException("A Specified Vertex Does Not Exist");
    }

    /**
     * Adds a directed edge to the graph
     *
     * @param vertex1       the starting vertex
     * @param vertex2       the ending vertex
     * @param tieStrength   the tie strength
     * @return true, if the directed edge was successfully added to the graph, false otherwise
     * @throws IllegalArgumentException if the input vertices are the same
     */
    public boolean addDirectedEdge(String vertex1, String vertex2, Character tieStrength) {
        if (vertex1.equals(vertex2)) {
            throw new IllegalArgumentException("Can Not Add an Edge From a Vertex to Itself");
        }
        addVertex(vertex1);
        addVertex(vertex2);
        if (!hasDirectedEdge(vertex1, vertex2)) {
            adjacencyList.get(vertex1).addEdge(vertex2, tieStrength);
            return true;
        }
        return false;
    }

    /**
     * Deletes a directed edge in the graph
     *
     * @param vertex1       the starting vertex
     * @param vertex2       the ending vertex
     * @return true, if the directed edge was successfully deleted to the graph, false otherwise
     * @throws IllegalArgumentException if the vertices are not in the graph
     */
    public boolean deleteDirectedEdge(String vertex1, String vertex2) {
        if (!(adjacencyList.containsKey(vertex1) && adjacencyList.containsKey(vertex2))) {
            throw new IllegalArgumentException("A Specified Vertex Does Not Exist");
        }
        if (hasDirectedEdge(vertex1, vertex2)) {
            adjacencyList.get(vertex1).deleteEdge(vertex2);
            return true;
        }
        return false;
    }

    /**
     * Checks if there is an undirected edge between two specified vertices
     *
     * @param vertex1       a vertex
     * @param vertex2       a vertex
     * @return true, if the undirected edge exists, false otherwise
     * @throws IllegalArgumentException if a specified vertex is not in the graph
     */
    public boolean hasUndirectedEdge(String vertex1, String vertex2) {
        return hasDirectedEdge(vertex1, vertex2) && hasDirectedEdge(vertex2, vertex1);
    }

    /**
     * Adds an undirected edge to the graph
     *
     * @param vertex1       a vertex
     * @param vertex2       a vertex
     * @param tieStrength   the tie strength
     * @return true, if the undirected edge was successfully added in the graph, false otherwise
     * @throws IllegalArgumentException if the input vertices are the same
     */
    public boolean addUndirectedEdge(String vertex1, String vertex2, Character tieStrength) {
        if (vertex1.equals(vertex2)) {
            throw new IllegalArgumentException("Can Not Add an Edge From a Vertex to Itself");
        }
        addVertex(vertex1);
        addVertex(vertex2);
        if (!(hasDirectedEdge(vertex1, vertex2) || hasDirectedEdge(vertex2, vertex1))) {
            adjacencyList.get(vertex1).addEdge(vertex2, tieStrength);
            adjacencyList.get(vertex2).addEdge(vertex1, tieStrength);
            return true;
        }
        return false;
    }


    /**
     * Deletes an undirected edge in the graph
     *
     * @param vertex1       a vertex
     * @param vertex2       a vertex
     * @return true, if the undirected edge was successfully deleted in the graph, false otherwise
     * @throws IllegalArgumentException if the specified vertices are not in the graph
     */
    public boolean deleteUndirectedEdge(String vertex1, String vertex2) {
        if (!(adjacencyList.containsKey(vertex1) && adjacencyList.containsKey(vertex2))) {
            throw new IllegalArgumentException("A Specified Vertex Does Not Exist");
        }
        if (hasUndirectedEdge(vertex1, vertex2)) {
            adjacencyList.get(vertex1).deleteEdge(vertex2);
            adjacencyList.get(vertex2).deleteEdge(vertex1);
            return true;
        }
        return false;
    }

    /**
     * Returns the tie strength between two specified vertices
     *
     * @param vertex1       a vertex
     * @param vertex2       a vertex
     * @return the tie strength between vertex1 and vertex2
     * @throws IllegalArgumentException if a specified vertex is not in the graph, or the edge does not exist
     */
    public char getTieStrength(String vertex1, String vertex2) {
        if (adjacencyList.containsKey(vertex1) && adjacencyList.containsKey(vertex2)) {
            return adjacencyList.get(vertex1).getTieStrength(vertex2);
        }
        throw new IllegalArgumentException("A Specified Vertex Does Not Exist");
    }

    /**
     * Resets the Graph
     */
    public void reset() {
        this.adjacencyList = new HashMap<>();
        this.size = 0;
        this.currTime = 0;
    }

    /**
     * Resets all the vertices in the graph
     */
    public void resetVertices() {
        for (String vertex : adjacencyList.keySet()) {
            getVertex(vertex).reset();
        }
    }

    /**
     * Returns the string representation of this graph
     *
     * @return String of this graph
     */
    @Override
    public String toString() {
        StringBuilder graph = new StringBuilder();
        for (String vertex : adjacencyList.keySet()) {
            graph.append(vertex).append(" -> [");
            for (String neighbor : adjacencyList.get(vertex).getNeighbors()) {
                graph.append(neighbor).append(", ");
            }
            graph.append("]\n");
        }
        return graph.toString();
    }
}
