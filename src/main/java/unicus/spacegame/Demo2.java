package unicus.spacegame;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import org.apache.commons.lang3.ArrayUtils;
import unicus.spacegame.spaceship.Spaceship;

import java.awt.*;

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


public class Demo2 implements IUpdateable {


    public static void main(String[] args) {
        new Demo2().run();
    }



    private void run() {

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
abstract class ControlScreen extends Screen implements IUpdateable{
    private boolean active = false;

    public ControlScreen(String screenName){
        super(screenName);
        gameScreens = ArrayUtils.add(gameScreens, this);
    }

    public void setAsActive() {
        ControlScreen.setActive(this);
    }
    protected abstract void onClose();
    protected abstract void onOpen();

    private static ControlScreen[] gameScreens = new ControlScreen[0];
    private static ControlScreen currentScreen() {
        for (ControlScreen gc : gameScreens) {
            if (gc.active)
                return gc;
        }
        return null;
    }
    private static void setActive(ControlScreen as){
        ControlScreen cs = currentScreen();
        if(cs == as)
            return;
        if(cs != null){
            cs.active = false;
            Game.loop().detach(cs);
            cs.onClose();
        }
        as.active = true;
        Game.screens().display(as);
        as.onOpen();
        Game.loop().attach(as);

    }

    @Override
    public void prepare()
    {
        //Intercept the preparation of this screen to confirm
        // that this is the active screen. Failing that, set it so.
        ControlScreen cs = currentScreen();
        if(cs != this) {
            if(cs != null) {
                cs.active = false;
                Game.loop().detach(cs);
                cs.onClose();
            }
            this.active = true;
            this.onOpen();
            Game.loop().attach(this);
        }
        //resume the UI preparation
        super.prepare();
    }
}


/** The situation screen acts as sort of a hub for all the functions of the game.
 * It also provides summaries for the current situation of the ship and crew.
 * In-story it is located in the command-center of the front section of the ship.
 */

class SituationScreen extends Screen {
    public SituationScreen(String screenName) {
        super(screenName);
    }
    @Override
    public void render(final Graphics2D g) {
        super.render(g);
        //...
    }
}

class ShipRefitController extends ControlScreen {
    private Spaceship homeShip;

    public ShipRefitController(String screenName, Spaceship spaceship) {
        super(screenName);

        this.homeShip = spaceship;
    }



    @Override
    protected void onClose() {

    }

    @Override
    protected void onOpen() {

    }

    /**
     * This method is called by the game loop on all objects that need to update
     * their attributes. It is called on every tick, means, it is called
     * Game.GameLoop.TICKS_PER_SECOND times per second.
     */
    @Override
    public void update() {

    }

    @Override
    public void render(final Graphics2D g) {
        super.render(g);
        //...
    }



    void addBuildWeaponTask( int sectionID, int weaponSlot, WeaponType weaponType) {
        RefitTask task = new RefitTask(100, RefitType.BuildWeapon, sectionID, weaponSlot, weaponType.toInt());
    }



}

/**
 * The ship refit screen is an interface to select changes to The Homeship
 */
