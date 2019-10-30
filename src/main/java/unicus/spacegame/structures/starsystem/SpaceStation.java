package unicus.spacegame.structures.starsystem;

public class SpaceStation extends AbstractSatellite {
    public SpaceStation(AbstractParentObject parent, int size, int orbitDistance, float orbitRotation) {
        super(parent, size, orbitDistance, orbitRotation);
    }

    @Override
    public String getTooltip() {
        return null;
    }
}
