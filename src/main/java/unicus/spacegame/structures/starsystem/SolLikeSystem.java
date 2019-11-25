package unicus.spacegame.structures.starsystem;

import unicus.spacegame.structures.civs.LifeData;
import unicus.spacegame.utilities.NameGenerator;

import java.util.Random;
import java.util.Stack;

import static unicus.spacegame.utilities.Constants.TAUf;

public class SolLikeSystem extends BaseStarSystem {
    private int numInner;
    private int lifePlanet;
    //private int numAsteroid1;
    private int numOuter;
    //private int numAsteroid2;

    protected String starName;

    public SolLikeSystem(long seed) {
        super(seed);
        starName = NameGenerator.makeWord("CVVC", seed);
        numInner = 2 + systemRand.nextInt(3);
        lifePlanet = systemRand.nextInt(numInner);
        //numAsteroid1 = 20 + systemRand.nextInt(50);
        numOuter = 1 + systemRand.nextInt(4);
        //numAsteroid2 = 10 + systemRand.nextInt(40);

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
        c = setCenter(ObjectType.STAR, ObjectSize.Random1(pr), pr.nextLong());

        int orbit = 0;
        for(int i = 0; i < numInner; i++){
            orbit++;
            pr = randoms.pop();
            if(i == lifePlanet){
                p = addLifePlanet(ObjectType.LIFE_PLANET, ObjectSize.Random1(pr), pr.nextLong(), c, orbit, 0.0f);
                System.out.println("Making planet with life");
            }else {
                p = addPlanet(ObjectType.PLANET, ObjectSize.Random1(pr), pr.nextLong(), c, orbit, 0.0f);
                System.out.println("Making lifeless planet");
            }
            int numMoons = pr.nextInt(9) - 6; //up to 2 moons. Must rolls 7 or higher.
            for (int j = 0; j < numMoons; j++) {
                System.out.println("Making moon");
                addPlanet(ObjectType.PLANET, p.size.Smaller(), pr.nextLong(), p, j+1, 0.0f);
            }
        }
        orbit++;
        pr = randoms.pop();
        System.out.println("Making asteroid belt");
        addJunkField(JunkFieldShape.BELT, new JunkContents[]{JunkContents.ASTEROIDS}, pr.nextLong(), TAUf, ObjectSize.LARGE, c, orbit, 0);
        for(int i = 0; i < numOuter; i++){
            orbit++;
            pr = randoms.pop();
            System.out.println("Making gas-planet");
            p = addPlanet(ObjectType.GAS_PLANET, ObjectSize.Random1(pr).Larger(), pr.nextLong(), c, orbit, 0.0f);
            int numOrbits = pr.nextInt(7) - 2; //up to 4 moons or rings. Must rolls 3 or higher.
            for (int j = 0; j < numOrbits; j++) {
                if (pr.nextFloat() > 0.7f) { //if this is a moon
                    System.out.println("Making moon");
                    addPlanet(ObjectType.PLANET, ObjectSize.Random1(pr), pr.nextLong(), p, j + 1, 0.0f);
                }
                else {
                    int numAsteroids = pr.nextInt(10);
                    System.out.println("Making asteroid ring system");
                    addJunkField(JunkFieldShape.BELT, new JunkContents[]{JunkContents.ASTEROIDS}, pr.nextLong(), TAUf, ObjectSize.SMALL, p, j, 0);
                }
            }
        }
        orbit++;
        pr = randoms.pop();
        System.out.println("Making asteroid belt");
        addJunkField(JunkFieldShape.BELT, new JunkContents[]{JunkContents.ASTEROIDS}, pr.nextLong(), TAUf, ObjectSize.LARGE, c, orbit, 0);



    }
}
