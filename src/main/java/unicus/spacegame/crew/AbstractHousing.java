package unicus.spacegame.crew;

public abstract class AbstractHousing {

    private final int keyID;
    private int capacity;
    private boolean active;

    public AbstractHousing(int keyID, int capacity) {

        this.keyID = keyID;
        this.capacity = capacity;
    }
    /**
     * Things happening at the end of a month.
     * May trigger events related to living situations.
     */
    public abstract void endOfMonth();

    public int getCapacity() {
        return capacity;
    }

    //the capacity of a housing unit may change
    protected void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if(!active) {

        }
        this.active = active;
    }

    public int getKeyID() {
        return keyID;
    }
}
