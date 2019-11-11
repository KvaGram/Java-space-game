package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;
import unicus.spacegame.structures.starsystem.SpaceJunkField;

import java.awt.*;

public class JunkFieldView extends SpaceObjectView {
    //hides super's object property
    //needs to be set separately in constructor
    private SpaceJunkField object;
    JunkFieldView(SpaceJunkField object) {
        super(object);
        this.object = object;
    }

    @Override
    public void update(float scale, Point offset) {
        super.update(scale, offset);
    }

    @Override
    public void paintObject(Graphics2D g) {
        super.paintObject(g);
    }
}
