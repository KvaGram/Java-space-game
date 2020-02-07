package unicus.spacegame.crew;


import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import static unicus.spacegame.crew.SkillType.*;

class SkillSet {
    private static final int SKILL_CAP = 100;
    private int[] skillPoints;

    public SkillSet(long r, int average, int tolerance, int fudge, int maxBiasBoon, SkillType... bias) {
        this(new Random(r), average, tolerance, fudge, maxBiasBoon, bias);
    }

    /**
     * Creates a new skill set for a crewman.
     * Note: the fudge and bias may not be used to the limit, as the randomized skillset are adjusted to the tolerance.
     * @param r Instance of Random to use (see {@link #SkillSet(long, int, int, int, int, SkillType...)}).
     * @param average The targeted average skill-level of the skill set
     * @param tolerance How much, above or below, the final average is allowed to be after randomizing
     * @param fudge How much non-bias skills may vary above or below the average
     * @param boon How much a bias skill may be above the average
     * @param bias list of skill types that the set is biased towards (more points)
     */
    public SkillSet(Random r, int average, int tolerance, int fudge, int boon, SkillType... bias) {
        SkillType[] types = SkillType.values();
        int numSkills = types.length;
        //Make sure the arguments are correct.
        if(average < 0 || average >= SKILL_CAP || tolerance < 0 || fudge < 0 || fudge > SKILL_CAP || boon < 0 || boon > SKILL_CAP)
            throw new IllegalArgumentException();

        skillPoints = new int[numSkills];


        int targetMinimalTotalPoints = (average - tolerance) * numSkills;
        int targetMaximumTotalPoints = (average + tolerance) * numSkills;

        if (targetMinimalTotalPoints < 0)
            targetMinimalTotalPoints = 0;
        if(targetMaximumTotalPoints > SKILL_CAP * numSkills)
            targetMaximumTotalPoints = SKILL_CAP * numSkills;

        //Total skill points
        int total = 0;
        for(int s = 0; s < types.length; s++) {
            if(ArrayUtils.contains(bias, SkillType.GetTypeByIndex(s))) {
                skillPoints[s] = average + r.nextInt(boon);
            }
            else {
                skillPoints[s] = average - fudge + r.nextInt(fudge * 2);
            }
            total += skillPoints[s];
        }


        double toAdjust = 0;
        if(total > targetMaximumTotalPoints)
            toAdjust = targetMaximumTotalPoints - total;
        else if (total < targetMinimalTotalPoints)
            toAdjust = targetMinimalTotalPoints - total;

        for (int s = 0; s < types.length; s++) {
            int a = (int)Math.floor( (toAdjust / (numSkills - s)));
            skillPoints[s] += a;
            toAdjust -= a;
        }
    }
    public SkillSet(int[] skillPoints) {
        this.skillPoints = skillPoints;
    }

    public int getSkill(SkillType skillType) {
        int skillIndex = SkillType.GetIndexByType(skillType);
        if(skillIndex < 0 || skillIndex >= skillPoints.length)
            throw new IllegalArgumentException("Skill " + skillType + " is not a valid skill. This is a bug. If you loaded from a save-file, please check the game version and the change logs for a change to skill-types.");
        return getSkill(skillIndex);
    }
    public int getSkill(int skillIndex) {
        if(skillIndex < 0 || skillIndex >= skillPoints.length)
            throw new IllegalArgumentException("index " + skillIndex + " is not a valid skill index.");
        return skillPoints[skillIndex];
    }

    //Trains skill at index skillIndex by amount
    public void trainSkill(int skillIndex, int amount) {
        this.skillPoints[skillIndex] += amount;
        if (this.skillPoints[skillIndex] > SKILL_CAP) {
            this.skillPoints[skillIndex] = SKILL_CAP;
        }
    }

    public int[] getSkillPoints() {
        return skillPoints;
    }

    protected void setSkillPoints(int[] skillPoints) {
        this.skillPoints = skillPoints;
    }
    /** Trains type skill by amount */
    public void trainSkill(SkillType type, int amount) {
        trainSkill(SkillType.GetIndexByType(type), amount);
    }
    /** Trains type skill by 1 */
    public void trainSkill(SkillType type) {
        trainSkill(type, 1);
    }

    public double averageStat() {
        double ret = 0.0;
        for(int s : skillPoints)
            ret += s;
        ret /= skillPoints.length;
        return ret;
    }

    public String asText() {
        StringBuffer t = new StringBuffer();
        t.append("--Skill set--\n");
        for (SkillType s :
                values()) {
            t.append(s.toString()).append(": ").append(skillPoints[s.ordinal()]).append("\n");
        }
        t.append("--End of skill set--");
        return t.toString();
    }

    /**
     * test SkillSet
     * @param args
     */
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        SkillSet set = new SkillSet(seed, 20, 0, 5, 50, socialization, weaponry);
        System.out.println(set.asText());
        System.out.println("Average: " + set.averageStat());
    }
}
