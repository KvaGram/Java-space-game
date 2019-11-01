package unicus.spacegame.structures.starsystem;

import java.util.*;

//NOTE: all uses of BasicSpaceObject are placeholders.
//They will be replaced by proper types as they are developed.

enum StarSystemTemplates {
    /**
     * This is a near clone of the Sol system.
     * Little is left to chance compared to an full recreation.
     */
    solClone{
        @Override
        public void pass1(StarSystem s, Random r) {
            int orbit = 0;
            BasicSpaceObject p;
            BasicSpaceObject c;
            //Sol
            c = s.setCenter(ObjectType.star, ObjectSize.SMALL);
            orbit = 1;
            //Mercury
            p = s.addPlanet(ObjectType.planet, ObjectSize.SMALL, c, orbit, 0.0f);
            orbit = 2;
            //Venus
            p = s.addPlanet(ObjectType.planet, ObjectSize.MODERATE, c, orbit, 0.0f);
            orbit = 3;
            //Earth
            p = s.addLifePlanet(ObjectType.lifePlanet, ObjectSize.MODERATE, c, orbit, 0.0f);
            orbit = 4;
            //Mars
            p = s.addPlanet(ObjectType.planet, ObjectSize.SMALL, c, orbit, 0.0f);
            orbit = 5;
            //The Asteroid Belt
            for(int a=0; a < 30; a++){
                float rot = r.nextFloat() *  TAU;
                s.addAsteroid(ObjectType.asteroid, ObjectSize.Random1(r), p, orbit, rot);
            }
            orbit = 6;
            //Jupiter
            p = s.addPlanet(ObjectType.gasPlanet, ObjectSize.LARGE, c, orbit, 0.0f);
            orbit = 7;
            //Saturn
            p = s.addPlanet(ObjectType.gasPlanet, ObjectSize.LARGE, c, orbit, 0.0f);
            for(int b=1; b <= 2; b++){ //Saturn is given two rings
                for(int a=0; a < 30; a++){
                    float rot = r.nextFloat() *  TAU;
                    s.addAsteroid(ObjectType.asteroid, ObjectSize.SMALL, c, b, rot);
                }
            }
            orbit = 8;
            //Uranus
            p = s.addPlanet(ObjectType.gasPlanet, ObjectSize.SMALL, s.center, orbit, 0.0f);

            orbit = 9;
            //Neptune
            p = s.addPlanet(ObjectType.gasPlanet, ObjectSize.SMALL, s.center, orbit, 0.0f);

            orbit = 10;
            //oort
            for(int a=0; a < 40; a++){
                float rot = r.nextFloat() *  TAU;
                s.addAsteroid(ObjectType.asteroid, ObjectSize.Random1(r), s.center, orbit, rot);
            }
        }
    },
    /**
     * The Sol-like system generates a system with two sets of planets.
     * The inner set has 2 to 4 planets, 1 of them has life on it.
     * Between the sets, there is a asteroid belt.
     * the outer set has 1 to 4 gas planets.
     * At the system border, there is another thinner belt
     */
    solLike {
        @Override
        public void pass1(StarSystem s, Random r) {
            //The star, each planet and each belt uses one random each.
            Stack<Random> randoms = new Stack<>();
            for(int i = 0; i < 11; i++)
                randoms.add(new Random(r.nextLong()));

            int numInner = 2 + r.nextInt(3);
            int lifePlanet = r.nextInt(numInner);
            int numAsteroid1 = 20 + r.nextInt(50);
            int numOuter = 1 + r.nextInt(4);
            int numAsteroid2 = 10 + r.nextInt(40);


            long[] seeds = new long[numInner + numOuter + 3];
            Arrays.setAll(seeds, i -> r.nextLong());

            Random pr;
            BasicSpaceObject p;
            BasicSpaceObject c;
            c = s.setCenter(ObjectType.star, ObjectSize.Random1(new Random(seeds[0])));

            int orbit = 0;
            for(int i = 0; i < numInner; i++){
                orbit++;
                pr = randoms.pop();
                if(i == lifePlanet){
                    p = s.addLifePlanet(ObjectType.lifePlanet, ObjectSize.Random1(pr), c, orbit, 0.0f);
                }else {
                    p = s.addPlanet(ObjectType.planet, ObjectSize.Random1(pr), c, orbit, 0.0f);
                }
                int numMoons = pr.nextInt(9) - 6; //up to 2 moons. Must rolls 7 or higher.
                for (int j = 0; j < numMoons; j++) {
                    s.addPlanet(ObjectType.planet, p.size.Smaller(), p, j+1, 0.0f);
                }
            }
            orbit++;
            pr = randoms.pop();
            for (int i = 0; i < numAsteroid1; i++) {
                float rot = pr.nextFloat() * TAU;
                s.addAsteroid(ObjectType.asteroid, ObjectSize.Random1(pr).Smaller(), s.center, orbit, rot);
            }
            for(int i = 0; i < numOuter; i++){
                orbit++;
                pr = randoms.pop();
                p = s.addPlanet(ObjectType.gasPlanet, ObjectSize.Random1(pr).Larger(), c, orbit, 0.0f);
                int numOrbits = pr.nextInt(7) - 2; //up to 4 moons or rings. Must rolls 3 or higher.
                for (int j = 0; j < numOrbits; j++) {
                    if (pr.nextFloat() > 0.7f) //if this is a moon
                        s.addPlanet(ObjectType.planet, ObjectSize.Random1(pr), p, j+1, 0.0f);
                    else{
                        float rot = pr.nextFloat() * TAU;
                        s.addAsteroid(ObjectType.asteroid, ObjectSize.Random1(pr).Smaller().Smaller(), p, j+1, rot);
                    }
                }
            }
            orbit++;
            pr = randoms.pop();
            for (int i = 0; i < numAsteroid2; i++) {
                float rot = pr.nextFloat() * TAU;
                s.addAsteroid(ObjectType.asteroid, ObjectSize.Random1(pr).Smaller(), s.center, orbit, rot);
            }
        }
    };

    //Pass1 generates the planets
    public abstract void pass1(StarSystem s, Random r);
    private static final float TAU = 6.283185307179586f;
}
