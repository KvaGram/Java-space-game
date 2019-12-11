package unicus.spacegame;

public interface CargoCollection {
    int getCargoUnits();
    boolean canMerge(CargoCollection other);
    boolean doMerge(CargoCollection other);
}
