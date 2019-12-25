package unicus.spacegame.spaceship;

public abstract class AbstractShipSection extends AbstractShipModule{

    public abstract AbstractShipModule[] GetModuleTypes();
    public AbstractShipSection(HomeShip.ShipLoc loc) {
        super(ShipPartType.Section, loc);
    }
    public abstract int getNumModules();

    public abstract boolean canBuildModule(ModuleType typeToBuild, StringBuffer message);
}
