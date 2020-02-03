package unicus.spacegame.crew;

public class AdultCrewman extends AbleCrewman {
    public AdultCrewman(int keyID, int birthDate, long randomSeed, int[] parents) {
        super(keyID, CrewmanState.adult, birthDate, randomSeed, parents);
    }

    public AdultCrewman(int keyID, int birthDate, CrewSelfID selfID, CrewmanGeneData geneData, int[] skillValues, double base_intelligence, double base_morale) {
        super(keyID, CrewmanState.adult, birthDate, selfID, geneData, skillValues, base_intelligence, base_morale);
    }

    protected AdultCrewman(AbleCrewman crewman) {
        super(crewman, CrewmanState.adult);
    }

    protected AdultCrewman(AbstractCrewman crewman) {
        super(crewman, CrewmanState.adult);
    }
}
