package unicus.spacegame.spaceship;

import org.apache.commons.lang3.ArrayUtils;
import unicus.spacegame.CargoCollection;

import java.io.PrintStream;
import java.util.Collection;

/**
 * Master class of all parts of the ship.
 * From the simplest components to the large sectionFrames holding it all together.
 *
 */
public abstract class AbstractShipPart {
    private final ShipPartType partType;
    protected final ShipLoc loc;

    protected AbstractShipPart(ShipPartType partType, ShipLoc loc) {

        this.partType = partType;
        this.loc = loc;
    }

    public ShipPartType getPartType() {
        return partType;
    }

    public ShipLoc getLoc() {
        return loc;
    }

    //Validates the part-type for illegal states.
    //Tests may use this in assertments
    public boolean validate(PrintStream out) {
        if(partType == ShipPartType.Module) {
            if(!loc.isValidModule()){
                out.println("ShipPart is a module, but location is not valid for a module.");
                return false;
            }
            if(loc.getModule() != this) {
                out.println("ShipPart is a module, but location does not point to this ShipPart");
                return false;
            }
            return true;
        }
        else if (partType == ShipPartType.Section) {
            if(!loc.isValidSection()){
                out.println("ShipPart is a section, but location is not valid for a section.");
                return false;
            }
            if(loc.getSection() != this) {
                out.println("ShipPart is a section, but location does not point to this ShipPart");
                return false;
            }
            return true;
        }
        else if (partType == ShipPartType.Component || partType == ShipPartType.Weapon) {
            if(loc.isValidModule()) {
                if(!ArrayUtils.contains(loc.getModule().getComponents(), this)){
                    out.println("ShipPart is a component or weapon, it has a valid location, but are not installed there. ");
                    return false;
                }
            }
            //This component is detached (presumably in storage).
            return true;
        }
        out.println("Unknown ship part type.");
        return false;
    }
    public boolean validate() {
        return validate(System.out);

    }

    //Descriptive name of the ship-part.
    public abstract String GetName();

    //Returns a list of storable parts, material and cargo retrieved/reclaimed
    // from removing/dismantling this ship-part.
    public abstract Collection<CargoCollection> getCargoOnDestruction();

    public void getInfo(StringBuffer b) {b.append("\n " + GetName());}

    /**
     * This function runs when the ship-part is removed or dismantled from the ship.
     * This function only deals with this object itself, any ship-part depended on this,
     * will be taken care of from HomeShip. .
     */
    public abstract void onDestroy();
}
