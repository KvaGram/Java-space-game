package unicus.spacegame.crew;

public class YouthCrewman extends AbleCrewman {
    public YouthCrewman(int keyID, int birthDate, long randomSeed, int[] parents) {
        super(keyID, CrewmanState.youth, birthDate, randomSeed, parents);
    }

    public YouthCrewman(int keyID, int birthDate, CrewSelfID selfID, CrewmanGeneData geneData, int[] skillValues, double base_intelligence, double base_morale) {
        super(keyID, CrewmanState.youth, birthDate, selfID, geneData, skillValues, base_intelligence, base_morale);
    }

    protected YouthCrewman(AbleCrewman crewman) {
        super(crewman, CrewmanState.youth);
    }

    protected YouthCrewman(AbstractCrewman crewman) {
        super(crewman, CrewmanState.youth);
    }
}
