package unicus.spacegame.structures.starsystem;

import org.apache.commons.lang3.StringUtils;
import unicus.spacegame.structures.civs.LifeData;
import unicus.spacegame.utilities.NameGenerator;

import java.util.Random;
import java.util.Stack;

import static unicus.spacegame.utilities.Constants.TAUf;

public class SolLikeSystem extends BaseStarSystem {
    private int numInner;
    private int lifePlanet;
    private int numAsteroid1;
    private int numOuter;
    private int numAsteroid2;

    protected String starName;

    public SolLikeSystem(long seed) {
        super(seed);
        starName = NameGenerator.makeWord("CVVC", seed);
        numInner = 2 + systemRand.nextInt(3);
        lifePlanet = systemRand.nextInt(numInner);
        numAsteroid1 = 20 + systemRand.nextInt(50);
        numOuter = 1 + systemRand.nextInt(4);
        numAsteroid2 = 10 + systemRand.nextInt(40);

        for (int i = 0; i < lifePlanet; i++)
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
        //list of random to use for planets
        Stack<Random> randoms = new Stack<>();
        for(int i = 0; i < 11; i++)
            randoms.add(new Random(systemRand.nextLong()));

        // planet specific random.
        Random pr;
        // current planet
        BasicSpaceObject p;
        // shorthand for center
        BasicSpaceObject c;

        pr = randoms.pop();
        c = setCenter(ObjectType.star, ObjectSize.Random1(pr), pr.nextLong());

        int orbit = 0;
        for(int i = 0; i < numInner; i++){
            orbit++;
            pr = randoms.pop();
            if(i == lifePlanet){
                p = addLifePlanet(ObjectType.lifePlanet, ObjectSize.Random1(pr), pr.nextLong(), c, orbit, 0.0f);
            }else {
                p = addPlanet(ObjectType.planet, ObjectSize.Random1(pr), pr.nextLong(), c, orbit, 0.0f);
            }
            int numMoons = pr.nextInt(9) - 6; //up to 2 moons. Must rolls 7 or higher.
            for (int j = 0; j < numMoons; j++) {
                addPlanet(ObjectType.planet, p.size.Smaller(), pr.nextLong(), p, j+1, 0.0f);
            }
        }
        orbit++;
        pr = randoms.pop();
        for (int i = 0; i < numAsteroid1; i++) {
            float rot = pr.nextFloat() * TAUf;
            addAsteroid(ObjectType.asteroid, ObjectSize.Random1(pr).Smaller(), pr.nextLong(), center, orbit, rot);
        }
        for(int i = 0; i < numOuter; i++){
            orbit++;
            pr = randoms.pop();
            p = addPlanet(ObjectType.gasPlanet, ObjectSize.Random1(pr).Larger(), pr.nextLong(), c, orbit, 0.0f);
            int numOrbits = pr.nextInt(7) - 2; //up to 4 moons or rings. Must rolls 3 or higher.
            for (int j = 0; j < numOrbits; j++) {
                if (pr.nextFloat() > 0.7f) //if this is a moon
                    addPlanet(ObjectType.planet, ObjectSize.Random1(pr), pr.nextLong(), p, j+1, 0.0f);
                else{
                    float rot = pr.nextFloat() * TAUf;
                    addAsteroid(ObjectType.asteroid, ObjectSize.Random1(pr).Smaller().Smaller(), pr.nextLong(), p, j+1, rot);
                }
            }
        }
        orbit++;
        pr = randoms.pop();
        for (int i = 0; i < numAsteroid2; i++) {
            float rot = pr.nextFloat() * TAUf;
            addAsteroid(ObjectType.asteroid, ObjectSize.Random1(pr).Smaller(), pr.nextLong(), center, orbit, rot);
        }



    }
}
