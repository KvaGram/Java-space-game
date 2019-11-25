package unicus.spacegame.structures.starsystem;

public enum JunkFieldShape
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
