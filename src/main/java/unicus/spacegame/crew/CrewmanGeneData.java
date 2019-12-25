package unicus.spacegame.crew;

/**
 * The CrewmanGeneData contains genetic heritage and traits.
 * NOTE: This class will be kept simple for now, but the structure show allow for future complex systems.
 */
public class CrewmanGeneData {
    /**
     * Genetic/biological parents.
     * Used to calculate genetic traits, including inbreeding problems.
     * Can be empty, normally has two, possible to have more, if some game mechanics require that.
     * (TODO: For familial/social parentage, see social connections)
     */
    int[] parentIDs;

    public CrewmanGeneData(int[] parentIDs) {

        this.parentIDs = parentIDs;
        //todo: add trait-feature that simulates consequences of inbreeding.
    }
}
