package unicus.spacegame.spaceship;

import org.apache.commons.lang3.ArrayUtils;

import java.io.PrintStream;

/**
 * A datastructure that a Spaceship is full of.
 * Needs to know what ModuleType it is, and what SectionType it is hosted in.
 */
public abstract class AbstractShipModule extends AbstractShipPart{

    public AbstractShipModule(Spaceship.ShipLoc loc) {
        super(ShipPartType.Module, loc);
    }
    protected AbstractShipModule(ShipPartType partType, Spaceship.ShipLoc loc) {
        super(partType, loc);
    }
    public abstract int getNumComponents();
    public abstract abstractShipComponent[] getComponents();
}


