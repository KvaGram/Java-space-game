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
    @Override //?
    public void paint(Graphics g) {
        super.paint(g);
        if (hasCargo) {
            g.setColor(Color.yellow);
            g.fillRect(440,190,120,50);
            g.setColor(Color.black);
            g.drawString("Full cargo bay", 450, 215);
            System.out.println("in if statement");
        } else {
            g.setColor(Color.red);
            g.fillRect(440,190,120,50);
            g.setColor(Color.black);
            g.drawString("Empty cargo bay", 450, 215);
            System.out.println("in else statement");
        }
    }
        public void drawEmptyCargo() {
        // (Graphics g)
        //super.paintComponent(g);
    }
}