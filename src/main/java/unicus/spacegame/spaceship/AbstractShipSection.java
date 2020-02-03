package unicus.spacegame.spaceship;

public abstract class AbstractShipSection extends AbstractShipModule{

    public abstract AbstractShipModule[] GetModuleTypes();
    public AbstractShipSection(ShipLoc loc) {
        super(ShipPartType.Section, loc);
    }
    @Deprecated
    public final int getNumModules(){return 6;};

    public abstract boolean canBuildModule(ModuleType typeToBuild, StringBuffer message);
}
