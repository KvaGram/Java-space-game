package unicus.spacegame.structures.starsystem;

public class DebrisField extends AbstractSatellite {
    public DebrisField (AbstractParentObject parent, int size, int orbitDistance, float orbitRotation) {
        super(parent, size, orbitDistance, orbitRotation);
    }

    @Override
    public String getTooltip() {
        return null;
    }
}