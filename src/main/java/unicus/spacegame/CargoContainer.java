package unicus.spacegame;

import java.util.ArrayList;

public interface CargoContainer {
    ArrayList<CargoCollection> getCollection();
    int getCapacity();
    boolean canAdd(CargoCollection newCargo);
    boolean doAdd(CargoCollection newCargo);
}
