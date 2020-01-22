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



    public Point.Double getCenterLocationOfSection(int section) {
        Point.Double ret = getTopLeftLocationOfSection(section);
        ret.y = Game.world().environment().getCenter().getY();

        if(section == HomeShip.getInstance().getHeadLocation())
            ret.x +=  HEAD_WIDTH/2.0;
        else if(section == HomeShip.getInstance().getTailLocation())
            ret.x += TAIL_WIDTH/2.0;
        else
            ret.x += SECTION_WIDTH/2.0;
        return ret;

    }
    public Point.Double getTopLeftLocationOfSection(int section){
        Point.Double ret = new Point.Double();
        ret.y = Game.world().environment().getCenter().getY();

        if(section == HomeShip.getInstance().getHeadLocation())
            ret.y -= HEAD_HEIGHT / 2.0;
        else if(section == HomeShip.getInstance().getTailLocation())
            ret.y -= TAIL_HEIGHT / 2.0;
        else
            ret.y -= SECTION_HEIGHT / 2.0;

        ret.x = START_X;
        for (int i = 0; i < section-1; i++) {
            if(i == HomeShip.getInstance().getHeadLocation())
                ret.x +=  HEAD_WIDTH;
            else if(i == HomeShip.getInstance().getTailLocation())
                ret.x += TAIL_WIDTH;
            else
                ret.x += SECTION_WIDTH;
        }
        return ret;
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

        int x = START_X;
        HomeShip.ShipLoc loc;
        for(int s = 0; s < homeShip.getFullLength(); s++) {
            loc = homeShip.getShipLoc(s, 0);
            int y = middle - SECTION_HEIGHT/2;

            Graphics2D partG = (Graphics2D) g.create();
            renderSection(partG, loc, x, y);
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
                //draw above spine
                int i;
                for(i = 0; i < numToDraw; i++) {
                    y = middle - SPINE_HEIGHT/2 - MODULE_HEIGHT*(i+1); //top edge of module draw area
                    int m = rollClamp(i + rotation, 6) + 1;
                    loc = homeShip.getShipLoc(s, m);

                    partG = (Graphics2D) g.create();
                    renderModule(partG, loc, x, y);
                    partG.dispose();
                }
                //draw below
                for(i = 0; i > -numToDraw; i--) {
                    y = middle + SPINE_HEIGHT/2 - MODULE_HEIGHT*(i); //top edge of module draw area
                    int m = rollClamp(i + 6 + rotation, 6)+1;
                    loc = homeShip.getShipLoc(s, m);

                    partG = (Graphics2D) g.create();
                    renderModule(partG, loc, x, y);
                    partG.dispose();
                }
            }
            if(s == homeShip.getHeadLocation()){
                x += HEAD_WIDTH;
            }
            else if(s== homeShip.getTailLocation()) {
                x += TAIL_WIDTH;
            }
            else {
                x += SECTION_WIDTH;
            }
        }
        g.dispose();
    }

    private void renderSection(Graphics2D g, HomeShip.ShipLoc loc, int x, int y) {
        SectionType sectionType;
        try {
            sectionType = loc.getSection().getSectionType();
        }
        catch (NullPointerException err) {
            //System.err.println(err);
            sectionType = SectionType.None;
        }
        x += Game.world().camera().getPixelOffsetX();
        y += Game.world().camera().getPixelOffsetY();

        g.setColor(sectionType.getColor());
        g.fillRect(x,y,SECTION_WIDTH, SECTION_HEIGHT);
    }
    private void renderModule(Graphics2D g, HomeShip.ShipLoc loc, int x, int y) {
        ModuleType moduleType;
        try {
            moduleType = loc.getModule().getModuleType();
        }
        catch (NullPointerException err) {
            //System.err.println(err);
            moduleType = ModuleType.Empty;
        }

        x += Game.world().camera().getPixelOffsetX();
        y += Game.world().camera().getPixelOffsetY();
        g.setColor(moduleType.getColor());
        g.fillRect(x+5, y+5, MODULE_WIDTH-10, MODULE_HEIGHT-10);
    }


    public void refresh() {


    }
}
