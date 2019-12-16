package unicus.spacegame.spaceship;

public class NullSection extends AbstractShipSection {

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
