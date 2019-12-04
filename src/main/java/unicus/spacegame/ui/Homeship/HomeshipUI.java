package unicus.spacegame.ui.Homeship;

import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.awt.*;

/**
 * The Homeship UI renders a 2D representation of the spaceship, its section frames and modules
 */
public class HomeshipUI extends GuiComponent {
    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height
     */
    protected HomeshipUI(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }
}
