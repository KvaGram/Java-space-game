package unicus.spacegame.crew;

public class JobAssignment {
    private final int jobID;
    private final int crewID;
    private WorkShare workshare;

    public JobAssignment(int jobID, int crewID){

        this.jobID = jobID;
        this.crewID = crewID;
        this.workshare = WorkShare.full;
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
}
enum WorkShare {
    /**
     * This crewmember has taken a vacation from this job, and gets no share of the workload.
    */
    vacation,
    /**
     * This crewmember has a reduced share of the workload
     */
    reduced,
    /**
     * This crewmember has a normal share of the workload.
     */
    full,
    /**
     * This crewmember has been asked to take up the slack, and takes on an extra share of the workload
     */
    extended
}