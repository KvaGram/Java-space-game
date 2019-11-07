package unicus.spacegame.structures.starsystem;

import java.util.ArrayList;

import static unicus.spacegame.utilities.Constants.TAUf;

/**
 * The SpaceJunkField is a loose collection of small objects, sharing the same orbit.
 * This can be an asteroidbelt, asteroidfield, debrisfield etc.
 * The objecttype for this is set to junkfield.
 *
 * TODO drop idea of individual asteroids! Complete the implementation of this.
 */
public class SpaceJunkField extends BasicSpaceObject {

    protected ArrayList<BasicSpaceObject> junkChildren;
    protected JunkFieldShape fieldShape;

    /**
     * Create a junk-field to act as the center of a system.
     * pattern restricted to cluster only.
     * @param seed The seed for random calculations.
     * @param types types of objects that can appear in the junk-field (repeated entries increase chance)
     */
    public SpaceJunkField(long seed, ObjectType[] types, int size) {
        super(ObjectType.NONE, ObjectSize.NONE, seed);
        junkChildren = new ArrayList<>();
        fieldShape = JunkFieldShape.CLUSTER;
    }

    public SpaceJunkField(JunkFieldShape pattern, ObjectType[] types, float radianLength, int size, long seed, BasicSpaceObject parent, int orbit, float rot) {
        super(ObjectType.NONE, ObjectSize.NONE, seed, parent, orbit, rot);
        junkChildren = new ArrayList<>();
        this.fieldShape = pattern;

    }
}
enum JunkFieldShape
{
    CLUSTER,
    CLOCKWISE_TAIL,
    COUNTER_CLOCKWISE_TAIL,
    /** The BELT is evently distrub **/
    BELT
}