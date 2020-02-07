package unicus.spacegame.utilities;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The ObjectKey is a unique ID value for an object.
 * The key values must be unique per object type,
 * as they are used to locate objects in lists.
 */
public class ObjectKey {
    private int lastKey;
    private int[] reserved;

    public ObjectKey() {
        this(Integer.MIN_VALUE);
    }

    public ObjectKey(int lastKey){
        this.lastKey = lastKey;
        setReserved();
    }
    public int yieldKey() {
        do {
            lastKey++;
        }
        while(ArrayUtils.contains(reserved, lastKey));
        return lastKey;
    }

    /**
     * Sets the keys reserved for special use.
     * For example:
     *      A crewman spawned from an event
     *      A permanent job like Bridge shifts and Construction.
     * {@link #yieldKey()} will never yield a reserved value.
     * note: value 0 is always reserved.
     * @param reserved
     */
    public void setReserved(int... reserved) {
        this.reserved = ArrayUtils.add(reserved, 0);
    }
    public void addReserved(int... newReserved) {
        this.reserved = ArrayUtils.addAll(reserved, newReserved);
    }
}
