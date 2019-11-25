package unicus.spacegame.structures.starsystem;

import unicus.spacegame.structures.civs.LifeData;

import java.util.ArrayList;
import java.util.Random;

/*
TODO: Change up the StarSystem structure and generation
Change the former StarSystemTemplates into just a plain enum (SystemTypes).
Turn class StarSystem into an abstract base class
Move factory code from the SystemTypes enum to super-classes of BaseStarSystem.
Consider moving list of planets, asteroids and life-planets to an inner class.
 */

public abstract class BaseStarSystem {
    boolean generated;
    ArrayList<BasicSpaceObject> planets;
    ArrayList<SpaceJunkField> junkFields;
    ArrayList<BasicSpaceObject> lifePlanets;
    BasicSpaceObject center;

    ArrayList<LifeData> lifeData;

    private final long systemSeed;
    protected Random   systemRand;

    public static BaseStarSystem makeSystem(long seed, SystemTypes type) {
        switch(type) {
            default:
                return new SolLikeSystem(seed);
        }
    }


    /**
     * Initiates the star system.
     * @param seed seed value for generating system
     */
    public BaseStarSystem(long seed){
        //sets the seed, name, random-instance and planet-lists.
        this.systemSeed = seed;
        lifeData = new ArrayList<>();
        resetRand();
        clearPlanets();
    }
    public abstract String getName();
    public BasicSpaceObject[] getAllObjects() {
        int size = lifePlanets.size() + planets.size() + junkFields.size() + 1;

        BasicSpaceObject[] result = new BasicSpaceObject[size];
        result[0] = center;
        int j = 1;
        for (BasicSpaceObject obj: lifePlanets) {
            result[j] = obj;
            j++;
        }
        for (BasicSpaceObject obj: planets) {
            result[j] = obj;
            j++;
        }
        for (BasicSpaceObject obj: junkFields) {
            result[j] = obj;
            j++;
        }
        return result;
    }

    public void resetRand() {
        systemRand = new Random(systemSeed);
    }
    public void clearPlanets() {
        this.planets = new ArrayList<>();
        this.junkFields = new ArrayList<>();
        this.lifePlanets = new ArrayList<>();
        this.center = null;
        generated = false;
        clearPlanetsInternal();
    }
    protected abstract void clearPlanetsInternal();
    public void generatePlanets() {
        if(generated)
            clearPlanets();
        resetRand();
        generatePlanetsInternal();
        generated = true;
        center.update();
    }
    protected abstract void generatePlanetsInternal();



    protected BasicSpaceObject addPlanet(BasicSpaceObject planet) {
        planets.add(planet);
        return planet;
    }
    protected BasicSpaceObject addPlanet(ObjectType type, ObjectSize size, long seed, BasicSpaceObject parent, int orbit, float rot){
        return addPlanet(new BasicSpaceObject(type, size, seed, parent, orbit, rot));
    }
    protected BasicSpaceObject addLifePlanet(BasicSpaceObject planet) {
        lifePlanets.add(planet);
        return planet;
    }
    protected BasicSpaceObject addLifePlanet(ObjectType type, ObjectSize size, long seed, BasicSpaceObject parent, int orbit, float rot){
        return addLifePlanet(new BasicSpaceObject(type, size, seed, parent, orbit, rot));
    }
    protected BasicSpaceObject addJunkField(SpaceJunkField junkField) {
        junkFields.add(junkField);
        return junkField;
    }
    protected BasicSpaceObject addJunkField(JunkFieldShape shape, JunkContents[] contents, long seed, float radianLength, ObjectSize size, BasicSpaceObject parent, int orbit, float rot){
        return addJunkField(new SpaceJunkField(shape, contents, seed, radianLength, size, parent, orbit, rot));
    }

    protected BasicSpaceObject setCenter(BasicSpaceObject centerObject){
        center = centerObject;
        return centerObject;
    }


    protected BasicSpaceObject setCenter(ObjectType type, ObjectSize size, long seed){
        return setCenter(new BasicSpaceObject(type, size, seed));
    }


    public BasicSpaceObject getCenter() {
        return center;
    }

    public boolean isGenerated() {
        return generated;
    }
}
