package unicus.spacegame.structures.starsystem;

/**
 * A planet capable of life.
 * Not necessarily human life, or even humanoid life, but life.
 */
public class LifePlanet extends AbstractPlanet {

    /**
     * This constructor creates a geocentric rouge planet.
     *
     */
    public LifePlanet(int size) {
        super(size);
    }

    public LifePlanet(AbstractParentObject parent, int size, int orbitDistance, float orbitRotation) {
        super(parent, size, orbitDistance, orbitRotation);
    }

    @Override
    public String getTooltip() {
        return null;
    }
}
