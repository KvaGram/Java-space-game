package unicus.spacegame;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Crew {
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
}

/*
* Skills-types put in an enum.
* This makes it easier to change the list in the future
* - Lars
*/
enum SkillTypes
{
    research,
    diplomacy,
    medical,
    teaching,
    navigation,
    engineering,
    mining,
    leadership,
    gunnery,
    boarding;

    // Static function for all of SkillTypes:

    public static int GetIndexByType(SkillTypes type) {
        return type.ordinal();
    }
    public static SkillTypes GetTypeByIndex(int index) {
        return SkillTypes.values()[index];
    }
    public static SkillTypes GetTypeByString(String name) {
        return SkillTypes.valueOf(name.toLowerCase().trim());
    }
    public static int GetIndexByString(String name) {
        return GetIndexByType(GetTypeByString(name));
    }
    public static int GetNumSkills(){
        return SkillTypes.values().length;
    }

    //Individual functions for each value:

    public String getName(){
        return this.toString();
    }
}

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