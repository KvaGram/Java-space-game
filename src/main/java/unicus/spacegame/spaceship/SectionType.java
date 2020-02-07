package unicus.spacegame.spaceship;

import unicus.spacegame.CargoCollection;
import unicus.spacegame.CargoContainer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A list of section-types
 */
public enum SectionType {
    None, Normal , Wheel , GravityPlated;
    public static SectionType[] getBuildable(){
        return new SectionType[]{Normal, Wheel, GravityPlated};
    }
    public Collection<CargoCollection> getBuildCost(){
        return CargoContainer.Null.getCollection();
    }
};
