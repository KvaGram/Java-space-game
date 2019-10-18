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
 *
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


/**
 * ShipPanel renders a model of a spaceship.
 */
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

/**
 * SpaceshipGUI renders a model of Spaceship
 * and allows a user to edit module layout of the spaceship.
 * @see Spaceship
 *
 * This prototype UI feature a popout drop-menu that allows
 * the user to change either a single module or an entire section.
 * (despite what the tooltip suggest, there is no information to get from bridge or engine ..yet)
 */
class SpaceshipGUI extends JPanel implements ComponentListener
{
    // spaceship is the datastructure this UI represents.
    Spaceship spaceship;

    //popBuild is the drop-menu that appears when clicking on a menu.
    //See methods openBuildMenu and closeBuildMenu.
    JPopupMenu popBuild;

    //These JMenuItem arrays are lists of elements that may appear in the popout drop-menu.
    JMenuItem[] popBuildOptionsModules;
    JMenuItem[] popBuildOptionsSections;
    JMenuItem[] popBuildSeparators;

    //The uiState prevents the tooltip box from rendering when popBuild is open.
    //It also prevents mouseTarget from being changed when popBuild is open
    UIState uiState;

    //mouseTargets is a list of Rectangle structures and target data.
    //mouseTarget is compared with the mousePoint to determine if the mouse is over a component.
    ArrayList<MouseTarget> mouseTargets;
    //The location of the mouse
    //see mouseMoved in the constructor.
    Point mousePoint;
    //mouseTarget is a reference to the current component the mouse is over.
    //If the mouse is not over a component, it is null.
    //It is set in updateTarget()
    MouseTarget mouseTarget;

    /**
     * Constructor of the Spaceship UI.
     * Requires a reference to the spaceship.
     * @param spaceship The spaceship to display and edit.
     */
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

