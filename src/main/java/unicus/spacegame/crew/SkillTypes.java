package unicus.spacegame.crew;

/*
* Skills-types put in an enum.
* This makes it easier to change the list in the future
* - Lars
*/
public enum SkillTypes
{
    research,
    diplomacy,
    medical,
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
