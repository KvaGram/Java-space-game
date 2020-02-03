package unicus.spacegame.spaceship.cunstruction;

//NOTE: might merge in with a generic jobTask superclass.
public abstract class ConstructionTask {
    private int labourCost;
    private String description;
    public double progress;

    public ConstructionTask(int labourCost, String description) {
        this.progress = 0.0;
        this.labourCost = labourCost;
        this.description = description;
    }

    /** TODO: move to bottom-most super-class for tasks.
     * Runs when finishing up the job, the construction job is finished.
     * Some related events could trigger.
     * @param message information message on any issues or information the player needs to know.
     * @return whatever the task was successfully completed.
     */
    public abstract boolean onFinish(StringBuffer message);

    /**
     * @param message information message on any issues or information the player needs to know.
     * @return whatever the task was successfully removed.
     */
    abstract boolean onRemove(StringBuffer message);
}
