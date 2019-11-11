package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;
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
            case CLUSTER:
                rotationMax = object.getRadianLength()/2 + object.getOrbitRotation();
                rotationMin = -object.getRadianLength()/2 + object.getOrbitRotation();
            case CLOCKWISE_TAIL:
                rotationMax = object.getRadianLength() + object.getOrbitRotation();
                rotationMin = object.getOrbitRotation();
            case COUNTER_CLOCKWISE_TAIL:
                rotationMax = object.getOrbitRotation();
                rotationMin = -object.getRadianLength() + object.getOrbitRotation();
        }
        //wraparound rotation radian range
        while (rotationMax > TAUf)
            rotationMax -= TAUf;
        while (rotationMin < 0)
            rotationMin += TAUf;

        double fullDistanceSizeScaled = object.getOrbitDistance() + object.getLocalSize() * scale;

        int highestX = (int)(Math.cos(rotationMax) * fullDistanceSizeScaled);
        int lowestX = highestX;
        int highestY = (int)(Math.sin(rotationMax) * fullDistanceSizeScaled);
        int lowestY = highestY;


        boolean wraps0 = rotationMin > rotationMax;
        if(wraps0) //if the field arcs over the 0 radian
            highestX = (int) fullDistanceSizeScaled;
        if(rotationMax > (1/4)*TAUf && (wraps0 || rotationMin < (1/4)*TAUf) )
            highestY = (int) fullDistanceSizeScaled;
        if(rotationMax > (2/4)*TAUf && (wraps0 || rotationMin < (1/4)*TAUf) )
            lowestX = (int) fullDistanceSizeScaled;
        if(rotationMax > (3/4)*TAUf && (wraps0 || rotationMin < (1/4)*TAUf) )
            lowestY = (int) fullDistanceSizeScaled;

        rect.x = lowestX;
        rect.width = highestX - lowestX;
        rect.y = lowestY;
        rect.width = highestY - lowestY;

        int strokeWidth = (int) Math.max(object.getLocalSize() * scale, 10);
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

        int startAngle = (int) Math.toDegrees(rotationMin);
        int arcAngle   = (int) Math.toDegrees(Math.abs(rotationMax - rotationMin));
        g.setStroke(drawStroke);
        g.setColor(planetTint);
        g.drawArc(rect.x, rect.y, rect.width, rect.height, startAngle, arcAngle);
        g.setStroke(new BasicStroke(1));
    }
}
