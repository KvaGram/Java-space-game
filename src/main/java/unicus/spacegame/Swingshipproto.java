package unicus.spacegame;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Swingshipproto {

    public static void main(String[] args) {
        //this part is mostly copypasted from codejava.net
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Creating the Frame
        JFrame outerframe = new JFrame("Ship view proto");
        outerframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        outerframe.setSize(1200, 800);

        //Creates main content panel
        JLayeredPane mainpanel = new JLayeredPane();

        //Create panel to host buttons
        JPanel buttonLayer = new JPanel();
        //set no background to be drawn
        buttonLayer.setOpaque(false);
        //Set bounds manually (not normally recommended)
        //This sets the panel to use the bottom 100 pixels of the frame screen.
        buttonLayer.setBounds(0, 700, 1200, 100);

        //Create dummy buttons
        JButton b_add = new JButton("Add cargo");
        JButton b_remove = new JButton("Remove cargo");
        JButton b_dummy = new JButton("More buttons to come");
        JButton b_crew = new JButton("Crew for example");

        //Add the buttons to the button layer
        buttonLayer.add(b_add);
        buttonLayer.add(b_remove);
        buttonLayer.add(b_dummy);
        buttonLayer.add(b_crew);

        //Create the ship ui layer
        ShipPanel shipUiLayer = new ShipPanel(); //extends JPanel
        shipUiLayer.setupModules();
        shipUiLayer.setOpaque(false);
        //Set bounds manually (not normally recommended)
        //This sets the panel to use the whole frame screen
        shipUiLayer.setBounds(0, 0, 1200, 800);

        //Create the background layer
        ImageIcon shuttleimage = new ImageIcon("resources/shuttlesideview.png");
        JLabel bgLayer = new JLabel(shuttleimage);
        //Set bounds manually (not normally recommended)
        //This sets the panel to use the whole frame screen
        bgLayer.setBounds(0, 0, 1200, 800);

        //set action listeners for cargo buttons
        b_add.addActionListener(arg0 -> {
            shipUiLayer.hasCargo = true;
            shipUiLayer.repaint();
        });
        b_remove.addActionListener(arg0 -> {
            shipUiLayer.hasCargo = false;
            shipUiLayer.repaint();
        });


        //Adds the panels to the main panel
        mainpanel.add(buttonLayer, 0);
        mainpanel.add(shipUiLayer, 100);
        mainpanel.add(bgLayer, 200);

        //Adds the main panel to the frame.
        outerframe.add(mainpanel);
        outerframe.setVisible(true);
    }
}

class ShipPanel extends JPanel {
    boolean hasCargo = false;
    private int shipImgWidth = 1111;
    private int shipImgHeight = 716;
    private int[][] moduleBounds;

    @Override //?
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //int w = getWidth(); ?
        if (hasCargo) {
            g.setColor(Color.yellow);
            g.fillRect(440,190,120,50);
            g.setColor(Color.black);
            g.drawString("Full cargo bay", 450, 215);
            //System.out.println("in if statement");
        } else {
            g.setColor(Color.red);
            g.fillRect(440,190,120,50);
            g.setColor(Color.black);
            g.drawString("Empty cargo bay", 450, 215);
            //System.out.println("in else statement");
        }
        g.setColor(new Color(150,100,0)); //brown
        g.fillRect(moduleBounds[0][0],moduleBounds[0][1],moduleBounds[0][2],moduleBounds[0][3]); // This is ugly but it works.
        // Officially suggested approach is to overload the method name: g.fillRect(int[] a) { g.fillRect(a[0][0], a[0][1]...) }
        // but that requires me to figure out access to Graphics g and its methods somehow. Maybe lambda?
        g.fillRect(moduleBounds[13][0],moduleBounds[13][1],moduleBounds[13][2],moduleBounds[13][3]);
        g.setColor(new Color(100,250,100)); //light green
        for (int i=1; i<=6; i++){
            g.fillRect(moduleBounds[i][0],moduleBounds[i][1], moduleBounds[i][2],moduleBounds[i][3]);
        }
        g.setColor(new Color(250,100,100)); //light red
        for (int i=7; i<=12; i++){
            g.fillRect(moduleBounds[i][0],moduleBounds[i][1], moduleBounds[i][2],moduleBounds[i][3]);
        }
    }

    public void setupModules() {
        moduleBounds = new int[14][4]; //14 modules initially, each with topleft X, topleft Y, width X, height Y
        int sw = shipImgWidth;
        int sh = shipImgHeight; // for modularity, so transform from image values to percentages can be adjusted
        int mw = (int) (sw * 0.1); //module box width
        int mh = (int) (sh * 0.08); //module box height

        int[] engine_coords ={(int)(sw*0.06), (int)(sh*0.4), mw, mh};
        moduleBounds[0] = engine_coords;
        int[] bridge_coords ={(int)(sw*0.9), (int)(sh*0.4), mw, mh};
        moduleBounds[13] = bridge_coords;
        for (int i=0; i<4; i++) {
            for (int j=0; j<3; j++) {
                int[] temp = {(int)(sw*(0.2+(i*0.15))), (int)(sh*(0.35+(j*0.1))), mw, mh};
                int x = 1 +(3*i)+ j;
                moduleBounds[x] = (temp.clone());
            }
        }
    }
}

