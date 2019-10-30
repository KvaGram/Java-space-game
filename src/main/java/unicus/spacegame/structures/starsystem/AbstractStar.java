package unicus.spacegame.structures.starsystem;

public abstract class AbstractStar extends AbstractPlanet {

    public AbstractStar(int size) {
        super(size);
    }

    public AbstractStar(AbstractParentObject parent, int size, int orbitDistance, float orbitRotation) {
        super(parent, size, orbitDistance, orbitRotation);
    }
}
