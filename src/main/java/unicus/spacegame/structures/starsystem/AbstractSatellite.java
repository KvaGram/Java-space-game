package unicus.spacegame.structures.starsystem;

import java.awt.*;

public abstract class AbstractSatellite extends AbstractSpaceObject implements Orbiter {
    protected int orbitDistance;
    protected float orbitRotation;
    protected AbstractParentObject parent;
    protected int size;

    public AbstractSatellite(AbstractParentObject parent, int size, int orbitDistance, float orbitRotation) {
        this.orbitDistance = orbitDistance;
        this.orbitRotation = orbitRotation;
        this.parent = parent;
        this.size = size;
    }

    @Override
    public Point getLocation() {
        Point p = parent.getLocation();
        if (orbitDistance == 0)
            return p;
        p.x += (int) Math.round( Math.cos(orbitRotation) * orbitDistance );
        p.y += (int) Math.round( Math.sin(orbitRotation) * orbitDistance );

        return p;
    }

    @Override
    public AbstractParentObject getParent() {
        return this.parent;
    }

    @Override
    public int getOrbitD() {
        return orbitDistance;
    }

    @Override
    public float getOrbitR() {
        return orbitRotation;
    }
    @Override
    public int getSize() {
        return size;
    }
}
