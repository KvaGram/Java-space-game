package unicus.spacegame;//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Swingmap extends JPanel {
    Point[] starPoints = new Point[4];

    public void newPoints(Random rand){
        starPoints[0] = new Point(20 + rand.nextInt(400), 20 + rand.nextInt(200));
        starPoints[1] = new Point(200 + rand.nextInt(400), 200 + rand.nextInt(200));
        starPoints[2] = new Point(10 + rand.nextInt(580), 10 + rand.nextInt(380));
        starPoints[3] = new Point(10 + rand.nextInt(580), 10 + rand.nextInt(380));
    }


    public static void main(String args[]) {
        Random rand = new Random(0);
        Swingmap map = new Swingmap();
        map.newPoints(rand);

        //Creating the Frame
        JFrame frame = new JFrame("Test swingmap");
        frame.add(map);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 450);

        frame.setVisible(true);
    }



    public void paint(Graphics g) {
        //Since the randomization is in the paint method, it will be re-rolled whenever you resize window.
        int starsize = 7;
        
        g.setColor(Color.black);
        g.fillRect(0,0,600,400);
        g.setColor(Color.white);
        g.drawLine(starPoints[0].x,starPoints[0].y,starPoints[1].x,starPoints[1].y);
        g.drawLine(starPoints[0].x,starPoints[0].y,starPoints[2].x,starPoints[2].y);
        g.drawLine(starPoints[0].x,starPoints[0].y,starPoints[3].x,starPoints[3].y);
        g.setColor(Color.yellow);
        g.fillOval(starPoints[0].x-2,starPoints[0].y-2,starsize,starsize);
        g.fillOval(starPoints[1].x-2,starPoints[1].y-2,starsize,starsize);
        g.fillOval(starPoints[2].x-2,starPoints[2].y-2,starsize,starsize);
        g.fillOval(starPoints[3].x-2,starPoints[3].y-2,starsize,starsize);
    }
}
