package unicus.spacegame.structures.starsystem;

import java.awt.*;
import java.util.ArrayList;

/**
 * Special empty center of a star system, used for co-orbeting multi-star systems
 * where nothing is in the center.
 */
public class SystemCenter extends  AbstractParentObject {
    private ArrayList<Orbiter> children;
    private Point centerLocation;
    public  SystemCenter() {
        this(new Point());
    }
    public SystemCenter(Point centerLocation) {
        this.centerLocation = centerLocation;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Point getLocation() {
        return (Point) centerLocation.clone();
    }

    @Override
    public String getTooltip() {
        return "";
    }


}
