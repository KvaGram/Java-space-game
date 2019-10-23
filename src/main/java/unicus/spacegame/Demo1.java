package unicus.spacegame;

import unicus.spacegame.ui.ShipViewUI;

import javax.swing.*;
import java.awt.*;

/**
 * Demo1 is the first attempt at putting together the test
 * components into a working playable proof of concept prototype.
 *
 *
 */
public class Demo1 extends JTabbedPane {

    SwingStarSystem     starSysView;
    Sectormaps          starMapView;
    ShipViewUI          starShipView;

    private final static String starSysTabName  = "Star system view";
    private final static String starMapTabName  = "Star-sector map view";
    private final static String starShipTabName = "Star-ship view";



    public Demo1()
    {
        super(BOTTOM, WRAP_TAB_LAYOUT);

        addTab("test1", new JPanel());
        addTab("test2", new JPanel());
        addTab("test3", new JPanel());
    }

    public static void main(String[] args) {
        Demo1 demo = new Demo1();

        JFrame frame = new JFrame("Ship view proto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 720);
        demo.setOpaque(true);
        demo.setBackground(Color.GRAY);
        frame.add(demo);

        frame.setVisible(true);
    }
}
