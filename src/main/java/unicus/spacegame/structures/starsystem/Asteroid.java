package unicus.spacegame.structures.starsystem;

public class Asteroid extends AbstractSatellite {
    public Asteroid(AbstractParentObject parent, int size, int orbitDistance, float orbitRotation) {
        super(parent, size, orbitDistance, orbitRotation);
    }

    @Override
    public String getTooltip() {
        return null;
    }
}
