package unicus.spacegame.spaceship;

//TODO: rename to CargoModule or CargoBay once the cargo code has been merged with this code.
// - also, this class should implement CargoContainer.

import unicus.spacegame.CargoCollection;
import unicus.spacegame.CargoContainer;

import java.util.ArrayList;

public class CargoModule extends AbstractShipModule implements CargoContainer {
    static int cargoCapacity;
    int numCargo;
    ArrayList<CargoCollection> myStuff;

    public CargoModule(HomeShip.ShipLoc loc) {
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

    /**
     * This function runs when the ship-part is removed or dismantled from the ship.
     * This function only deals with this object itself, any ship-part depended on this,
     * will be taken care of from HomeShip. .
     */
    @Override
    public void onDestroy() {

    }

    //Cargo container implementation:

    @Override
    public ArrayList<CargoCollection> getCollection() {
        return myStuff;
    }

    @Override
    public int getCapacity() {
        return cargoCapacity;
    }

    @Override
    public boolean canAdd(CargoCollection newCargo) {
        return (newCargo.getCargoUnits() + numCargo <= cargoCapacity);
    }

    @Override
    public boolean doAdd(CargoCollection newCargo) {
        assert (newCargo.getCargoUnits() + numCargo <= cargoCapacity);
        myStuff.add(newCargo);
        numCargo += newCargo.getCargoUnits();
        return false;
    }
}
