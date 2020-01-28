package unicus.spacegame.spaceship;

import java.awt.*;

/**
 * A list of section-types
 * For test, each type has a color
 *
 *
 */
public enum SectionType {
    None(new Color(0,0,0)), Normal(new Color(177,180,121)), Wheel(new Color(4,218,0)), GravityPlated(new Color(0,31,175)), Special(new Color(0,0,0));

    private final Color color;

    SectionType(Color color) {

        this.color = color;
    }

    public SectionType[] getBuildable(){
        return new SectionType[]{Normal, Wheel, GravityPlated};
    }

    public Color getColor() {
        return color;
    }
};
