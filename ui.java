//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
//I don't know why I need to import these next two separately
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class gui {
    public static void main(String args[]) {

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
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(b_mine);
        panel.add(b_reset);
		panel.add(b_raid);
		panel.add(b_travel);
		
        // Text Area at the Center
        JTextArea ta = new JTextArea("Hello spaceworld!\n");
		
		//Associating actions with the buttons
		b_mine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ta.append("You mined 100 ore.\n");
			}
		});
		b_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ta.append("Restoring...\n");
				int hullint = 100;
				label.setText("Hull Integrity: "+hullint+"%");
			}
		});
		b_raid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ta.append("You got into a fight and your ship is damaged.\n");
				int hullint = 50;
				label.setText("Hull Integrity: "+hullint+"%");
			}
		});
		b_travel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ta.append("This should take you to another star system.\n");
				ta.append("But it's not implemented yet.\n");
			}
		});

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }
}