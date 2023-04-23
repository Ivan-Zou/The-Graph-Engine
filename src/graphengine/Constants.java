package graphengine;

import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;

/*
 * A class that stores all the constants used in this program
 */
public class Constants {
    public static final Rectangle WINDOW_SIZE = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    public static final int WINDOW_WIDTH = (int) (WINDOW_SIZE.width * 0.9);
    public static final int WINDOW_HEIGHT = (int) (WINDOW_SIZE.height * 0.9);

    public static final int SPACER_WIDTH = 20;

    public static final int TEXT_FIELD_LENGTH = 5;

    public static final double START_FINISH_X = 50;
    public static final double START_FINISH_Y = 0;
    public static final double START_FINISH_Z = 0;
}
