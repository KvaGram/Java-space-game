package unicus.spacegame.crew;

import java.util.Random;

public abstract class AbstractCrewman {

    /**
     * The keyID is used to identify this crewman in a list.
     * The ID is unique to the crewman, no matter their state.
     * When creating a new crewman, the ID should be generated from SpaceCrew.GetNewKey()
     * When converting a crewman from one state to another, the ID must be copied from the existing object.
     */
    public final int keyID;
    /**
     * The state of the crewman determines what the crewman can do, and what events can apply on them.
     * The state is set final for the object.
     * A crewman must be converted in order to change state.
     */
    private final CrewmanState state;
    /**
     * The name of the crewman.
     * The name of the crewman may change over time, and it only used descriptively.
     */
    private final CrewSelfID selfID;
    /**
     * Date of birth in months relative to start of the game.
     * Used to calculate the age of the crewman
     */
    public final int birthDate;

    /**
     * The crewman's genetic data. Traits may be inherited from parents, and may be passed to offspring.
     */
    public final CrewmanGeneData geneData;

    public int getAge(int currentDate) {
        return currentDate - birthDate;
    }
    public float getAgeYears(int currentDate) {
        return getAge(currentDate) / 12.0f;
    }

    /**
     * Generates a random new AbstractCrewman.
     * @param keyID a unique ID for this crewman
     * @param state The state, of point in life, of this crewman
     * @param birthDate Date of birth in months relative to start of game
     * @param randomSeed Seed value for setting random aspects of the crewman.
     * @param parents The reference keyIDs of the genetic parents (can be left empty)
     */
    protected AbstractCrewman(int keyID, CrewmanState state, int birthDate, long randomSeed, int[] parents) {
        this.keyID = keyID;
        this.state = state;
        this.selfID = new CrewSelfID();
        this.birthDate = birthDate;
        this.geneData = new CrewmanGeneData(parents);
        onRandomize(new Random(randomSeed));
    }

    /**
     * Creates a new crewman with manually set characteristics.
     * Used in start-scenarios and load
     * @param keyID a unique ID for this crewman
     * @param state The state, of point in life, of this crewman
     * @param birthDate Date of birth in months relative to start of game
     * @param selfID The name, gender and title the crewman identifies with.
     * @param geneData The genetic traits and heritage of the crewman.
     */
    protected AbstractCrewman(int keyID, CrewmanState state, int birthDate, CrewSelfID selfID, CrewmanGeneData geneData) {
        this.keyID = keyID;
        this.state = state;
        this.selfID = selfID;
        this.birthDate = birthDate;
        this.geneData = geneData;
    }

    /**
     * Copy a crewman, and give it a new state
     * This is used for converting from one state to another.
     * @param crewman The crewman to copy or convert
     * @param newState The new state of the crewman
     */
    protected AbstractCrewman(AbstractCrewman crewman, CrewmanState newState) {
        this.keyID = crewman.keyID;
        this.selfID = crewman.selfID;
        this.birthDate = crewman.birthDate;
        this.geneData = crewman.geneData;

        this.state = newState;
    }

    public int getKeyID() {
        return keyID;
    }

    public CrewmanState getState() {
        return state;
    }

    /**
     * Randomize properties of the crewman
     * @param r random instance
     */
    protected void onRandomize(Random r) {
        //TODO: geneData.randomize(r);
        selfID.giveSkiffyName(r);
        //TODO: selfID.gender = geneData.getAssumedGender();

        //sets assumed gender to either male or female until genes are implemented
        selfID.gender = new CrewGender[]{CrewGender.male, CrewGender.female}[r.nextInt(2)];
    }

    /**
     * Called at the beginning of the end of a month cycle.
     * To be implemented in child classes.
     */
    protected abstract void endOfMonthStart();

    /**
     * Called at the end of the end of month cycle.
     * To be implemented in child classes.
     */
    protected abstract void endOfMonthEnd();

}
