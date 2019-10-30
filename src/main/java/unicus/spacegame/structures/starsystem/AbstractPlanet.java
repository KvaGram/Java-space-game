package unicus.spacegame.structures.starsystem;

import java.awt.*;

public abstract class AbstractPlanet extends AbstractParentObject implements Orbiter {
    protected int orbitDistance;
    protected float orbitRotation;
    protected AbstractParentObject parent;
    protected int size;

    /**
     * Make self-centered planet (star?)
     * @param size
     */
    public AbstractPlanet(int size){
        this.orbitDistance = 0;
        this.orbitRotation = 0;
        this.size = size;
        this.parent = this;
    }
    public AbstractPlanet(AbstractParentObject parent, int size, int orbitDistance, float orbitRotation) {
        this.orbitDistance = orbitDistance;
        this.orbitRotation = orbitRotation;
        this.parent = parent;
        this.size = size;
    }

    @Override
    public Point getLocation() {
        if (parent == this)
            return new Point(0, 0);
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
