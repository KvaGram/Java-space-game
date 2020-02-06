package unicus.spacegame.crew;


import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Random;
import static unicus.spacegame.crew.SkillTypes.*;

class SkillSet {
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
        if(average < 0 || average > 100 || tolerance < 0 || fudge < 0 || fudge > 100 || boon < 0 || boon > 100)
            throw new IllegalArgumentException();

        stats = new int[types.length];

        int targetMinimalTotalPoints = (average - tolerance) * types.length;
        int targetMaximumTotalPoints = (average + tolerance) * types.length;

        if (targetMinimalTotalPoints < 0)
            targetMinimalTotalPoints = 0;
        if(targetMaximumTotalPoints > 100)
            targetMaximumTotalPoints = 100;

        for(int s = 0; s < types.length; s++) {
            if(ArrayUtils.contains(bias, SkillTypes.GetTypeByIndex(s))) {
                stats[s] = average + r.nextInt(boon);
            }
            stats[s] = average - fudge + r.nextInt(fudge * 2);
        }

        int toAdjust = 0;
        int total = Arrays.stream(stats).sum();
        if(total > targetMaximumTotalPoints)
            toAdjust = targetMaximumTotalPoints - total;
        else if (total < targetMinimalTotalPoints)
            toAdjust = targetMinimalTotalPoints - total;

        for(int s = 0; s < types.length; s++) {
            stats[s] += toAdjust;
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
        SkillSet set = new SkillSet(seed, 50, 10, 10, 40, socialization, weaponry);
        System.out.println(set.asText());
    }
}
