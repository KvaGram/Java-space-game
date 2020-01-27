package unicus.spacegame;

import java.util.ArrayList;
import java.util.Collection;

public interface CargoContainer {
    Collection<CargoCollection> getCollection();
    int getCapacity();
    boolean canAdd(CargoCollection newCargo);
    boolean doAdd(CargoCollection newCargo);

    /**
     * Null is a dummy implementation of CargoContainer that do not and can not hold any cargo.
     */
    public static final CargoContainer Null = new CargoContainer() {
        @Override
        public ArrayList<CargoCollection> getCollection() {
            return new ArrayList<>();
        }
        @Override
        public int getCapacity() {
            return 0;
        }
        @Override
        public boolean canAdd(CargoCollection newCargo) {
            return false;
        }
        @Override
        public boolean doAdd(CargoCollection newCargo) {
            return false;
        }
    };
}
