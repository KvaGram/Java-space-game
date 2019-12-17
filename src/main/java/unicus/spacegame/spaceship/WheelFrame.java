package unicus.spacegame.spaceship;

public class WheelFrame extends AbstractShipSection {
    @Override
    public AbstractShipModule[] GetModuleTypes() {
        return new AbstractShipModule[0];
    }

    public WheelFrame(Spaceship.ShipLoc sectionLoc) {
        super(sectionLoc);
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    /**
     * Whatever this section-frame provides gravity.
     *
     * @return true
     */
    @Override
    public boolean useGravity() {
        return true;
    }

    @Override
    public int getNumModules() {
        return 3;
    }

    @Override
    public boolean canBuildModule(ModuleType typeToBuild, StringBuffer message) {
        return false;
    }

    @Override
    public String GetName() {
        return "WheelFrame at " + loc.toString();
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }
}
