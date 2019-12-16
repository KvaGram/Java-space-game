package unicus.spacegame.spaceship;

public class HabitatModule extends AbstractShipModule {
    public HabitatModule(Spaceship.ShipLoc loc) {
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
     * For modules:
     * Whatever this module requires gravity to be constructed.
     *
     * @return true
     */
    @Override
    public boolean useGravity() {
        return true;
    }

    @Override
    public String GetName() {
        return "Habitat module at " + loc.toString();
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }
}
