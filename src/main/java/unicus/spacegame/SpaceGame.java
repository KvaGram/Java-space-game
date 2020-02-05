package unicus.spacegame;

import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.configuration.ClientConfiguration;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.ui.DebugConsole;

/**
 * Main game class.
 */
public class SpaceGame implements IUpdateable {
    private static DebugConsole debugConsole;
    private static HomeShip homeShip;
    private static SpaceCrew spaceCrew;

    private static int GameMonth;


    /*
    NOTE: List under construction, and may be moved out of source code
    Dependency chart
    '->' = depends on

    Construction -> SpaceCrew
     */

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
        SpaceCrew.GenerateStart1();

    }

    public static void main(String[] args) {
        GenerateStart1();
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
}
