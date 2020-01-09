package unicus.spacegame.crew;

public class HousingAssignment {
    private final int housingID;
    private final int crewID;

    public HousingAssignment(int housingID, int crewID) {

        this.housingID = housingID;
        this.crewID = crewID;
    }

    public int getHousingID() {
        return housingID;
    }

    public int getCrewID() {
        return crewID;
    }
}
