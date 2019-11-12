package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;
import unicus.spacegame.structures.starsystem.JunkFieldShape;
import unicus.spacegame.structures.starsystem.SpaceJunkField;

import java.awt.*;
import java.util.ArrayList;

import static unicus.spacegame.utilities.Constants.TAUf;

public class JunkFieldView extends SpaceObjectView {
    //hides super's object property
    //needs to be set separately in constructor
    private SpaceJunkField object;
    private float rotationMax = 0;
    private float rotationMin = 0;
    private Stroke drawStroke;
    JunkFieldView(SpaceJunkField object) {
        super(object);
        this.object = object;
        drawStroke = new BasicStroke(1);
    }

    @Override
    public void update(float scale, Point offset) {
        super.update(scale, offset);

        switch (object.getFieldShape()) {
            case BELT:
                rotationMax = TAUf;
                rotationMin = 0;
                break;
            case CLUSTER:
                rotationMax = object.getRadianLength()/2 + object.getOrbitRotation();
                rotationMin = -object.getRadianLength()/2 + object.getOrbitRotation();
                break;
            case CLOCKWISE_TAIL:
                rotationMax = object.getRadianLength() + object.getOrbitRotation();
                rotationMin = object.getOrbitRotation();
                break;
            case COUNTER_CLOCKWISE_TAIL:
                rotationMax = object.getOrbitRotation();
                rotationMin = -object.getRadianLength() + object.getOrbitRotation();
                break;
        }
        //wraparound rotation radian range
        while (rotationMax > TAUf)
            rotationMax -= TAUf;
        while (rotationMin < 0)
            rotationMin += TAUf;

        double fullDistanceSize = object.getOrbitDistance() + object.getLocalSize();

        int highestX = (int)(Math.cos(rotationMax) * fullDistanceSize);
        int lowestX = highestX;
        int highestY = (int)(Math.sin(rotationMax) * fullDistanceSize);
        int lowestY = highestY;


        boolean wraps0 = rotationMin > rotationMax;
        if(wraps0) //if the field arcs over the 0 radian
            highestX = (int) fullDistanceSize;
        if(rotationMax > (1f/4)*TAUf && (wraps0 || rotationMin < (1f/4)*TAUf) )
            highestY = (int) fullDistanceSize;
        if(rotationMax > (2f/4)*TAUf && (wraps0 || rotationMin < (1f/4)*TAUf) )
            lowestX = (int) -fullDistanceSize;
        if(rotationMax > (3f/4)*TAUf && (wraps0 || rotationMin < (1f/4)*TAUf) )
            lowestY = (int) -fullDistanceSize;

        rect.width = (int) ((highestX - lowestX)*scale);
        rect.height = (int) ((highestY - lowestY)*scale);
        rect.x = (int)((lowestX + offset.x)*scale);
        rect.y = (int)((lowestY + offset.y)*scale);

        int strokeWidth = (int) Math.max(object.getLocalSize() * scale, 6);
        drawStroke = new BasicStroke(strokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10);
    }

    @Override
    public boolean contains(Point p) {
        return false;
        //return super.contains(p);

        /*
        note:

        1: check if within rect.
        if root object, return true
        2: check if distance to parent equals field's distance plus/minus size (scaled)
        if belt/ring, return true
        3: check if area moused over is in the field (not sure how yet)



        //If the mouse/marker is not over the rectangle, return false.
        if(!super.contains(p))
            return false;
        //else if the object is a root object, return true
        if(object.isRoot())
            return true;
        //otherwise, calculate if the mouse is over the arc/field

        if(object.isRoot())
            return super.contains(p);

        return false;

         */
    }

    @Override
    public void paintObject(Graphics2D g) {
        //super.paintObject(g);
        g.setStroke(drawStroke);
        //g.setColor(planetTint);
        g.setColor(Color.white);
        if(object.getFieldShape() == JunkFieldShape.BELT) {
            g.drawOval(pLocation.x-orbitRad, pLocation.y-orbitRad, orbitRad*2, orbitRad*2);
        } else {
            int startAngle = (int) Math.toDegrees(rotationMin);
            int arcAngle   = (int) Math.toDegrees(Math.abs(rotationMax - rotationMin));
            g.drawArc(pLocation.x-orbitRad, pLocation.y-orbitRad, orbitRad*2, orbitRad*2, startAngle, arcAngle);
        }
        g.setStroke(new BasicStroke(1));
    }

    @Override
    public void paintObjectOrbit(Graphics2D g) {
        //g.setColor(Color.PINK);
        g.setColor(orbitColor);
        g.drawOval(pLocation.x-orbitRad, pLocation.y-orbitRad, orbitRad*2, orbitRad*2);
    }
}
