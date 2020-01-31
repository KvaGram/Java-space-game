package unicus.spacegame.spaceship.cunstruction;

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
     * @return whatever the task was successfully completed.
     */
    public abstract boolean onFinish();

    /**
     *
     * @return whatever the task was successfully removed.
     */
    abstract boolean onRemove();
}
