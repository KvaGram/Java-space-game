package unicus.spacegame.ui.Homeship;

import de.gurkenlabs.litiengine.gui.GuiComponent;
import unicus.spacegame.spaceship.SectionType;
import unicus.spacegame.spaceship.Spaceship;

import java.awt.*;
import java.util.Collections;

/**
 * The Homeship UI renders a 2D representation of the spaceship, its section frames and modules
 */
public class HomeshipUI extends GuiComponent {
    public SectionComponentUI[] sections;
    public ModuleComponentUI[] modules;
    public GunSlotComponentUI[] gunSlots;
    private Spaceship homeship;

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

    @Override
    public void prepare() {
        super.prepare();
        updateLayout();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }

    public static int START_X = 400;
    public static int START_Y = 100;
    public static int SECTION_WIDTH = 300;
    public static int SECTION_HEIGHT = 600;
    public static int MODULE_WIDTH = 200;
    public static int MODULE_HEIGHT = 100;
    public static int WEAPON_WIDTH = 50;
    public static int WEAPON_HEIGHT = 50;

    void updateLayout() {

        double newX;
        double newY;
        for(SectionComponentUI s : sections) {
            if(!s.loc.isValidSection()) {
                s.suspend();
                continue;
            }
            else if(s.isSuspended())
                s.prepare();
            newX = START_X + SECTION_WIDTH * s.loc.getS();
            newY = START_Y;
            s.setLocation(newX, newY);
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
            newX = START_X + SECTION_WIDTH * m.loc.getS() + SECTION_WIDTH - MODULE_WIDTH;
            newY = START_Y + MODULE_HEIGHT * m.loc.getM() + spread * m.loc.getM() + spread/2.0;
            m.setLocation(newX, newY);
        }
        for(GunSlotComponentUI g : gunSlots) {


            //todo: check for number of weapons in section.
            // - For now 6 gun slots, unless empty section, then 0.
            if (!g.loc.isValidSection() || g.loc.getSection() == SectionType.None || g.gunSlot < 0 || g.gunSlot >= 6) {
                g.suspend();
                continue;
            }
            else if(g.isSuspended())
                g.prepare();

            int numGuns = 6;
            int spread = (SECTION_HEIGHT - numGuns * WEAPON_HEIGHT) / numGuns;
            newX = START_X + SECTION_WIDTH * g.loc.getS();
            newY = START_Y + WEAPON_HEIGHT * g.gunSlot + spread * g.gunSlot + spread/2.0;
            g.setLocation(newX, newY);
        }

    }
}
class SectionComponentUI extends  GuiComponent{

    protected Spaceship.ModuleLoc loc;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected SectionComponentUI(Spaceship.ModuleLoc loc) {

        super(0, 0, HomeshipUI.SECTION_WIDTH, HomeshipUI.SECTION_HEIGHT);
        this.loc = loc;
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(186, 190, 180));
        g.fillRoundRect((int)getX() + 5, (int)getY() + 5, (int)getWidth()-5, (int)getHeight()-5, 10, 10);

        g.setColor(new Color(61, 58, 50));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect((int)getX() + 5, (int)getY() + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}
class ModuleComponentUI extends  GuiComponent{

    protected Spaceship.ModuleLoc loc;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected ModuleComponentUI(Spaceship.ModuleLoc loc) {
        super(0, 0, HomeshipUI.MODULE_WIDTH, HomeshipUI.MODULE_HEIGHT);
        this.loc = loc;
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(110,40,0));
        g.fillRoundRect((int)getX() + 5, (int)getY() + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);

        g.setColor(new Color(70, 46, 16));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect((int)getX() + 5, (int)getY() + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}
class GunSlotComponentUI extends GuiComponent{

    //NOTE: maybe put gunslot in moduleloc. with that rename moduleloc to something more fitting
    protected Spaceship.ModuleLoc loc;
    protected int gunSlot;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     */
    protected GunSlotComponentUI(Spaceship.ModuleLoc loc, int gunSlot) {
        super(0, 0, HomeshipUI.WEAPON_WIDTH, HomeshipUI.WEAPON_HEIGHT);
        this.loc = loc;
        this.gunSlot = gunSlot;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(255,0, 0));
        g.fillRoundRect((int)getX() + 5, (int)getY() + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);

        g.setColor(new Color(70, 0,0));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect((int)getX() + 5, (int)getY() + 5, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}