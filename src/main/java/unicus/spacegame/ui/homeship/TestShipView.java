package unicus.spacegame.ui.homeship;

import de.gurkenlabs.litiengine.*;
import de.gurkenlabs.litiengine.configuration.ClientConfiguration;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.spaceship.HomeShip;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class TestShipView extends Screen implements IUpdateable {
    Environment shipViewEnv;
    HomeshipGUI homeshipGUI;
    ConfigMenu configMenu;

    public static void main(String[] args) {
        Image cursor;
        try {

            //try loading file.
            cursor = ImageIO.read(Resources.getLocation("cursor1.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        } catch (IOException | IllegalArgumentException err) {
            //paint backup icon.
            System.out.println(err);
            cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics g = cursor.getGraphics();
            g.setColor(Color.red);
            g.drawOval(0, 0, 16, 16);
        }

        Game.init();
        Game.window().cursor().set(cursor, Align.CENTER, Valign.MIDDLE);
        URL spaceshipURL = TestShipView.class.getResource("spaceship.litidata");
        Resources.load(spaceshipURL);
        Input.mouse().setGrabMouse(false);
        Game.graphics().setBaseRenderScale(1.5f);

        SpaceCrew sc = SpaceCrew.GenerateStart1();
        HomeShip hs = HomeShip.GenerateStart1(new Random(0), 3, 10, 0.4f, 0.8f);
        TestShipView view = new TestShipView();
        Game.screens().display(view);
        Game.start();


        //Game.world().loadEnvironment("Spaceship");
        //Game.world().camera().setFocus(150, 100);

    }

    protected TestShipView() {
        super("TEST_SHIP_VIEW");
        configMenu = new ConfigMenu(0, 0, Game.window().getResolution().width, Game.window().getResolution().height/2.0 );
        getComponents().add(configMenu);
        configMenu.suspend();
        shipViewEnv = Game.world().getEnvironment("Spaceship");
    }

    /**
     * This method is called by the game loop on all objects that are attached to the loop.
     * It's called on every tick of the loop and the frequency can be configured using the <code>ClientConfiguration</code>.
     *
     * @see ClientConfiguration#setMaxFps(int)
     */
    @Override
    public void update() {

    }

    /**
     * This flag controls whether this instance is currently active and thereby needs to be updated by the game loop.
     *
     * @return True if this instance should be updated; otherwise false.
     */
    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void render(final Graphics2D g) {
        if (Game.world().environment() != null) {
            Game.world().environment().render(g);
        }

        super.render(g);
    }

    @Override
    public void prepare() {
        super.prepare();
        Game.world().reset("Spaceship");
        Game.world().loadEnvironment("Spaceship");
        homeshipGUI = new HomeshipGUI();
        shipViewEnv = Game.world().environment();
        shipViewEnv.add(homeshipGUI, RenderType.GROUND);
        homeshipGUI.drawMode = HomeshipGUI.HomeShipDrawMode.extruded;
        homeshipGUI.setSelectionFocus(6, 30, false);

        configMenu.open(HomeShip.getInstance().getShipLoc(1, 2));
    }

    class ConfigMenu extends GuiComponent {
        Menu menu;
        HomeShip.ShipLoc loc;
        /**
         * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
         *
         * @param x      the x
         * @param y      the y
         * @param width  the width
         * @param height
         */
        protected ConfigMenu(double x, double y, double width, double height) {
            super(x, y, width, height);

        }
        void open(HomeShip.ShipLoc loc) {
            menu = new Menu(0, 0, getWidth()/5, getHeight(), "test1", "test2", "test3", "test4");
            this.loc = loc;
            this.prepare();
            this.getComponents().add(menu);
            menu.prepare();
            homeshipGUI.drawMode = HomeshipGUI.HomeShipDrawMode.cutout;
            //Point2D focus = homeshipGUI.getSectionFocusPoint(loc.getS());
            homeshipGUI.setSelectionFocus(6, 30, true);

            //focus.setLocation(focus.getX(), focus.getY() - getHeight()/4);
            //Game.world().camera().setFocus(focus);
        }
        void close() {
            this.getComponents().remove(menu);
            this.suspend();
            this.menu = null;

            //Point2D focus = homeshipGUI.getSectionFocusPoint(loc.getS());
            //Game.world().camera().setFocus(focus);
        }

        @Override
        public void render(Graphics2D _g) {
            Graphics2D g = (Graphics2D)_g.create();

            g.setColor(new Color(0,0,150,100));
            g.fillRect(0, 0, (int)getWidth(), (int)getHeight());

            g.translate((int)getWidth()/5, 0);
            g.setColor(Color.white);
            //g.fillRect(0,0,50,50);
            g.drawString("Hello world", 0, 10);

            g.dispose();
            super.render(_g);
        }
    }
}