class SpaceshipGUI extends JPanel
{
    Spaceship spaceship;
    public SpaceshipGUI(Spaceship spaceship)
    {
        this.spaceship = spaceship;
    }
    @Override
    public void paintComponent(Graphics _g){
        Graphics2D g = (Graphics2D) _g;
        Rectangle bounds = getBounds();

        //Paint modules
        for(int i = 0; i < spaceship.length; i++)
            for (int j = 0; j < spaceship.modules[i].length; j++)
                PaintShipModule(i, j, g);

        //Paint bridge
        int bridgeWidth = bounds.width / (spaceship.length + 2) - 10;
        int bridgeHeight = bounds.height / 2;
        int bridgeX = 10;
        int bridgeY = bounds.height / 4;

        g.setColor(Color.green);
        g.fillArc(bridgeX, bridgeY, bridgeWidth*2, bridgeHeight, 90, 180);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.drawArc(bridgeX, bridgeY, bridgeWidth*2, bridgeHeight, 90, 180);
        g.drawLine(bridgeX + bridgeWidth, bridgeY, bridgeX + bridgeWidth, bridgeY+bridgeHeight);

        //paint engineering


    }
    void PaintShipModule(int sIndex, int mIndex, Graphics2D g){
        Rectangle bounds = getBounds();

        int baseWidth = bounds.width / (spaceship.length + 2);
        int baseHeight = bounds.height / (spaceship.sectionTypes[sIndex].getNumModules());

        Rectangle drawRect = new Rectangle();
        int width  = baseWidth - 20;
        int height = baseHeight - 20;
        int x = baseWidth * (sIndex + 1) + 10 + bounds.x;
        int y = baseHeight * mIndex + 10 + bounds.x;

        g.setStroke(new BasicStroke(1));
        g.setColor(spaceship.modules[sIndex][mIndex].moduleType.getPaintColor());
        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(x, y, width, height, 10, 10);

    }
    public static void main(String[] args) {
        Spaceship ship = Spaceship.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
        SpaceshipGUI gui = new SpaceshipGUI(ship);

        JFrame frame = new JFrame("Ship modules proto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        gui.setBounds(0,0,1200, 800);
        frame.add(gui);

        frame.setVisible(true);



        System.out.println(ship.toString());
    }
}


//New code.
class Spaceship {
    public int length;
    //lists the type of sections currently installed. 0 is near bridge, other end near engineering.
    public SectionType[] sectionTypes;
    public ShipModule[][] modules;

