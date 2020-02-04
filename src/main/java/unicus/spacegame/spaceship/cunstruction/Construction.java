package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.crew.AbstractJob;
import unicus.spacegame.crew.AbleCrewman;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.crew.Workplace;
import unicus.spacegame.spaceship.ShipLoc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Construction is a unique job and workplace.
 */
public class Construction extends AbstractJob implements Workplace {
    //The key for the Construction job is special and meant to be easily recognizable.
    static final public int CONSTRUCTION_JOB_KEY = 1;
    private static Construction instance;

    private ArrayList<RefitTask> workQueue;

    private Construction() {
        super(CONSTRUCTION_JOB_KEY, 9);
        workQueue = new ArrayList<>();
        if (instance == null)
            instance = this;
        SpaceCrew.getInstance().addJobs(this);
    }

    @Override
    public void endOfMonth() {
        super.endOfMonth();
        double workRemaining = monthWorkDone;
        StringBuffer message = new StringBuffer();

        //runs over the tasks twice.
        //If a task somehow is dependent on another task (out of order), and fails to complete at the first pass,
        // it will get another chance to complete at the second pass.
        //NOTE: this could be optimized by checking for any such conflicts at this stage.
        for (int i = 0; i < 2; i++) {
            for (int j = workQueue.size() - 1; j >= 0; j--) {
                RefitTask task = workQueue.get(j);
                //Adds progress to a task, reducing the amount of work left.
                // If a task is complete, run the onFinish(StringBuffer) method with param message
                workRemaining = task.addProgress(workRemaining, true, message.append("\n"));
            }
        }
    }

    public static ArrayList<ShipLoc> getBusyLocations(){
        ArrayList<ShipLoc> ret = new ArrayList<>();
        for (RefitTask task : instance.workQueue) {
            ret.addAll(Arrays.asList(task.targets));
        }
        return ret;
    }

    /**
     * Returns a list of all jobs that depends on this object being active.
     * Should this object be removed, these jobs must also be removed.
     *
     * @return KeyID of the job(s) dependent on this object.
     */
    @Override
    public int[] getDependentJobs() {
        return new int[]{this.getKeyID()};
    }

    /**
     * Returns a list the job(s) associated with this object.
     * This is meant as a tool for player interface.
     * Should include all dependent jobs.
     *
     * @return
     */
    @Override
    public int[] getAllJobs() {
        return new int[]{this.getKeyID()};
    }


    /**
     * Gets the amount of workload to be put on assigned crewmembers this month.
     *
     * @return A value of workload pressure.
     */
    @Override
    public double getMonthlyWorkload() {
        return 0;
    }

    /**
     * Calculates a base efficiency for how well a crewman will do this job.
     * Used in UI to show percentage efficiency.
     * Note: implementation should include the result from {@link AbleCrewman#getGeneralWorkModifier()},
     * unless implementation has an alternative.
     *
     * @param crewID The ID of the crewman
     * @return The base efficiency of the crewman, where 1.0 equals 100%.
     */
    @Override
    public double getWorkModifierOfCrewman(int crewID) {
        return 0;
    }


    public static Construction I() {
        if (instance == null)
            new Construction();
        return instance;
    }

    static public boolean AddTask(RefitTask task) {
        if (I().workQueue.contains(task))
            return false;
        I().workQueue.add(0, task);
        I().setActive(!I().workQueue.isEmpty());
        return true;
    }
    static public boolean RemoveTask(RefitTask task) {
        boolean success = I().workQueue.remove(task);
        I().setActive(!I().workQueue.isEmpty());
        return success;
    }
    static public RefitTask[] getWorkQueue(){
        return (RefitTask[]) I().workQueue.toArray();
    }
}



