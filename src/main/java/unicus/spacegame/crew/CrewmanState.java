package unicus.spacegame.crew;

/**
 * The CrewmanState defines what kind of crewman this is.
 * Typically there is one CrewState value per implemented AbstractCrewman class.
 * Used internally to determine the type of crewman it is, and to ensure safe casting.
 *
 * NOTE: To change CrewState you must use a conversion function to replace the object.
 *  - when converting a Crewman object, the old object MUST be deleted afterwards
 */
public enum CrewmanState {
    infant, toddler, child, youth, adult, senior, corpse, memorial
}