    public Spaceship(int length)
    {
        this.length = length;
        sectionTypes = new SectionType[length];
        modules = new ShipModule[length][0];
        for (int i = 0; i < length; i++)
        {
            //None-type sections
            sectionTypes[i] = SectionType.None;
            modules[i] = new ShipModule[0];
        }
    }
    //Warning: this WILL replace existing any existing modules.
    //Code calling this should check with CanBuildSection first
    public void BuildSection(int index, SectionType sectionType)
    {
        // ( index >= 0 && index < length);
        int sLength = sectionType.getNumModules();
        sectionTypes[index] = sectionType;
        modules[index] = new ShipModule[sLength];
        for(int i = 0; i < sLength; i++){
            modules[index][i] = new ShipModule(sectionType);
        }
    }

    public boolean CanBuildSection(int index){
        //(this is a stub)
        return true; //TODO: write CanBuildSection
    }

    //Warning: this WILL replace existing the existing module.
    //Code calling this should check with CanBuildmodule first
    public void BuildModule(int sIndex, int mIndex, ModuleType mType){
        modules[sIndex][mIndex] = new ShipModule(sectionTypes[sIndex], mType);
    }

    public boolean CanBuildmodule(int sIndex, int mIndex, ModuleType mType){
        //(this is a stub)
        return true; //TODO: write CanBuildSection
    }

    static public Spaceship GenerateStart1(Random rand, int minLength, int maxLength, float minFull, float maxFull){
        int length = rand.nextInt(maxLength - minLength) + minLength;
        float fullRange = maxFull - minFull;
        float full = rand.nextFloat() * fullRange + minFull;
        return GenerateStart1(rand, length, full);
    }

    private static Spaceship GenerateStart1(Random rand, int length, float full){
        //length MUST be at least 2.
        if (length < 2)
            length = 2;

        Spaceship ship = new Spaceship(length);
        //center of the wheel section hosts the first hab module
        int habstart = SectionType.Wheel.getNumModules() / 2;
        ship.BuildSection(0, SectionType.Wheel);
        ship.BuildModule(0, habstart, ModuleType.Habitat);

        int normSize = SectionType.Normal.getNumModules();
        float currentFilled = 0.0f;
        float fillPerModule = 1.0f / (normSize * (length-1));
        for(int i = 1; i < length; i++){
            //stub. TODO: add some fudge to have some empty sections
            ship.BuildSection(i, SectionType.Normal);
            for(int j = 0; j < normSize; j++)
            {
                //stub. TODO: add some fudge on what modules has cargo.
                ModuleType type = ModuleType.Cargo;
                ship.BuildModule(i, j, type);
            }
        }
        return ship;
    }

    public static void main(String[] args) {
        Spaceship ship = Spaceship.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
        System.out.println(ship.toString());
    }
}


enum SectionType {
    None{@Override
        int getNumModules() {
            return 0;
        }
        @Override
        boolean getHasGravity() {
            return false;
        }
    }, Normal {@Override
        int getNumModules() {
            return 5;
        }
        @Override
        boolean getHasGravity() {
            return false;
        }
    }, Wheel {@Override
        int getNumModules() {
            return 3;
        }
        @Override
        boolean getHasGravity() {
            return true;
        }
    };
    abstract int getNumModules();
    abstract boolean getHasGravity();
};
enum ModuleType {
    Empty{@Override
        boolean getNeedGravity() {
            return false;
        }
        @Override
        Color getPaintColor() {
            return new Color(50,50,70);
        }
    },Cargo {@Override
        boolean getNeedGravity() {
            return false;
        }

        @Override
        Color getPaintColor() {
            return new Color(160,82,45);
        }
    }, Habitat {@Override
        boolean getNeedGravity() {
            return true;
        }
        @Override
        Color getPaintColor() {
            return new Color(100,200,0);
        }
    };

    abstract boolean getNeedGravity();
    abstract Color getPaintColor();
};


class ShipModule {
    public SectionType sectionType;
    public ModuleType moduleType;

    public ShipModule (SectionType sectionType, ModuleType moduleType) {
        super();
        this.sectionType = sectionType;
        this.moduleType = moduleType;
    }
    public ShipModule(SectionType sectionType) {
        super();
        this.sectionType = sectionType;
        this.moduleType = ModuleType.Empty;
    }
}

