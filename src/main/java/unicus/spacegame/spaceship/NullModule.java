package unicus.spacegame.spaceship;

public class NullModule extends AbstractShipModule {

    public NullModule(HomeShip.ShipLoc loc) {
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
     * @return false
     */
    @Override
    public boolean useGravity() {
        return false;
    }

    @Override
    public String GetName() {
        return "Empty space for a module "  +  loc.toString();
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }

    /**
     * This function runs when the ship-part is removed or dismantled from the ship.
     * This function only deals with this object itself, any ship-part depended on this,
     * will be taken care of from HomeShip. .
     */
    @Override
    public void onDestroy() {

    }
}
