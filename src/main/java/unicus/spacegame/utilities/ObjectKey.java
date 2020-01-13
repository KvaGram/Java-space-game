package unicus.spacegame.utilities;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The ObjectKey is a unique ID value for an object.
 * The key values must be unique per object type,
 * as they are used to locate objects in lists.
 */
public class ObjectKey {
    private int nextKey;
    private int[] reserved;

    public ObjectKey() {
        this(Integer.MIN_VALUE);
    }

    public ObjectKey(int nextKey){
        this.nextKey = nextKey;
        setReserved();
    }
    public int yieldKey() {
        int ret = nextKey;

        do {
            nextKey++;
        }
        while(ArrayUtils.contains(reserved, nextKey));
        return nextKey;
    }

    /**
     * Sets the keys reserved for special use.
     * {@link #yieldKey()} will never yield a reserved value.
     * note: value 0 is always reserved.
     * @param reserved
     */
    public void setReserved(int... reserved) {
        this.reserved = ArrayUtils.add(reserved, 0);
    }
}
