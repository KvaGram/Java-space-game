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

    private final float radianlength;
    protected JunkFieldShape fieldShape;
    protected JunkContents[] contents;

    /**
     * Create a junk-field to act as the center of a system.
     * pattern restricted to cluster only.
     * @param seed The seed for random calculations.
     * @param contents types of junk objects that can appear in the junk-field (repeated entries increase chance)
     */
    public SpaceJunkField(long seed, JunkContents[] contents, ObjectSize size) {
        super(ObjectType.JUNK_FIELD, size, seed);
        fieldShape = JunkFieldShape.CLUSTER;
        this.contents = contents;
        this.radianlength = TAUf;
    }

    public SpaceJunkField(JunkFieldShape shape, JunkContents[] contents, long seed, float radianLength, ObjectSize size, BasicSpaceObject parent, int orbit, float rot) {
        super(ObjectType.JUNK_FIELD, size, seed, parent, orbit, rot);
        this.fieldShape = shape;
        this.contents = contents;
        this.radianlength = radianLength;
    }
}
enum JunkFieldShape
{
    /** (DEFAULT) The CLUSTER is a junk-field centered around one place, like a squished ball.**/
    CLUSTER,
    /**The CLOCKWISE_TAIL is a junk-field that is very dense on one side, then thins out in a tail in one direction.**/
    CLOCKWISE_TAIL,
    /**The COUNTER_CLOCKWISE_TAIL is the CLOCKWISE_TAIL mirrored in direction.**/
    COUNTER_CLOCKWISE_TAIL,
    /** The BELT is a junk-field wrapping all around an orbit as a ring or belt. **/
    BELT
}
enum JunkContents
{
    /**(DEFAULT) ASTEROIDS contain various metals and rocks that can be mined**/
    ASTEROIDS,
    /**ICETEROIDS, or ice-asteroids, is made of mostly ice. Good source of water.**/
    ICETEROIDS,
    /**BASIC_DEBRIS can se searched for scrap metal and basic components**/
    BASIC_DEBRIS,
    /**ADVANCED_DEBRIS can be searched for a rare find of advanced Shineium-based components**/
    ADVANCED_DEBRIS,
    /**A SATELLITE_SWARM is technically not junk. Not unless you want it to be. The owners might get mad if you take any though.**/
    SATELLITE_SWARM,

}