package unicus.spacegame.utilities;

public class Math {
    /**
     * rolls the value up or down until it is between 0 and {@code limit}
     */
    public static int rollClamp(int value, int limit) {
        assert(limit > 0);
        value %= limit;
        if(value < 0)
            value += limit;
        return value;
    }
}
