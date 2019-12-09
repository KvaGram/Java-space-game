package unicus.spacegame.ui.Homeship;

import de.gurkenlabs.litiengine.gui.GuiComponent;
import unicus.spacegame.spaceship.SectionType;
import unicus.spacegame.spaceship.Spaceship;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The Homeship UI renders a 2D representation of the spaceship, its section frames and modules
 */
public class HomeshipUI extends GuiComponent {
    public SectionComponentUI[] sections;
    public ModuleComponentUI[] modules;
    public GunSlotComponentUI[] gunSlots;
    public Scrollbar scrollbar;
    private Spaceship homeship;
    private final Point scroll;
    private final Dimension area; //Full pixel screen-size of the homeship.
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

        scroll = new Point(0,0);
        area = new Dimension();

        this.scrollbar = new Scrollbar(0, height - 100, width, 40, scroll, area);
        this.scrollbar.addScrollListener(() -> {
            updateScroll();
        });
        getComponents().add(this.scrollbar);

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
            sections[s] = new SectionComponentUI(homeship.getModuleLoc(s, -1));

            for (int m = 0; m < sectionNumModules; m++) {
                int mm = s*sectionNumModules + m;
                modules[mm] = new ModuleComponentUI(homeship.getModuleLoc(s, m));
            }
            for (int g = 0; g < SectionNumGunSlots; g++) {
                int gg = s*sectionNumModules + g;
                gunSlots[gg] = new GunSlotComponentUI(homeship.getModuleLoc(s, -1), g);
            }
        }
        Collections.addAll(getComponents(), sections);
        Collections.addAll(getComponents(), modules);
        Collections.addAll(getComponents(), gunSlots);
    }

    private void updateScroll() {
        for(SectionComponentUI s : sections) {s.setScroll(scroll);}
        for(ModuleComponentUI  s : modules)  {s.setScroll(scroll);}
        for(GunSlotComponentUI s : gunSlots) {s.setScroll(scroll);}
    }

    @Override
    public void prepare() {
        super.prepare();
        updateLayout();
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
            s.localPos.x = newX;
            s.localPos.y = newY;
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
            m.localPos.x = newX;
            m.localPos.y = newY;
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
            g.localPos.x = newX;
            g.localPos.y = newY;
        }
        updateScroll();
    }
}
abstract class HomeshipUIComponent extends GuiComponent {
    protected Spaceship.ModuleLoc loc;
    protected final Point localPos;
    protected HomeshipUIComponent(int width, int height, Spaceship.ModuleLoc loc) {
        super(0,0, width, height);
        this.loc = loc;
        this.localPos = new Point();
    }
    public int getIntX() {
        return (int)getX();
    }
    public int getIntY() {
        return (int)getY();
    }

    /**
     * Sets the GUI component X and Y variables based on the local position and the scroll offset.
     */
    public void setScroll(Point scroll) {
        this.setLocation(localPos.x - scroll.x, localPos.y - scroll.y);
    }
}

class SectionComponentUI extends HomeshipUIComponent{
    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected SectionComponentUI(Spaceship.ModuleLoc loc) {

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

    protected Spaceship.ModuleLoc loc;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected ModuleComponentUI(Spaceship.ModuleLoc loc) {
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
    protected GunSlotComponentUI(Spaceship.ModuleLoc loc, int gunSlot) {
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
interface ScrollbarListener{void onScrollUpdate();}
class Scrollbar extends GuiComponent {
    //Area the scrollbar is scrolling for
    private final Dimension area;
    //The offset the scrollbar manipulates
    private final Point scroll;
    private ArrayList<ScrollbarListener> listeners;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height
     */
    public Scrollbar(double x, double y, double width, double height, Point scroll, Dimension area) {
        super(x, y, width, height);
        this.area = area;
        this.scroll = scroll;
        this.setForwardMouseEvents(false);
        listeners = new ArrayList<>();
    }
    @Override
    public void mouseDragged(final MouseEvent e) {

        //if(isSuspended() || !this.getBoundingBox().contains(e.getX(), e.getY())) {
        //    super.mouseDragged(e);
        //    return;
        //}
        int x = e.getX();
        scroll.x = (int)(scrollable() * (x / (float)area.width));
        callScrollUpdate();
    }

    private int scrollable() {
        return area.width - (int)getWidth();
    }

    public void callScrollUpdate() {
        for (ScrollbarListener evt : listeners) {
            evt.onScrollUpdate();
        }
    }
    public void addScrollListener(ScrollbarListener listener) {
        listeners.add(listener);
    }
    public boolean removeScrollListener(ScrollbarListener listener) {
        return listeners.remove(listener);
    }
    public boolean tooSmall(){
        return scrollable() <= 0;
    }


    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if(tooSmall())
            return;

        double scrollX = scroll.x * scrollable() / getWidth();
        RoundRectangle2D bar = new RoundRectangle2D.Double(getX(),getY(), getWidth(), getHeight(), 10, 10);
        Rectangle.Double knob = new Rectangle.Double(scrollX, getY(), getHeight(), getHeight());

        g.setColor(Color.darkGray);
        g.fill(bar);
        g.setColor(Color.LIGHT_GRAY);
        g.fill(knob);

        //ShapeRenderer.render(g, bar);
        //ShapeRenderer.render(g, knob);
    }

}