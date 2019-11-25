package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BaseStarSystem;
import unicus.spacegame.structures.starsystem.BasicSpaceObject;
import unicus.spacegame.structures.starsystem.SolLikeSystem;

import javax.swing.*;
import java.awt.*;

public class SystemView extends JComponent {

    private BaseStarSystem system;
    private Boolean showOrbits;
    private SpaceObjectView[] objectViews;
    private Point offset;
    private float scale;

    private Dimension currentSize;

    public SystemView() {
//        this.setBackground(Color.black);
    }


    public void setSystem(BaseStarSystem system) {
        this.system = system;
        showOrbits = true;
        currentSize = this.getSize();
        if(!system.isGenerated())
            system.generatePlanets();

        BasicSpaceObject[] objList = system.getAllObjects();
        objectViews = new SpaceObjectView[objList.length];
        for (int i = 0, objListLength = objList.length; i < objListLength; i++)
            objectViews[i] = SpaceObjectView.CreateViewFor(objList[i]);
        updateObjects();
    }

    /**
     * Updates scale, offset and the rects of the objectviews
     * Also updates
     */
    public void updateObjects()
    {

        int sysRadius = system.getCenter().getGeneratedFullSize();
        offset = new Point(sysRadius, sysRadius);
        int size = Math.min(getWidth(), getHeight());

        if( size >= sysRadius*2)
            scale = 1;
        else {
            scale = (float)size / (float)(sysRadius*2);
        }
        for (SpaceObjectView obj:objectViews) {
            obj.update(scale, offset);
        }
        repaint();
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
        this.setOpaque(true);
        this.setBackground(Color.black);
        super.paintComponent(_g);
        _g.setColor(Color.black);
        _g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g = (Graphics2D)_g;
        if(system == null || !system.isGenerated())
            return;
        if(!currentSize.equals(this.getSize())) {
            currentSize = getSize();
            updateObjects();
            return; //updateObjects calls repaint, so PaintComponent should abort here.
        }


        if(showOrbits){
            for (SpaceObjectView obj:objectViews) {
                obj.paintObjectOrbit(g);
            }
        }
        for (SpaceObjectView obj:objectViews) {
            obj.paintObject(g);
        }
        //show mouse-over rectangles
        for (SpaceObjectView obj:objectViews) {
            g.setColor(Color.yellow);
            g.drawRect(obj.rect.x, obj.rect.y, obj.rect.width, obj.rect.height);
        }

    }
    public static void main(String[] args) {
        BaseStarSystem starSystem = new SolLikeSystem(0);
        SystemView systemView = new SystemView();

        JFrame frame = new JFrame("Test Star system view (version 2)");
        frame.add(systemView);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 720);
        systemView.setSystem(starSystem);
        //systemView.setOpaque(true);
        systemView.setBounds(0,0,900, 700);


        frame.setVisible(true);
    }
}
