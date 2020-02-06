package unicus.spacegame.crew;

/*
* Skills-types put in an enum.
* This makes it easier to change the list in the future
* - Lars
*/
public enum SkillTypes
{
    socialization("The ability to come to agreement with other, be they fellow crew, or aliens."),
    navigation("The ability to steer and navigate spacecraft, be it in sublight or warp speeds."),
    weaponry("The proficiency of using tools of destruction, to blow stuff up! Be it for mining or combat."),
    combat("The skill of fighting in a combat with hostiles. Be it close or at range with handheld weapons."),
    artifice("The skill of understanding tools, devices and other technology."),
    doctoring("The skill of healing any biological creature, be it human, alien or plants");

    // Static function for all of SkillTypes:

    public final String description;

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

    SkillTypes(String description){

        this.description = description;
    }
}
