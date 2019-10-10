package unicus.spacegame;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

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
    Planet[] planets;
    //Contains the background graphics of the star system.
    SpaceView spaceViewLayer;
    JPanel buttonLayer;

    JButton btnNewSystem;
    JButton btnDoTrade;
    JButton btnDoRaid;
    JButton btnDoMine;

    public SwingStarSystem(){
        planets = new Planet[0]; //placeholder empty system
        spaceViewLayer = new SpaceView();
        buttonLayer = new JPanel();
        buttonLayer.setOpaque(false);
        //buttonLayer = new SpaceView();
        spaceViewLayer.setBounds(0, 0, 900, 720);
        buttonLayer.setBounds(0, 620, 900, 100);

        setPreferredSize(new Dimension(900, 720));
        setBorder(BorderFactory.createTitledBorder("Hello world"));

        //buttonLayer.setLayout(new BoxLayout(buttonLayer, BoxLayout.X_AXIS));


        btnNewSystem = new JButton("Generate new system");
        btnDoTrade   = new JButton("Trade with locals");
        btnDoRaid    = new JButton("Raid tradeShips");
        btnDoMine    = new JButton("Mine asteroids");

        btnNewSystem.addActionListener(this);
        btnDoTrade.addActionListener(this);
        btnDoRaid.addActionListener(this);
        btnDoMine.addActionListener(this);

        buttonLayer.add(btnNewSystem);
        buttonLayer.add(btnDoTrade);
        buttonLayer.add(btnDoRaid);
        buttonLayer.add(btnDoMine);


        this.add(spaceViewLayer, 100);
        this.add(buttonLayer, 0);

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

    public void newPlanets(Random rand){
        final float TAU = (float)Math.PI * 2;
        int planetcount = rand.nextInt(4) + 2; //between 1 and 4 planets in the system, + 1 star.
        planets = new Planet[planetcount];

        planets[0] = new Planet(PlanetType.star, randomPlanetColor(rand, PlanetType.star), 50, 0, 0, null);
        int nextSafeOrbit = 75;
        for(int i = 1; i < planetcount; i++) {

            PlanetType type = PlanetType.random(rand, false);
            int size = rand.nextInt(20) + 5;
            int orbitD = rand.nextInt(20) + nextSafeOrbit;
            float orbitR = rand.nextFloat() * TAU;
            Color color = randomPlanetColor(rand, type);
            planets[i] = new Planet(type, color, size, orbitD, orbitR, planets[0]);
            nextSafeOrbit = orbitD + size + 10;
        }
        repaint();
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

/**
 * Super class for orbital objects.
 * By design, only the centerpeice of a system (normally a star) can be created without a parent orbital
 */
abstract class Base_Planet {
    final public double TAU = Math.PI * 2;
    Base_Planet parent;

    public Base_Planet(PlanetType type, @Nullable Base_Planet parent){
        this.parent = parent;
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
/**
 *
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