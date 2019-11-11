package unicus.spacegame.structures.starsystem;

import static unicus.spacegame.utilities.Constants.TAUf;

/**
 * The SpaceJunkField is a loose collection of small objects, sharing the same orbit.
 * This can be an asteroidbelt, asteroidfield, debrisfield etc.
 * The objecttype for this is set to junkfield.
 *
 * TODO drop idea of individual asteroids! Complete the implementation of this.
 */
public class SpaceJunkField extends BasicSpaceObject {

    protected float radianLength;
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
        this.radianLength = TAUf;
    }

    public SpaceJunkField(JunkFieldShape shape, JunkContents[] contents, long seed, float radianLength, ObjectSize size, BasicSpaceObject parent, int orbit, float rot) {
        super(ObjectType.JUNK_FIELD, size, seed, parent, orbit, rot);
        this.fieldShape = shape;
        this.contents = contents;
        this.radianLength = radianLength;
    }
    public JunkFieldShape getFieldShape() {
        return fieldShape;
    }

    public float getRadianLength() {
        return radianLength;
    }
}

