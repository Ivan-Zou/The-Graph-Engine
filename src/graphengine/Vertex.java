package graphengine;

import java.util.HashMap;
import java.util.Set;

public class Vertex {
    // A Hash Map that maps a neighbor of this vertex to the strength of the tie this vertex has with the neighbor
    HashMap<String, Character> neighbors;

    // tracks if this vertex has been discovered in the graph traversal
    private boolean discovered;

    // tracks start and end times for DFS
    private int startTime;
    private int finishTime;

    public Vertex() {
        this.neighbors = new HashMap<>();
        this.discovered = false;
        this.startTime = -1;
        this.finishTime = -1;
    }

    /**
     * Add a directed edge from this vertex to a specified vertex with the strength of the new edge
     *
     * @param newNeighbor       the vertex to create a new edge to
     * @param tieStrength       the strength of the new edge
     */
    public void addEdge(String newNeighbor, Character tieStrength) {
        neighbors.put(newNeighbor, tieStrength);
    }

    /**
     * Delete a directed edge from this vertex to a specified vertex
     *
     * @param neighbor       the vertex to delete the edge to
     */
    public void deleteEdge(String neighbor) {
        neighbors.remove(neighbor);
    }

    /**
     * Checks if this vertex has an edge to a specified vertex
     * @param neighbor      the vertex to check
     * @return true, if this vertex has an edge to neighbor, false otherwise
     */
    public boolean hasEdge(String neighbor) {
        return neighbors.containsKey(neighbor);
    }

    // GETTER METHODS

    public boolean isDiscovered() {
        return discovered;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    /**
     * Returns the strength of the tie from this vertex to a specified vertex
     *
     * @param neighbor      the vertex
     * @return the tie strength ('S' for Strong, 'W' for Weak)
     * @throws IllegalArgumentException if there is no edge from this vertex to neighbor
     */
    public char getTieStrength(String neighbor) {
        if (hasEdge(neighbor)) {
            return neighbors.get(neighbor);
        } else {
            throw new IllegalArgumentException("This Tie Does Not Exist");
        }
    }

    /**
     * Returns all the neighbors of this vertex
     *
     * @return neighbors of this vertex
     */
    public Set<String> getNeighbors() {
        return neighbors.keySet();
    }

    // SETTER METHODS

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * Resets the vertex
     */
    public void reset() {
        this.discovered = false;
        this.startTime = -1;
        this.finishTime = -1;
    }
}
