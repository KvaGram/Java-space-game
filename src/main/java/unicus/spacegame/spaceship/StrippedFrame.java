package unicus.spacegame.spaceship;

public class StrippedFrame extends AbstractShipSection {

    public StrippedFrame(Spaceship.ShipLoc loc) {
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
        return "Stripped Section at " +  loc.toString();
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }
}
