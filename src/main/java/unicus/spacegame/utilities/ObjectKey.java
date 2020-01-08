package unicus.spacegame.utilities;

/**
 * The ObjectKey is a unique ID value for an object.
 * The key values must be unique per object type,
 * as they are used to locate objects in lists.
 */
public class ObjectKey {
    private int nextKey;

    public ObjectKey() {
        this(Integer.MIN_VALUE);
    }

    public ObjectKey(int nextKey){

        this.nextKey = nextKey;
    }
    public int yieldKey() {
        int ret = nextKey;
        nextKey ++;
        return ret;
    }
}
