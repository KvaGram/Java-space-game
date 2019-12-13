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
class NullModule extends AbstractShipModule {

    public NullModule(Spaceship.ShipLoc loc) {
        super(loc);
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    @Override
    public String GetName() {
        return "Empty space for a module";
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }
}
abstract class AbstractShipSection extends AbstractShipModule{

    public abstract AbstractShipModule[] GetModuleTypes();
    public AbstractShipSection(Spaceship.ShipLoc loc) {
        super(ShipPartType.Section, loc);
    }
    public abstract int getNumModules();

    public abstract boolean canBuildModule(ModuleType typeToBuild, StringBuffer message);
}
class NullSection extends AbstractShipSection {

    public NullSection(Spaceship.ShipLoc loc) {
        super(loc);
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    @Override
    public AbstractShipModule[] GetModuleTypes() {
        return new AbstractShipModule[0];
    }

    @Override
    public int getNumModules() {
        return 0;
    }

    @Override
    public boolean canBuildModule(ModuleType typeToBuild, StringBuffer message) {
        message.append("This section is stripped");
        return false;
    }

    @Override
    public String GetName() {
        return "Stripped Section";
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }
}

abstract class abstractShipComponent extends AbstractShipPart {

    protected abstractShipComponent(Spaceship.ShipLoc loc) {
        super(ShipPartType.Component, loc);
    }
    protected abstractShipComponent(ShipPartType partType, Spaceship.ShipLoc loc) {
        super(partType, loc);
    }
}


enum ShipPartType {Section, Module, Component, Weapon}

/**
 * Master class of all parts of the ship.
 * From the simplest components to the large sectionFrames holding it all together.
 *
 */
abstract class AbstractShipPart {
    private final ShipPartType partType;
    protected final Spaceship.ShipLoc loc;

    protected AbstractShipPart(ShipPartType partType, Spaceship.ShipLoc loc) {

        this.partType = partType;
        this.loc = loc;
    }

    public ShipPartType getPartType() {
        return partType;
    }

    public Spaceship.ShipLoc getLoc() {
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
    //TODO: change CargoPlaceholder[] to CargoContainer
    //Returns a container of storable parts, material and cargo retrieved/reclaimed
    // from removing/dismantling this ship-part.
    public abstract CargoPlaceholder[] getCargoOnDestruction();
}
