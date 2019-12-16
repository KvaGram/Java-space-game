package unicus.spacegame.spaceship;

public class BasicFrame extends AbstractShipSection {
    @Override
    public AbstractShipModule[] GetModuleTypes() {
        return new AbstractShipModule[0];
    }

    public BasicFrame(Spaceship.ShipLoc loc) {
        super(loc);
    }

    @Override
    public int getNumComponents() {
        return 6;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    /**
     * Whatever this section-frame provides gravity.
     *
     * @return false
     */
    @Override
    public boolean useGravity() {
        return false;
    }

    @Override
    public int getNumModules() {
        return 6;
    }

    @Override
    public boolean canBuildModule(ModuleType typeToBuild, StringBuffer message) {
        return false;
    }

    @Override
    public String GetName() {
        return null;
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }
}
