package unicus.spacegame.ui.homeship;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.RenderEngine;
import de.gurkenlabs.litiengine.graphics.RenderType;
import unicus.spacegame.spaceship.*;

import java.awt.*;

import static unicus.spacegame.utilities.Math.rollClamp;

/*
The plan: try to recreate the code from the old Demo1 SpaceshipGUI.
Use a Target object with a Rectangle2D for each screenobject.
Show modules and weapons in seperate view-modes.
Idea: show closed version of the ship (only the section-frames)
 */
@EntityInfo(renderType = RenderType.GROUND )
public class HomeshipGUI extends Entity implements IRenderable {

    private static int START_X = 128;
    private static int HEAD_HEIGHT = 208;
    private static int HEAD_WIDTH = 208;
    private static int TAIL_HEIGHT = 208;
    private static int TAIL_WIDTH = 112;
    private static int SECTION_WIDTH = 112;
    private static int SECTION_HEIGHT = 208;
    private static int MODULE_WIDTH = 112;
    private static int MODULE_HEIGHT = 80;
    private static int WEAPON_WIDTH = 32;
    private static int WEAPON_HEIGHT = 32;
    private static int SPINE_HEIGHT = 48;

    public int getRotation() {
        return rotation;
    }


    public void setRotation(int rotation) {
        this.rotation = rollClamp(rotation, 6);
    }

    /**
     * The draw-mode for the HomeShip render.
     * Closed: shows only an exterior view of the ship.
     * cutout: shows a cutout interior of the ship, only the modules directly above and below the spine are visible.
     * extruded: shows the cutout view, but the normally hidden modules are visible in a fake view extruding above and below.
     */
    enum HomeShipDrawMode{closed, cutout, unwrapped}
    public HomeShipDrawMode drawMode;
    /**
     * Rotation of the ship, for displaying different modules.
     * clamped to [0,5]
     */
    private int rotation;
    public HomeshipGUI() {
        super();
    }

    /**
     * The x-coordinate on the map where drawing starts.
     * @return
     */
    public double getStartX() {
        double x = Game.world().environment().getCenter().getX();
        x -= getShipDrawWidth() / 2;
        return x - x % 16;
    }

    /**
     * returns the draw width of the ship on screen.
     * @return
     */
    private double getShipDrawWidth() {
        double size = HomeShip.getMiddleLength() * SECTION_WIDTH;
        size += HEAD_WIDTH;
        size += TAIL_WIDTH;
        return size;
    }

    private double getSectionDrawX(int section) {
        double x = getStartX();
        if(section == HomeShip.getHeadLocation())
            return x;
        x += HEAD_WIDTH;
        x += (section-1) * SECTION_WIDTH;
        return x;
    }

    /**
     * Moves the focus to the set section location.
     * If module set to something other than 0, sets the rotation so the module appears as first slot above the spine.
     * @param loc
     * @param menuMode
     */
    public void setSelection(ShipLoc loc, boolean menuMode) {
        setSelectionFocus(loc.getS(), 30, menuMode);
        //Set the closest rotation, based on current rotation and target module index.
        rotation = loc.getM();
    }
    public void setSelectionFocus(int section, int panFrames, boolean menuMode) {
        double y = Game.world().environment().getCenter().getY();
        //If in menu-mode, move the camera up a bit, so the ship appears below
        //the configuration menu.
        if (menuMode)
            y -= SECTION_WIDTH;
        double x = getSectionDrawX(section);
        if(section == HomeShip.getHeadLocation())
            x+= HEAD_WIDTH/2.0;
        else if (section == HomeShip.getTailLocation())
            x+= TAIL_WIDTH/2.0;
        else
            x+= SECTION_WIDTH/2.0;

        if(panFrames > 0)
            Game.world().camera().pan(x, y, panFrames);
        else
            Game.world().camera().setFocus(x, y);
    }
    private double getSectionDrawY(int section){
        double y = Game.world().environment().getCenter().getY();

        if(section == HomeShip.getHeadLocation())
            y -= HEAD_HEIGHT / 2.0;
        else if(section == HomeShip.getTailLocation())
            y -= TAIL_HEIGHT / 2.0;
        else
            y -= SECTION_HEIGHT / 2.0;
        return y;
    }

    /**
     * Gets the draw Y coordinate for a module.
     *
     * @param spineDist is the distance from the spine. a value of 0 means it is connected to the spine.
     * @param above If the module is above or below the spine.
     * @return pixel Y coordinate on map for drawing module.
     */
    private double getModuleDrawY(int spineDist, boolean above) {
        assert(0 <= spineDist && spineDist < 3);
        double y = Game.world().environment().getCenter().getY();
        if(above) {
            if(drawMode != HomeShipDrawMode.closed)
                y -= SPINE_HEIGHT / 2.0;
            y -= (spineDist+1) * MODULE_HEIGHT;
        }
        else {
            if(drawMode != HomeShipDrawMode.closed)
                y += SPINE_HEIGHT / 2.0;
            y += (spineDist) * MODULE_HEIGHT;
        }
        return y;
    }

