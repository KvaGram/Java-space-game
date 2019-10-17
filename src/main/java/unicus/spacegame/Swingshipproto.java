package unicus.spacegame;
import javax.swing.*;
import java.awt.*;

public class Swingshipproto {

    public static void main(String[] args) {
        //this part is mostly copypasted from codejava.net
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Creating the Frame
        JFrame outerframe = new JFrame("Ship view proto");
        outerframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        outerframe.setSize(1200, 800);

        //Creates main content panel
        JLayeredPane mainpanel = new JLayeredPane();

        //Create panel to host buttons
        JPanel buttonLayer = new JPanel();
        //set no background to be drawn
        buttonLayer.setOpaque(false);
        //Set bounds manually (not normally recommended)
        //This sets the panel to use the bottom 100 pixels of the frame screen.
        buttonLayer.setBounds(0, 700, 1200, 100);

        //Create dummy buttons
        JButton b_add = new JButton("Add cargo");
        JButton b_remove = new JButton("Remove cargo");
        JButton b_dummy = new JButton("More buttons to come");
        JButton b_crew = new JButton("Crew for example");

        //Add the buttons to the button layer
        buttonLayer.add(b_add);
        buttonLayer.add(b_remove);
        buttonLayer.add(b_dummy);
        buttonLayer.add(b_crew);

        //Create the ship ui layer
        ShipPanel shipUiLayer = new ShipPanel(); //extends JPanel
        shipUiLayer.setupModules();
        shipUiLayer.setOpaque(false);
        //Set bounds manually (not normally recommended)
        //This sets the panel to use the whole frame screen
        shipUiLayer.setBounds(0, 0, 1200, 800);

        //Create the background layer
        ImageIcon shuttleimage = new ImageIcon("resources/shuttlesideview.png");
        JLabel bgLayer = new JLabel(shuttleimage);
        //Set bounds manually (not normally recommended)
        //This sets the panel to use the whole frame screen
        bgLayer.setBounds(0, 0, 1200, 800);

        //set action listeners for cargo buttons
        b_add.addActionListener(arg0 -> {
            shipUiLayer.hasCargo = true;
            shipUiLayer.repaint();
        });
        b_remove.addActionListener(arg0 -> {
            shipUiLayer.hasCargo = false;
            shipUiLayer.repaint();
        });


        //Adds the panels to the main panel
        mainpanel.add(buttonLayer, 0);
        mainpanel.add(shipUiLayer, 100);
        mainpanel.add(bgLayer, 200);

        //Adds the main panel to the frame.
        outerframe.add(mainpanel);
        outerframe.setVisible(true);
    }
}

class ShipPanel extends JPanel {
    boolean hasCargo = false;
    private int shipImgWidth = 1111;
    private int shipImgHeight = 716;
    private int[][] moduleBounds;

    @Override //?
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //int w = getWidth(); ?
        if (hasCargo) {
            g.setColor(Color.yellow);
            g.fillRect(440,190,120,50);
            g.setColor(Color.black);
            g.drawString("Full cargo bay", 450, 215);
            //System.out.println("in if statement");
        } else {
            g.setColor(Color.red);
            g.fillRect(440,190,120,50);
            g.setColor(Color.black);
            g.drawString("Empty cargo bay", 450, 215);
            //System.out.println("in else statement");
        }
        g.setColor(new Color(150,100,0)); //brown
        g.fillRect(moduleBounds[0][0],moduleBounds[0][1],moduleBounds[0][2],moduleBounds[0][3]); // This is ugly but it works.
        // Officially suggested approach is to overload the method name: g.fillRect(int[] a) { g.fillRect(a[0][0], a[0][1]...) }
        // but that requires me to figure out access to Graphics g and its methods somehow. Maybe lambda?
        g.fillRect(moduleBounds[13][0],moduleBounds[13][1],moduleBounds[13][2],moduleBounds[13][3]);
        g.setColor(new Color(100,250,100)); //light green
        for (int i=1; i<=6; i++){
            g.fillRect(moduleBounds[i][0],moduleBounds[i][1], moduleBounds[i][2],moduleBounds[i][3]);
        }
        g.setColor(new Color(250,100,100)); //light red
        for (int i=7; i<=12; i++){
            g.fillRect(moduleBounds[i][0],moduleBounds[i][1], moduleBounds[i][2],moduleBounds[i][3]);
        }
    }

    public void setupModules() {
        moduleBounds = new int[14][4]; //14 modules initially, each with topleft X, topleft Y, width X, height Y
        int sw = shipImgWidth;
        int sh = shipImgHeight; // for modularity, so transform from image values to percentages can be adjusted
        int mw = (int) (sw * 0.1); //module box width
        int mh = (int) (sh * 0.08); //module box height

        int[] engine_coords ={(int)(sw*0.06), (int)(sh*0.4), mw, mh};
        moduleBounds[0] = engine_coords;
        int[] bridge_coords ={(int)(sw*0.85), (int)(sh*0.4), mw, mh};
        moduleBounds[13] = bridge_coords;
        for (int i=0; i<4; i++) {
            for (int j=0; j<3; j++) {
                int[] temp = {(int)(sw*(0.2+(i*0.15))), (int)(sh*(0.35+(j*0.1))), mw, mh};
                int x = 1 +(3*i)+ j;
                moduleBounds[x] = (temp.clone());
            }
        }
    }
}