package unicus.spacegame.structures.starsystem;

import unicus.spacegame.structures.civs.LifeData;
import unicus.spacegame.utilities.NameGenerator;

import java.util.Random;

import static unicus.spacegame.utilities.Constants.TAUf;

/**
 * The Sol-clone generates a star system that looks almost like Sol.
 */
public class SolCloneSystem extends BaseStarSystem {
    String starName;
    /**
     * Initiates the star system.
     *
     * @param seed seed value for generating system
     */
    public SolCloneSystem(long seed) {
        super(seed);
        starName = NameGenerator.makeWord("CVC", seed);
        lifeData.add(new LifeData(systemRand.nextLong()));
    }

    @Override
    public String getName() {
        return "The "+starName+" starsystem";
    }

    @Override
    protected void clearPlanetsInternal() {

    }

    @Override
    protected void generatePlanetsInternal() {
        //r is shorthand for systemRand.
        Random r = systemRand;
        int orbit = 0;
        BasicSpaceObject p;
        BasicSpaceObject c;
        //Sol
        c = setCenter(ObjectType.STAR, ObjectSize.SMALL, r.nextLong());
        orbit = 1;
        //Mercury
        p = addPlanet(ObjectType.PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, 0.0f);
        orbit = 2;
        //Venus
        p = addPlanet(ObjectType.PLANET, ObjectSize.MODERATE, r.nextLong(), c, orbit, 0.0f);
        orbit = 3;
        //Earth
        p = addLifePlanet(ObjectType.LIFE_PLANET, ObjectSize.MODERATE, r.nextLong(), c, orbit, 0.0f);
        orbit = 4;
        //Mars
        p = addPlanet(ObjectType.PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, 0.0f);
        orbit = 5;
        //The Asteroid Belt
        for(int a=0; a < 30; a++){
            float rot = r.nextFloat() *  TAUf;
            addAsteroid(ObjectType.ASTEROID, ObjectSize.Random1(r), r.nextLong(), p, orbit, rot);
        }
        orbit = 6;
        //Jupiter
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.LARGE, r.nextLong(), c, orbit, 0.0f);
        orbit = 7;
        //Saturn
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.LARGE, r.nextLong(), c, orbit, 0.0f);
        for(int b=1; b <= 2; b++){ //Saturn is given two rings
            for(int a=0; a < 30; a++){
                float rot = r.nextFloat() *  TAUf;
                addAsteroid(ObjectType.ASTEROID, ObjectSize.SMALL, r.nextLong(), c, b, rot);
            }
        }
        orbit = 8;
        //Uranus
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, 0.0f);

        orbit = 9;
        //Neptune
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, 0.0f);

        orbit = 10;
        //oort
        for(int a=0; a < 40; a++){
            float rot = r.nextFloat() *  TAUf;
            addAsteroid(ObjectType.ASTEROID, ObjectSize.Random1(r), r.nextLong(), c, orbit, rot);
        }
    }
}
