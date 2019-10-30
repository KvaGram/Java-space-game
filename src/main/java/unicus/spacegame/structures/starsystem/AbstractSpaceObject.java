package unicus.spacegame.structures.starsystem;

import java.awt.*;

/**
 * The space object is any kind of object in a star system that
 * can visible and be interacted with.
 *
 * todo: figure out what information this need to send to the view component.
 */
public abstract class AbstractSpaceObject {
    public abstract int getSize();
    public abstract Point getLocation();
    public abstract String getTooltip();
}
