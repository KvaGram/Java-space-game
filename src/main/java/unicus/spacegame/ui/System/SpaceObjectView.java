package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;

import java.awt.*;

public class SpaceObjectView {
    private BasicSpaceObject object;
    public Rectangle rect;

    SpaceObjectView(BasicSpaceObject object) {
        this.object = object;
        this.rect = new Rectangle();
    }

    public boolean contains(Point p) {
        return rect.contains(p);
    }

    public void updateRect(float scale, Point offset) {
        rect.x = (int)((offset.x + object.getGeneratedLocation().x)*scale);
        rect.y = (int)((offset.y + object.getGeneratedLocation().y)*scale);
        rect.height = (int) (object.getLocalSize() * scale);
        rect.width  = (int) (object.getLocalSize() * scale);
    }
    public void paintObject(Graphics2D g) {

        g.setColor(Color.yellow);
        g.drawOval(rect.x, rect.y, rect.width, rect.height);
    }

}