    /**
     * Test runner for SpaceshipGUI.
     * Generates a Spaceship based a random seed or args[0].
     * Makes basic JFrame sized 1200 x 400 to host the spaceshipGUI.
     * @param args (optional) argument 1 is random seed.
     */
    public static void main(String[] args) {
        Random rand;
        if(args.length > 1){
            try{
                rand = new Random(Integer.parseInt(args[0]));
            } catch (NumberFormatException err) {
                System.err.println(err);
                rand = new Random();
            }
        } else {
            rand = new Random();
        }
        Spaceship ship = Spaceship.GenerateStart1(rand, 2, 10, 0.0f, 1.0f);
        SpaceshipGUI gui = new SpaceshipGUI(ship);

        JFrame frame = new JFrame("Ship modules proto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 400);
        gui.setBounds(0,0,1200, 400);
        gui.setOpaque(true);
        gui.setBackground(Color.GRAY);
        frame.add(gui);

        frame.setVisible(true);

        System.out.println(ship.toString());
    }

    /**
     * Opens the popBuild, showing only the menu elements needed in the context.
     * Sets uiState to Build.
     */
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

    /**
     * Closes the popBuild, hiding all menu elements.
     * resets uiState back to Select.
     */
    private void closeBuildMenu() {
        popBuild.removeAll();
        popBuild.revalidate();
        popBuild.setVisible(false);

        uiState = UIState.select;
    }

    /**
     * Replaces a module in Spaceship.
     * Uses data from MouseTarget to find the module to replace.
     * The type Empty represents the lack of a module where one could be built.
     *  Currently not yet implemented conditions where a module could not be built.
     * @param type type of module to construct.
     */
    public void tryBuildModule(ModuleType type) {
        //TODO try building module
        spaceship.BuildModule(mouseTarget.loc.x, mouseTarget.loc.y, type);
        repaint();
    }

    /**
     * Replaces an entire section in Spaceship.
     * Uses data from MouseTarget to find the section to replace.
     * The type None represents a section stripped down to the framework.
     *  Currently not yet implemented conditions where a section could not be built.
     * @param type type of section to construct
     */
    public void tryBuildSection(SectionType type) {
        //TODO try building section
        spaceship.BuildSection(mouseTarget.loc.x, type);
        repaint();
    }

    /**
     * This function is called by Swing.
     * It renders the model.
     * @param _g the Swing graphics reference for rendering elements on screen.
     */
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

    /**
     * Renders a small tooltip when uiState is set to Select,
     * and the mouse is over a MouseTarget.
     * @param g the Swing graphics reference
     */
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

    /**
     * Renders a module of the spaceship.
     * @param sIndex The section index.
     * @param mIndex The module index (of section)
     * @param g the Swing graphics reference, casted to Graphics2D.
     */
    void PaintShipModule(int sIndex, int mIndex, Graphics2D g){
        Rectangle r = getShipModuleRect(sIndex, mIndex);

        g.setStroke(new BasicStroke(1));
        g.setColor(spaceship.modules[sIndex][mIndex].moduleType.getPaintColor());
        g.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
    }

    /**
     * Generates a Rectangle for a spaceship module, based on component screenspace.
     * used by buildMouseTargets and PaintShipModule
     * @param sIndex The section index.
     * @param mIndex The module index (of section)
     * @return
     */
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

    /**
     * Renders a representation of naked framework for a
     * None type SectionType. This is needed as there would be no modules to render.
     * @param sIndex The section index
     * @param g the Swing graphics reference, casted to Graphics2D.
     */
    void paintEmptySec(int sIndex, Graphics2D g){
        Rectangle r = getShipEmptySecRect(sIndex);

        g.setStroke(new BasicStroke(1));
        g.setColor(Color.PINK);
        g.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
    }

    /**
     * Generates a Rectangle representation for a naked framework for a
     * None type SectionType. This is needed as there would be no modules to click on.
     * Used by buildMouseTargets and getShipModuleRect
     * @param sIndex The section index
     * @return
     */
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

    /**Generates a Rectangle representing the size and location of
     * the Bridge of the Spaceship.
     * Used by buildMouseTargets and PaintComponent
     * @return
     */
    public Rectangle getBridgeRect(){
        Rectangle bounds = getBounds();
        Rectangle drawRect = new Rectangle();
        drawRect.width = bounds.width / (spaceship.length + 2) - 10;
        drawRect.height = bounds.height / 2;
        drawRect.x = 10;
        drawRect.y = bounds.height / 4;

        return drawRect;
    }

    /**Generates a Rectangle representing the size and location of
     * the Engines of the Spaceship.
     * Used by buildMouseTargets and PaintComponent
     * @return
     */
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


    /**
     * Defines the clickable areas of the UI.
     * Currently called every time the screen is re-rendered.
     * (this is excessive and should be improved if better alternatives are found!)
     */
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

    /**
     * Updates what component (if any) the mouse is over.
     * Runs whenever the mouse is moved.
     * Runs only when uiState is set to Select.
     */
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

    /**
     * The program states.
     * Select - select a module or section to do stuff with
     * Build - do stuff with a module or section.
     */
    enum UIState {
        select, build
    }

    /**
     * The types of targets the mouse can find.
     *
     * module - can build modules and sections from here.
     * static module - can currently not do anything (other than seeing a tooltip)
     * section - currently only appears on empty sections, can only build sections from here.
     */
    enum MouseTargetType {
        module, staticModule, section
    }


    /**
     * A target the mouse can hover over and click on.
     */
    class MouseTarget
    {
        MouseTargetType type;
        String name;
        //The rect is the region the mouse must be within.
        Rectangle rect;
        //Where applicable, x - the section, y - the module, of the Spaceship.
        Point loc;
        public MouseTarget(MouseTargetType type, String name, Rectangle rect, Point loc){
            this.type = type;
            this.name = name;
            this.rect = rect;
            this.loc  =  loc;
        }

        /**
         * Generates the text for a tooltip to be displayed.
         * @return
         */
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


/**
 * The datastructure representing a spaceship.
 * The spaceship has a length of sections (at least 2).
 * Each section has a number of modules, depending on the SectionType.
 */
class Spaceship {
    public int length;
    //lists the type of sections currently installed. 0 is near bridge, other end near engineering.
    public SectionType[] sectionTypes;
    public ShipModule[][] modules;

    /**
     * Creates a length long spaceship, naked down to the framework.
     * Not meant to be used directly. Use one of the Generate functions instead.
     * @param length
     */
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

    /**
     * Replaces a section of the spaceship.
     * Warning: this WILL replace the existing modules in it with empty modules, without asking!
     * Planned: function to check if replacing a section should be allowed (by gameplay rules)
     *
     * @param index section index of module to replace.
     * @param sectionType The new section type.
     */
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

    /**
     * Replaces a module of the spaceship.
     * Warning: this WILL replace the existing module, without asking!
     * Planned: function to check if replacing a module should be allowed (by gameplay rules)
     *
     * @param sIndex The section index
     * @param mIndex The module index (of section)
     * @param mType The new module type.
     */
    public void BuildModule(int sIndex, int mIndex, ModuleType mType){
        modules[sIndex][mIndex] = new ShipModule(sectionTypes[sIndex], mType);
    }

    /**
     * Generates a new spaceship, with adjustable range of specification.
     * @param rand The instance of Random to use.
     * @param minLength minimal length of the ship
     * @param maxLength maximum length of the ship
     * @param minFull minimal cargo to spawn with (range 0, 1)
     * @param maxFull maximum cargo to spawn with (range 0, 1)
     * @return A Spaceship
     */
    static public Spaceship GenerateStart1(Random rand, int minLength, int maxLength, float minFull, float maxFull){
        int length = rand.nextInt(maxLength - minLength) + minLength;
        float fullRange = maxFull - minFull;
        float full = rand.nextFloat() * fullRange + minFull;
        return GenerateStart1(rand, length, full);
    }

    /**
     * Generates a new spaceship, with some fixed specification
     * @param rand The instance of Random to use.
     * @param length The length of the Spaceship
     * @param full How much of the potential space will be filled with cargo (range 0, 1)
     * @return A Spaceship
     */
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

    /**
     * Test-creates a spaceship, then prints the structure to console.
     * @param args
     */
    public static void main(String[] args) {
        Spaceship ship = Spaceship.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
        System.out.println(ship.toString());
    }
}


/**
 * The type of Section a spaceship may have.
 * A SectionType have different amount of modules,
 * and some may not have gravity.
 */
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

/**
 * The type of Module a section may have.
 * A module may require gravity to be constructed.
 * A module has a color associated with it.
 */
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

/**
 * A datastructure that a Spaceship is full of.
 * Needs to know what ModuleType it is, and what SectionType it is hosted in.
 */
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
