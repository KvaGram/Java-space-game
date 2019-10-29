package unicus.spacegame.ui;

//import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import unicus.spacegame.NameGenerator;
import unicus.spacegame.StarData;
import static java.lang.System.out;

/**
 * Prototype screen for displaying a star system.
 * Planet data should probably be moved to its own class in final product.
 *
 */

//Intellij's warnings distracts development.
// Note to self: occationally lift this to check for sound advice for improving code.
@SuppressWarnings("ALL")
public class SwingStarSystem extends JLayeredPane implements ActionListener {
    //Lists all planets, including the star.
    //Idea: add support for binary & ternary stars?
    Base_Planet[] planets;
    //Contains the background graphics of the star system.
    SpaceView spaceViewLayer;
    JPanel buttonLayer;
    JPanel infoOverlay;

    JButton btnNewSystem;
    JButton btnDoTrade;
    JButton btnDoRaid;
    JButton btnDoMine;

    JLabel systemNameText;

    String systemName;

    final float TAU = 6.283185307179586f;

    public SwingStarSystem(){
        planets = new Base_Planet[0]; //placeholder empty system
        spaceViewLayer = new SpaceView();
        buttonLayer = new JPanel();
        buttonLayer.setOpaque(false);

        infoOverlay = new JPanel();
        infoOverlay.setOpaque(false);


        spaceViewLayer.setBounds(0, 0, 900, 720);
        buttonLayer.setBounds(0, 620, 900, 100);
        infoOverlay.setBounds(0, 0, 900, 720);

        setPreferredSize(new Dimension(900, 720));
        setBorder(BorderFactory.createTitledBorder("Hello world"));

        //buttonLayer.setLayout(new BoxLayout(buttonLayer, BoxLayout.X_AXIS));


        btnNewSystem = new JButton("Generate new system");
        btnDoTrade   = new JButton("Trade with locals");
        btnDoRaid    = new JButton("Raid tradeShips");
        btnDoMine    = new JButton("Mine asteroids");

        Font font = new Font("Times", Font.BOLD, 30);
        systemNameText = new JLabel(systemName, JLabel.CENTER);
        systemNameText.setForeground(new Color(50, 250, 250));
        systemNameText.setFont(font);

        btnNewSystem.addActionListener(this);
        btnDoTrade.addActionListener(this);
        btnDoRaid.addActionListener(this);
        btnDoMine.addActionListener(this);

        buttonLayer.add(btnNewSystem);
        buttonLayer.add(btnDoTrade);
        buttonLayer.add(btnDoRaid);
        buttonLayer.add(btnDoMine);

        infoOverlay.add(systemNameText);



        this.add(spaceViewLayer, new Integer(0));
        this.add(infoOverlay, new Integer(10));
        this.add(buttonLayer, new Integer(20));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Random rand = new Random();

        if (source == btnNewSystem){
            newPlanets(rand);
        }
        else if (source == btnDoTrade){
            out.println("You do some trading");
        }
        else if (source == btnDoRaid){
            out.println("You do some raiding");
        }
        else if (source == btnDoMine){
            out.println("You do some mining");
        }

    }
    private void setButtonsActive(){
        btnDoTrade.setEnabled(GetNumLife() >= 1);
        btnDoRaid.setEnabled(GetNumLife() >= 2);
        btnDoMine.setEnabled(GetNumMinable() >= 1);
    }

    public void setStar(StarData currentStar) {
        //Generates a starsystem from seed.
        Random r = new Random(currentStar.seed);
        newPlanets(r);
    }

    /**
     * View is an internal class that extends JPanel.
     * It renders the current star system
     */
    private class SpaceView extends JPanel {
        public SpaceView()
        {
            setBackground(Color.black);
        }
        //public void paint(Graphics g)
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Rectangle rect =  this.getBounds();
            int planetCount = planets.length;

