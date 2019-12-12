package unicus.spacegame.ui.Homeship;

import de.gurkenlabs.litiengine.gui.ComponentMouseEvent;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import unicus.spacegame.spaceship.SectionType;
import unicus.spacegame.spaceship.Spaceship;
import unicus.spacegame.ui.Axis2D;
import unicus.spacegame.ui.MenuController;
import unicus.spacegame.ui.Scrollbar;
import unicus.spacegame.ui.ScrollbarListener;
import unicus.spacegame.ui.PopMenu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Collections;

import static unicus.spacegame.ui.Axis2D.*;

/**
 * The Homeship UI renders a 2D representation of the spaceship, its section frames and modules
 */
public class HomeshipUI extends GuiComponent {
    private HomeshipUIController controller;
    public SectionComponentUI[] sections;
    public ModuleComponentUI[] modules;
    public GunSlotComponentUI[] gunSlots;
    public Scrollbar scrollbar;
    private Spaceship homeship;
    private final Dimension viewArea = new Dimension(); //visible pixel screen-size of the homeship
    private final Dimension area = new Dimension(); //Full pixel screen-size of the homeship.
    private PopMenu popMenu;


    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height
     */
    public HomeshipUI(Spaceship homeship, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.homeship = homeship;
        this.viewArea.setSize((int)width, (int)height);

        //number of sections will not change (might refactor to allow this later)
        int numSections = homeship.length;
        sections = new SectionComponentUI[homeship.length];
        //There can be up to 6 modules per section
        int sectionNumModules = 6;
        modules = new ModuleComponentUI[homeship.length * 6];
        //There can be up to 6 gun slots per section
        int SectionNumGunSlots = 6;
        gunSlots = new GunSlotComponentUI[homeship.length * 6];

        for (int s = 0; s < numSections; s++) {
            //extra code-block, so variable-name loc can be reused.
            {
                Spaceship.ShipLoc loc = homeship.getShipLoc(s, 0, -1);
                sections[s] = new SectionComponentUI(loc);
                scrollbar.addScrollListener(sections[s]);
                sections[s].onClicked(componentMouseEvent -> onShipPartClicked(loc, componentMouseEvent));
            }
            for (int m = 0; m < sectionNumModules; m++) {
                Spaceship.ShipLoc loc = homeship.getShipLoc(s, m+1, -1);
                int mm = s*sectionNumModules + m;
                modules[mm] = new ModuleComponentUI(loc);
                scrollbar.addScrollListener(modules[mm]);
                modules[mm].onClicked(componentMouseEvent -> onShipPartClicked(loc, componentMouseEvent));
            }
            for (int g = 0; g < SectionNumGunSlots; g++) {
                Spaceship.ShipLoc loc = homeship.getShipLoc(s, 0, g);
                int gg = s*sectionNumModules + g;
                //TODO: remove g parameter from GunSlotComponentUI constructor
                gunSlots[gg] = new GunSlotComponentUI(loc, g);
                scrollbar.addScrollListener(gunSlots[gg]);
                gunSlots[gg].onClicked(componentMouseEvent -> onShipPartClicked(loc, componentMouseEvent));
            }
        }
        Collections.addAll(getComponents(), sections);
        Collections.addAll(getComponents(), modules);
        Collections.addAll(getComponents(), gunSlots);



        this.scrollbar = new Scrollbar(40, height - 80, width-80, 40, horizontal, area, viewArea);
        getComponents().add(this.scrollbar);

        popMenu = new PopMenu(0,0,400, 200, new String[0], new boolean[0]);
        getComponents().add(popMenu);
    }

    private void onShipPartClicked(Spaceship.ShipLoc loc, ComponentMouseEvent componentMouseEvent) {
        popMenu.setX(componentMouseEvent.getEvent().getX());
        popMenu.setY(componentMouseEvent.getEvent().getY());
        if(controller != null)
            controller.onSelected(loc);
    }

    @Override
    public void prepare() {
        super.prepare();
        updateLayout();

        openMenu(null, new String[]{"test normal 1","test normal 2", "test normal 3", "test disabled 3", "test disabled 4"}, new boolean[]{true, true, true, false, false});
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }

    public static int START_Y = 100;
    public static int HEAD_HEIGHT = 500;
    public static int HEAD_WIDTH = 0;
    public static int TAIL_HEIGHT = 500;
    public static int TAIL_WIDTH = 0;
    public static int SECTION_WIDTH = 300;
    public static int SECTION_HEIGHT = 600;
    public static int MODULE_WIDTH = 200;
    public static int MODULE_HEIGHT = 100;
    public static int WEAPON_WIDTH = 50;
    public static int WEAPON_HEIGHT = 50;

