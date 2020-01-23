package unicus.spacegame.ui.homeship;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.ICamera;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.RenderEngine;
import de.gurkenlabs.litiengine.graphics.RenderType;
import unicus.spacegame.spaceship.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
    private static int HEAD_HEIGHT = 304;
    private static int HEAD_WIDTH = 208;
    private static int TAIL_HEIGHT = 304;
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
    enum HomeShipDrawMode{closed, cutout, extruded}
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
        double size = HomeShip.getInstance().getMiddleLength() * SECTION_WIDTH;
        size += HEAD_WIDTH;
        size += TAIL_WIDTH;
        return size;
    }

    private double getSectionDrawX(int section) {
        double x = getStartX();
        if(section == HomeShip.getInstance().getHeadLocation())
            return x;
        x += HEAD_WIDTH;
        x += (section-1) * SECTION_WIDTH;
        return x;
    }


    public Point.Double getSectionFocusPoint(int section) {
        double y = Game.world().environment().getCenter().getY();
        double x = getSectionDrawX(section);
        if(section == HomeShip.getInstance().getHeadLocation())
            x+= HEAD_WIDTH/2.0;
        else if (section == HomeShip.getInstance().getTailLocation())
            x+= TAIL_WIDTH/2.0;
        else
            x+= SECTION_WIDTH/2.0;
        return new Point2D.Double(x, y);
    }
    private double getSectionDrawY(int section){
        double y = Game.world().environment().getCenter().getY();

        if(section == HomeShip.getInstance().getHeadLocation())
            y -= HEAD_HEIGHT / 2.0;
        else if(section == HomeShip.getInstance().getTailLocation())
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
            y -= SPINE_HEIGHT / 2.0;
            y -= (spineDist+1) * MODULE_HEIGHT;
        }
        else {
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

        int middle = (int)Game.world().environment().getCenter().getY();

        ////test-render a small white box
        //g.setColor(new Color(255, 255, 255));
        //g.fillRect(0, 0, 50, 50);

        HomeShip homeShip = HomeShip.getInstance();
        //AbstractShipModule[][] modules = homeShip.getModules();

        HomeShip.ShipLoc loc;
        for(int s = 0; s < homeShip.getFullLength(); s++) {
            loc = homeShip.getShipLoc(s, 0);

            Graphics2D partG = (Graphics2D) g.create();
            renderSection(partG, loc);
            partG.dispose();
            /*
            Module-drawing.
            Do not draw modules if:
            Draw-mode is set to closed.
            current section is the head (first) or tail (last).
            Module-graphics for tail and head (will be) built into the section graphics.
            */
            if (drawMode != HomeShipDrawMode.closed && (s != homeShip.getHeadLocation() && s != homeShip.getTailLocation())) {
                //If cutout mode, draw one module above and below. If extruded, draw 3 above and below.
                int numToDraw = drawMode == HomeShipDrawMode.extruded ? 3 : 1;
                for(int i = 0; i < numToDraw; i++) {
                    //Render module above spine
                    int m = rollClamp(i + rotation, 6) + 1;
                    loc = homeShip.getShipLoc(s, m);

                    partG = (Graphics2D) g.create();

                    renderModule(partG, loc, i, true);
                    partG.dispose();

                    //Render module below spine
                    m = rollClamp(i + 6 + rotation, 6)+1;
                    loc = homeShip.getShipLoc(s, m);

                    partG = (Graphics2D) g.create();
                    renderModule(partG, loc, i, false);
                    partG.dispose();
                }
            }
        }
        g.dispose();
    }

    private void renderSection(Graphics2D g, HomeShip.ShipLoc loc) {
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
    private void renderModule(Graphics2D g, HomeShip.ShipLoc loc, int spineDist, boolean above) {
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
    }


    public void refresh() {


    }
}
