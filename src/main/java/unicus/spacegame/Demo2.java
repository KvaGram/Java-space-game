package unicus.spacegame;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.ui.DebugConsole;
import unicus.spacegame.ui.homeship.HomeshipUI;
import unicus.spacegame.ui.homeship.HomeshipUIController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Demo2 is a second proof of concept demo/prototype test.
 * Where the first version were barebones ui components built on swing patched together into its own thing,
 * this one will be slightly more planned, and built on Litiengine
 *
 * Note that this demo might break once we enter production, or a Demo3 is started.
 *
 * Completed features for this demo:
 *
 *
 * Planned features for this demo:
 *      Game turns / months (time progressions)
 *      Crew management
 *      crew housing
 *      Crew job assignment
 *      production queue / tasklists (jobs)
 *      Spaceship refit
 *      Star-travel / hyperspeed
 *      (stub) Foraging expeditions (planets)
 *      (stub) Searching debris
 *      (stub) Mining asteroids
 *      Starship combat (minigame proof of concept)
 *
 * Planned features not to include:
 *      Cargo resource management
 *      Spaceship module upgrades
 *      Spaceship weapon layout
 *      Local diplomacy (alien lords)
 *      Alien native life
 *      Scripted events (predictable happenings)
 *      Random events (slice-of-life, unexpected happenings)
 *      Crew skill-levels
 *
 *      and more
 *
 *
 */


class Demo2 implements IUpdateable {

    ShipRefitController shipView;

    Demo2(String[] args) {
    }

    public static void main(String[] args) {
        Demo2 demo = new Demo2(args);
        demo.init();
        demo.run();
    }

    private void init() {
        Game.setInfo("gameinfo.xml");
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

        Random r = new Random(0);
        SpaceCrew spaceCrew = SpaceCrew.GenerateStart1(); //(STUB!)
        HomeShip homeship = HomeShip.GenerateStart1(r, 8, 20, .2f, .8f);
        Game.init();
        Game.window().cursor().set(cursor, 8, 8);
        Input.mouse().setGrabMouse(false);

        shipView = new ShipRefitController("SHIPVIEW", homeship);

        DebugConsole console = new DebugConsole(homeship);
        console.run();
        console.addCrewCommands();
        Game.loop().attach(console);
    }


    private void run() {
        Game.start();
        Game.screens().display("SHIPVIEW");
        //TODO: add and display brigadier
    }

    /**
     * This method is called by the game loop on all objects that need to update
     * their attributes. It is called on every tick, means, it is called
     * Game.GameLoop.TICKS_PER_SECOND times per second.
     */
    @Override
    public void update() {

    }
}

/** The situation screen acts as sort of a hub for all the functions of the game.
 * It also provides summaries for the current situation of the ship and crew.
 * In-story it is located in the command-center of the front section of the ship.
 */

class SituationScreen extends GameScreen {
    public SituationScreen(String screenName) {
        super(screenName);
    }
    @Override
    public void render(final Graphics2D g) {
        super.render(g);
        //...
    }
}
/**
 * The ship refit screen is an interface to select changes to The Homeship
 */
class ShipRefitController extends GameScreen implements IUpdateable, HomeshipUIController {
    private HomeShip homeShip;
    private HomeshipUI homeshipUI;

    public ShipRefitController(String screenName, HomeShip homeShip) {
        super(screenName);
        this.homeShip = homeShip;

        homeshipUI = new HomeshipUI(homeShip, 0, 0, Game.window().getWidth(), Game.window().getHeight());
        getComponents().add(homeshipUI);
        Game.loop().attach(this);
        Game.screens().add(this);
    }

    /**
     * This method is called by the game loop on all objects that need to update
     * their attributes. It is called on every tick, means, it is called
     * Game.GameLoop.TICKS_PER_SECOND times per second.
     */
    @Override
    public void update() {
        if(isSuspended())
            return;
        //System.out.println("Hello world");

    }

    @Override
    public void render(final Graphics2D g) {
        super.render(g);
    }


    @Override
    public void onSelected(HomeShip.ShipLoc loc) {
        //TODO what to do when a section, module or component is selected.
        //Expected to run homeshipUI.openMenu
    }
}


