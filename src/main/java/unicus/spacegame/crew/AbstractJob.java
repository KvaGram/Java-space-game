package unicus.spacegame.crew;

/**
 * What is a job?
 * A job is a responsibility, a set of tasks, that needs to be done during each month.
 * A crewmembers needs to be assigned to a job for a job to function.
 *
 * Some jobs are temporary. There are two types of temporary jobs.
 *      * Recurring temporary jobs will be automatically disabled once the current task(s) are completed or aborted.
 *          This includes jobs like ship-repair, refit jobs, research, diplomacy etc.
 *          Jobs of this type are created at the start of a new game
 *          These jobs will be disabled when not in use.
 *      * Temporary jobs, spawned from events, eventchains and speacial needs.
 *          This includes jobs like guarding an alien prisoner, childcare, planetary expeditions etc.
 *          Jobs of this type are automatically created when needed, in case of childcare even automatically assigned.
 *          These jobs will be deleted once their use has been fullfilled.
 * Other jobs are set from the configuration of the ship, ie the modules constructed.
 *
 * A job's function varies, and so does the consequences for a job not being done.
 *
 * A list of jobs are stored in SpaceCrew
 * A list of job-assignments are stored in SpaceCrew
 * @see SpaceCrew
 *
 * Each job has a unique keyID
 *      The keyID is used to connect
 *          the job to workplace (if there is one)
 *          the crewmembers assigned to it.
 *      The keyID is always unique.
 *          If a duplicate keyID where to exist, the game may not function properly.
 * Each job have a maximum number of worker slots.
 *      This value is constant for a job, no matter the upgrades to the workplace.
 *      Note the worker slots need not be filled to reach full efficiency.
 * Each job has a workload to be shared between the workers.
 *      The workload may not be fixed.
 *      The workload is based on the amount of work expected to be done within the month.
 * Each job has a worker evaluation
 *       This is used to check how effeciently this worker *can* work.
 * Each job has a end-of-month report, where the work process is calculated.
 *      The final amount of work done is compared to the expected amount, providing effeciency data.
 *      Consequences for too little or too much work done varies between jobs, and are implemented here.
 * Implementation
 *      The implementation of AbstractJob may happen in a ship module or component as a inner or package-private class.
 *      However the implementation is made:
 *          A job-keyID must be obtained from SpaceCrew
 *          The job must be added to the list of jobs in SpaceCrew when an instance is created.
 *          The job must be removed from the list of jobs in SpaceCrew when an instance is removed.
 */

public abstract class AbstractJob {
    private final int keyID;
    private final int numWorkerSlots;
    private boolean active;

    protected AbstractJob(int keyID, int numWorkerSlots){
        this.keyID = keyID;
        this.numWorkerSlots = numWorkerSlots;
        active = true;
    }

    public int getKeyID() {
        return keyID;
    }

    public int getNumWorkerSlots() {
        return numWorkerSlots;
    }

    /**
     * Gets the amount of workload to be put on assigned crewmembers this month.
     * @return A value of workload pressure.
     */
    public abstract double getMonthlyWorkload();

    /**
     * Calculates how much work this worker will normally produce.
     * This is used to:
     *      show efficiency percentage (work divided by workload)
     *      used as first step to calculate how muc work this assigned crewman will do.
     * NOTE: Implementation must be calculation only, and not alter any data, as this may be called multiple times.
     *
     * @param crewman The assigned crewman
     * @param workload The amount of workload assigned to this crewman for this job.
     * @return An estimated amount of work a crewman will do on the job.
     *
     * NOTE: class AdultCrewman may be replaced with a more general class of all crewmen who can take jobs.
     */
    public abstract double evaluateWorker(AdultCrewman crewman, double workload);

    /**
     * Completes work required for the month.
     * Completes task list and or operations.
     * May triggers events related to what has been worked on.
     * @param workDone how much work has been done in total
     */
    public abstract void endOfMonth(double workDone);

    /**
     * Whatever this job is currently active.
     * Jobs can be disabled. No crew will work the job.
     * endOfMonth will still be called on a disabled job.
     * When a job is disabled, all assigned crewmen get unassigned from the job.
     * @return whatever the job is active.
     */
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            //This unassigns all crewmen assigned to this job.
            SpaceCrew.getInstance().unassignAllCrew(keyID);
        }
    }
}


