package unicus.spacegame.spaceship;

import unicus.spacegame.CargoCollection;
import unicus.spacegame.CargoContainer;

import java.util.ArrayList;

/**
 * A datastructure that a Spaceship is full of.
 * Needs to know what ModuleType it is, and what SectionType it is hosted in.
 */
public class ShipModule {
    public SectionType sectionType;
    public ModuleType moduleType;

    public ShipModule (SectionType sectionType, ModuleType moduleType) {
        super();
        this.sectionType = sectionType;
        this.moduleType = moduleType;
    }
    public ShipModule(SectionType sectionType) {
        super();
        this.sectionType = sectionType;
        this.moduleType = ModuleType.Empty;
    }

    public String GetName() {
        return sectionType.toString() + " " + moduleType.toString();
    }
}

class CargoModule extends ShipModule implements CargoContainer {
    static int cargoCapacity;
    int numCargo;
    ArrayList<CargoCollection> myStuff;

    public CargoModule(SectionType sectionType) {
        super(sectionType, ModuleType.Cargo);
        // cargoCapacity = 21 // TODO adjust this number later
    }

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