package unicus.spacegame.crew;
import org.apache.commons.lang3.ArrayUtils;
import unicus.spacegame.spaceship.cunstruction.Construction;
import unicus.spacegame.utilities.ObjectKey;
/*
 * Refactor notes:
 * Crew.java is renamed to SpaceCrew.java.
 * Package spacegame.crew has been added as a place to keep all crew code
 * SpaceCrew is going to be the main model class for crew
 *   in the same way HomeShip holds the model for the home-ship.
 * */

public class SpaceCrew {

    private static SpaceCrew instance;
    public static SpaceCrew getInstance() {
        return instance;
    }

    private final ObjectKey crewKeys;
    private final ObjectKey jobKeys;
    private final ObjectKey housingKeys;

    public SpaceCrew(){
        this.crewmen = new AbstractCrewman[0];
        this.jobs = new AbstractJob[0];
        this.jobAssignments = new JobAssignment[0];
        this.housing = new AbstractHousing[0];
        this.housingAssignments = new HousingAssignment[0];
        instance = this;

        jobKeys = new ObjectKey();
        crewKeys = new ObjectKey();
        housingKeys = new ObjectKey();

        //set reserved keys
        jobKeys.setReserved(Construction.CONSTRUCTION_JOB_KEY);
    }

    //TODO: crewGenerator (start scenarios), crew-lists

    /*
    Note: consider replacing array with hash map of key ids
     */
    /**
     * List of all crewman objects that can be referenced in game, living or dead.
     * All lists and references of crewmen eventually refer to this list.
     */
    private AbstractCrewman[] crewmen;
    /**
     * Lists of all job objects that can be referenced in game, active or not.
     * All lists and references to jobs eventually refer to this list.
     */
    private AbstractJob[] jobs;
    private JobAssignment[] jobAssignments;

    private AbstractHousing[] housing;
    private HousingAssignment[] housingAssignments;

    //STUB!
    public static SpaceCrew GenerateStart1() {
        return new SpaceCrew();
    }

    public AbstractJob getJob(int jobID){
        for (AbstractJob j : jobs) {
            if(j.getKeyID() == jobID)
                return j;
        }
        return null;
    }
    public AbstractCrewman getCrew(int crewID){
        for (AbstractCrewman c : crewmen) {
            if(c.getKeyID() == crewID)
                return c;
        }
        return null;
    }
    public AbstractHousing getHousing(int housingID){
        for (AbstractHousing h : housing) {
            if(h.getKeyID() == housingID) {
                return h;
            }
        }
        return null;
    }

    /**
     * Adds new crewmen to the list of crewmen.
     * If a crewman already exists (same keyID), the old object will be replaced with the new.
     *
     * @param newCrewObjects
     */
    public void addReplaceCrewmen(AbstractCrewman... newCrewObjects) {
        int[] toRemove = new int[0];
        for (AbstractCrewman c:newCrewObjects) {
            for (int i = 0; i < crewmen.length; i++)
                if (crewmen[i].keyID == c.keyID) toRemove = ArrayUtils.add(toRemove, i);
        }
        crewmen = ArrayUtils.removeAll(crewmen, toRemove);
        crewmen = ArrayUtils.addAll(crewmen, newCrewObjects);
    }

    //NOTE: for now, removing crewmen should be considered impossible.
    //in-game, the last CrewState, memorial, may be considered the closest thing to 'removed'.

    //public void removeCrewmen(int... crewKeys) {
    //    int[] toRemove = new int[0];
    //    for (int key:crewKeys)
    //        for (int i = 0; i < crewmen.length; i++)
    //            if (crewmen[i].keyID == key) toRemove = ArrayUtils.add(toRemove, i);
    //    crewmen = ArrayUtils.removeAll(crewmen, toRemove);
    //
    //}
    /**
     * Adds new job to the list of jobs.
     * If a job already exists (same keyID), it should not, the old object will be replaced with the new.
     *
     * @param newJobObjects
     */
    public void addJobs(AbstractJob... newJobObjects) {
        int[] toRemove = new int[0];
        for (AbstractJob j:newJobObjects) {
            for (int i = 0; i < jobs.length; i++)
                if (jobs[i].getKeyID() == j.getKeyID()) toRemove = ArrayUtils.add(toRemove, i);
        }
        jobs = ArrayUtils.removeAll(jobs, toRemove);
        jobs = ArrayUtils.addAll(jobs, newJobObjects);
    }
    public void removeJobs(int... jobKeys) {
        int[] toRemove = new int[0];
        int i;
        for (i = 0; i < jobs.length; i++)
            if (ArrayUtils.contains(jobKeys, jobs[i].getKeyID()))
                toRemove = ArrayUtils.add(toRemove, i);
        jobs = ArrayUtils.removeAll(jobs, toRemove);
        toRemove = new int[0];
        for (i = 0; i < jobAssignments.length; i++)
            if(ArrayUtils.contains(jobKeys, jobAssignments[i].getJobID()))
                toRemove = ArrayUtils.add(toRemove, i);
        jobAssignments = ArrayUtils.removeAll(jobAssignments, toRemove);
    }

