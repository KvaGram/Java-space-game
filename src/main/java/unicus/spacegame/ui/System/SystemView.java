package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BaseStarSystem;
import unicus.spacegame.structures.starsystem.BasicSpaceObject;

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;

public class SystemView extends JComponent {

    private BaseStarSystem system;
    private Boolean showOrbits;
    private SpaceObjectView[] objectViews;
    private Point offset;
    private float scale;



    public void setSystem(BaseStarSystem system) {
        this.system = system;
        showOrbits = true;

        BasicSpaceObject[] objList = system.getAllObjects();
        objectViews = new SpaceObjectView[objList.length];
        for (int i = 0, objListLength = objList.length; i < objListLength; i++)
            objectViews[i] = new SpaceObjectView(objList[i]);
    }

    /**
     * Updates scale, offset and the rects of the objectviews
     * Also updates
     */
    public void update()
    {
        int sysRadius = system.getCenter().getGeneratedFullSize();
        offset = new Point(sysRadius, sysRadius);
        int size = Math.min(getWidth(), getHeight());

        if( size >= sysRadius*2)
            scale = 1;
        else {
            float scale = (float)size / (float)(sysRadius*2);
        }
        for (SpaceObjectView obj:objectViews) {
            obj.updateRect(scale, offset);
        }

    }

    public void setShowOrbits(Boolean val){
        showOrbits = val;
        repaint();
    }
    public Boolean getShowOrbits(){
        return showOrbits;
    }

    @Override
    public void paintComponent(Graphics _g)
    {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D)_g;

        if(showOrbits){
            g.setColor(Color.LIGHT_GRAY);
            drawOrbits(g, system.getCenter());
        }
        for (SpaceObjectView obj:objectViews) {
            obj.paintObject(g);
        }
    }
    public void drawOrbits(Graphics2D g, BasicSpaceObject object) {
        //TODO: scale and offset to fit component
        Point p = object.getGeneratedLocation();
        for (int i = 1; i < object.getHighestOrbit(); i++) {
            int radius = object.getOrbitDistance(i);
            g.drawOval(p.x-radius, p.y-radius, radius*2, radius*2);
        }
        for (BasicSpaceObject child : object.getChildren()) {
            drawOrbits(g, child);
        }
    }

}
