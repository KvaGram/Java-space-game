package unicus.spacegame.crew;

public class SeniorCrewman extends AbleCrewman {

    public SeniorCrewman(int keyID, int birthDate, long randomSeed, int[] parents) {
        super(keyID, CrewmanState.senior, birthDate, randomSeed, parents);
    }

    public SeniorCrewman(int keyID, int birthDate, CrewSelfID selfID, CrewmanGeneData geneData, int[] skillValues, double base_intelligence, double base_morale) {
        super(keyID, CrewmanState.senior, birthDate, selfID, geneData, skillValues, base_intelligence, base_morale);
    }

    protected SeniorCrewman(AbleCrewman crewman) {
        super(crewman, CrewmanState.senior);
    }

    protected SeniorCrewman(AbstractCrewman crewman) {
        super(crewman, CrewmanState.senior);
    }
}
