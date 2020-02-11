package unicus.spacegame.ui.homeship;

import de.gurkenlabs.litiengine.*;
import de.gurkenlabs.litiengine.configuration.ClientConfiguration;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.*;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.spaceship.*;
import unicus.spacegame.ui.crew.CrewMenu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class ShipViewController extends Screen implements IUpdateable {
    private static ShipViewController SVC;
    public static ShipViewController SVC(){
        if(SVC == null)
            new ShipViewController();
        return SVC;
    }

    Environment shipViewEnv;
    HomeshipGUI homeshipGUI;
    ConfigMenu configMenu;

    ShipLoc selectionLoc;

    public static void main(String[] args) {
        Image cursor;
        try {

            //try loading file.
            cursor = ImageIO.read(Resources.getLocation("cursor1.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        } catch (IOException | IllegalArgumentException err) {
            //paint backup icon.
            System.out.println(err);
            cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics g = cursor.getGraphics();
            g.setColor(Color.red);
            g.drawOval(0, 0, 16, 16);
        }

        Game.init();
        Game.window().cursor().set(cursor, Align.CENTER, Valign.MIDDLE);
        URL spaceshipURL = ShipViewController.class.getResource("spaceship.litidata");
        Resources.load(spaceshipURL);
        Input.mouse().setGrabMouse(false);
        Game.graphics().setBaseRenderScale(1.5f);

        SpaceCrew sc = SpaceCrew.GenerateStart1();
        HomeShip hs = HomeShip.GenerateStart1(new Random(0), 3, 10, 0.4f, 0.8f);
        ShipViewController view = new ShipViewController();
        Game.screens().display(view);
        Game.start();

        view.open(hs.getShipLoc(4, 5));


    }



    protected ShipViewController() {
        super("SHIP");
        //Load the map environment.
        URL spaceshipURL = ShipViewController.class.getResource("spaceship.litidata");
        Resources.load(spaceshipURL);

        configMenu = new ConfigMenu(0, 0, Game.window().getResolution().width, Game.window().getResolution().height/2.0 );
        getComponents().add(configMenu);
        shipViewEnv = Game.world().getEnvironment("Spaceship");
        selectionLoc = HomeShip.HS().getShipLoc(0,1);

        Input.keyboard().onKeyTyped(KeyEvent.VK_LEFT, keyEvent -> onLeft());
        Input.keyboard().onKeyTyped(KeyEvent.VK_A, keyEvent -> onLeft());

        Input.keyboard().onKeyTyped(KeyEvent.VK_RIGHT, keyEvent -> onRight());
        Input.keyboard().onKeyTyped(KeyEvent.VK_D, keyEvent -> onRight());

        Input.keyboard().onKeyTyped(KeyEvent.VK_UP, keyEvent -> onUp());
        Input.keyboard().onKeyTyped(KeyEvent.VK_W, keyEvent -> onUp());

        Input.keyboard().onKeyTyped(KeyEvent.VK_DOWN, keyEvent -> onDown());
        Input.keyboard().onKeyTyped(KeyEvent.VK_S, keyEvent -> onDown());

        Input.keyboard().onKeyTyped(KeyEvent.VK_ENTER, keyEvent -> onEnter());

        Input.keyboard().onKeyTyped(KeyEvent.VK_ESCAPE, keyEvent -> onExit());
        Input.keyboard().onKeyTyped(KeyEvent.VK_BACK_SPACE, keyEvent -> onExit());

        //set instance
        SVC = this;
    }
    void onLeft(){
        if(!getMenuOpen()) {
            setSelection(selectionLoc.prevSection());
        }
    }
    void onRight(){
        if(!getMenuOpen()) {
            setSelection(selectionLoc.nextSection());
        }
    }
    void onUp(){
        if(!getMenuOpen()) {
            setSelection(selectionLoc.nextModule());
        }
    }
    void onDown(){
        if(!getMenuOpen()) {
            setSelection(selectionLoc.prevModule());
        }
    }
    void onEnter(){
        if(!getMenuOpen())
            open(selectionLoc);
    }
    void onExit(){
        if(getMenuOpen())
            close();
    }

    boolean getMenuOpen(){return configMenu.isVisible();}

    /**
     * This method is called by the game loop on all objects that are attached to the loop.
     * It's called on every tick of the loop and the frequency can be configured using the <code>ClientConfiguration</code>.
     *
     * @see ClientConfiguration#setMaxFps(int)
     */
    @Override
    public void update() {

    }

    public void open(ShipLoc loc) {
        selectionLoc = loc;
        homeshipGUI.setSelection(loc, true);
        configMenu.open();
    }
    void close() {
        configMenu.suspend();
        configMenu.getComponents().clear();
        configMenu.menu = null;
        configMenu.testCrewMenu = null;
        configMenu.testModuleInfo = null;

        homeshipGUI.setSelection(selectionLoc, false);
    }
    void setSelection(ShipLoc loc) {
        selectionLoc = loc;
        homeshipGUI.setSelection(selectionLoc, false);
    }

    /**
     * This flag controls whether this instance is currently active and thereby needs to be updated by the game loop.
     *
     * @return True if this instance should be updated; otherwise false.
     */
    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void render(final Graphics2D g) {
        Graphics2D childG = (Graphics2D) g.create();
        if (Game.world().environment() != null) {
            Game.world().environment().render(childG);
        }
        super.render(childG);
        childG.dispose();
    }

    @Override
    public void prepare() {
        super.prepare();
        configMenu.suspend();
        Game.world().reset("Spaceship");
        Game.world().loadEnvironment("Spaceship");

        homeshipGUI = new HomeshipGUI();
        shipViewEnv = Game.world().environment();
        shipViewEnv.add(homeshipGUI, RenderType.GROUND);
        homeshipGUI.drawMode = HomeshipGUI.HomeShipDrawMode.unwrapped;
        homeshipGUI.setSelection(ShipLoc.get(0,0), false);
    }



    class ConfigMenu extends GuiComponent {
        private final ExitButton exitButton;
        Menu menu;

        CrewMenu testCrewMenu;
        ModuleInfo testModuleInfo;
        RefitPanel testRefitPanel;
        int[] testCrewList = new int[]{0,0,0};
        int testMenuColumns = 2;
        int testMenuRows = 3;

        /**
         * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
         *
         * @param x      the x
         * @param y      the y
         * @param width  the width
         * @param height
         */
        protected ConfigMenu(double x, double y, double width, double height) {
            super(x, y, width, height);
            exitButton = new ExitButton(width - 50 + x, y, 50, 50);
            exitButton.onClicked(componentMouseEvent -> close());
        }
        void open() {
            menu = new Menu(0, 0, getWidth()/5, getHeight(), "test1", "test2", "test3", "test4");
            this.getComponents().add(menu);
            homeshipGUI.drawMode = HomeshipGUI.HomeShipDrawMode.cutout;
            //Point2D focus = homeshipGUI.getSectionFocusPoint(loc.getS());
            //homeshipGUI.setSelectionFocus(6, 30, true);

            //focus.setLocation(focus.getX(), focus.getY() - getHeight()/4);
            //Game.world().camera().setFocus(focus);

            //This is a test:
            //testCrewMenu = new CrewMenu(getWidth() / 5, 0, getWidth() / 5, getHeight(), testMenuRows, testMenuColumns, testCrewList);
            //testModuleInfo = new ModuleInfo(menu.getWidth(), 30, getWidth() - menu.getWidth(), getHeight());
            testRefitPanel = new RefitPanel(menu.getWidth(), 30, getWidth() - menu.getWidth(), getHeight());
            //this.getComponents().add(testModuleInfo);
            //this.getComponents().add(testCrewMenu);
            this.getComponents().add(testRefitPanel);

            this.getComponents().add(exitButton);
            this.prepare();
        }

        @Override
        public void render(Graphics2D _g) {
            Graphics2D g = (Graphics2D)_g.create();
            g.translate(getX(), getY());

            Font infoFont = new Font(Font.MONOSPACED, Font.PLAIN, 24);

            g.setColor(new Color(0,0, 215, 255));
            g.fillRect(0, 0, (int)getWidth(), (int)getHeight());

            g.translate((int)getWidth()/5, 0);
            g.setColor(Color.gray);
            g.setFont(infoFont);



            //g.fillRect(0,0,50,50);
            g.drawString("Hello world", 0, 10);

            g.dispose();
            super.render(_g);
        }
    }
    abstract class ConfigPanel extends GuiComponent{
        /**
         * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
         *
         * @param x      the x
         * @param y      the y
         * @param width  the width
         * @param height the height
         */
        protected ConfigPanel(double x, double y, double width, double height) {
            super(x, y, width, height);

        }


        @Override
        public void render(Graphics2D _g) {
            super.render(_g);
            Graphics2D g = (Graphics2D) _g.create();
        }
    };
    class ExitButton extends GuiComponent {
        Rectangle exitRect;

        /**
         * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
         *
         * @param x      the x
         * @param y      the y
         * @param width  the width
         * @param height
         */
        protected ExitButton(double x, double y, double width, double height) {
            super(x, y, width, height);
            exitRect = new Rectangle((int)getWidth() - 50, 0, (int)width, (int)height);
        }

        @Override
        public void render(Graphics2D _g) {
            super.render(_g);
            Graphics2D g = (Graphics2D) _g.create();
            g.translate(getX(), getY());

            g.setColor(Color.red);
            g.fillRect(exitRect.x, exitRect.y, exitRect.width, exitRect.height);
            g.setColor(Color.black);

            g.setStroke(new BasicStroke(5));
            g.drawLine(exitRect.x, exitRect.y + exitRect.height, exitRect.x + exitRect.width, exitRect.y);
            g.drawLine(exitRect.x + exitRect.width, exitRect.y + exitRect.height, exitRect.x, exitRect.y);
        }
    }

    class RefitPanel extends ConfigPanel {
        private static final String INFO =
                "This is a %1$s module installed in a %2$s. frame. " +
                "On this panel, you may refit (change) the " +
                "module or the frame itself. " +
                "Please note that switching out the frame will " +
                "also result in the removal of the installed modules!";
        ListField moduleRefitOptions;
        ListField sectionRefitOptions;
        Rectangle.Double infoArea;
        Rectangle.Double messageArea;
        PlainButton addTask;

        private String message = "Hello world. Yes, I am message.";
        private boolean addTaskReady = false;

        private ModuleType[] buildableModules;
        private SectionType[] buildableFrames;



        protected RefitPanel(double x, double y, double width, double height) {
            super(x, y, width, height);

            AbstractShipSection section = selectionLoc.getSection();
            AbstractShipModule module = selectionLoc.getModule();

            //Check whatever the section is stripped, and whatever the module is empty.
            boolean strippedSection = section.getSectionType() == SectionType.None;
            boolean emptyModule = module.getModuleType() == ModuleType.Empty;

            //gets possible alternate modules and frames that can be built here.
            //If the section is stripped, no modules can be built.
            buildableModules = strippedSection ? new ModuleType[0] : ModuleType.getBuildable(section.useGravity());
            buildableFrames = SectionType.getBuildable();

            //Arraylists for constructing the menus.
            ArrayList<String> moduleOptions = new ArrayList<>();
            ArrayList<String> sectionOptions = new ArrayList<>();

            //Set display text.
            String moduleAddText = emptyModule ? "Build a %1$s module" : "Replace with a %1$s module";
            String frameAddText = strippedSection ? "Construct a %1$s frame" : "Replace with a %1$s frame";

            //Add the buildable alternatives.
            for(ModuleType m : buildableModules)
                moduleOptions.add(String.format(moduleAddText, m.name()));

            for(SectionType s : buildableFrames)
                sectionOptions.add(String.format(frameAddText, s.name()));

            if(!strippedSection) {
                sectionOptions.add("- Dismantle the section and its modules");
                if(!emptyModule) {
                    moduleOptions.add("- Dismantle Module ");
                }
            }

            //Add components - because the coordinate system is global for child components, x and y is added.
            moduleRefitOptions = new ListField((1/3.0) * width + x, y, (1/4.0) * width, (1/4.0) * height, moduleOptions.toArray(), 4, null, null);
            sectionRefitOptions = new ListField((2/3.0) * width + x, y, (1/4.0) * width, (1/4.0) * height, sectionOptions.toArray(), 4, null, null);
            addTask = new PlainButton((5/6.0) * width + x, (3/4.0) * height, (1/6.0)*width, (1/4.0) * height, Color.green, Color.white, "Add task");

            //Add text areas.
            infoArea = new Rectangle.Double(0,0,(1/4.0) * width + y,(3/4.0) * height);
            messageArea = new Rectangle.Double(0,(3/4.0) * height,(5/6.0) * width,(1/4.0) * height);

            List<GuiComponent> c = getComponents();
            c.add(moduleRefitOptions);
            c.add(sectionRefitOptions);
            c.add(addTask);

            addTask.onClicked(new Consumer<>() {
                /**
                 * Performs this operation on the given argument.
                 *
                 * @param componentMouseEvent the input argument
                 */
                @Override
                public void accept(ComponentMouseEvent componentMouseEvent) {
                    if(isAddTaskReady()) {
                        //TODO: send the temporary stored task to the construction task-list.
                    }
                    System.out.println("On Add Task");
                }
            });
            moduleRefitOptions.onChange(value -> {
                System.out.println("moduleRefitOptions.onChange -> " + value);
                sectionRefitOptions.deselect();
                clearAddTask();
                if(value >= 0) {
                    StringBuffer text = new StringBuffer();
                    boolean canDo = false;
                    if (value < buildableModules.length) {
                        canDo = HomeShip.HS().canBuildModule(selectionLoc, buildableModules[value], text);
                    }
                    else {
                        canDo = HomeShip.HS().canRemoveModule(selectionLoc, text);
                    }
                    message = text.toString();
                    setAddTaskReady(canDo);
                    //TODO: create a new construction task, and store it temporarily.
                }
            });
            sectionRefitOptions.onChange(value -> {
                System.out.println("sectionRefitOptions.onChange -> " + value);
                moduleRefitOptions.deselect();
                if(value >= 0) {
                    StringBuffer text = new StringBuffer();
                    boolean canDo = false;
                    if (value < buildableFrames.length) {
                        canDo = HomeShip.HS().canBuildSection(selectionLoc, buildableFrames[value], text);
                    }
                    else {
                        canDo = HomeShip.HS().canRemoveSection(selectionLoc, text);
                    }
                    message = text.toString();
                    setAddTaskReady(canDo);
                    //TODO: create a new construction task, and store it temporarily.
                }
            });

        }

        @Override
        public void render(Graphics2D _g) {
            super.render(_g);
            Graphics2D g = (Graphics2D) _g.create((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
            String infoText = String.format(INFO, selectionLoc.getModule().GetName(), selectionLoc.getSection().GetName());

            TextRenderer.renderWithLinebreaks(g, infoText, infoArea.x, infoArea.y, infoArea.width);
            TextRenderer.renderWithLinebreaks(g, message, messageArea.x, messageArea.y, messageArea.width);
        }
        public void clearAddTask(){
            setAddTaskReady(false);
            message = "Select an option from the list above.";
        }

        public boolean isAddTaskReady() {
            return addTaskReady;
        }

        public void setAddTaskReady(boolean value) {
            this.addTaskReady = value;
            addTask.bgColor = value ? Color.green : Color.gray;
        }
    }

    class ModuleInfo extends ConfigPanel {
        AbstractShipModule module;
        PlainButton refitButton;
        DropdownListField dropListModules;

        /**
         * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
         *
         * @param x      the x
         * @param y      the y
         * @param width  the width
         * @param height the height
         */
        protected ModuleInfo(double x, double y, double width, double height) {
            super(x, y, width, height);
            module = selectionLoc.getModule();


            refitButton = new PlainButton(500, 5, 200, 100, Color.red, Color.black, "Change Module");
            getComponents().add(refitButton);

            dropListModules = new DropdownListField(800, 50, 100, 300, new Object[]{"test0", "test1", "test2", "test3", "test4", "test5"}, 6);
            getComponents().add(dropListModules);
        }
        @Override
        public void render(Graphics2D _g) {
            super.render(_g);
            Graphics2D g = (Graphics2D) _g.create();
            g.translate(getX(), getY());
            Font font = new Font(Font.SERIF, Font.PLAIN, 24);
            g.setFont(font);
            StringBuffer info = new StringBuffer();
            module.getInfo(info);

            g.setColor(Color.white);
            String[] textlines = info.toString().split("\n");
            for (int i = 0; i < textlines.length; i++) {
                String text = textlines[i];
                g.drawString(text, 50, 20 + 15 * i);
            }
        }
    }
}
class PlainButton extends GuiComponent {

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height
     */
    public Color bgColor;
    public Color textColor;
    public String text;

    protected PlainButton(double x, double y, double width, double height, Color bgColor, Color textColor, String text) {
        super(x, y, width, height);
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.text = text;
    }

    @Override
    public void render(Graphics2D _g) {
        super.render(_g);
        Graphics2D g = (Graphics2D)_g.create((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        g.setColor(bgColor);
        g.fillRect(0, 0, (int)getWidth(), (int)getHeight());

        double textw = g.getFontMetrics().stringWidth(text);
        double texth = g.getFont().getSize();

        int xOffset = 0;
        int yOffset = (int)texth;

        double xScale = 1.0;
        double yScale = 1.0;

        if(textw < getWidth())
            xOffset += (int)((getWidth() - textw) / 2);
        else
            xScale = getWidth() / textw;

        if(texth < getHeight())
            yOffset += (int)((getHeight() - texth) / 2);
        else
            yScale = getHeight() / texth;
        g.setColor(textColor);
        g.scale(xScale, yScale);
        g.drawString(text, xOffset, yOffset);
        g.dispose();
    }
}