            int i;
            for(i = 0; i < planetCount; i++)
                planets[i].paintOrbit(g, rect);
            for(i = 0; i < planetCount; i++)
                planets[i].PaintPlanet(g, rect);
        }
    }

    /**
     * Generates a star ssytem
     * @param rand The random number generator to use.
     */
    public void newPlanets(Random rand){

        //asteroid belts count as 'planet' in this limit
        int maxPlanets = 10; //The system's number of planets will number
        int minPlanets = 1;  //     between this minimum and maximum.
        int maxMoons = 1; //A asteroid ring counts as a moon in this list
        int maxLife = 2; //Maximum number of planets that may have life on them.
        int maxBelts = 3; //maximum number of asteroid belts in the system


        //setting constants for odds of a planet
        float planetHasMoon = 0.2f;
        float planetHasRing = 0.2f;
        //note: planet cannot have both a moon and a belt.
        //If a planet rolls a moon, the roll for a belt is ignored.
        float planetIsAsteroidbelt = 0.1f;
        float planetHasLife = 0.3f;

        ArrayList<Base_Planet> newPlanetsList = new ArrayList<Base_Planet>();

        // - Generate star
        Star star = new Star(randomPlanetColor(rand, PlanetType.star), 40);
        newPlanetsList.add(star);

        // - generate planets
       int numPlanets = rand.nextInt(maxPlanets - minPlanets) + minPlanets;
       int nextSafeOrbit = 70;

       int lifeCreated = 0;
       int beltsCreated = 0;

       for(int i = 0; i < numPlanets; i++) {
           int orbitD = rand.nextInt(20) + nextSafeOrbit;
           boolean isABelt = rand.nextFloat() < planetIsAsteroidbelt;

           // - Asteroidbelt
           if (isABelt && beltsCreated < maxBelts) {
               Asteroidbelt belt = new Asteroidbelt(orbitD + 10, star);
               nextSafeOrbit = orbitD + 50;
               newPlanetsList.add(belt);
               beltsCreated++;
               continue;
           }
           boolean hasLife = rand.nextFloat() < planetHasLife;
           PlanetType type = PlanetType.dead;

           // - Planet with life
           if (hasLife && lifeCreated < maxLife) {
               type = PlanetType.life;
               lifeCreated++;
           }
           int size = rand.nextInt(20) + 5;
           float orbitR = rand.nextFloat() * TAU;
           Planet planet = new Planet(type, randomPlanetColor(rand, type), size, orbitD, orbitR, star);
           newPlanetsList.add(planet);
           nextSafeOrbit = orbitD + size + 10;

           // - Creating moons
           float m_orbitR = rand.nextFloat() * TAU;
           int m_size = Math.min(rand.nextInt(10) + 2, (int) (size * 0.6));
           int m_orbitD = size + m_size + rand.nextInt(5);

           boolean hasRing = rand.nextFloat() < planetHasRing;
           if (hasRing && beltsCreated < maxBelts) {
               //counting the rings as asteroidbelts.
               Asteroidbelt belt = Asteroidbelt.MakeRing(m_orbitD, planet);
               nextSafeOrbit += 10;
               newPlanetsList.add(belt);
               beltsCreated++;
           }
           //currently limited to just 1 moon.
           boolean hasMoon = rand.nextFloat() < planetHasMoon;
           if (hasMoon) {
               boolean m_hasLife = rand.nextFloat() < planetHasLife;
               PlanetType m_type = PlanetType.dead;

               // - Moon with life
               if (hasLife && lifeCreated < maxLife) {
                   type = PlanetType.life;
                   lifeCreated++;
               }
               Planet moon = new Planet(m_type, randomPlanetColor(rand, m_type), m_size, m_orbitD, m_orbitR, planet);
               //regarding above declaration, does anyone else Della Duck's theme playing in the background? (joke)
               newPlanetsList.add(moon);
               nextSafeOrbit += 15;
           }
       }
       planets = newPlanetsList.toArray(new Base_Planet[newPlanetsList.size()]);

       systemName = NameGenerator.makeWord("CVV VCVV", rand).toUpperCase();
       systemNameText.setText(systemName);
       setButtonsActive();
       repaint();
    }
    private Color randomPlanetColor(Random rand, PlanetType type) {
        float[] hsb = {rand.nextFloat(), rand.nextFloat(), rand.nextFloat()};
        return randomPlanetColor(hsb, type);
    }
    private Color randomPlanetColor(float[] hsb, PlanetType type) {
        float h = hsb[0];
        float s = hsb[1];
        float b = hsb[2];
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

    public int GetNumLife()
    {
        int found = 0;
        for(int i = 0; i < planets.length; i++)
        {
            if (planets[i].GetType() == PlanetType.life)
                found ++;
        }
        return found;
    }
    public int GetNumMinable()
    {
        int found = 0;
        for(int i = 0; i < planets.length; i++)
        {
            if (planets[i].GetType() == PlanetType.asteroidbelt)
                found ++;
        }
        return found;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        SwingStarSystem starsys = new SwingStarSystem();
        starsys.newPlanets(rand);

        //Creating the Frame
        JFrame frame = new JFrame("Test swingstarsystem");
        frame.add(starsys);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 720);

        frame.setVisible(true);
    }
}
//NOTE: The enum could be replaced with a bitset.
//That way it could act more of a set of flags then just a label.
enum PlanetType {
    star, asteroidbelt, dead, life;

