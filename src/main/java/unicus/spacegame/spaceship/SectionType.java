package unicus.spacegame.spaceship;

/**
 * A list of section-types
 * For test, each type has a color
 */
public enum SectionType {
    None, Normal , Wheel , GravityPlated;
    public SectionType[] getBuildable(){
        return new SectionType[]{Normal, Wheel, GravityPlated};
    }
};
