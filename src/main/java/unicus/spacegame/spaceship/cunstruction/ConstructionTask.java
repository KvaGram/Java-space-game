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

    public int getLabourCost() {
        return labourCost;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone(){
        double remaining = getLabourCost() - progress;
        return remaining <= 0;
    }

    /**
     * Adds work to the progress of the task.
     * Returns unused workinput.
     * @param workInput Amount of work to add to task.
     * @param callFinish Whatever {@link #onFinish(StringBuffer)} should be called if work is done.
     * @param message Feedback information the player might want (only applicable if callFinish is true).
     * @return any work that did not get added to the progress.
     */
    public double addProgress(double workInput, boolean callFinish, StringBuffer message) {
        double remaining = getLabourCost() - progress;
        //Get amount of work to offload into this task.
        double w = Math.min(remaining, workInput);
        //Transfer work progress to task.
        progress += w;
        workInput -= w;
        if(callFinish && isDone())
            onFinish(message);

        return workInput;
    }
    public double addProgress (double workInput){
        return addProgress(workInput, false, null);
    }
}