    public void addHousing(AbstractHousing... newHousingObjects) {
        int[] toRemove = new int[0];
        for (AbstractHousing h:newHousingObjects) {
            for (int i = 0; i < housing.length; i++)
                if (housing[i].getKeyID() == h.getKeyID()) toRemove = ArrayUtils.add(toRemove, i);
        }
        housing = ArrayUtils.removeAll(housing, toRemove);
        housing = ArrayUtils.addAll(housing, newHousingObjects);
    }

    public void removeHousing(int... housingKeys) {
        int[] toRemove = new int[0];
        int i;
        for (i = 0; i < housing.length; i++)
            if (ArrayUtils.contains(housingKeys, housing[i].getKeyID()))
                toRemove = ArrayUtils.add(toRemove, i);
        housing = ArrayUtils.removeAll(housing, toRemove);
        toRemove = new int[0];
        for (i = 0; i < housingAssignments.length; i++)
            if (ArrayUtils.contains(housingKeys, housingAssignments[i].getHousingID()))
                toRemove = ArrayUtils.add(toRemove, i);
        housingAssignments = ArrayUtils.removeAll(housingAssignments, toRemove);
    }


    public boolean canAssignJobCrew(int jobID, int crewID) {
        return canAssignJobCrew(jobID, crewID, new StringBuffer());
    }

    public boolean canAssignJobCrew(int jobID, int crewID, StringBuffer message) {
        AbstractJob job = getJob(jobID);
        AbleCrewman crewman = getAbleCrew(crewID);
        if(job == null) {
            message.append("Cannot assign crewman, invalid job ID.");
            return false;
        }
        if(crewman == null) {
            message.append("Cannot assign crewman, invalid crewman ID, or not able to work.");
            return false;
        }

        if(!job.crewmanAllowedJob(crewman, message))
            return false;
        int numAssigned = 0;
        for (JobAssignment a : jobAssignments) {
            if(a.getJobID() == jobID) {
                if(a.getCrewID() == crewID) {
                    message.append("Cannot assign crewman, crewman is already assigned.");
                    return false;
                }

                numAssigned ++;
                if(numAssigned >= job.getNumWorkerSlots()) {
                    message.append("Cannot assign crewman, the workplace is full.");
                    return false;
                }
            }
        }
        message.append("Crewman may be assigned.");
        return true;
    }

    private AbleCrewman getAbleCrew(int crewID) {
        AbstractCrewman crewman = getCrew(crewID);
        if(crewman.getState().isWorkAble())
            return (AbleCrewman) crewman;
        else
            return null;
    }

    public void assignJobCrew(int jobID, int crewID) {
        if(!canAssignJobCrew(jobID, crewID))
            return;
        JobAssignment newJA = new JobAssignment(jobID, crewID);
        jobAssignments = ArrayUtils.add(jobAssignments, newJA);
    }

    public void unassignJobCrew(int jobID, int crewID) {
        for (int i = 0; i < jobAssignments.length; i++) {
            if(jobAssignments[i].getJobID() == jobID && jobAssignments[i].getCrewID() == crewID) {
                jobAssignments = ArrayUtils.remove(jobAssignments, i);
                return;
            }
        }
    }

    public void unassignAllJobCrew(int jobID) {
        int[] toRemove = new int[0];
        for (int i = 0; i < jobAssignments.length; i++) {
            if(jobAssignments[i].getJobID() == jobID) {
                toRemove = ArrayUtils.add(toRemove, i);
            }
        }
        jobAssignments = ArrayUtils.removeAll(jobAssignments, toRemove);
    }

