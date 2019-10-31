package unicus.spacegame.structures.starsystem;

import java.util.ArrayList;
import java.util.Random;

public class StarSystem {
    boolean lockGeneration;
    StarSystemTemplates template;
    ArrayList<BasicSpaceObject> planets;
    ArrayList<BasicSpaceObject> asteroids;
    ArrayList<BasicSpaceObject> lifePlanets;
    BasicSpaceObject center;




    public StarSystem(StarSystemTemplates template){
        this.planets = new ArrayList<>();
        this.asteroids = new ArrayList<>();
        this.lifePlanets = new ArrayList<>();

        this.template = template;
        lockGeneration = false;
    }

    public void pass1(){
        if(lockGeneration)
            return;
        template.pass1(this, new Random()); //new random is temporary
    }


    public BasicSpaceObject addPlanet(BasicSpaceObject planet) {
        planets.add(planet);
        return planet;
    }
    public BasicSpaceObject addLifePlanet(BasicSpaceObject planet) {
        lifePlanets.add(planet);
        return planet;
    }
    public BasicSpaceObject addAsteroid(BasicSpaceObject asteroid) {
        asteroids.add(asteroid);
        return asteroid;

    }
}
