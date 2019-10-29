package unicus.spacegame;
//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        //Creating the Frame
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m1_1 = new JMenuItem("Open");
        JMenuItem m1_2 = new JMenuItem("Save as");
        m1.add(m1_1);
        m1.add(m1_2);
        JMenuItem m2_1 = new JMenuItem("About");
        m2.add(m2_1);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Hull Integrity: 100%");
        JTextField tf = new JTextField(10); // is 10 characters wide
        JButton b_mine = new JButton("Mine");
        JButton b_reset = new JButton("Reset");
        JButton b_raid = new JButton("Raid");
        JButton b_travel = new JButton("Travel");
        JButton b_image = new JButton("Image");
        JButton b_starmap = new JButton("Stars");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(b_mine);
        panel.add(b_reset);
        panel.add(b_raid);
        panel.add(b_travel);
        panel.add(b_image);
        panel.add(b_starmap);

        // Text Area at the Center
        JTextArea ta = new JTextArea("Hello spaceworld!\n");

        //Associating actions with the buttons
        b_mine.addActionListener(arg0 -> {
            ta.append("You mined 100 ore.\n");
        });
        b_reset.addActionListener(arg0 -> {
            ta.append("Restoring...\n");
            int hullint = 100;
            label.setText("Hull Integrity: "+hullint+"%");
        });
        b_raid.addActionListener(arg0 -> {
            ta.append("You got into a fight and your ship is damaged.\n");
            int hullint = 50;
            label.setText("Hull Integrity: "+hullint+"%");
        });
        b_travel.addActionListener(arg0 -> {
            ta.append("This should take you to another star system.\n");
            ta.append("But it's not implemented yet.\n");
        });
        b_image.addActionListener(arg0 -> {
            ImageIcon spaceimage = new ImageIcon(Main.class.getResource("ui/spaceshipicon.png"));
            JLabel lbl = new JLabel(spaceimage);
            JOptionPane.showMessageDialog(null, lbl, "ImageDialog",
                    JOptionPane.PLAIN_MESSAGE, null);
        });
        //The very complicated starmap generator
        b_starmap.addActionListener(arg0 -> {
            JFrame jstar = new JFrame("Starmap");
            jstar.setSize(300,300);
            jstar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //New JPanel details go here, then jstar.add(it)
        });

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }
}
