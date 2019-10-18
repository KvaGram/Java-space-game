package unicus.spacegame;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Swingshipproto and ShipPanel belong to a previous prototype.
 * Feel free to run and test those, but the current version is the SpaceshipGUI.
 * @see Swingshipproto
 * @see ShipPanel
 * @see SpaceshipGUI
 */
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

class SpaceshipGUI extends JPanel implements ComponentListener
{
    Spaceship spaceship;
    JPopupMenu popBuild;
    JMenuItem[] popBuildOptionsModules;
    JMenuItem[] popBuildOptionsSections;
    JMenuItem[] popBuildSeparators;
    UIState uiState;



    public SpaceshipGUI(Spaceship spaceship)
    {
        this.spaceship = spaceship;
        this.mousePoint = new Point(0, 0);
        this.uiState = UIState.select;
        this.buildMouseTargets();

        //Setup handlers for moving or clicking mouse.
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mousePoint.x = e.getX();
                mousePoint.y = e.getY();
                updateTarget();
                repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("CLICK");
                if(uiState == UIState.build){
                    closeBuildMenu();
                    return;
                }

                if(mouseTarget != null){
                    if(mouseTarget.type == MouseTargetType.module || mouseTarget.type == MouseTargetType.section) {
                        popBuild.setLocation(e.getXOnScreen(), e.getYOnScreen());
                        openBuildMenu();
                    }
                }

            }
        };
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);


        //Setup context menu for building modules
        ModuleType[] mTypes = ModuleType.values();
        SectionType[] sTypes = SectionType.values();
        popBuild = new JPopupMenu("Build menu");
        popBuildOptionsModules  = new JMenuItem[mTypes.length];
        popBuildOptionsSections = new JMenuItem[sTypes.length];
        popBuildSeparators      = new JMenuItem[1];

        ActionListener buildMenuHandlerModule = e -> {
            int index = ArrayUtils.indexOf(popBuildOptionsModules, e.getSource());
            if (index >= 0){
                System.out.println("Doing stuff! -> " + ModuleType.values()[index]);
                tryBuildModule(ModuleType.values()[index]);
            }else{
                System.out.println("Not doing stuff :-\\");
            }
            closeBuildMenu();
        };
        ActionListener buildMenuHandlerSection = e -> {
            int index = ArrayUtils.indexOf(popBuildOptionsSections, e.getSource());
            if (index >= 0){
                System.out.println("Doing stuff! -> " + SectionType.values()[index]);
                tryBuildSection(SectionType.values()[index]);
            }else{
                System.out.println("Not doing stuff :-\\");
            }
            closeBuildMenu();
        };
        for(int i = 0; i < mTypes.length; i++) {
            popBuildOptionsModules[i] = new JMenuItem("Build Module: " + mTypes[i]);
            popBuildOptionsModules[i].addActionListener(buildMenuHandlerModule);
        }
        for(int i = 0; i < sTypes.length; i++) {
            popBuildOptionsSections[i] = new JMenuItem("Build Section: " + sTypes[i]);
            popBuildOptionsSections[i].addActionListener(buildMenuHandlerSection);
        }
        for(int i= 0; i < 1; i++){
            popBuildSeparators[i] = new JMenuItem(" --- ");
        }

    }
    public static void main(String[] args) {
        Spaceship ship = Spaceship.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
        SpaceshipGUI gui = new SpaceshipGUI(ship);

        JFrame frame = new JFrame("Ship modules proto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        gui.setBounds(0,0,1200, 800);
        gui.setOpaque(true);
        gui.setBackground(Color.GRAY);
        frame.add(gui);

        frame.setVisible(true);

        System.out.println(ship.toString());
    }

    public void openBuildMenu() {
        if(mouseTarget.type == MouseTargetType.staticModule)
            return;
        if(mouseTarget.type == MouseTargetType.module) {
            for (int i = 0; i < popBuildOptionsModules.length; i++) {
                //TODO filter away module types that don't fit
                popBuild.add(popBuildOptionsModules[i]);
            }
            popBuild.add(popBuildSeparators[0]); //adds a separator.
        }
        for(int i = 0; i < popBuildOptionsSections.length; i++){
            //TODO filter away current section type
            popBuild.add(popBuildOptionsSections[i]);
        }
        popBuild.setVisible(true);
        uiState = UIState.build;
    }
    private void closeBuildMenu() {
        popBuild.removeAll();
        popBuild.revalidate();
        popBuild.setVisible(false);

        uiState = UIState.select;
    }
    public void tryBuildModule(ModuleType type) {
        //TODO try building module
        spaceship.BuildModule(mouseTarget.loc.x, mouseTarget.loc.y, type);
        repaint();
    }
    public void tryBuildSection(SectionType type) {
        //TODO try building section
        spaceship.BuildSection(mouseTarget.loc.x, type);
        repaint();
    }

    @Override
    public void paintComponent(Graphics _g){

        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;

        //Paint modules
        for(int i = 0; i < spaceship.length; i++) {
            int sLength = spaceship.modules[i].length;
            if(sLength < 1)
                paintEmptySec(i, g);
            for (int j = 0; j < sLength; j++)
                PaintShipModule(i, j, g);
        }
        g.setStroke(new BasicStroke(4));
        //Paint bridge
        Rectangle bridge = getBridgeRect();

        g.setColor(Color.green);
        g.fillArc(bridge.x, bridge.y, bridge.width*2, bridge.height, 90, 180);
        g.setColor(Color.BLACK);
        g.drawArc(bridge.x, bridge.y, bridge.width*2, bridge.height, 90, 180);
        g.drawLine(bridge.x + bridge.width, bridge.y, bridge.x + bridge.width, bridge.y+bridge.height);

        //paint engineering
        Rectangle engine = getEngineRect();
        g.setColor(Color.red);
        g.fillArc(engine.x - engine.width, engine.y, engine.width*2, engine.height, 270, 180);
        g.setColor(Color.black);
        g.drawArc(engine.x - engine.width, engine.y, engine.width*2, engine.height, 270, 180);
        g.drawLine(engine.x, engine.y, engine.x, engine.y + engine.height);

        //g.setColor(Color.red);
        //g.fillOval(mousePoint.x, mousePoint.y, 5,5);

        if(uiState == UIState.select && mouseTarget != null){
            paintTooltip(g);
        }

        buildMouseTargets();
    }

    private void paintTooltip(Graphics2D g){
        //Rectangle tipRect = new Rectangle(mousePoint.x, mousePoint.y, 300, 50);
        Font font = new Font("Serif", Font.BOLD, 20);
        g.setFont(font);

        String[] tipText = mouseTarget.getToolTip().split("\n");
        if (tipText.length < 1)
            return;

        int tipX = mousePoint.x;
        int tipY = mousePoint.y;

        int charLength = 0;
        for (String s : tipText)
            charLength = Math.max(s.length(), charLength);

        int tipWidth = (charLength+2) * 10;
        int tipHeight = tipText.length * 30;

        Rectangle bounds = getBounds();
        int xMargin = bounds.x + bounds.width - tipX - tipWidth;
        if (xMargin < 0)
            tipX += xMargin;
        int yMargin = bounds.y + bounds.height - tipY - tipHeight;
        if (yMargin < 0)
            tipY += yMargin;


        g.setColor(new Color(200, 200, 200));
        g.fillRect(tipX, tipY, tipWidth, tipHeight);

        g.setColor(new Color(20, 20, 20));
        for(int i = 0; i < tipText.length; i++)
            g.drawString(tipText[i], tipX + 20, tipY + i * 30 + 20);
    }

    void PaintShipModule(int sIndex, int mIndex, Graphics2D g){
        Rectangle r = getShipModuleRect(sIndex, mIndex);

        g.setStroke(new BasicStroke(1));
        g.setColor(spaceship.modules[sIndex][mIndex].moduleType.getPaintColor());
        g.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
    }

    public Rectangle getShipModuleRect(int sIndex, int mIndex){
        Rectangle bounds = getBounds();

        int baseWidth = bounds.width / (spaceship.length + 2);
        int baseHeight = bounds.height / (spaceship.sectionTypes[sIndex].getNumModules());

        Rectangle drawRect = new Rectangle();
        drawRect.width  = baseWidth - 20;
        drawRect.height = baseHeight - 20;
        drawRect.x = baseWidth * (sIndex + 1) + 10 + bounds.x;
        drawRect.y = baseHeight * mIndex + 10 + bounds.x;

        return drawRect;
    }
    void paintEmptySec(int sIndex, Graphics2D g){
        Rectangle r = getShipEmptySecRect(sIndex);

        g.setStroke(new BasicStroke(1));
        g.setColor(Color.PINK);
        g.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
    }
    public Rectangle getShipEmptySecRect(int sIndex){
        Rectangle bounds = getBounds();
        Rectangle drawRect = new Rectangle();
        int baseWidth = bounds.width / (spaceship.length + 2);
        drawRect.height = bounds.height / 10;
        drawRect.width = bounds.width / (spaceship.length + 2) - 10;
        drawRect.x = baseWidth * (sIndex + 1) + 10 + bounds.x;
        drawRect.y = bounds.height / 2 - bounds.height / 20;

        return drawRect;
    }

    public Rectangle getBridgeRect(){
        Rectangle bounds = getBounds();
        Rectangle drawRect = new Rectangle();
        drawRect.width = bounds.width / (spaceship.length + 2) - 10;
        drawRect.height = bounds.height / 2;
        drawRect.x = 10;
        drawRect.y = bounds.height / 4;

        return drawRect;
    }
    public Rectangle getEngineRect(){
        Rectangle bounds = getBounds();
        int baseWidth = bounds.width / (spaceship.length + 2);

        Rectangle drawRect = new Rectangle();
        drawRect.width = bounds.width / (spaceship.length + 2) - 10;
        drawRect.height = bounds.height / 2;
        drawRect.x = (spaceship.length+1) * baseWidth;
        drawRect.y = bounds.height / 4;

        return drawRect;
    }



    public void buildMouseTargets()
    {
        mouseTargets = new ArrayList<MouseTarget>();
        mouseTargets.add(new MouseTarget(
                MouseTargetType.staticModule, "bridge",
                getBridgeRect(), new Point()
        ));
        mouseTargets.add(new MouseTarget(
                MouseTargetType.staticModule, "engine",
                getEngineRect(), new Point()
        ));
        for(int i = 0; i < spaceship.length; i++) {
            if (spaceship.modules[i].length < 1) {
                mouseTargets.add(new MouseTarget(
                   MouseTargetType.section,
                   "empty section",
                   getShipEmptySecRect(i), new Point(i, -1)
                ));
            }
            for (int j = 0; j < spaceship.modules[i].length; j++) {
                mouseTargets.add(new MouseTarget(
                        MouseTargetType.module,
                        spaceship.modules[i][j].GetName(),
                        getShipModuleRect(i, j), new Point(i, j)
                ));
            }
        }

        //force-update the mouse target. (warning: breaks out of function if target was found)
        for (MouseTarget t : mouseTargets) {
            if (t.rect.contains(mousePoint)){
                mouseTarget = t;
                return;
            }
        }
        mouseTarget = null;
    }

    public void updateTarget() {
        //only change target in select state.
        if(uiState != UIState.select)
            return;
        for (MouseTarget t : mouseTargets) {
            if (t.rect.contains(mousePoint)) {
                if (t != mouseTarget) {
                    //Target changed
                }
                mouseTarget = t;
                return;
            }
        }
        mouseTarget = null;
    }


    ArrayList<MouseTarget> mouseTargets;
    Point mousePoint;
    MouseTarget mouseTarget;

    //<editor-fold desc="component listener">
    @Override
    public void componentResized(ComponentEvent e) {
        buildMouseTargets();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        buildMouseTargets();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        buildMouseTargets();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
    //</editor-fold>

    enum UIState {
        select, build
    }
    enum MouseTargetType {
        module, staticModule, section
    }



    class MouseTarget
    {
        MouseTargetType type;
        String name;
        Rectangle rect;
        Point loc;
        public MouseTarget(MouseTargetType type, String name, Rectangle rect, Point loc){
            this.type = type;
            this.name = name;
            this.rect = rect;
            this.loc  =  loc;
        }

        public String getToolTip() {
            String text = "";

            if(type == MouseTargetType.module){
                text =  "Ship module located at section " + loc.x + " index " + loc.y;
                text += "\nThis is a " + name;
                text += "\nClick to switch out module or section";
            }
            else if (type == MouseTargetType.staticModule)
            {
                text =  "This is the " + name + ".";
                text += "\nClick to access information";
            }
            else if (type == MouseTargetType.section){
                text  = "Empty ship section at index" + loc.x + ".";
                text += "\nClick to construct a section here.";
            }


            return text;
        }
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

    //Warning: this WILL replace existing the existing module.
    //Code calling this should check with CanBuildmodule first
    public void BuildModule(int sIndex, int mIndex, ModuleType mType){
        modules[sIndex][mIndex] = new ShipModule(sectionTypes[sIndex], mType);
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
        int totCargoSpace = (normSize * (length-1));
        int usedCargoSpace = 0;
        int targetFilled = Math.round( (float)totCargoSpace * full);

        for(int i = 1; i < length; i++){
            //If none of the modules are used, can targetFilled still be reached?
            boolean canBeEmpty = (usedCargoSpace + normSize * (length - i - 2)) < targetFilled;
            if(canBeEmpty && rand.nextFloat() < 0.3f){
                ship.BuildSection(i, SectionType.None);
            } else {
                ship.BuildSection(i, SectionType.Normal);
                for(int j = 0; j < normSize; j++)
                {
                    ModuleType type;
                    if( usedCargoSpace >= targetFilled || (canBeEmpty && rand.nextFloat() < 0.3f)){
                        type = ModuleType.Empty;
                    } else {
                        type = ModuleType.Cargo;
                        usedCargoSpace++;
                    }
                    ship.BuildModule(i, j, type);
                }
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
    }, GravityPlated {
        @Override
        int getNumModules() {
            return 4;
        }@Override
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

    public String GetName() {
        return sectionType.toString() + " " + moduleType.toString();
    }
}
