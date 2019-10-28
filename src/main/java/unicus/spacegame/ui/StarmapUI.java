package unicus.spacegame.ui;

import unicus.spacegame.Sectormaps;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    JButton toggleGrid;


    public StarmapUI(long seed){
        this.setLayout(new BoxLayout(this, 1));

        //this.setLayout(new BorderLayout());

        map = new Sectormaps(seed);
        toggleGrid = new JButton("Toggle grid");
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
                        //System.out.println("Found star! : x " + d[0] +" y "+ d[1] +" seed "+ d[2]);
                        fireStarTravel(d, st.subSector, st.index);
                        return;
                    }
                }
                System.out.println("No star found. :(");
            }
        };
        map.addMouseListener(ma);
        map.addMouseMotionListener(ma);

        toggleGrid.addActionListener(arg0 -> {
            map.toggleGrid();
        });

        //sets clickable zones for stars
        starTargets = new ArrayList<StarTarget>();
        for(int i = 0; i < map.getNumSubSectors(); i++){
            for(int j = 0; j < map.getNumStarsBySector(i); j++){
                starTargets.add(new StarTarget(i, j));
            }
        }


        this.add(scrollPane);
        this.add(toggleGrid);
        scrollPane.setBounds(0, 50, 200, 200);
        toggleGrid.setBounds(0, 0, 75, 50);

        setPreferredSize(new Dimension(200, 250));
    }

    public Sectormaps getMap(){
        return map;
    }
    //Event system based on tutorial https://www.javaworld.com/article/2077351/events-and-listeners.html
    private EventListenerList StarEventListeners = new EventListenerList();
    public void AddStarListener(StarEventListener listener)    {
        StarEventListeners.add(StarEventListener.class, listener);
    }
    public void ARemoveStarListener(StarEventListener listener)    {
        StarEventListeners.remove(StarEventListener.class, listener);
    }
    protected void fireStarTravel(int[] starData, int subsection, int index){
        Object[] listeners = StarEventListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i<numListeners; i+=2)
        {
            if (listeners[i]==StarEventListener.class)
            {
                // pass the event to the listeners event dispatch method
                ((StarEventListener)listeners[i+1]).onTravelToStar(starData, subsection, index);
            }
        }
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
