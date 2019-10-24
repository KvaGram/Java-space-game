package unicus.spacegame.ui;

import unicus.spacegame.Main;
import unicus.spacegame.Sectormaps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * StarmapUI acts as a wrapper for Sectormap.
 * The following features are added here:
 * srollbars
 * tooltip box (todo)
 * clickable stars (todo)
 * location of spaceship (todo, temporary)
 *      (spaceship spawns at first star)
 */
public class StarmapUI extends JPanel {
    Sectormaps map;
    JScrollPane scrollPane;
    Point mousePoint;
    ArrayList<StarTarget> starTargets;


    public StarmapUI(long seed){
        this.setLayout(new BoxLayout(this, 0));
        map = new Sectormaps(seed);

        scrollPane = new JScrollPane(map, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mousePoint = new Point();



        MouseAdapter ma = new MouseAdapter(){
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePoint.x = e.getX();
                mousePoint.y = e.getY();
                //todo: check if on a star, if so, show a tooltip.
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                for (StarTarget st : starTargets){
                    if(st.contains(mousePoint)) {
                        int[] d = map.getStarData(st.subSector, st.index);
                        System.out.println("Found star! : x " + d[0] +" y "+ d[1] +" seed "+ d[2]);
                        return;
                    }
                }
                System.out.println("No star found. :(");
            }
        };

        //sets clickable zones for stars
        starTargets = new ArrayList<StarTarget>();
        for(int i = 0; i < map.getNumSubSectors(); i++){
            for(int j = 0; j < map.getNumStarsBySector(i); j++){
                starTargets.add(new StarTarget(i, j));
            }
        }

        map.addMouseListener(ma);
        map.addMouseMotionListener(ma);

        this.add(scrollPane);
        scrollPane.getViewport().setSize(3000, 3000);
    }


    class StarTarget{
        Rectangle rect;
        int subSector;
        int index;

        public StarTarget(int subSector, int index) {
            this.subSector = subSector;
            this.index = index;

            Point p = map.getStarPoint(subSector, index);
            int s = map.getStarsize();
            rect = new Rectangle(p.x-s/2, p.y-s/2, s, s);
        }
        public boolean contains(Point p){
            return rect.contains(p);
        }
        public long getSeed(){
            return map.getStarSeed(subSector, index);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("starmap UI test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        StarmapUI starmapUI = new StarmapUI(0);
        frame.add(starmapUI);
        starmapUI.setBounds(0,0,1400, 600);

        frame.setVisible(true);
    }
}
