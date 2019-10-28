package unicus.spacegame;

import unicus.spacegame.spaceship.Spaceship;
import unicus.spacegame.ui.ShipViewUI;
import unicus.spacegame.ui.StarEventListener;
import unicus.spacegame.ui.StarmapUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    GameData gameData;


    public Demo1()
    {
        super(new CardLayout());
        setBorder(new EmptyBorder(4,4,4,4));
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
        //gamePane.setLayout(new GridBagLayout());


        Random shipRand = new Random(r.nextLong());
        long starRandom = r.nextLong();

        Spaceship ship = Spaceship.GenerateStart1(shipRand, 4, 8, 0.3f, 0.8f);

        starSysView  = new SwingStarSystem();
        starMapView  = new StarmapUI(starRandom);
        starShipView = new ShipViewUI(ship);

        gamePane.addTab(starSysTabName, starSysView);
        gamePane.addTab(starMapTabName, starMapView);
        gamePane.addTab(starShipTabName, starShipView);

        StarEventListener starEventListener = new StarEventListener() {
            @Override
            public void onTravelToStar(int[] starData, int subsection, int index) {
                //todo: starSysView.setSystem(starData);

                //todo: validate that player can travel to this star.
                System.out.println(String.format("Traveling to system subsec %1s index %2s with seed %3s, located at x%4s y%5s", subsection, index, starData[2], starData[0], starData[1]));

                gameData.currentSubSec = subsection;
                gameData.currentIndex = index;
                starMapView.getMap().setShipLocation(gameData.getCurrentLocation());
            }
        };
        starMapView.AddStarListener(starEventListener);

        gameData = new GameData();
        gameData.currentIndex = 0;
        gameData.currentSubSec = 0;
        starMapView.getMap().setShipLocation(gameData.getCurrentLocation());


        this.add(gamePane);//, 0);
        this.remove(setupPanel);

    }
    class GameData {
        //todo: replace with StarData currentStar
        public int currentSubSec;
        public int currentIndex;

        public Point getCurrentLocation(){
            return starMapView.getMap().getStarPoint(currentSubSec, currentIndex);
        }



    }


    public static void main(String[] args) {
        Demo1 demo = new Demo1();

        JFrame frame = new JFrame("Ship view proto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);
        demo.setOpaque(true);
        demo.setBackground(Color.GRAY);
        frame.add(demo);

        frame.setVisible(true);
    }
}