    public static PlanetType random(Random rand, boolean starAllowed){
        final int numStarTypes = 1;
        PlanetType[] types = {
                PlanetType.dead,
                PlanetType.life
        };
        int numTypes = types.length;
        return types[rand.nextInt(numTypes)];
    }
}

/**
 * Super class for orbital objects.
 * By design, only the centerpeice of a system (normally a star) can be created without a parent orbital
 */
abstract class Base_Planet {
    final public double TAU = Math.PI * 2;
    Base_Planet parent;
    PlanetType type;
    public Base_Planet(PlanetType type, Base_Planet parent){
        this.parent = parent;
        this.type = type;
    }
    public Base_Planet(PlanetType type){
        this.parent = null;
        this.type = type;
    }

    public PlanetType GetType(){
        return type;
    }
    /** Calculate the X and Y coordinates to draw this object.
     * Orbit distance and orbit rotation are calulated in relation to the parent (eg. a star)
     * If the object have no parent, the center of the Rectangle is returned.
     * @param rect Graphical area the orbitary system is drawn in.
     * @return draw-coordinates for this orbital
     */
    public abstract Point GetPoint(Rectangle rect);
    public abstract void PaintPlanet(Graphics g, Rectangle rect);
    public abstract void paintOrbit(Graphics g, Rectangle rect);
}
class Asteroidbelt extends Base_Planet {
    public int orbitDistance;
    int maxsize = 12;
    int minsize = 2;
    int count = 20;
    int noise = 20; //random offset in x and y, applied on each asteroid

    public Asteroidbelt(int orbitDistance, Base_Planet parent) {
        super(PlanetType.asteroidbelt, parent);
        this.orbitDistance = orbitDistance;
    }

    public static Asteroidbelt MakeRing(int orbitDistance, Base_Planet parent) {
        Asteroidbelt belt = new Asteroidbelt(orbitDistance, parent);
        belt.maxsize = 3;
        belt.minsize = 1;
        belt.count = 10;
        belt.noise = 2;

        return belt;
    }

    @Override
    public Point GetPoint(Rectangle rect) {
        if(parent == null)
            return new Point((int)rect.getCenterX(), (int)rect.getCenterY());
        Point p = parent.GetPoint(rect);
        p.x += 0;
        p.y += orbitDistance;
        return p;
    }

    @Override
    public void PaintPlanet(Graphics g, Rectangle rect){
        Random localRand = new Random(0); //A seed MUST be set, if to maintain consistency.
        Color color = Color.LIGHT_GRAY;

        g.setColor(color);

        for(int i = 0; i < count; i++){
            Point p2 = parent.GetPoint(rect);
            double rot = TAU * i / count;

            p2.x += (int) (Math.cos(rot) * orbitDistance) - noise/2 + localRand.nextInt(noise);
            p2.y += (int) (Math.sin(rot) * orbitDistance) - noise/2 + localRand.nextInt(noise);
            int aSize = localRand.nextInt(maxsize-minsize) + minsize;

            g.fillRect(p2.x - aSize, p2.y - aSize, aSize, aSize);
        }

    }

