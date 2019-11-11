package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;

import java.awt.*;

public class SpaceObjectView {
    private BasicSpaceObject object;
    private Rectangle rect;
    private Point location; //location in panel(screen)-space
    private Point pLocation; //parent location in panel(screen)-space
    private int orbitRad; //radius distance from parent in panel(screen)-space
    private Color orbitColor;
    private Color planetTint;

    SpaceObjectView(BasicSpaceObject object) {
        this.object = object;
        this.rect = new Rectangle();
        location = new Point();
        pLocation = new Point();
        switch(object.getType()){
            case LIFE_PLANET:
                orbitColor = new Color(0,70,0);
                planetTint = new Color(0,170,0);
                break;
            case JUNK_FIELD:
                orbitColor = new Color(0,0,200);
                planetTint = new Color(50, 50,200);

            default:
                orbitColor = Color.gray;
                planetTint = Color.red;
        }


    }

    public boolean contains(Point p) {
        return rect.contains(p);
    }

    public void update(float scale, Point offset) {
        int localSize = (int) (object.getLocalSize() * scale);
        location.x = (int)((offset.x + object.getGeneratedLocation().x)*scale);
        location.y = (int)((offset.y + object.getGeneratedLocation().y)*scale);

        pLocation.x = (int)((offset.x + object.getParentLocation().x)*scale);
        pLocation.y = (int)((offset.y + object.getParentLocation().y)*scale);

        rect.x = location.x - localSize/2;
        rect.y = location.y - localSize/2;
        rect.height = localSize;
        rect.width  = localSize;

        if (object.isRoot())
            orbitRad = 0;
        else {
            orbitRad = (int)(object.getOrbitDistance() * scale);
        }
    }
    public void paintObject(Graphics2D g) {

        g.setColor(planetTint);
        g.fillOval(rect.x, rect.y, rect.width, rect.height);
    }

    public void paintObjectOrbit(Graphics2D g) {
        //todo: make softer code for ignoring orbits for some objects. (blacklist maybe?)
//        if(object.getType() == ObjectType.asteroid)
//            return;
        g.setColor(orbitColor);
        g.drawOval(pLocation.x-orbitRad, pLocation.y-orbitRad, orbitRad*2, orbitRad*2);

    }
}


/*
    public void drawOrbits(Graphics2D g, BasicSpaceObject object) {
        //TODO: scale and offset to fit component
        Point loc = object.getGeneratedLocation();

        int x = loc.x + offset.x;
        int y = loc.y + offset.y;
        x = (int) (x * scale);
        y = (int) (y * scale);

        for (int i = 1; i < object.getHighestOrbit(); i++) {
            if (object.getLargestChildByOrbit(i).getType() == ObjectType.asteroid)
                continue;
            int radius = (int) (object.getOrbitDistance(i) * scale );
            g.drawOval(x-radius, y-radius, radius*2, radius*2);
        }
        for (BasicSpaceObject child : object.getChildren()) {
            //drawOrbits(g, child);
        }
    }
 */