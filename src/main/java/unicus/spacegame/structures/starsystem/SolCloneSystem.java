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
        float rot = 0; //current rotation
        int orbit = 0;
        BasicSpaceObject p;
        BasicSpaceObject c;
        //Sol
        c = setCenter(ObjectType.STAR, ObjectSize.SMALL, r.nextLong());
        orbit = 1;
        //Mercury
        rot = r.nextFloat() * TAUf;
        p = addPlanet(ObjectType.PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, rot);
        orbit = 2;
        //Venus
        rot = r.nextFloat() * TAUf;
        p = addPlanet(ObjectType.PLANET, ObjectSize.MODERATE, r.nextLong(), c, orbit, rot);
        orbit = 3;
        //Earth
        rot = r.nextFloat() * TAUf;
        p = addLifePlanet(ObjectType.LIFE_PLANET, ObjectSize.MODERATE, r.nextLong(), c, orbit, rot);
        orbit = 4;
        //Mars
        rot = r.nextFloat() * TAUf;
        p = addPlanet(ObjectType.PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, rot);
        orbit = 5;
        //The Asteroid Belt
        addJunkField(JunkFieldShape.BELT, new JunkContents[]{JunkContents.ASTEROIDS}, r.nextLong(), TAUf, ObjectSize.LARGE, c, orbit, 0);
        orbit = 6;
        //Jupiter
        rot = r.nextFloat() * TAUf;
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.LARGE, r.nextLong(), c, orbit, rot);
        //Trojans, Hilda, Greeks asteroid fields
        for (int i = 0; i < 3; i++) {
            rot += 0.25f * TAUf;
            if (rot > TAUf)
                rot -= TAUf;
            addJunkField(JunkFieldShape.CLUSTER, new JunkContents[]{JunkContents.ASTEROIDS}, r.nextLong(), TAUf, ObjectSize.MODERATE, p, orbit, 0);
        }
        orbit = 7;
        //Saturn
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.LARGE, r.nextLong(), c, orbit, rot);
        for(int i=1; i <= 2; i++){ //Saturn is given two rings
            addJunkField(JunkFieldShape.BELT, new JunkContents[]{JunkContents.ASTEROIDS}, r.nextLong(), TAUf, ObjectSize.SMALL, p, i, 0);
        }
        orbit = 8;
        //Uranus
        rot = r.nextFloat() * TAUf;
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, rot);

        orbit = 9;
        //Neptune
        rot = r.nextFloat() * TAUf;
        p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.SMALL, r.nextLong(), c, orbit, rot);

        orbit = 10;
        //oort
        addJunkField(JunkFieldShape.BELT, new JunkContents[]{JunkContents.ASTEROIDS}, r.nextLong(), TAUf, ObjectSize.SMALL, c, orbit, 0);
    }
}
