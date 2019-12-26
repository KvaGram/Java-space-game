package unicus.spacegame.crew;
/*
Note that some features of the AdultCrewman may be moved to a new superclass
 or interfaces as other implementations of AbstractCrewman are written.
 */

/**
 * A typical adult crewman.
 */
public class AdultCrewman extends AbstractCrewman {

    //Maximum obtainable level in a skill.
    private static final int SKILL_CAP = 100;
    //Stress-level where a stress-related crisis event will trigger.
    private static final double CRISIS_TRIGGER_STRESS = 100.0;

    //Skill values for the crewman.
    private int[] skillValues;

    //The accumulated stress of this crewman.
    double stress;

    //Base value intelligence for the crewman.
    protected double base_intelligence;

    /**
     * Gets the intelligence of the crewman, including traits modifying it.
     * Intelligence is used to calculate the chance of gaining a bonus skill-points when learning.
     * An intelligence level above 100 is guaranteed at least one bonus skill point when learning.
     * Intelligence is also used to calculate a chance for a workplace accident.
     * A crewman with intelligence above 100 is unlikely to have an accident.
    */
    public double getIntelligence(){
        return base_intelligence; //todo: add or subtract value according to traits
    }
    public int getSkill(SkillTypes skillType) {
        int skillIndex = SkillTypes.GetIndexByType(skillType);
        if(skillIndex < 0 || skillIndex >= skillValues.length)
            throw new IllegalArgumentException("Skill " + skillType + " is not a valid skill. This is a bug. If you loaded from a save-file, please check the game version and the change logs for a change to skill-types.");
        return getSkill(skillIndex);
    }
    public int getSkill(int skillIndex) {
        if(skillIndex < 0 || skillIndex >= skillValues.length)
            throw new IllegalArgumentException("index " + skillIndex + " is not a valid skill index.");
        return skillValues[skillIndex]; //todo: add or subtract value according to traits
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

    public AdultCrewman(int keyID, int birthDate, long randomSeed, int[] parents) {
        super(keyID, CrewmanState.adult, birthDate, randomSeed, parents);
        this.skillValues = new int[SkillTypes.values().length];
    }

    public AdultCrewman(int keyID, int birthDate, CrewSelfID selfID, CrewmanGeneData geneData, int[] skillValues) {
        super(keyID, CrewmanState.adult, birthDate, selfID, geneData);
        this.skillValues = skillValues;
    }

    protected AdultCrewman(AdultCrewman crewman) {
        super(crewman, CrewmanState.adult);
        this.skillValues = crewman.skillValues;
    }
    protected AdultCrewman(AbstractCrewman crewman) {
        super(crewman, CrewmanState.adult);
        this.skillValues = new int[SkillTypes.values().length];
    }
}
