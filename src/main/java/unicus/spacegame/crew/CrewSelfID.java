package unicus.spacegame.crew;

import unicus.spacegame.utilities.NameGenerator;

import java.util.Random;

/**
 * The way the crewman identify their self, contains name and gender.
 * This class is a bit of a stub. It can be expanded to provide a more rich presentation of the crewman
 */
public class CrewSelfID {
   public String name;
   public CrewGender gender;

    /**
     * Make a default CrewSelfID object.
     * No name, male.
     */
   public CrewSelfID(){
       this("(NO-NAME)", CrewGender.male);
   }

    /**
     * MAke a new CrewSelfID with a set name and gender
     * @param name base name of the crewman
     * @param gender gender of the crewman
     */
   public CrewSelfID(String name, CrewGender gender) {
       this.name = name;
       this.gender = gender;
   }

   public void giveSkiffyName(long seed){
       name = NameGenerator.makeWord("CVCCVC", seed);
   }
    public void giveSkiffyName(Random rand){
        name = NameGenerator.makeWord("CVCCVC", rand);
    }


}
