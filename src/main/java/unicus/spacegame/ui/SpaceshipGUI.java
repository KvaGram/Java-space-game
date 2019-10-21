package unicus.spacegame.ui;

import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.css.Rect;
import unicus.spacegame.spaceship.ModuleType;
import unicus.spacegame.spaceship.SectionType;
import unicus.spacegame.spaceship.Spaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;



/**
 * SpaceshipGUI renders a model of Spaceship
 * and allows a user to edit module layout of the spaceship.
 * @see Spaceship
 *
 * This prototype UI feature a popout drop-menu that allows
 * the user to change either a single module or an entire section.
 * (despite what the tooltip suggest, there is no information to get from bridge or engine ..yet)
 */
public class SpaceshipGUI extends JPanel
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

    private Rectangle screen;

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
        this.screen = this.getBounds();
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
        Spaceship ship = Spaceship.GenerateStart1(rand, 2, 10, 0.3f, 1.0f);
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

        if(!getBounds().equals(screen)){
            screen = getBounds();
            buildMouseTargets();
        }
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
        //System.out.println("Re-calculating mouse targets");
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