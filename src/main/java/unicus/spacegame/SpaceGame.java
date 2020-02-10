package unicus.spacegame;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.configuration.ClientConfiguration;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.gameevent.GameEvent;
import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.ui.DebugConsole;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Month;
import java.util.Random;

import static unicus.spacegame.ui.homeship.ShipViewController.SVC;

/**
 * Main game class.
 */
public class SpaceGame implements IUpdateable {
    // (subject to change) Start of game: March 2104
    private static final int START_YEAR = 2104;
    private static final int START_MONTH = 3;

    private static SpaceGame SG;
    private static DebugConsole debugConsole;
    private static HomeShip homeShip;
    private static SpaceCrew spaceCrew;

    private static int GameMonth;

    public static SpaceGame SG() {
        if(SG == null)
            new SpaceGame();
        return SG;
    }
    public SpaceGame() {
        SG = this;
    }

    private void startGame() {
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
        SpaceCrew.GenerateStart1();
        HomeShip.GenerateStart1(r, 6, 0.75f);
        Game.init();
        Game.window().cursor().set(cursor, 8, 8);
        Input.mouse().setGrabMouse(false);

        GenerateStart1();


        Game.loop().attach(debugConsole);
        Game.loop().attach(this);
        Game.start();
        //open the ship-view controller
        Game.screens().display(SVC());
    }

    public static int getGameMonth() {
        return GameMonth;
    }

    /**
     * Run the end-of-month cycle, and advance the game-month (aka game-turn).
     *
     * Update order in the end of month cycle
     *  1: Job assignments, jobs housing.
     *  2: Homeship structure, module and amenities.
     *  3: Crew morale and needs.
     */
    public static void NextMonth() {
        spaceCrew.endOfMonthJobsHousing();
        homeShip.endOfMonth();
        spaceCrew.endOfMonthCrew();

        GameMonth ++;
    }


    /**
     *  Generates a fairly typical start scenario.
     *  Start with a crew of 10 adults with high stats.
     *      6 ship crew with existing roles, 4 passengers without any roles.
     *  Start with a ship of a middle length of 6 (total length of 8)
     *  Start with 1 wheel section with two habitat modules.
     *  Start with 20 cargo modules installed
     *  Start with ? tons of common metal.
     *  Start with ? tons of precious metal.
     *  Start with 24 months worth of food (number of crew).
     *  Start with 36 months worth of water (number of crew).
     *  Start with ? units of basic mechanical parts
     *  Start with ? units of basic electronic parts
     *
     *  Guaranteed spawn near neutral or friendly aliens with functional spaceport.
     *  Tutorial friendly.
     *
     */
    private static void GenerateStart1() {
        spaceCrew = SpaceCrew.GenerateStart1();
        homeShip = HomeShip.GenerateStart1(new Random(0), 6, 0.75f);

        //TODO: add the cargostuff.

        debugConsole = DebugConsole.getInstance();
        debugConsole.addGameCommands();

    }


    public static void main(String[] args) {
        SG().startGame();
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

    public static String getDate(){
        return getDate(getGameMonth());
    }

    private static String getDate(int gameMonth) {
        int tot_month = (gameMonth + START_MONTH);
        int real_year = START_YEAR + tot_month / 12;
        Month real_month = Month.of(tot_month%12 + 1);

        return real_month.name() + " " + real_year;
    }
}
