package unicus.spacegame.structures.starsystem;

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
    private final String name;
    boolean generated;
    ArrayList<BasicSpaceObject> planets;
    ArrayList<BasicSpaceObject> asteroids;
    ArrayList<BasicSpaceObject> lifePlanets;
     BasicSpaceObject center;

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
     * @param name name of the star system.
     */
    public BaseStarSystem(long seed, String name){
        //sets the seed, name, random-instance and planet-lists.
        this.systemSeed = seed;
        this.name = name;
        resetRand();
        clearPlanets();
    }
    public String getName() {
        return name;
    }


    public void resetRand() {
        systemRand = new Random(systemSeed);
    }
    public void clearPlanets() {

        this.planets = new ArrayList<>();
        this.asteroids = new ArrayList<>();
        this.lifePlanets = new ArrayList<>();
        this.center = null;
        generated = false;
    }
    public void generatePlanets() {
        if(generated)
            clearPlanets();
        //random is reset to ensure consistency.
        resetRand();
        this.planets = new ArrayList<>();
        this.asteroids = new ArrayList<>();
        this.lifePlanets = new ArrayList<>();

        template.GeneratePlanets(this, systemRand);
    }



    public BasicSpaceObject addPlanet(BasicSpaceObject planet) {
        planets.add(planet);
        return planet;
    }
    public BasicSpaceObject addPlanet(ObjectType type, ObjectSize size, long seed, BasicSpaceObject parent, int orbit, float rot){
        return addPlanet(new BasicSpaceObject(type, size, seed, parent, orbit, rot));
    }
    public BasicSpaceObject addLifePlanet(BasicSpaceObject planet) {
        lifePlanets.add(planet);
        return planet;
    }
    public BasicSpaceObject addLifePlanet(ObjectType type, ObjectSize size, long seed, BasicSpaceObject parent, int orbit, float rot){
        return addLifePlanet(new BasicSpaceObject(type, size, seed, parent, orbit, rot));
    }
    public BasicSpaceObject addAsteroid(BasicSpaceObject asteroid) {
        asteroids.add(asteroid);
        return asteroid;
    }
    public BasicSpaceObject addAsteroid(ObjectType type, ObjectSize size, long seed, BasicSpaceObject parent, int orbit, float rot){
        return addAsteroid(new BasicSpaceObject(type, size, seed, parent, orbit, rot));
    }

    public BasicSpaceObject setCenter(BasicSpaceObject centerObject){
        center = centerObject;
        return centerObject;
    }


    public BasicSpaceObject setCenter(ObjectType type, ObjectSize size, long seed){
        return setCenter(new BasicSpaceObject(type, size, seed));
    }



}
