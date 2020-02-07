package unicus.spacegame.crew;

import java.util.Random;

/*
* Skills-types put in an enum.
* This makes it easier to change the list in the future
* - Lars
*/
public enum SkillType
{
    socialization("The ability to come to agreement with other, be they fellow crew, or aliens."),
    navigation("The ability to steer and navigate spacecraft, be it in sublight or warp speeds."),
    weaponry("The proficiency of using tools of destruction, to blow stuff up! Be it for mining or combat."),
    combat("The skill of fighting in a combat with hostiles. Be it close or at range with handheld weapons."),
    artifice("The skill of understanding tools, devices and other technology."),
    doctoring("The skill of healing any biological creature, be it human, alien or plants");

    // Static function for all of SkillTypes:

    public final String description;

    public static int GetIndexByType(SkillType type) {
        return type.ordinal();
    }
    public static SkillType GetTypeByIndex(int index) {
        return SkillType.values()[index];
    }
    public static SkillType GetTypeByString(String name) {
        return SkillType.valueOf(name.toLowerCase().trim());
    }
    public static int GetIndexByString(String name) {
        return GetIndexByType(GetTypeByString(name));
    }
    public static int GetNumSkills(){
        return SkillType.values().length;
    }

    //Individual functions for each value:

    public String getName(){
        return this.toString();
    }

    SkillType(String description){

        this.description = description;
    }

    public static SkillType random(Random r){return values()[r.nextInt(values().length)];}
    public static SkillType random(long randomSeed){return random(new Random(randomSeed));}
}
