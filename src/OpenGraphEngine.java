import graphengine.RunGraphEngine;

import javax.swing.SwingUtilities;

public class OpenGraphEngine {
    public static void main(String[] args) {
        Runnable graphEngine = new RunGraphEngine();
        SwingUtilities.invokeLater(graphEngine);
    }
}
