package graphengine;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import graphengine.algorithms.BFS;
import graphengine.algorithms.DFS;
import graphengine.algorithms.IdentifySTC;
import graphengine.algorithms.TopologicalSort;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

public class RunGraphEngine implements Runnable {

    private enum ConstructGraph {
        ADD_VERTEX, DELETE_VERTEX,
        ADD_DIRECTED_EDGE, DELETE_DIRECTED_EDGE,
        ADD_UNDIRECTED_EDGE, DELETE_UNDIRECTED_EDGE
    }

    private JFrame mainframe;
    private JFrame outputFrame;

    private JTextField vertexTextField;
    private JTextField vertex1TextField;
    private JTextField vertex2TextField;
    private TextField sourceTextField;

    private JComboBox<String> tieStrengthList;

    private Graph inputGraph;
    private MultiGraph inputGraphView;

    private MultiGraph outputGraphView;

    protected String styleSheet =
            "node {" +
                    "fill-color: #bababa;" +
                    "size: 35px;" +
                    "fill-mode: dyn-plain;" +
                    "stroke-color: black;" +
                    "text-size: 25px;" +
                    "text-style: bold;" +
                    "}" +
            "edge {" +
                    "text-size: 25px;" +
                    "text-background-mode: rounded-box;"+
                    "text-background-color: white;"+
                    "}" +
            "sprite {" +
                    "fill-color: white;" +
                    "fill-mode: dyn-plain;" +
                    "shape: box;" +
                    "size: 30px;" +
                    "text-size: 20px;" +
                    "}";

    @Override
    public void run() {
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIManager.put("Button.disabledText", Color.black);
        initGUI();
    }

