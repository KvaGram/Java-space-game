package unicus.spacegame.crew;

public class JobAssignment {
    private final int jobID;
    private final int crewID;
    private WorkShare workshare;

    //temporary stored values.
    private double monthWorkloadShare;
    private double monthWorkProduced;

    public JobAssignment(int jobID, int crewID){

        this.jobID = jobID;
        this.crewID = crewID;
        this.workshare = WorkShare.full;

        this.monthWorkloadShare = 0;
        this.monthWorkProduced = 0;
    }

    /**
     * Called first in the end of month cycle.
     * Update the month's workload and workProduced
     *
     * Planned feature:
     *  1. Store monthly workload (JobAssignment)
     *  2. Store amount of work done (JobAssignment)
     *      1. (AbstractJob) Calculate crewman’s general efficiency at the job by checking stats and relevant traits.
     *      2. (Adult/Able Crewman) calculate further efficiency based on general traits of crewman.
     */
    public void endOfMonth() {
        monthWorkloadShare = getWorkloadShare();
        monthWorkProduced = calculateWork();
        //TODO: chance to trigger workplace event
    }

    /**
     * Calculate the amount of work this assigned crewman will contribute to the job.
     * @return An amount of work that will be produced by this crewman on this assignment.
     */
    public double calculateWork() {
        //Get modifier - crewman efficiency on job. factors in skill, traits, etc.
        //NOTE: getWorkModifierOfCrewman may or may not call AdultCrewman#getGeneralWorkModifier(), depending on implementation.
        double m = SpaceCrew.SC().getJob(jobID).getWorkModifierOfCrewman(crewID);
        //multiplies with the crewman's share in the job
        return m * getWorkloadShare();
    }

    /**
     * Gets the share of workload (impact on morale) put on this crewmember on the job.
     * Notes: * the final morale impact may be affected by an workplace event.
     *        * The final amount of work done is calculated in {@link #calculateWork()}.
     *        * work points and morale points work on the same scale, and is displayed in the 1'000's in the UI.
     *        * For estimates and EOM functions, use {@link #calculateWork()} for final amount of work.
     * @return A base value amount of work, used for calculating production and morale impact.
     */
    public double getWorkloadShare() {
        // Get how much work is expected or required on the job
        double w = SpaceCrew.SC().getJob(jobID).getMonthlyWorkload();
        // Get a share of how much of this work is on the assigned crewman
        return w * getWorkloadShareModifier();
    }

    /**
     * Gets the share modifier of the workload.
     * This value is based on the number of crewmen assigned to this job, and their {@link #workshare} state.
     * @return A modifier value between 0.0 and 1.0
     */
    public double getWorkloadShareModifier() {
        int totalShares = 0; //total 'shares' of the job.
        for (JobAssignment ja:SpaceCrew.SC().getJobAssignmentsByJob(jobID)) {
            totalShares += ja.workshare.share;
        }
        //Divide the crewman's share with the total amount to get their share of the workload.
        return (double)workshare.share / (double)totalShares;
    }



    public WorkShare getWorkshare() {
        return workshare;
    }

    public void setWorkshare(WorkShare workshare) {
        this.workshare = workshare;
    }

    public int getCrewID() {
        return crewID;
    }

    public int getJobID() {
        return jobID;
    }


    /**
     * The amount of work done by this assignment by the end of the month.
     * To be called only by {@link AbstractJob#endOfMonth() }
     * @return
     */
    public double getMonthWorkProduced() {
        return monthWorkProduced;
    }

    public void setMonthWorkProduced(double monthWorkProduced) {
        this.monthWorkProduced = monthWorkProduced;
    }

    /**
     * Amount of workload from this assignment this month. This impacts a crewman's crewman's morale.
     * @return
     */
    public double getMonthWorkloadShare() {
        return monthWorkloadShare;
    }

    public void setMonthWorkloadShare(double monthWorkloadShare) {
        this.monthWorkloadShare = monthWorkloadShare;
    }

}
enum WorkShare {
    /**
     * This crewmember has taken a vacation from this job, and gets no share of the workload.
    */
    vacation(0),
    /**
     * This crewmember has a reduced share of the workload
     */
    reduced(1),
    /**
     * This crewmember has a normal share of the workload.
     */
    full(3),
    /**
     * This crewmember has been asked to take up the slack, and takes on an extra share of the workload
     */
    extended(5);

    public final int share;

    WorkShare(int share) {

        this.share = share;
    }
}