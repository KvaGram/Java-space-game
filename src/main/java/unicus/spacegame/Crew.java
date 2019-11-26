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

class Crewman {
    //IDs in order to be able to refer to skills by number as an alternative to name. A HashMap could perhaps be used instead.
    static int ID_research = 0;
    static int ID_diplomacy = 1;
    static int ID_medical = 2;
    static int ID_teaching = 3;
    static int ID_navigation = 4;
    static int ID_engineering = 5;
    static int ID_mining = 6;
    static int ID_leadership = 7;
    static int ID_gunnery = 8;
    static int ID_boarding = 9;
    double age, stress, intelligence;
    boolean[] roles;
    //Skill values for each crewman. Currently capped at 100 in the skill increase function.
    int s_research, s_diplomacy, s_medical, s_teaching, s_navigation, s_engineering, s_mining, s_leadership, s_gunnery, s_boarding;
    String name;
    /** Creates a new crewman with randomly generated name, no stress, young adult age */
    Crewman() {
        this.name = makeSkiffyName();
        this.stress = 0;
        this.age = 20 + (new Random().nextInt(20)); //20-40
        this.intelligence = new Random().nextInt(100); //0-99
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
    /**
     * Assigns a full set of roles to a crewman in the style (research=true, diplomacy=false, medical=false, etc)
     * @param newRoles Which roles are to be active or inactive
     */
    public void setRoles(boolean[] newRoles) {
        this.roles = newRoles;
    }
    /**
     * Assigns one new role to a crewman
     * @param roleID Index of role to put crewman on
     */
    public void giveRole(int roleID) {
        this.roles[roleID] = true;
    }
    /**
     * Removes one role from a crewman
     * @param roleID Index of the role to take crewman off
     */
    public void removeRole(int roleID) {
        this.roles[roleID] = false;
    }
    public void ageUp() { //Stock function to be called whenever a year passes
        this.age += 1;
    }
    public void ageUp(double amount) { //When incrementing crewmember's age by some specific amount
        this.age += amount;
    }
    public void changeStress(int amount) { //
        this.stress += amount;
    }
    //Training methods at the moment are a flat 1 increase. Possible change: amount-to-increase argument.
    public void trainResearch() {
        this.s_research += 1;
        if (s_research > 100) { s_research = 100; }
    }
    public void trainDiplomacy() {
        this.s_diplomacy += 1;
        if (s_diplomacy > 100) { s_diplomacy = 100; }
    }
    public void trainMedical() {
        this.s_medical += 1;
        if (s_medical > 100) { s_medical = 100; }
    }
    public void trainTeaching() {
        this.s_teaching += 1;
        if (s_teaching > 100) { s_teaching = 100; }
    }
    public void trainNavigation() {
        this.s_navigation += 1;
        if (s_navigation > 100) { s_navigation = 100; }
    }
    public void trainEngineering() {
        this.s_engineering += 1;
        if (s_engineering > 100) { s_engineering = 100; }
    }
    public void trainMining() {
        this.s_mining += 1;
        if (s_mining > 100) { s_mining = 100; }
    }
    public void trainLeadership() {
        this.s_leadership += 1;
        if (s_leadership > 100) { s_leadership = 100; }
    }
    public void trainGunnery() {
        this.s_gunnery += 1;
        if (s_gunnery > 100) { s_gunnery = 100; }
    }
    public void trainBoarding() {
        this.s_boarding += 1;
        if (s_boarding > 100) { s_boarding = 100; }
    }
    //Should training ever be reduced for an individual crewmember?
    public void renameTo(String newname) { //In case player doesn't like a randomly generated name, or wants to name the crew after Star Trek characters
        this.name = newname;
    }
    public void renameRandomly() {
        this.name = makeSkiffyName();
    }
    public void trainSkillByName(String skill) {
        switch (skill.toLowerCase()) { //so function accepts both "research" and "Research"
            case "research":
                this.trainResearch(); break;
            case "diplomacy":
                this.trainDiplomacy(); break;
            case "medical":
                this.trainMedical(); break;
            case "teaching":
                this.trainTeaching(); break;
            case "navigation":
                this.trainNavigation(); break;
            case "engineering":
                this.trainEngineering(); break;
            case "mining":
                this.trainMining(); break;
            case "leadership":
                this.trainLeadership(); break;
            case "gunnery":
                this.trainGunnery(); break;
            case "boarding":
                this.trainBoarding(); break;
        } //cases are an ugly approach. This is known.
    }
    public void trainSkillByID(int skillID) {
        String[] temp = {"research", "diplomacy", "medical", "teaching", "navigation", "engineering", "mining", "leadership", "gunnery", "boarding"};
        String skillname = temp[skillID];
        trainSkillByName(skillname);
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