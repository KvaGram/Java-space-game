package unicus.spacegame.crew;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
/*
 * Refactor notes:
 * Crew.java is renamed to SpaceCrew.java.
 * Package spacegame.crew has been added as a place to keep all crew code
 * SpaceCrew is going to be the main model class for crew
 *   in the same way HomeShip holds the model for the home-ship.
 * */

public class SpaceCrew {
    private static SpaceCrew instace;
    public static SpaceCrew getInstance() {
        return instace;
    }

    private final ObjectKey crewKeys;
    private final ObjectKey jobKeys;

    public SpaceCrew(){
        this.crewmen = new AbstractCrewman[0];
        this.jobs = new AbstractJob[0];
        this.jobAssignments = new JobAssignment[0];
        instace = this;

        jobKeys = new ObjectKey();
        crewKeys = new ObjectKey();
    }

    //TODO: Add constructor, crewGenerator (start scenarios), crew-lists

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

    private void removeCrewmen(int... crewKeys) {
        int[] toRemove = new int[0];
        for (int key:crewKeys)
            for (int i = 0; i < crewmen.length; i++)
                if (crewmen[i].keyID == key) toRemove = ArrayUtils.add(toRemove, i);
        crewmen = ArrayUtils.removeAll(crewmen, toRemove);
    }
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
        for (int i = 0; i < jobs.length; i++)
            if (ArrayUtils.contains(jobKeys, jobs[i].getKeyID()))  toRemove = ArrayUtils.add(toRemove, i);
        jobs = ArrayUtils.removeAll(jobs, toRemove);
    }
    public boolean canAssignCrew(int jobID, int crewID) {
        return canAssignCrew(jobID, crewID, new StringBuffer());
    }
    public boolean canAssignCrew(int jobID, int crewID, StringBuffer message) {
        AbstractJob job = getJob(jobID);
        AbstractCrewman crewman = getCrew(crewID);
        if(job == null) {
            message.append("Cannot assign crewman, invalid job ID.");
            return false;
        }
        if(crewman == null) {
            message.append("Cannot assign crewman, invalid crewman ID");
            return false;
        }
        if(false) { //TODO: check for illegible for work
            message.append("Cannot assign crewman, this crewman can't work.");
            return false;
        }
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
    public void assignCrew(int jobID, int crewID) {
        if(!canAssignCrew(jobID, crewID))
            return;
        JobAssignment newJA = new JobAssignment(jobID, crewID);
        jobAssignments = ArrayUtils.add(jobAssignments, newJA);
    }
    public void unassignCrew(int jobID, int crewID) {
        for (int i = 0; i < jobAssignments.length; i++) {
            if(jobAssignments[i].getJobID() == jobID && jobAssignments[i].getCrewID() == crewID) {
                jobAssignments = ArrayUtils.remove(jobAssignments, i);
                return;
            }
        }
    }
    public void unassignAllCrew(int jobID) {
        int[] toRemove = new int[0];
        for (int i = 0; i < jobAssignments.length; i++) {
            if(jobAssignments[i].getJobID() == jobID) {
                toRemove = ArrayUtils.add(toRemove, i);
            }
        }
        jobAssignments = ArrayUtils.removeAll(jobAssignments, toRemove);
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
    public JobAssignment[] getAssignmentsByJob(int jobID){
        JobAssignment[] assignments = new JobAssignment[0];
        for (JobAssignment ja : jobAssignments) {
            if(ja.getJobID() == jobID)
                assignments = ArrayUtils.add(assignments, ja);
        }
        return assignments;

    }
    public JobAssignment[] getAssignmentsByCrewman(int crewID){
        JobAssignment[] assignments = new JobAssignment[0];
        for (JobAssignment ja : jobAssignments) {
            if(ja.getCrewID() == crewID)
                assignments = ArrayUtils.add(assignments, ja);
        }
        return assignments;
    }
    public JobAssignment getAssignment(int jobID, int crewID) {
        for (JobAssignment ja : jobAssignments) {
            if(ja.getJobID() == jobID && ja.getCrewID() == crewID)
                return ja;
        }
        return null;
    }


    //TODO: rewrite tester to use the new AdultCrewman class and features
    public static void main(String[] args) {
        //Create window
        JFrame frame = new JFrame("Crew Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        JTextArea j_text = new JTextArea("Example of Crew overview. \n");
        JPanel j_buttonpanel = new JPanel();

        //Create crew for testing
        ArrayList<Crewman> crewmen = new ArrayList<Crewman>();
        Crewman John = new Crewman("John Smith");
        crewmen.add(new Crewman("Ole Nordmann"));

        //Job tracker
        CrewJob c = new CrewJob(new JobStub("default", 0), new Crewman());

        //Buttons
        JButton b_recruit = new JButton("Recruit");
        j_buttonpanel.add(b_recruit);
        b_recruit.addActionListener(arg0 -> {
            j_text.append("Got new crewman.\n");
            crewmen.add(new Crewman());
        });
        JButton b_train = new JButton("Train");
        j_buttonpanel.add(b_train);
        b_train.addActionListener(arg0 -> j_text.append("Crewman trained. Placeholder.\n"));
        JButton b_inspect = new JButton("Inspect");
        j_buttonpanel.add(b_inspect);
        b_inspect.addActionListener(arg0 -> {
            j_text.append("Crew data: barebones.\n");
            for (Crewman N: crewmen ) {
                j_text.append(N.name+", age "+N.age+". ");
            }
            j_text.append("\n");
        });

        //final visibility arrangement
        frame.getContentPane().add(BorderLayout.SOUTH, j_buttonpanel);
        frame.getContentPane().add(BorderLayout.CENTER, j_text);
        frame.setVisible(true);
    }

    public AbstractCrewman[] getCrewmen() {
        return crewmen;
    }
}

/*
TODO: Old code below.
 To be refactored into new classes and objects
 */


class Crewman {
    /* changes:
    *
    * Skill values made into an array instead,
    * and the static constants are replaced by enum SkillTypes.
    * Note that if we keep skills capped at 100, we might consider storing the values as byte instead of int.
    *
    * boolean[] roles is obsolete, adn has been culled.
    *
    * culled setRoles, giveRole and removeRole.
    *
    * replaced skill-training functions with one single function running on skillValues.
    * Added SKILL_CAP as an easy-to-change constant.
    *
    * rewrote trainSkillByName and trainSkillByID to redirect to trainSkill.
    *
    *  - Lars
    * */

    private static final int SKILL_CAP = 100;

    //Skill values for each crewman. Currently capped at 100 in the skill increase function.
    private int[] skillValues;
    double age, stress, intelligence;
    String name;


    /** Creates a new crewman with randomly generated name, no stress, young adult age */
    Crewman() {
        this.name = makeSkiffyName();
        this.stress = 0;
        this.age = 20 + (new Random().nextInt(20)); //20-40
        this.intelligence = new Random().nextInt(100); //0-99
        //initiate skillValue list, by number of skills in SkillTypes.
        this.skillValues = new int[SkillTypes.GetNumSkills()];

    }
    /** Creates a new crewman with a specific name, otherwise as normal constructor */
    Crewman(String name) {
        this();
        this.name = name;
    }

    /**
     * Generates a vaguely sci-fi-sounding, English-pronounceable name such as "Hytwav", "Vukdyz" or "Bendor".
     * @return Name string with consonant-vowel pattern CVCCVC
     */
    public String makeSkiffyName() {
        String vowels = "aeiouy";
        String consonants = "bcdfghjklmnprstvwxz";
        char[] protoname = new char[6];
        for (int i=0; i<protoname.length; i++) {
            if (i%3 == 1) {
                protoname[i] = vowels.charAt(new Random().nextInt(vowels.length()));
            } else {
                protoname[i] = consonants.charAt(new Random().nextInt(consonants.length()));
            }
            protoname[0] = Character.toUpperCase(protoname[0]);
        }
        return new String(protoname);
    }


    /*
    NOTE: culled setRoles, giveRole and removeRole.
    TODO: decide where and how jobs are set.

     - Lars
    * */

    public void ageUp() { //Stock function to be called whenever a year passes
        this.age += 1;
    }
    public void ageUp(double amount) { //When incrementing crewmember's age by some specific amount
        this.age += amount;
    }
    public void changeStress(int amount) { //
        this.stress += amount;
    }

    //Trains skill at index skillIndex by amount
    public void trainSkill(int skillIndex, int amount) {
        this.skillValues[skillIndex] += amount;
        if (this.skillValues[skillIndex] > SKILL_CAP) {
            this.skillValues[skillIndex] = SKILL_CAP;
        }
    }
    //Trains type skill by amount
    public void trainSkill(SkillTypes type, int amount) {
        trainSkill(SkillTypes.GetIndexByType(type), amount);
    }
    //Trains type skill by 1
    public void trainSkill(SkillTypes type) {
        trainSkill(type, 1);
    }


    //Should training ever be reduced for an individual crewmember?
    public void renameTo(String newname) { //In case player doesn't like a randomly generated name, or wants to name the crew after Star Trek characters
        this.name = newname;
    }
    public void renameRandomly() {
        this.name = makeSkiffyName();
    }

    /*
    NOTE: rewrote trainSkillByName and trainSkillByID to redirect to trainSkill.
     */

    public void trainSkillByName(String skillName) {
        trainSkill(SkillTypes.GetIndexByString(skillName), 1);
    }
    public void trainSkillByID(int skillID) {
        trainSkill(skillID, 1);
    }
}

//create JobStub objects for: research, diplomacy, medical, teaching, navigation, engineering, mining, leadership, gunnery, boarding;
class JobStub {
    String name;
    int ID;
    public JobStub(String jobName, int jobID) {
        this.name = jobName;
        this.ID = jobID;
    }
}

class CrewJob {
    JobStub job;
    Crewman crewman;

    static ArrayList<CrewJob> jobRelations = new ArrayList<>();

    public CrewJob(JobStub job, Crewman crewman){
        this.job = job;
        this.crewman = crewman;
    }
    public static boolean addCrewJob(CrewJob crewJob){
        return jobRelations.add(crewJob);
    }
    public static boolean removeCrewJob(CrewJob crewJob){
        return jobRelations.remove(crewJob);
    }
    public static ArrayList<CrewJob> GetJobRelations(){
        return jobRelations;
    }
}