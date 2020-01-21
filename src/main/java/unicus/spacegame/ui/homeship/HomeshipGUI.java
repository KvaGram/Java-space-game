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

import static unicus.spacegame.utilities.Math.rollClamp;

/*
The plan: try to recreate the code from the old Demo1 SpaceshipGUI.
Use a Target object with a Rectangle2D for each screenobject.
Show modules and weapons in seperate view-modes.
Idea: show closed version of the ship (only the section-frames)
 */
@EntityInfo(renderType = RenderType.GROUND )
public class HomeshipGUI extends Entity implements IRenderable {

    private static int START_X = 50;
    private static int HEAD_HEIGHT = 500;
    private static int HEAD_WIDTH = 300;
    private static int TAIL_HEIGHT = 500;
    private static int TAIL_WIDTH = 300;
    private static int SECTION_WIDTH = 300;
    private static int SECTION_HEIGHT = 600;
    private static int MODULE_WIDTH = 200;
    private static int MODULE_HEIGHT = 100;
    private static int WEAPON_WIDTH = 50;
    private static int WEAPON_HEIGHT = 50;

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
        ICamera cam = Game.world().camera();
        int width = Game.window().getWidth();
        int height = Game.window().getHeight();
        Graphics2D g = (Graphics2D) _g.create((int)cam.getPixelOffsetX(), (int)cam.getPixelOffsetY(), width, height);

        int middle = height/2;

        //test-render a small white box
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 100, 50, 50);

        HomeShip homeShip = HomeShip.getInstance();
        //AbstractShipModule[][] modules = homeShip.getModules();

        int x = START_X;
        HomeShip.ShipLoc loc;
        for(int s = 0; s < homeShip.getFullLength(); s++) {
            loc = homeShip.getShipLoc(s, 0);
            int y = middle - SECTION_HEIGHT/2;

            Graphics2D partG = (Graphics2D) g.create(x, y, width, height);
            renderSection(partG, loc);
            partG.dispose();

            if(drawMode != HomeShipDrawMode.closed) {
                //If outline mode, draw one module above and below. If extruded, draw 3 above and below.
                int numToDraw = drawMode == HomeShipDrawMode.extruded ? 3 : 1;
                //draw above
                int i;
                /*
                        int i;
                        for(i = 0; i < numToDraw; i++) {
                            int m = rollClamp(i + rotation, 6)+1;
                            output[middle + i] = m;
                        }
                        for(i = -1; i >= -numToDraw; i--) {
                            int m = rollClamp(i + 6 + rotation, 6)+1;
                            output[middle + i] = m;
                        }
                 */

                for(i = 0; i < numToDraw; i++) {
                    y = middle - MODULE_HEIGHT*(i); //top edge of module draw area
                    int m = rollClamp(i + rotation, 6) + 1;
                    loc = homeShip.getShipLoc(s, m);

                    partG = (Graphics2D) g.create(x, y, width, height);
                    renderModule(partG, loc);
                    partG.dispose();
                }
                //draw below
                for(i = 0; i > -numToDraw; i--) {
                    y = middle - MODULE_HEIGHT*(i); //top edge of module draw area
                    int m = rollClamp(i + 6 + rotation, 6)+1;
                    loc = homeShip.getShipLoc(s, m);

                    partG = (Graphics2D) g.create(x, y, width, height);
                    renderModule(partG, loc);
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

    private void renderSection(Graphics2D g, HomeShip.ShipLoc loc) {
        SectionType sectionType;
        try {
            sectionType = loc.getSection().getSectionType();
        }
        catch (Error err) {
            System.err.println(err);
            sectionType = SectionType.None;
        }
        g.setColor(sectionType.getColor());
        g.fillRect(0,0,SECTION_WIDTH, SECTION_HEIGHT);
    }
    private void renderModule(Graphics2D g, HomeShip.ShipLoc loc) {
        ModuleType moduleType;
        try {
            moduleType = loc.getModule().getModuleType();
        }
        catch (Error err) {
            System.err.println(err);
            moduleType = ModuleType.Empty;
        }
        g.setColor(moduleType.getColor());
        g.fillRect(5, 5, MODULE_WIDTH, MODULE_HEIGHT);
    }



    public void refresh() {


    }
}
