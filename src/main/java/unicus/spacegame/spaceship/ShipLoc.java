package unicus.spacegame.spaceship;

/**
 * Ship location, formally inner class of {@link HomeShip}.
 * Properties of HomeShip used in ShipLoc now uses a static reference {@link #HS}.
 * Used to store a location of a section or module
 *
 * A section-value of 0 points to the head end of the ship.
 * A section-value of {@link this.length} points to the tail end of the ship.
 * A section-value not inside the above two is invalid, and points to nothing.
 * A module located at the head or tail are to be considered immutable once added.
 *
 * A module-value of 0 refer to the section itself.
 * A module-value above number of modules in the section or -1 and below are invalid, and does not point to any module or component.
 *
 * Regarding 'missing' modules in sections (see {@link HomeShip#HomeShip(int)}):
 *    ShipLocations for the head end of the ship is padded with references to the bridge, before adding other modules.
 *    ShipLocations for the tail end of the ship is padded with references to engineering, before adding other modules.
 *    Other ShipLocations are padded with instances of {@link NullModule} when added.
 * NOTE: For any valid location, there exists an object to reference.
 *
 *
 */
public class ShipLoc {
    private static HomeShip HS;

    //section, module.
    int s, m;

    public ShipLoc(int s, int m){
        this.s = s; this.m = m;
        if(HS == null)
            HS = HomeShip.getInstance();
    }

    public boolean isValidSection() {return s >= 0 && s <= HS.tailLocation;}
    public boolean isValidModule() {
        if (!isValidSection() || m < 0)
            return false;
        return HS.modules.containsKey(this);
    }

    /**
     * @return If this is the head section.
     */
    public boolean isHead(){return s == HS.headLocation;} //Note: headLocation is always 0

    /**
     * @return If this is the tail section.
     */
    public boolean isTail(){return s == HS.tailLocation;}


    public AbstractShipModule getModule() {
        if(isValidModule())
            return HS.modules.get(this);
        return null;
    }
    public AbstractShipSection getSection() {
        if (isValidSection()) {
            return (AbstractShipSection) new ShipLoc(s, 0).getModule();
        }
        return null;
    }
    public ShipLoc[] getModuleLocList() {
        if(!isValidSection())
            return null;

        ShipLoc[] ret = new ShipLoc[HomeShip.MODULES_PER_SECTION];
        for (int i = 0; i < HomeShip.MODULES_PER_SECTION; i++) {
            ret[i] = new ShipLoc(s, i + 1); //offset by 1, because the SectionObject occupies index 0.
        }
        return ret;
    }

    /**
     * One Ship location equals another
     * when they have the same section and module value.
     * @param obj object to compare.
     * @return Whatever the two locations are the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof ShipLoc))
            return false;
        ShipLoc other = (ShipLoc)obj;

        return other.s == s && other.m == m;
    }

    /**
     * The hash value for a ship location, is the section number, multiplied by the maximum number of
     * module objects (this includes the section object), plus the module index.
     * @return a unique indexed value of the ship location (given valid values for section and module).
     */
    @Override
    public int hashCode() {
        int h = (HomeShip.MAX_MODULE_OBJECTS * s) + m;
        return h;
    }

    public int getM() {
        return m;
    }
    public int getS() {
        return s;
    }

    @Override
    public String toString() {
        return "(Section " + s + ", Module " + m + ")";
    }

    /**
     * Rolls the module-index.
     * Should not be used for locations meant to represent a section index.
     * @param num number of modules to roll.
     * @return A new ShipLoc linking to the new module index.
     */
    public ShipLoc rollModule(int num) {
        int newM = m + num; //add the roll amount.
        /*
        The following rolls newW to be within the [1, MAX_MODULE_OBJECTS] range
         note: MODULES_PER_SECTION is expected to be 6,
              and MAX_MODULE_OBJECTS is expected to be 7 ( MODULES_PER_SECTION + 1).
        */
        while (newM >= HomeShip.MAX_MODULE_OBJECTS)
            newM -= HomeShip.MODULES_PER_SECTION;
        while (newM < 1) //Note: index 0 is reserved for the section-object.
            newM += HomeShip.MODULES_PER_SECTION;

        return new ShipLoc(s, newM);
    }
    public ShipLoc nextModule(){return rollModule(1);}
    public ShipLoc prevModule(){return rollModule(-1);}

    /**
     * Moves the section-index.
     * @param num number of sections to move.
     * @return A new ShipLoc linking to the new section index.
     */
    public ShipLoc moveSection(int num) {
        // Clamp newS to [headLocation, tailLocation]
        // Where headLocation is always 0, and tailLocation is the last section index.
        int newS = Math.max(HS.headLocation, Math.min(HS.tailLocation, s + num));
        return new ShipLoc(newS, m);
    }
    public ShipLoc nextSection() {return moveSection(1);}
    public ShipLoc prevSection() {return moveSection(-1);}



}
