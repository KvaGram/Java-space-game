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
    none, tiny, small, moderate, large, xlarge, giant;

    public static ObjectSize Random(Random r) {
        ObjectSize[] vals = ObjectSize.values();
        return vals[r.nextInt(vals.length)];
    }
}
