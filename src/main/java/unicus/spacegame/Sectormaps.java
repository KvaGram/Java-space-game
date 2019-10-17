//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Main extends JPanel {
    Random rand;
    int starsize;

    public static void main(String args[]) {
        JFrame frame = new JFrame("Map Frame");
        frame.add(new Main());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 650);
        JPanel panel = new JPanel(); //the magic of copy and paste from stackoverflow
        frame.setVisible(true);
    }

    public int ff(int size) { //Fudge factor
        return (int)(new Random().nextInt(1+size*2)-size);
    }

    public boolean tooClose(int[] p1, int[] p2, int distance) {
        int xdist = Math.abs(p1[0]-p2[0]);
        int ydist = Math.abs(p1[1]-p2[1]);
        double hyp = Math.sqrt((xdist*xdist)+(ydist*ydist));
        if (hyp > distance) { return false; } else { return true; }
    }
    public boolean tooLinear(int[] p1, int[] p2, int[] p3, double theta) {
        double a = Math.atan2((p2[1]-p1[1]),(p2[0]-p1[0]));
        double b = Math.atan2((p3[1]-p1[1]),(p3[0]-p1[0]));
        double c = Math.atan2((p3[1]-p2[1]),(p3[0]-p2[0]));
        double ab = Math.abs(a-b);
        double ac = Math.abs(a-c);
        double bc = Math.abs(b-c);
        if ( (ab<theta) || (ac<theta) || (bc<theta) ) { return true; }
        else { return false; }
    }

    public int[] pointInTriangle(int P1x, int P1y, int P2x, int P2y, int P3x, int P3y) {
        Random r = new Random();
        double s = r.nextDouble();
        double t = Math.sqrt(r.nextDouble()); //counters biasing towards P3 due to wedge compression
        double protox = (((1-t)*P1x) + (t*(((1-s)*P2x) + (s*P3x))));
        double protoy = (((1-t)*P1y) + (t*(((1-s)*P2y) + (s*P3y))));
        int x = (int) protox;
        int y = (int) protoy;
        int[] result = {x,y};
        return result;
    }

    public void paintComponent(Graphics g) {
        rand = new Random(0);
        //initial background
        g.setColor(Color.black);
        g.fillRect(0,0,1000,600);

        rand = new Random(0);
        starsize = 7;
        int ystart = 50;
        int yheight = 200;
        int xstart = 20;
        int xwidth = 300;
        int xmid = (int) (xwidth/2); //A triangle's base is xwidth; a triangle's area is xmid*yheight.
        int room = 40; //pixels
        double angle = 0.14; //radians. About 8 degrees.

        //Draw triangles to get an idea of what I'm doing;
        g.setColor(Color.red);
        //horizontal lines
        g.drawLine((xstart+xmid),ystart, (xstart+(5*xmid)),ystart);
        g.drawLine(xstart,(ystart+yheight), (xstart+6*xmid),(ystart+yheight));
        g.drawLine((xstart+xmid),(ystart+2*yheight), (xstart+(5*xmid)),ystart+(2*yheight));
        //diagonal lines
        for (int i=0; i<3; i++) {
            g.drawLine(xstart+(2*i*xmid),(ystart+yheight), (xstart+xmid+(2*i*xmid)),ystart);
            g.drawLine(xstart+xmid+(2*i*xmid),(ystart), (xstart+xmid+xmid+(2*i*xmid)), (ystart+yheight));
        }
        for (int i=0; i<3; i++) {
            g.drawLine(xstart+(2*i*xmid),(ystart+yheight), (xstart+xmid+(2*i*xmid)),(ystart+2*yheight));
            g.drawLine(xstart+xmid+(2*i*xmid),(ystart+2*yheight), (xstart+xmid+xmid+(2*i*xmid)), (ystart+yheight));
        }

        //Place and connect stars in the first triangle just to have some idea what I'm doing.
        //Point generation
        int sys1stars = 3; //general case will probably be 1d3
        int s1coords[][] = new int[sys1stars][2]; //first dimension stars, second dimension x and y coords
        s1coords[0] = pointInTriangle(xstart,(ystart+yheight), (xstart+xmid),ystart, (xstart+xmid+xmid),(ystart+yheight)).clone();
        s1coords[1] = pointInTriangle(xstart,(ystart+yheight), (xstart+xmid),ystart, (xstart+xmid+xmid),(ystart+yheight)).clone();
        s1coords[2] = pointInTriangle(xstart,(ystart+yheight), (xstart+xmid),ystart, (xstart+xmid+xmid),(ystart+yheight)).clone();
        //filter so they are not too close to each other
        while( tooClose(s1coords[0], s1coords[1], room) || tooClose(s1coords[0],s1coords[2],room) || tooClose(s1coords[1],s1coords[2],room) || tooLinear(s1coords[0],s1coords[1],s1coords[2],angle) ) {
            s1coords[0] = pointInTriangle(xstart,(ystart+yheight), (xstart+xmid),ystart, (xstart+xmid+xmid),(ystart+yheight)).clone();
            s1coords[1] = pointInTriangle(xstart,(ystart+yheight), (xstart+xmid),ystart, (xstart+xmid+xmid),(ystart+yheight)).clone();
            s1coords[2] = pointInTriangle(xstart,(ystart+yheight), (xstart+xmid),ystart, (xstart+xmid+xmid),(ystart+yheight)).clone();
        }

        //Draw stars
        g.setColor(Color.yellow);
        for(int i=0; i<s1coords.length; i++) {
            g.fillOval(s1coords[i][0],s1coords[i][1],starsize,starsize);
        }
        g.setColor(Color.white);
        for(int i=0; i<(s1coords.length-1); i++) {
            g.drawLine(s1coords[i][0],s1coords[i][1],s1coords[i+1][0],s1coords[i+1][1]);
        }
        g.drawLine(s1coords[s1coords.length-1][0],s1coords[s1coords.length-1][1],s1coords[0][0],s1coords[0][1]); //fencepost

        //Second sector
        int sys2stars = 2;
        int s2coords[][] = new int[sys2stars][2];
        int[] s2p1 = {(xstart+xmid),ystart};
        int[] s2p2 = {(xstart+xmid+xmid),(ystart+yheight)}; //Triangle corners
        int[] s2p3 = {(xstart+(3*xmid)),ystart};
        s2coords[0] = pointInTriangle(s2p1[0],s2p1[1], s2p2[0],s2p2[1], s2p3[0],s2p3[1]);
        s2coords[1] = pointInTriangle(s2p1[0],s2p1[1], s2p2[0],s2p2[1], s2p3[0],s2p3[1]);
        while( tooClose(s2coords[0], s2coords[1], room)) {
            s2coords[0] = pointInTriangle(s2p1[0],s2p1[1], s2p2[0],s2p2[1], s2p3[0],s2p3[1]);
            s2coords[1] = pointInTriangle(s2p1[0],s2p1[1], s2p2[0],s2p2[1], s2p3[0],s2p3[1]);
        }

        //Draw second
        g.setColor(Color.yellow);
        for(int i=0; i<s2coords.length; i++) {
            g.fillOval(s2coords[i][0],s2coords[i][1],starsize,starsize);
        }
        g.setColor(Color.white);
        for(int i=0; i<(s2coords.length-1); i++) {
            g.drawLine(s2coords[i][0],s2coords[i][1],s2coords[i+1][0],s2coords[i+1][1]);
        }

        //Third sector
        int sys3stars = 3; //general case will probably be 1d3
        int s3coords[][] = new int[sys3stars][2]; //first dimension stars, second dimension x and y coords
        s3coords[0] = pointInTriangle((xstart+2*xmid),(ystart+yheight), (xstart+3*xmid),ystart, (xstart+4*xmid),(ystart+yheight)).clone();
        s3coords[1] = pointInTriangle((xstart+2*xmid),(ystart+yheight), (xstart+3*xmid),ystart, (xstart+4*xmid),(ystart+yheight)).clone();
        s3coords[2] = pointInTriangle((xstart+2*xmid),(ystart+yheight), (xstart+3*xmid),ystart, (xstart+4*xmid),(ystart+yheight)).clone();
        //filter so they are not too close to each other
        while( tooClose(s3coords[0], s3coords[1], room) || tooClose(s3coords[0],s3coords[2],room) || tooClose(s3coords[1],s3coords[2],room) || tooLinear(s3coords[0],s3coords[1],s3coords[2],angle) ) {
            s3coords[0] = pointInTriangle((xstart+2*xmid),(ystart+yheight), (xstart+3*xmid),ystart, (xstart+4*xmid),(ystart+yheight)).clone();
            s3coords[1] = pointInTriangle((xstart+2*xmid),(ystart+yheight), (xstart+3*xmid),ystart, (xstart+4*xmid),(ystart+yheight)).clone();
            s3coords[2] = pointInTriangle((xstart+2*xmid),(ystart+yheight), (xstart+3*xmid),ystart, (xstart+4*xmid),(ystart+yheight)).clone();
        }
        g.setColor(Color.yellow);
        for(int i=0; i<s3coords.length; i++) {
            g.fillOval(s3coords[i][0],s3coords[i][1],starsize,starsize);
        }
        g.setColor(Color.white);
        for(int i=0; i<(s3coords.length-1); i++) {
            g.drawLine(s3coords[i][0],s3coords[i][1],s3coords[i+1][0],s3coords[i+1][1]);
        }
        g.drawLine(s3coords[s3coords.length-1][0],s3coords[s3coords.length-1][1],s3coords[0][0],s3coords[0][1]);

    }
}