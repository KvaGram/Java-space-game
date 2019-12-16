package unicus.spacegame.spaceship;

//TODO: rename to CargoModule or CargoBay once the cargo code has been merged with this code.
// - also, this class should implement CargoContainer.

public class CargoShipModule extends AbstractShipModule {
    public CargoShipModule(Spaceship.ShipLoc loc) {
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
     * @return false
     */
    @Override
    public boolean useGravity() {
        return false;
    }

    @Override
    public String GetName() {
        return "Cargo bay at " + loc.toString();
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }
}
