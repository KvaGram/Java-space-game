package unicus.spacegame.structures.starsystem;

/**
 * What type of object the spaceObject is.
 * Used with ObjectSize to determine how the object is rendered.
 */
public enum ObjectType {
    /**A NONE object is not a thing in itself, but may support other objects. An example would be the sender of a system with co-orbiting stars.*/
    NONE,
    /**The JUNK_FIELD is an area dense with space-junk
     * @see JunkContents */
    JUNK_FIELD,
    /**An artificial satellite.*/
    SATELLITE,
    /**A space station. May have a spaceport.**/
    SPACE_STATION,
    /**An asteroid outside an asteroid field. Could have been towed for mining.**/
    ROUGE_ASTEROID,
    /**A typical lifeless planet**/
    PLANET,
    /**A nice planet with life on it. Not necessarily habitable by humans.**/
    LIFE_PLANET,
    /**A planet made mostly out of nothing but hot air. dense hot air.**/
    GAS_PLANET,
    /**Nature's nuclear fusion reactor. Tends to be quite shiney**/
    STAR
}
