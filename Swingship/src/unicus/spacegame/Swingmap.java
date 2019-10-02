//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Swingmap extends JPanel {
    public static void main(String args[]) {

        //Creating the Frame
        JFrame frame = new JFrame("Map Frame");
        frame.add(new Swingmap());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 450);

        JPanel panel = new JPanel(); // the panel is not visible in output

        //Adding Components to the frame.
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        Random rand = new Random();
        //Since the randomization is in the paint method, it will be re-rolled whenever you resize window.
        int starsize = 7;
        int s1_x = 20 + rand.nextInt(400);
        int s1_y = 20 + rand.nextInt(200);
        int s2_x = 200 + rand.nextInt(400);
        int s2_y = 200 + rand.nextInt(200);
        int s3_x = 10 + rand.nextInt(580);
        int s3_y = 10 + rand.nextInt(380);
        int s4_x = 10 + rand.nextInt(580);
        int s4_y = 10 + rand.nextInt(380);
        g.setColor(Color.black);
        g.fillRect(0,0,600,400);
        g.setColor(Color.white);
        g.drawLine(s1_x,s1_y,s2_x,s2_y);
        g.drawLine(s1_x,s1_y,s3_x,s3_y);
        g.drawLine(s1_x,s1_y,s4_x,s4_y);
        g.setColor(Color.yellow);
        g.fillOval(s1_x-2,s1_y-2,starsize,starsize);
        g.fillOval(s2_x-2,s2_y-2,starsize,starsize);
        g.fillOval(s3_x-2,s3_y-2,starsize,starsize);
        g.fillOval(s4_x-2,s4_y-2,starsize,starsize);
    }
}
