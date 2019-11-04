package unicus.spacegame.structures.starsystem;

import java.util.Random;

/**
 * Determines how large a space object is.
 * This is used with ObjectType to set how an object is rendered.
 * If none, the object is not rendered.
 *
 * Note: Size is relative to the type.
 * A small gas-planet is still much larger than a large regular planet.
 * A Giant planet may be larger than a tiny gas-planet though.
 */
public enum ObjectSize {
    NONE, TINY, SMALL, MODERATE, LARGE, XLARGE, GIANT;

//    public static ObjectSize Random(Random r) {
//        ObjectSize[] vals = ObjectSize.values();
//        return vals[r.nextInt(vals.length)];
//    }
    /**
     *
     * @return An objectsize larger than Tiny, and smaller than Giant
     */
    private static final ObjectSize[] random1 = {SMALL, MODERATE, LARGE, XLARGE};
    public static ObjectSize Random1(Random r) {
        return random1[r.nextInt(random1.length)];
    }

    /**
     * Get smaller value, greater than NONE
     * @return
     */
    public ObjectSize Smaller() {
        ObjectSize[] vals = values();
        return vals[Math.max(1, this.ordinal()-1)];
    }

    /**
     * Get larger value, up to GIANT
     * @return
     */
    public ObjectSize Larger() {
        ObjectSize[] vals = values();
        return vals[Math.min(vals.length-1, this.ordinal()-1)];
    }

    public boolean smallerThan(ObjectSize other) {
        return this.ordinal() < other.ordinal();
    }
    public boolean largerThen(ObjectSize other) {
        return this.ordinal() > other.ordinal();
    }
    //add more specific types of random
    //prevent none to be picked.
    //prevent none and tiny to be picked.
}
