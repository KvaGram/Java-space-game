package unicus.spacegame.ui.Homeship;

import de.gurkenlabs.litiengine.gui.GuiComponent;
import unicus.spacegame.spaceship.Spaceship;

import java.awt.*;
import java.util.ArrayList;
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
            sections[s] = new SectionComponentUI(homeship.getModuleLoc(s, -1), 100, 100);

            for (int m = 0; m < sectionNumModules; m++) {
                int mm = s*sectionNumModules + m;
                modules[mm] = new ModuleComponentUI(homeship.getModuleLoc(s, m), 100, 100);
            }
            for (int g = 0; g < SectionNumGunSlots; g++) {
                int gg = s*sectionNumModules + g;
                gunSlots[gg] = new GunSlotComponentUI(homeship.getModuleLoc(s, -1), g, 100, 100);
            }
        }
        Collections.addAll(getComponents(), sections);
        Collections.addAll(getComponents(), modules);
        Collections.addAll(getComponents(), gunSlots);

    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }
}
class SectionComponentUI extends  GuiComponent{

    private Spaceship.ModuleLoc loc;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     * @param width  the width
     * @param height
     */
    protected SectionComponentUI(Spaceship.ModuleLoc loc, double width, double height) {

        super(0, 0, width, height);
        this.loc = loc;
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if (!loc.isValidSection())
            return;

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(186, 190, 180));
        g.fillRoundRect(5, 5, (int)getWidth()-5, (int)getHeight()-5, 10, 10);

        g.setColor(new Color(61, 58, 50));
        g.setStroke(new BasicStroke(4));
        g.fillRoundRect(4, 4, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}
class ModuleComponentUI extends  GuiComponent{

    private Spaceship.ModuleLoc loc;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     * @param width  the width
     * @param height
     */
    protected ModuleComponentUI(Spaceship.ModuleLoc loc, double width, double height) {
        super(0, 0, width, height);
        this.loc = loc;
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (!loc.isValidModule())
            return;

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(110,40,0));
        g.fillRoundRect(5, 5, (int)getWidth()-5, (int)getHeight()-5, 10, 10);

        g.setColor(new Color(70, 46, 16));
        g.setStroke(new BasicStroke(4));
        g.fillRoundRect(4, 4, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}
class GunSlotComponentUI extends GuiComponent{

    private Spaceship.ModuleLoc loc;
    private int gunSlot;

    /**
     * Instantiates a new gui component at the point (x,y) with the dimension (width,height).
     *
     * @param width  the width
     * @param height
     */
    protected GunSlotComponentUI(Spaceship.ModuleLoc loc, int gunSlot, double width, double height) {
        super(0, 0, width, height);
        this.loc = loc;
        this.gunSlot = gunSlot;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (!loc.isValidSection() || gunSlot < 0 || gunSlot > 6 )
            return;

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(255,0, 0));
        g.fillRoundRect(5, 5, (int)getWidth()-5, (int)getHeight()-5, 10, 10);

        g.setColor(new Color(70, 0,0));
        g.setStroke(new BasicStroke(4));
        g.fillRoundRect(4, 4, (int)getWidth()-4, (int)getHeight()-4, 10, 10);
    }
}