    /**
     * Renders the visual contents of this instance onto the provided graphics context.
     *
     * <p>
     * If an <code>Entity</code> implements this interface, this method will be called right after the entity was rendered from the environment.
     * Allowing for a custom rendering mechanism.
     * </p>
     *
     * <p>
     * This interface can be implemented in general by anything that should be rendered to the game's screen.
     * </p>
     *
     * @param _g The current graphics object onto which this instance will render its visual contents.
     * @see RenderEngine#renderEntity(Graphics2D, IEntity)
     */
    @Override
    public void render(Graphics2D _g) {

        Graphics2D g = (Graphics2D) _g.create();
        g.setColor(Color.white);
        g.drawString("Rotation: " + rotation, 10, 20);

        int middle = (int)Game.world().environment().getCenter().getY();

        ////test-render a small white box
        //g.setColor(new Color(255, 255, 255));
        //g.fillRect(0, 0, 50, 50);

        HomeShip homeShip = HomeShip.HS();
        //AbstractShipModule[][] modules = homeShip.getModules();

        ShipLoc loc;
        for(int s = 0; s < HomeShip.getFullLength(); s++) {
            loc = homeShip.getShipLoc(s, 0);

            Graphics2D partG = (Graphics2D) g.create();
            renderSection(partG, loc);
            partG.dispose();

            if(s == HomeShip.getHeadLocation() || s == HomeShip.getTailLocation())
                continue; //Modules for head and tail are special, and are rendered with the section
            if(drawMode == HomeShipDrawMode.closed) {
                /*
                 in this mode, draw some surface effects, windows etc for the modules facing the player's view.
                 Draw these over the spine.
                 That would be rotation + 1 and rotation + 2
                 Add Section object offset, off course.

                */
                int m;
                //Draw above
                m = rollClamp(rotation + 1, 6) + 1;
                loc = homeShip.getShipLoc(s, m);
                partG = (Graphics2D) g.create();
                renderModule(partG, loc, 0, true);
                partG.dispose();
                //Draw below
                m = rollClamp(rotation + 2, 6) + 1;
                loc = homeShip.getShipLoc(s, m);
                partG = (Graphics2D) g.create();
                renderModule(partG, loc, 0, false);
                partG.dispose();
            }
            else if(drawMode == HomeShipDrawMode.cutout) {
                /*
                 draw cutout view of modules.
                 module to draw above spine is rotation + 0 and below is rotation + 3.
                 Add Section object offset, off course.
                */

                int m;
                //Draw above
                m = rollClamp(rotation + 0, 6) + 1;
                loc = homeShip.getShipLoc(s, m);
                partG = (Graphics2D) g.create();
                renderModule(partG, loc, 0, true);
                partG.dispose();
                //Draw below
                m = rollClamp(rotation + 3, 6) + 1;
                loc = homeShip.getShipLoc(s, m);
                partG = (Graphics2D) g.create();
                renderModule(partG, loc, 0, false);
                partG.dispose();
            }
            else if(drawMode == HomeShipDrawMode.unwrapped) {
                /*
                unwrap the ship by rendering the modules in this order, from above the spine.
                This un-wrapping fake perspective works by
                dragging the invisible modules from the front above the spine, and the those behind to below.
                2 + rotation
                1 + rotation
                0 + rotation
                (spine)
                3 + rotation
                4 + rotation
                5 + rotation
                */
                int i, j, m;
                for(i=0; i < 6; i++) {
                    j = i % 3;
                    m = rollClamp(rotation + i, 6) + 1;
                    loc = homeShip.getShipLoc(s, m);
                    partG = (Graphics2D) g.create();
                    renderModule(partG, loc, j, i<3);
                    partG.dispose();
                }
            }
        }
        g.dispose();
    }

    private void renderSection(Graphics2D g, ShipLoc loc) {
        SectionType sectionType;
        try {
            sectionType = loc.getSection().getSectionType();
        }
        catch (NullPointerException err) {
            //System.err.println(err);
            sectionType = SectionType.None;
        }
        int x = (int)getSectionDrawX(loc.getS());
        int y = (int)getSectionDrawY(loc.getS());
        x += Game.world().camera().getPixelOffsetX();
        y += Game.world().camera().getPixelOffsetY();

        g.setColor(sectionType.getColor());
        g.fillRect(x,y,SECTION_WIDTH, SECTION_HEIGHT);
    }
    private void renderModule(Graphics2D g, ShipLoc loc, int spineDist, boolean above) {
        ModuleType moduleType;
        try {
            moduleType = loc.getModule().getModuleType();
        }
        catch (NullPointerException err) {
            //System.err.println(err);
            moduleType = ModuleType.Empty;
        }
        int x = (int)getSectionDrawX(loc.getS());
        int y = (int)getModuleDrawY(spineDist, above);

        x += Game.world().camera().getPixelOffsetX();
        y += Game.world().camera().getPixelOffsetY();
        g.setColor(moduleType.getColor());
        g.fillRect(x+5, y+5, MODULE_WIDTH-10, MODULE_HEIGHT-10);
        g.setColor(Color.white);
        g.drawString("Section " + loc.getS(),x+10, y+20);
        g.drawString("Module " + loc.getM(),x+10, y+35);
    }
}
