package unicus.spacegame.spaceship;

/**
 * A datastructure that a Spaceship is full of.
 * Needs to know what ModuleType it is, and what SectionType it is hosted in.
 */
public class ShipModule {
    public SectionType sectionType;
    public ModuleType moduleType;

    public ShipModule (SectionType sectionType, ModuleType moduleType) {
        super();
        this.sectionType = sectionType;
        this.moduleType = moduleType;
    }
    public ShipModule(SectionType sectionType) {
        super();
        this.sectionType = sectionType;
        this.moduleType = ModuleType.Empty;
    }

    public String GetName() {
        return sectionType.toString() + " " + moduleType.toString();
    }

    //STUB. TODO: return value is a list of Cargo.
    //This includes stored cargo, components that will be dismantled
    // and the raw material of the module itself (minus a recycle-loss).
    //This function will be abstract, and implemented on each module sub-class.
    public Object[] getCargoOnDestruction() {
        return new Object[0];
    }
}