    @Override
    public void paintOrbit(Graphics g, Rectangle rect) {
        g.setColor(Color.darkGray);
        g.drawOval(rect.width/2 - orbitDistance, rect.height/2 - orbitDistance, orbitDistance*2, orbitDistance*2);
    }
}

/**
 * A typical planet.
 * It may or may not hold life.
 */
class Planet extends Base_Planet {

    public Color color;
    public int size;
    public int orbitDistance;
    public float orbitRotation;

    public Planet(PlanetType type, Color color, int size, int orbitDistance, float orbitRotation, Base_Planet parent) {
        super(type, parent);
        this.color = color;
        this.size = size;
        this.orbitDistance = orbitDistance;
        this.orbitRotation = orbitRotation;
    }

    @Override
    public void PaintPlanet(Graphics g, Rectangle rect){

        //int x = (int) (Math.cos(orbitRotation) * orbitDistance) + size;
        //int y = (int) (Math.sin(orbitRotation) * orbitDistance) + size;
        Point p = GetPoint(rect);
        g.setColor(color);
        g.fillOval(p.x-size, p.y - size, size*2, size*2);
    }
    @Override
    public void paintOrbit(Graphics g, Rectangle rect)
    {
        if(parent == null)
            return;

        Point p = parent.GetPoint(rect);
        g.setColor(Color.white);
        g.drawOval(p.x - orbitDistance, p.y - orbitDistance, orbitDistance*2, orbitDistance*2);
    }

    @Override
    public Point GetPoint(Rectangle rect) {
        if(parent == null)
            return new Point((int)rect.getCenterX(), (int)rect.getCenterY());
        Point p = parent.GetPoint(rect);
        p.x += (int) (Math.cos(orbitRotation) * orbitDistance);
        p.y += (int) (Math.sin(orbitRotation) * orbitDistance);
        return p;
    }
}

class Star extends Base_Planet {

    public Color color;
    public int size;
    public int orbitDistance;
    public float orbitRotation;

    /**
     * Constructor for orbiting or co-orbiting star.
     * @param color
     * @param size
     * @param orbitDistance
     * @param orbitRotation
     * @param parent
     */
    public Star(Color color, int size, int orbitDistance, float orbitRotation, Base_Planet parent) {
        super(PlanetType.star, parent);
        this.color = color;
        this.size = size;
        this.orbitDistance = orbitDistance;
        this.orbitRotation = orbitRotation;
    }

    /**
     * Constructor for a single star in the center of the system
     * @param color
     * @param size
     */
    public Star (Color color, int size){
        super(PlanetType.star, null);
        this.color = color;
        this.size = size;

        orbitDistance = 0;
        orbitRotation = 0;
    }

    @Override
    public void PaintPlanet(Graphics g, Rectangle rect){

        //int x = (int) (Math.cos(orbitRotation) * orbitDistance) + size;
        //int y = (int) (Math.sin(orbitRotation) * orbitDistance) + size;
        Point p = GetPoint(rect);
        g.setColor(color);
        g.fillOval(p.x-size, p.y - size, size*2, size*2);
    }
    @Override
    public void paintOrbit(Graphics g, Rectangle rect)
    {
        g.setColor(Color.white);
        g.drawOval(rect.width/2 - orbitDistance, rect.height/2 - orbitDistance, orbitDistance*2, orbitDistance*2);
    }

    @Override
    public Point GetPoint(Rectangle rect) {
        if(parent == null)
            return new Point((int)rect.getCenterX(), (int)rect.getCenterY());
        Point p = parent.GetPoint(rect);
        p.x += (int) (Math.cos(orbitRotation) * orbitDistance);
        p.y += (int) (Math.sin(orbitRotation) * orbitDistance);
        return p;
    }
}