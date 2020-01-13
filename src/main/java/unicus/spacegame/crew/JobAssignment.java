package unicus.spacegame.crew;

public class JobAssignment {
    private final int jobID;
    private final int crewID;
    private WorkShare workshare;

    //temporary stored values.
    private double monthWorkload;
    private double monthWork;

    public JobAssignment(int jobID, int crewID){

        this.jobID = jobID;
        this.crewID = crewID;
        this.workshare = WorkShare.full;

        this.monthWorkload = 0;
        this.monthWork = 0;
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
     * Gets the amount of workload shared for this assignment.
     * @return
     */
    public double getWorkload(){
        int totalShares = 0;
        for (JobAssignment ja:SpaceCrew.getInstance().getJobAssignmentsByJob(jobID)) {
            totalShares += ja.workshare.share;
        }
        double WorkLoadShare = (double)workshare.share / (double)totalShares;
        AbstractJob j = SpaceCrew.getInstance().getJob(jobID);
        return WorkLoadShare * j.getMonthlyWorkload();
    }

    /**
     * The amount of work done by this assignment by the end of the month.
     * To be called only by {@link AbstractJob#endOfMonth() }
     * @return
     */
    public double getMonthWork() {
        return monthWork;
    }

    public void setMonthWork(double monthWork) {
        this.monthWork = monthWork;
    }

    /**
     * Amount of workload from this assignment this month. This impacts a crewman's crewman's morale.
     * @return
     */
    public double getMonthWorkload() {
        return monthWorkload;
    }

    public void setMonthWorkload(double monthWorkload) {
        this.monthWorkload = monthWorkload;
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