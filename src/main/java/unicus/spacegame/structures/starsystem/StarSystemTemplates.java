package unicus.spacegame.structures.starsystem;

import java.util.ArrayList;
import java.util.Random;

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
            //Sol
            s.center = new BasicSpaceObject(ObjectType.star, ObjectSize.small);
            orbit = 1;
            //Mercury
            p = s.addPlanet(new BasicSpaceObject(ObjectType.planet, ObjectSize.small, s.center, orbit, 0.0f));
            orbit = 2;
            //Venus
            p = s.addPlanet(new BasicSpaceObject(ObjectType.planet, ObjectSize.moderate, s.center, orbit, 0.0f));
            orbit = 3;
            //Earth
            p = s.addLifePlanet(new BasicSpaceObject(ObjectType.lifePlanet, ObjectSize.moderate, s.center, orbit, 0.0f));
            orbit = 4;
            //Mars
            p = s.addPlanet(new BasicSpaceObject(ObjectType.planet, ObjectSize.small, s.center, orbit, 0.0f));
            orbit = 5;
            //The Asteroid Belt
            for(int a=0; a < 30; a++){
                float rot = r.nextFloat() *  TAU;
                s.addAsteroid(new BasicSpaceObject(ObjectType.asteroid, ObjectSize.Random(r), p, orbit, rot));
            }
            orbit = 6;
            //Jupiter
            p = s.addPlanet(new BasicSpaceObject(ObjectType.gasPlanet, ObjectSize.large, s.center, orbit, 0.0f));
            orbit = 7;
            //Saturn
            p = s.addPlanet(new BasicSpaceObject(ObjectType.gasPlanet, ObjectSize.large, s.center, orbit, 0.0f));
            for(int b=1; b <= 2; b++){ //Saturn is given two rings
                for(int a=0; a < 30; a++){
                    float rot = r.nextFloat() *  TAU;
                    s.addAsteroid(new BasicSpaceObject(ObjectType.asteroid, ObjectSize.small, s.center, b, rot));
                }
            }
            orbit = 8;
            //Uranus
            p = s.addPlanet(new BasicSpaceObject(ObjectType.gasPlanet, ObjectSize.small, s.center, orbit, 0.0f));

            orbit = 9;
            //Neptune
            p = s.addPlanet(new BasicSpaceObject(ObjectType.gasPlanet, ObjectSize.small, s.center, orbit, 0.0f));

            orbit = 10;
            //oort
            for(int a=0; a < 40; a++){
                float rot = r.nextFloat() *  TAU;
                s.addAsteroid(new BasicSpaceObject(ObjectType.asteroid, ObjectSize.Random(r), s.center, orbit, rot));
            }



        }
    };

    //Pass1 generates the planets
    public abstract void pass1(StarSystem s, Random r);
    private static final float TAU = 6.283185307179586f;
}
