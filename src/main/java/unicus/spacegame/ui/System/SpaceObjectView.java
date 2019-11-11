package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;
import unicus.spacegame.structures.starsystem.SpaceJunkField;

import java.awt.*;

public class SpaceObjectView {
    private BasicSpaceObject object;
    protected Rectangle rect;
    protected Point location; //location in panel(screen)-space
    protected Point pLocation; //parent location in panel(screen)-space
    private int orbitRad; //radius distance from parent in panel(screen)-space
    protected Color orbitColor;
    protected Color planetTint;

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

    public static SpaceObjectView CreateViewFor(BasicSpaceObject object) {
        try {
            switch (object.getType()) {
                case JUNK_FIELD:
                    return new JunkFieldView((SpaceJunkField)object);
                default:
                    return new SpaceObjectView(object);
            }

        } catch (ClassCastException err) {
            System.out.println(err);
            return new SpaceObjectView(object);
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
        //TODO: undo debug change
        //g.fillOval(rect.x, rect.y, rect.width, rect.height);
        g.drawOval(rect.x, rect.y, rect.width, rect.height);
    }

    public void paintObjectOrbit(Graphics2D g) {
        g.setColor(orbitColor);
        g.drawOval(pLocation.x-orbitRad, pLocation.y-orbitRad, orbitRad*2, orbitRad*2);

    }
}