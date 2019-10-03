package unicus.spacegame;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * Prototype screen for displaying a star system.
 * Planet data should probably be moved to its own class in final product.
 *
 */
public class SwingStarSystem extends JPanel {
    //Lists all planets, including the star.
    //Idea: add support for binary & ternary stars?
    Planet[] planets;
    int planetcount;

    public void newPlanets(Random rand){
        final float TAU = (float)Math.PI * 2;
        planetcount = rand.nextInt(4) + 2; //between 1 and 4 planets in the system, + 1 star.
        planets = new Planet[planetcount];

        planets[0] = new Planet(randomPlanetColor(rand, PlanetType.star), 50, 0, 0);
        int nextSafeOrbit = 75;
        for(int i = 1; i < planetcount; i++) {

            PlanetType type = PlanetType.random(rand, false);
            int size = rand.nextInt(20) + 5;
            int orbitD = rand.nextInt(20) + nextSafeOrbit;
            float orbitR = rand.nextFloat() * TAU;
            Color color = randomPlanetColor(rand, type);
            planets[i] = new Planet(color, size, orbitD, orbitR);
            nextSafeOrbit = orbitD + size + 10;
        }
    }
    private Color randomPlanetColor(Random rand, PlanetType type){
        float h = rand.nextFloat();
        float s = rand.nextFloat();
        float b = rand.nextFloat();
        switch (type) {
            case dead:
                s = s / 10; //low saturation on dead planets
                b = b / 10 + 0.9f; //dead planets are very bight
                break;
            case life:
                h = h / 6f - 1/6 + 0.48f;//planets with life has a hue near cyan
                s = s / 10 + 0.6f; //planets with life are moderately saturated.
                b = b / 2 + 0.4f; //planets with life are a bit shaded
                break;
            case star:
                s = 1; //stars are always fully saturated.
                b = b / 10 + 0.9f; //Stars are always very bright.
                break;
            default:
                break;
        }
        final Color hsbColor = Color.getHSBColor(h, s, b);
        return hsbColor;
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0,0,600,400);

        int i;
        for(i = 0; i < planetcount; i++)
            planets[i].paintOrbit(g);
        for(i = 0; i < planetcount; i++)
            planets[i].paintPlanet(g);
    }
    private enum PlanetType{
        star, dead, life;

        public static PlanetType random(Random rand, boolean starAllowed){
            final int numStarTypes = 1;
            PlanetType[] types = values();
            if(!starAllowed)
                types = Arrays.copyOfRange(types, numStarTypes, types.length);

            int numTypes = types.length;
            return types[rand.nextInt(numTypes)];
        }
    }
    private class Planet{
        final public double TAU = Math.PI * 2;

        public Color color;
        public int size;
        public int orbitDistance;
        public float orbitRotation;
        public Planet(Color color, int size, int orbitDistance, float orbitRotation){
            this.color = color;
            this.size = size;
            this.orbitDistance = orbitDistance;
            this.orbitRotation = orbitRotation;
        }
        public void paintPlanet(Graphics g){
            int x = (int) (Math.cos(orbitRotation) * orbitDistance) + size;
            int y = (int) (Math.sin(orbitRotation) * orbitDistance) + size;
            g.setColor(color);
            g.fillOval(300 - x, 200 - y, size*2, size*2);
        }
        public void paintOrbit(Graphics g)
        {

            g.setColor(Color.white);
            g.drawOval(300 - orbitDistance, 200 - orbitDistance, orbitDistance*2, orbitDistance*2);
        }
    }
    public static void main(String[] args) {
        Random rand = new Random();
        SwingStarSystem starsys = new SwingStarSystem();
        starsys.newPlanets(rand);

        //Creating the Frame
        JFrame frame = new JFrame("Test swingstarsystem");
        frame.add(starsys);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 450);

        frame.setVisible(true);
    }
}
