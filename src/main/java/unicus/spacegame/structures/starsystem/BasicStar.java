package unicus.spacegame.structures.starsystem;

/**
 * A basic star at the center of the star system.
 */
public class BasicStar extends AbstractStar {

    public BasicStar(int size) {
        super(size);
    }

    @Override
    public String getTooltip() {
        return null;
    }
}