    /**
     * Initializes the GUI for this program
     */
    private void initGUI() {
        // The main Screen where the user will construct their graph and select the algorithms
        mainframe = new JFrame("The Graph Engine");

        // A panel that contains all the graph construction components
        final JPanel constructGraphPanel = new JPanel();
        mainframe.add(constructGraphPanel, BorderLayout.NORTH);

        // input box and buttons to add/delete a vertex from the graph
        constructGraphPanel.add(new JLabel("Vertex: "));
        vertexTextField = new JTextField(Constants.TEXT_FIELD_LENGTH);
        constructGraphPanel.add(vertexTextField);
        // add vertex button
        final JButton addVertexButton = new JButton("Add Vertex");
        addVertexButton.addActionListener(eventAction -> updateInputGraph(ConstructGraph.ADD_VERTEX));
        constructGraphPanel.add(addVertexButton);
        // delete vertex button
        final JButton deleteVertexButton = new JButton("Delete Vertex");
        deleteVertexButton.addActionListener(eventAction -> updateInputGraph(ConstructGraph.DELETE_VERTEX));
        constructGraphPanel.add(deleteVertexButton);

        // Spacer component
        constructGraphPanel.add(Box.createRigidArea(new Dimension(Constants.SPACER_WIDTH, 0)));

        // input box and buttons to add/delete an edge (directed or undirected) from the graph
        constructGraphPanel.add(new JLabel("Edge: "));
        vertex1TextField = new JTextField(Constants.TEXT_FIELD_LENGTH);
        constructGraphPanel.add(vertex1TextField);
        constructGraphPanel.add(new JLabel(" - "));
        vertex2TextField = new JTextField(Constants.TEXT_FIELD_LENGTH);
        constructGraphPanel.add(vertex2TextField);
        // drop down box to select tie strength of the edge
        final String[] tieStrengthOptions = {"Weak", "Strong"};
        tieStrengthList = new JComboBox<>(tieStrengthOptions);
        constructGraphPanel.add(new JLabel("Tie Strength: "));
        constructGraphPanel.add(tieStrengthList);
        // add directed edge button
        final JButton addDirectedEdgeButton = new JButton("Add Directed Edge");
        addDirectedEdgeButton.addActionListener(actionEvent -> updateInputGraph(ConstructGraph.ADD_DIRECTED_EDGE));
        constructGraphPanel.add(addDirectedEdgeButton);
        // delete directed edge button
        final JButton deleteDirectedEdgeButton = new JButton("Delete Directed Edge");
        deleteDirectedEdgeButton.addActionListener(eventAction -> updateInputGraph(ConstructGraph.DELETE_DIRECTED_EDGE));
        constructGraphPanel.add(deleteDirectedEdgeButton);
        // add undirected edge button
        final JButton addUndirectedEdgeButton = new JButton("Add Undirected Edge");
        addUndirectedEdgeButton.addActionListener(eventAction -> updateInputGraph(ConstructGraph.ADD_UNDIRECTED_EDGE));
        constructGraphPanel.add(addUndirectedEdgeButton);
        // delete undirected edge button
        final JButton deleteUndirectedEdgeButton = new JButton("Delete Undirected Edge");
        deleteUndirectedEdgeButton.addActionListener(eventAction -> updateInputGraph(ConstructGraph.DELETE_UNDIRECTED_EDGE));
        constructGraphPanel.add(deleteUndirectedEdgeButton);

        // Spacer component
        constructGraphPanel.add(Box.createRigidArea(new Dimension(Constants.SPACER_WIDTH, 0)));

        // reset graph button
        final JButton resetGraphButton = new JButton("Reset Graph");
        resetGraphButton.addActionListener(eventAction -> resetGraph());
        constructGraphPanel.add(resetGraphButton);

        // Graph constructed by the user
        inputGraph = new Graph();
        // The visual representation of the user constructed graph
        inputGraphView = new MultiGraph("Graph");
        inputGraphView.addAttribute("ui.stylesheet", styleSheet);
        Viewer inputGraphViewer = new Viewer(inputGraphView, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        inputGraphViewer.enableAutoLayout();
        JPanel graphPanel = inputGraphViewer.addDefaultView(false);
        mainframe.add(graphPanel, BorderLayout.CENTER);

        // A panel that contains all the algorithm components
        final JPanel algorithmPanel = new JPanel();
        mainframe.add(algorithmPanel, BorderLayout.SOUTH);

        // input box for the source vertex
        algorithmPanel.add(new JLabel("Source: "));
        sourceTextField = new TextField(Constants.TEXT_FIELD_LENGTH);
        algorithmPanel.add(sourceTextField);

        // Spacer component
        algorithmPanel.add(Box.createRigidArea(new Dimension(Constants.SPACER_WIDTH, 0)));

        // BFS button
        final JButton bfsButton = new JButton("BFS");
        bfsButton.setFocusable(false);
        bfsButton.addActionListener(actionEvent -> runAlgorithm(GraphAlgorithm.BFS));
        algorithmPanel.add(bfsButton);

        // Spacer in between buttons
        algorithmPanel.add(Box.createRigidArea(new Dimension(Constants.SPACER_WIDTH, 0)));

        // DFS button
        final JButton dfsButton = new JButton("DFS");
        dfsButton.setFocusable(false);
        dfsButton.addActionListener(actionEvent -> runAlgorithm(GraphAlgorithm.DFS));
        algorithmPanel.add(dfsButton);

        // Spacer in between buttons
        algorithmPanel.add(Box.createRigidArea(new Dimension(Constants.SPACER_WIDTH, 0)));

        // Topological Sort button
        final JButton topoButton = new JButton("Topological Sort");
        topoButton.setFocusable(false);
        topoButton.addActionListener(actionEvent -> runAlgorithm(GraphAlgorithm.TOPOLOGICAL_SORT));
        algorithmPanel.add(topoButton);

        // Spacer in between buttons
        algorithmPanel.add(Box.createRigidArea(new Dimension(Constants.SPACER_WIDTH, 0)));

        // Identify STC button
        final JButton stcButton = new JButton("Identify STC");
        stcButton.setFocusable(false);
        stcButton.addActionListener(actionEvent -> runAlgorithm(GraphAlgorithm.IDENTIFY_STC));
        algorithmPanel.add(stcButton);

        mainframe.pack();
        mainframe.setVisible(true);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // window for the result of the BFS and DFS algorithm
        outputFrame = new JFrame("Algorithm Output");

        outputGraphView = new MultiGraph("Tree");
        outputGraphView.addAttribute("ui.stylesheet", styleSheet);
        Viewer outputGraphViewer = new Viewer(outputGraphView, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        outputGraphViewer.enableAutoLayout();
        JPanel outputPanel = outputGraphViewer.addDefaultView(false);
        outputFrame.add(outputPanel);

        outputFrame.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        outputFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Updates the input graph depending on the inputs of the user
     *
     * @param operation     the operation to perform on the input graph
     */
    private void updateInputGraph(ConstructGraph operation) {
        try {
            if (operation == ConstructGraph.ADD_VERTEX) {
                // get input vertex from text field
                String vertex = vertexTextField.getText();
                if (!inputGraph.addVertex(vertex)) {
                    throw new IllegalArgumentException("This Vertex Already Exists");
                }
                // if a vertex was successfully added to the input graph, then add it to the graph representation
                GraphView.addVertex(inputGraphView, vertex);
            } else if (operation == ConstructGraph.DELETE_VERTEX) {
                // get input vertex from text field
                String vertex = vertexTextField.getText();
                if (!inputGraph.deleteVertex(vertex)) {
                    throw new IllegalArgumentException("This Vertex Does Not Exist");
                }
                // If a vertex was successfully deleted from the input graph, then delete it from the graph representation
                GraphView.deleteVertex(inputGraphView, vertex);
            } else if (operation == ConstructGraph.ADD_DIRECTED_EDGE) {
                // get inputs from text fields
                String vertex1 = vertex1TextField.getText();
                String vertex2 = vertex2TextField.getText();
                String chosenTieStrength = (String) tieStrengthList.getSelectedItem();
                String tieStrengthNullCheck = chosenTieStrength != null ? chosenTieStrength : "Weak";
                Character tieStrength = tieStrengthNullCheck.equals("Weak") ? 'W' : 'S';

                if (!inputGraph.addDirectedEdge(vertex1, vertex2, tieStrength)) {
                    throw new IllegalArgumentException("This Directed Edge Already Exists");
                }
                // If an edge was successfully added to the input graph, then add it to the graph representation
                GraphView.addDirectedEdge(inputGraphView, vertex1, vertex2, tieStrength);

            } else if (operation == ConstructGraph.DELETE_DIRECTED_EDGE){
                // get inputs from text fields
                String vertex1 = vertex1TextField.getText();
                String vertex2 = vertex2TextField.getText();
                if (!inputGraph.deleteDirectedEdge(vertex1, vertex2)) {
                    throw new IllegalArgumentException("This Directed Edge Does Not Exist");
                }
                // If an edge was successfully deleted from the input graph, then delete it from the graph representation
                GraphView.deleteDirectedEdge(inputGraphView, vertex1, vertex2);
            } else if (operation == ConstructGraph.ADD_UNDIRECTED_EDGE) {
                // get inputs from text fields
                String chosenTieStrength = (String) tieStrengthList.getSelectedItem();
                String tieStrengthNullCheck = chosenTieStrength != null ? chosenTieStrength : "Weak";
                Character tieStrength = tieStrengthNullCheck.equals("Weak") ? 'W' : 'S';
                String vertex1 = vertex1TextField.getText();
                String vertex2 = vertex2TextField.getText();

                if (!inputGraph.addUndirectedEdge(vertex1, vertex2, tieStrength)) {
                    throw new IllegalArgumentException("An Edge Already Exists Between These Two Vertices");
                }
                // If an edge was successfully added to the input graph, then add it to the graph representation
                GraphView.addUndirectedEdge(inputGraphView, vertex1, vertex2, tieStrength);

            }  else if (operation == ConstructGraph.DELETE_UNDIRECTED_EDGE) {
                // get inputs from text fields
                String vertex1 = vertex1TextField.getText();
                String vertex2 = vertex2TextField.getText();

                if (!inputGraph.deleteUndirectedEdge(vertex1, vertex2)) {
                    throw new IllegalArgumentException("This Undirected Edge Does Not Exist");
                }
                // If an edge was successfully deleted from the input graph, then delete it from the graph representation
                GraphView.deleteUndirectedEdge(inputGraphView, vertex1, vertex2);
            }
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog( new JDialog(mainframe, "ERROR", true), exception.getMessage());
            return;
        }
        // Reset the text all of the Text Fields
        vertexTextField.setText("");
        vertex1TextField.setText("");
        vertex2TextField.setText("");
    }

    /**
     * Reset the input graph and the visual representation of the graph
     */
    public void resetGraph() {
        inputGraph.reset();
        GraphView.reset(inputGraphView, styleSheet);
    }

    /**
     * Execute algorithms on the input graph and display the results based on user input
     *
     * @param algorithm     the algorithm to execute on the input graph
     */
    public void runAlgorithm(GraphAlgorithm algorithm) {
        JDialog errMessage = new JDialog(mainframe, "ERROR", true);
        if (inputGraph.getSize() == 0) {
            JOptionPane.showMessageDialog(errMessage, "The Graph is Empty");
            return;
        }
        // reset the times and discovery of all the vertices of the input graph
        inputGraph.resetVertices();
        Graph outputGraph = new Graph();
        try {
            if (algorithm == GraphAlgorithm.BFS) {
                // get source vertex from text field
                String source = sourceTextField.getText();

                if (source == null || !inputGraph.hasVertex(source)) {
                    throw new IllegalArgumentException("Source Vertex Does Not Exist");
                }
                outputGraph = BFS.runBFS(inputGraph, source);
            } else if (algorithm == GraphAlgorithm.DFS) {
                // get source vertex from text field
                String source = sourceTextField.getText();

                if (source == null || !inputGraph.hasVertex(source)) {
                    throw new IllegalArgumentException("Source Vertex Does Not Exist");
                }
                outputGraph = DFS.runDFS(inputGraph, source);
            } else if (algorithm == GraphAlgorithm.TOPOLOGICAL_SORT) {
                LinkedList<String> topologicalSorting = TopologicalSort.runTopologicalSort(inputGraph);
                JOptionPane.showMessageDialog(new JDialog(mainframe, "Topological Sorting", true),
                        topologicalSorting.toString());
                return;
            } else if (algorithm == GraphAlgorithm.IDENTIFY_STC) {
                LinkedList<String>[] results = IdentifySTC.identifySTC(inputGraph);
                String output = "Vertices Satisfying STC: " + results[0].toString() +
                                "\nVertices Violating STC: " + results[1].toString();
                JOptionPane.showMessageDialog(new JDialog(mainframe, "STC Identification", true), output);
                return;
            }
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog( new JDialog(mainframe, "ERROR", true), exception.getMessage());
            sourceTextField.setText("");
            return;
        }
        sourceTextField.setText("");
        GraphView.drawForest(outputGraph, outputGraphView, algorithm, styleSheet);
        outputFrame.setVisible(true);
    }
}
