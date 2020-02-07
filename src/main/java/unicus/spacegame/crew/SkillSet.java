package unicus.spacegame.crew;


import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Random;
import static unicus.spacegame.crew.SkillTypes.*;

class SkillSet {
    private static final int SKILL_CAP = 100;

    public SkillSet(long r, int average, int tolerance, int fudge, int maxBiasBoon, SkillTypes... bias) {
        this(new Random(r), average, tolerance, fudge, maxBiasBoon, bias);
    }

    /**
     * Creates a new skill set for a crewman.
     * Note: the fudge and bias may not be used to the limit, as the randomized skillset are adjusted to the tolerance.
     * @param r Instance of Random to use (see {@link #SkillSet(long, int, int, int, int, SkillTypes...)}).
     * @param average The targeted average skill-level of the skill set
     * @param tolerance How much, above or below, the final average is allowed to be after randomizing
     * @param fudge How much non-bias skills may vary above or below the average
     * @param boon How much a bias skill may be above the average
     * @param bias list of skill types that the set is biased towards (more points)
     */
    public SkillSet(Random r, int average, int tolerance, int fudge, int boon, SkillTypes... bias) {
        SkillTypes[] types = SkillTypes.values();
        int numSkills = types.length;
        if(average < 0 || average > SKILL_CAP || tolerance < 0 || fudge < 0 || fudge > SKILL_CAP || boon < 0 || boon > SKILL_CAP)
            throw new IllegalArgumentException();

        stats = new int[numSkills];
        int targetMinimalTotalPoints = (average - tolerance) * numSkills;
        int targetMaximumTotalPoints = (average + tolerance) * numSkills;

        if (targetMinimalTotalPoints < 0)
            targetMinimalTotalPoints = 0;
        if(targetMaximumTotalPoints > SKILL_CAP * numSkills)
            targetMaximumTotalPoints = SKILL_CAP * numSkills;

        //Total skill points
        int total = 0;
        for(int s = 0; s < types.length; s++) {
            if(ArrayUtils.contains(bias, SkillTypes.GetTypeByIndex(s))) {
                stats[s] = average + r.nextInt(boon);
            }
            else {
                stats[s] = average - fudge + r.nextInt(fudge * 2);
            }
            total += stats[s];
        }


        double toAdjust = 0;
        if(total > targetMaximumTotalPoints)
            toAdjust = targetMaximumTotalPoints - total;
        else if (total < targetMinimalTotalPoints)
            toAdjust = targetMinimalTotalPoints - total;

        for (int s = 0; s < types.length; s++) {
            int a = (int)Math.floor( (toAdjust / (numSkills - s)));
            stats[s] += a;
            toAdjust -= a;
        }
    }
    public SkillSet(int[] stats) {
        this.stats = stats;
    }
    private int[] stats;


    public int[] getStats() {
        return stats;
    }

    protected void setStats(int[] stats) {
        this.stats = stats;
    }

    public double averageStat() {
        double ret = 0.0;
        for(int s : stats)
            ret += s;
        ret /= stats.length;
        return ret;
    }

    public String asText() {
        StringBuffer t = new StringBuffer();
        t.append("--Skill set--\n");
        for (SkillTypes s :
                values()) {
            t.append(s.toString()).append(": ").append(stats[s.ordinal()]).append("\n");
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
