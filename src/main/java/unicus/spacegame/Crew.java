package unicus.spacegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
        //Buttons
        JButton b_recruit = new JButton("Recruit");
        j_buttonpanel.add(b_recruit);
        b_recruit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                j_text.append("Crewman recruited. Placeholder.\n");
            }
        });
        JButton b_train = new JButton("Train");
        j_buttonpanel.add(b_train);
        b_train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                j_text.append("Crewman trained. Placeholder.\n");
            }
        });

        //Create crew for testing
        Crewman[] crewmen;
        Crewman John = new Crewman("John Smith");

        //final visibility arrangement
        frame.getContentPane().add(BorderLayout.SOUTH, j_buttonpanel);
        frame.getContentPane().add(BorderLayout.CENTER, j_text);
        frame.setVisible(true);
    }
}

class Crewman {
    static int ID_research = 0; //in order to refer to roles[ID]. maybe hashmap<string, boolean> instead?
    static int ID_diplomacy = 1;
    static int ID_medical = 2;
    static int ID_teaching = 3;
    double age, stress, intelligence;
    boolean[] roles;
    int research, diplomacy, medical, teaching, navigation, engineering, mining, leadership, gunnery, boarding;
    String name;
    Crewman() {
        this.name = makeSkiffyName();
        this.stress = 0;
        this.age = 20 + (new Random().nextInt(20)); //20-40
        this.intelligence = new Random().nextInt(100); //0-99
    }
    Crewman(String name) {
        this();
        this.name = name;
    }
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
    public void setRoles(boolean[] newRoles) {
        this.roles = newRoles;
    }
    public void giveRole(int roleID) {
        this.roles[roleID] = true;
    }
    public void removeRole(int roleID) {
        this.roles[roleID] = false;
    }
    public void ageUp() {
        this.age += 1;
    }
    public void changeStress(int amount) {
        this.stress += amount;
    }
    public void trainResearch() {
        this.research += 1;
    }
    public void nameRedshirt(String newname) {
        this.name = newname;
    }
    public void trainSkill(String skill) {
        switch (skill.toLowerCase()) { //so function accepts both "research" and "Research"
            case "research":
                this.trainResearch();
                break;
        }
    }
}