package unicus.spacegame;

import unicus.spacegame.spaceship.Spaceship;
import unicus.spacegame.ui.ShipViewUI;
import unicus.spacegame.ui.StarmapUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Demo1 is the first attempt at putting together the test
 * components into a working playable proof of concept prototype.
 *
 *
 */
public class Demo1 extends JPanel {

    JTabbedPane gamePane;
    JPanel setupPanel;

    SwingStarSystem     starSysView;
    StarmapUI           starMapView;
    ShipViewUI          starShipView;

    private final static String starSysTabName  = "Star system view";
    private final static String starMapTabName  = "Star-sector map view";
    private final static String starShipTabName = "Star-ship view";





    public Demo1()
    {
        super();
        gamePane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.WRAP_TAB_LAYOUT);
        setupPanel = new JPanel();
        setupPanel.setBackground(Color.white);
        JTextField seedBox = new JTextField("0",50);
        JButton startButton = new JButton("Start Game");

        this.add(setupPanel);
        setupPanel.add(seedBox);
        setupPanel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seed = seedBox.getText();
                long seedNum = 0;

                //If the seed input is a number, parse the number directly.
                //If it cannot be parsed, then the string are converted to a long by adding the value of its chars.
                try{
                    seedNum = Long.parseLong(seed);
                } catch (NumberFormatException err){
                    seedNum = Long.MIN_VALUE;
                    for(char c : seed.toCharArray())
                        seedNum += c;
                }
                //start the game using the parsed or generated seed value.
                startGame(new Random(seedNum));
            }
        });

    }

    /**
     *
     * @param r
     */
    public void startGame(Random r)
    {
        Random shipRand = new Random(r.nextLong());
        long starRandom = r.nextLong();

        Spaceship ship = Spaceship.GenerateStart1(shipRand, 4, 8, 0.3f, 0.8f);

        starSysView  = new SwingStarSystem();
        starMapView  = new StarmapUI(starRandom);
        starShipView = new ShipViewUI(ship);

        gamePane.addTab(starSysTabName, starSysView);
        gamePane.addTab(starMapTabName, starMapView);
        gamePane.addTab(starShipTabName, starShipView);

        this.remove(setupPanel);
        this.add(gamePane);
        gamePane.setBounds(0,0,getWidth(), getHeight());

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