    public JobAssignment[] getJobAssignmentsByJob(int jobID){
        JobAssignment[] assignments = new JobAssignment[0];
        for (JobAssignment ja : jobAssignments) {
            if(ja.getJobID() == jobID)
                assignments = ArrayUtils.add(assignments, ja);
        }
        return assignments;

    }
    public JobAssignment[] getJobAssignmentsByCrewman(int crewID){
        JobAssignment[] assignments = new JobAssignment[0];
        for (JobAssignment ja : jobAssignments) {
            if(ja.getCrewID() == crewID)
                assignments = ArrayUtils.add(assignments, ja);
        }
        return assignments;
    }
    public JobAssignment getJobAssignment(int jobID, int crewID) {
        for (JobAssignment ja : jobAssignments) {
            if(ja.getJobID() == jobID && ja.getCrewID() == crewID)
                return ja;
        }
        return null;
    }

    public boolean canAssignHouseCrew(int housingID, int crewID) {
        return canAssignHouseCrew(housingID, crewID, new StringBuffer());
    }
    public boolean canAssignHouseCrew(int housingID, int crewID, StringBuffer message) {
        AbstractHousing h = getHousing(housingID);
        AbstractCrewman c = getCrew(crewID);

        if(h == null) {
            message.append("Invalid housing selection");
            return false;
        }
        if(c == null) {
            message.append("Invalid crewman selection");
            return false;
        }
        if(c.getState() == CrewmanState.corpse || c.getState() == CrewmanState.memorial) {
            message.append("This crewman is dead. It simply would not be proper.");
            return false;
        }
        if(getHouseAssignment(housingID, crewID) != null) {
            message.append("This crewman already lives here.");
            return false;
        }
        int numResidents = getResidentsOfHouse(housingID).length;
        if(numResidents >= h.getCapacity()) {
            message.append("The house is full of people already!");
            return false;
        }
        message.append("The crewman can move here");
        return true;
    }

    public void assignHousingCrew(int housingID, int crewID, boolean force) {
        if(force || canAssignHouseCrew(housingID, crewID)){
            evictCrewman(crewID); //remove crewman from any previous housing
           HousingAssignment newHA = new HousingAssignment(housingID, crewID);
           housingAssignments = ArrayUtils.add(housingAssignments, newHA);
        }
    }

    public void evictCrewman(int crewID){
        int[] toRemove = new int[0];
        for (int i = 0; i < housingAssignments.length; i++) {
            if(housingAssignments[i].getCrewID() == crewID) {
                toRemove = ArrayUtils.add(toRemove, i);
            }
            housingAssignments = ArrayUtils.removeAll(housingAssignments, toRemove);
        }
    }

    public void evictAllFromHousing(int housingID) {
        int[] toRemove = new int[0];
        for (int i = 0; i < housingAssignments.length; i++) {
            if(housingAssignments[i].getHousingID() == housingID) {
                toRemove = ArrayUtils.add(toRemove, i);
            }
            housingAssignments = ArrayUtils.removeAll(housingAssignments, toRemove);
        }
    }

    public HousingAssignment[] getResidentsOfHouse(int housingID){
        HousingAssignment[] assignments = new HousingAssignment[0];
        for (HousingAssignment ha : housingAssignments) {
            if(ha.getHousingID() == housingID)
                assignments = ArrayUtils.add(assignments, ha);
        }
        return assignments;
    }

    public HousingAssignment getHousingByCrew(int crewID){
        for (HousingAssignment ha : housingAssignments) {
            if(ha.getCrewID() == crewID)
                return ha;
        }
        return null; //home
    }

    public HousingAssignment getHouseAssignment(int housingID, int crewID) {
        for (HousingAssignment ha : housingAssignments) {
            if(ha.getHousingID() == housingID && ha.getCrewID() == crewID)
                return ha;
        }
        return null;
    }

    public AbstractCrewman[] getCrewmen() {
        return crewmen;
    }
    public AbstractJob[] getJobs() {
        return jobs;
    }

    public ObjectKey getCrewKeys() {
        return crewKeys;
    }
    public ObjectKey getJobKeys() {
        return jobKeys;
    }
    public ObjectKey getHousingKeys() {
        return housingKeys;
    }

}