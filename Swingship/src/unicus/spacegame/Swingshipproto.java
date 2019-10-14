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
        JFrame outerframe = new JFrame("Chat Frame");
        outerframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        outerframe.setSize(1200, 800);

        //Creating the buttons row
        JPanel bottompanel = new JPanel();
        JButton b_add = new JButton("Add cargo");
        JButton b_remove = new JButton("Remove cargo");
        JButton b_dummy = new JButton("More buttons to come");
        JButton b_crew = new JButton("Crew for example");
        bottompanel.add(b_add);
        bottompanel.add(b_remove);
        bottompanel.add(b_dummy);
        bottompanel.add(b_crew);

        //Creating the image area
        ShipPanel upperpanel = new ShipPanel(); //extends JPanel
        upperpanel.setupModules();
        ImageIcon shuttleimage = new ImageIcon("shuttlesideview.png");
        upperpanel.add(new JLabel(shuttleimage));

        //Button actions
        b_add.addActionListener(arg0 -> {
            upperpanel.hasCargo = true;
            upperpanel.repaint();
        });
        b_remove.addActionListener(arg0 -> {
            upperpanel.hasCargo = false;
            upperpanel.repaint();
        });

        //Add components in frame and show it.
        outerframe.getContentPane().add("South", bottompanel);
        outerframe.getContentPane().add("North", upperpanel);
        outerframe.setVisible(true);
    }
}

class ShipPanel extends JPanel {
    boolean hasCargo = false;
    private int shipImgWidth = 1111;
    private int shipImgHeight = 716;
    private int[][] moduleBounds;

    @Override //?
    public void paint(Graphics g) {
        super.paint(g);
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
        int[] bridge_coords ={(int)(sw*0.9), (int)(sh*0.4), mw, mh};
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