    //TODO: needs update due to the ShipLoc change.
    void updateLayout() {
        area.height = SECTION_HEIGHT;
        area.width = HEAD_WIDTH + homeship.length * SECTION_WIDTH + TAIL_WIDTH;

        int newX;
        int newY;
        for(SectionComponentUI s : sections) {
            if(!s.loc.isValidSection()) {
                s.suspend();
                continue;
            }
            else if(s.isSuspended())
                s.prepare();
            newX = HEAD_WIDTH + SECTION_WIDTH * s.loc.getS();
            newY = START_Y;
            s.setLocalPos(newX, newY);
        }

        for(ModuleComponentUI m : modules) {
            int numModules = m.loc.getSection().getNumModules();
            if(!m.loc.isValidModule() || m.loc.getM() >= numModules) {
                m.suspend();
                continue;
            }
            else if(m.isSuspended()){
                m.prepare();
            }

            int spread = (SECTION_HEIGHT - numModules * MODULE_HEIGHT) / numModules;
            newX = HEAD_WIDTH + SECTION_WIDTH * m.loc.getS() + SECTION_WIDTH - MODULE_WIDTH;
            newY = START_Y + MODULE_HEIGHT * m.loc.getM() + spread * m.loc.getM() + (int)(spread/2.0);
            m.setLocalPos(newX, newY);
        }
        for(GunSlotComponentUI g : gunSlots) {


            //todo: check for number of weapons in section.
            // - For now 6 gun slots, unless empty section, then 0.

            if (!g.loc.isValidSection()|| g.loc.getSection() == SectionType.None || g.gunSlot < 0 || g.gunSlot >= 6) {
                g.suspend();
                continue;
            }
            else if(g.isSuspended())
                g.prepare();

            int numGuns = 6;
            int spread = (SECTION_HEIGHT - numGuns * WEAPON_HEIGHT) / numGuns;
            newX = HEAD_WIDTH + SECTION_WIDTH * g.loc.getS();
            newY = START_Y + WEAPON_HEIGHT * g.gunSlot + spread * g.gunSlot + (int)(spread/2.0);

            g.setLocalPos(newX, newY);
        }
    }
    void openMenu(MenuController controller, String[] options, boolean[] enabledOptions) {
        popMenu.setController(controller);
        popMenu.setOptions(options, enabledOptions);
        popMenu.prepare();
    }
    void closeMenu() {
        popMenu.suspend();
    }

    //This override is enabled to make it possible to click out of the popmenu.
    //It will also let the controller know that this happened.
    @Override
    public void mouseClicked(final MouseEvent e) {
        //If popMenu is suspended, or mouse is over the popMenu, do as normal.
        if(popMenu.isSuspended() || popMenu.isHovered() ) {
            super.mouseClicked(e);
            return;
        }
        //otherwise, let controller know the menu has been closed.

    }
}
abstract class HomeshipUIComponent extends GuiComponent implements ScrollbarListener {
    protected Spaceship.ShipLoc loc;
    private final Point localPos;
    private boolean needUpdate;
    private int scrollOffset;

    protected HomeshipUIComponent(int width, int height, Spaceship.ShipLoc loc){
        super(0,0, width, height);
        this.loc = loc;
        this.localPos = new Point();
        needUpdate = true;
        scrollOffset = 0;
    }
    public void setLocalPos(int x, int y) {
        localPos.x = x;
        localPos.y = y;
        needUpdate = true;
    }
    public int getIntX() {
        return (int)getX();
    }
    public int getIntY() {
        return (int)getY();
    }

    public void onScrollUpdate(Axis2D axis, double value) {
        scrollOffset = (int)value;
        needUpdate = true;
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if(needUpdate){
            setLocation(localPos.x - scrollOffset, localPos.y);
            needUpdate = false;
        }
    }
}

class SectionComponentUI extends HomeshipUIComponent{
    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected SectionComponentUI(Spaceship.ShipLoc loc) {

        super(HomeshipUI.SECTION_WIDTH, HomeshipUI.SECTION_HEIGHT, loc);
        this.loc = loc;
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);

        int x = getIntX();
        int y = getIntY();

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(186, 190, 180));
        g.fillRoundRect(x + 5, y + 5, (int)getWidth()-5, (int)getHeight()-5, 10, 10);

        g.setColor(new Color(61, 58, 50));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(x + 5, y + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}
class ModuleComponentUI extends  HomeshipUIComponent{

    protected Spaceship.ShipLoc loc;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected ModuleComponentUI(Spaceship.ShipLoc loc) {
        super(HomeshipUI.MODULE_WIDTH, HomeshipUI.MODULE_HEIGHT, loc);
        this.loc = loc;
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);

        int x = getIntX();
        int y = getIntY();

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(110,40,0));
        g.fillRoundRect(x + 5, y + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);

        g.setColor(new Color(70, 46, 16));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(x + 5, y + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}
class GunSlotComponentUI extends HomeshipUIComponent{

    //NOTE: maybe put gunslot in moduleloc. with that rename moduleloc to something more fitting
    protected int gunSlot;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected GunSlotComponentUI(Spaceship.ShipLoc loc, int gunSlot) {
        super(HomeshipUI.WEAPON_WIDTH, HomeshipUI.WEAPON_HEIGHT, loc);
        this.gunSlot = gunSlot;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        int x = getIntX();
        int y = getIntY();

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(255,0, 0));
        g.fillRoundRect(x + 5, y + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);

        g.setColor(new Color(70, 0,0));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(x + 5, y + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}

