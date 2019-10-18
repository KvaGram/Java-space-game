
package unicus.spacegame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Sectormaps extends JPanel {
    Random rand;
    int starsize;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Map Frame");
        frame.add(new Sectormaps());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 1050);
        frame.setVisible(true);
    }

    //NOTE: If distance argument * number of stars is too large compared to sector size, stars may never fit and generator algo may loop forever.
    public boolean tooCloseStarPair(int[] p1, int[] p2, int distance) {
        int xdist = Math.abs(p1[0]-p2[0]);
        int ydist = Math.abs(p1[1]-p2[1]);
        double hyp = Math.sqrt((xdist*xdist)+(ydist*ydist));
        return (hyp < distance);
    }
    public boolean tooCloseInSector(int[][] sector, int distance) {
        if (sector.length <= 1) {
            return false;
        } else if (sector.length == 2) {
            return tooCloseStarPair(sector[0],sector[1], distance);
        } else {
            boolean foundClosePair = false;
            for (int i=0; i<sector.length; i++) {
                for (int j=0; j<i; j++) {
                    foundClosePair = foundClosePair || tooCloseStarPair(sector[i],sector[j],distance);
                }
            }
            return foundClosePair;
        }
    }
    public boolean tooLinearTriple(int[] p1, int[] p2, int[] p3, double theta) {
        double a = Math.atan2((p2[1]-p1[1]),(p2[0]-p1[0]));
        double b = Math.atan2((p3[1]-p1[1]),(p3[0]-p1[0]));
        double c = Math.atan2((p3[1]-p2[1]),(p3[0]-p2[0]));
        double ab = Math.abs(a-b);
        double ac = Math.abs(a-c);
        double bc = Math.abs(b-c);
        return (ab < theta) || (ac < theta) || (bc < theta); /*carefully compacted*/
    }
    public boolean tooLinearInSector(int[][] sector, double theta) {
        if (sector.length <= 2) {
            return false;
        } else {
            boolean foundLinearTriplet = false;
            for (int i=0; i<sector.length; i++) {
                for (int j=0; j<i; j++) {
                    for (int k=0; k<j; k++) {
                        foundLinearTriplet = foundLinearTriplet || tooLinearTriple(sector[i],sector[j],sector[k], theta);
                    }
                }
            }
            return foundLinearTriplet;
        }
    }

    public int[][] getClosestPair(int[][] sector1, int[][] sector2) {
        int[][] result = new int[2][];
        result[0] = sector1[0].clone();
        result[1] = sector2[0].clone();
        double shortest = Math.sqrt(Math.pow((sector1[0][0]-sector2[0][0]),2) + Math.pow((sector1[0][1]-sector2[0][1]),2));
        for (int i=0; i<sector1.length; i++) {
            for (int j=0; j<sector2.length; j++) {
                double hyp = Math.sqrt(Math.pow((sector1[i][0]-sector2[j][0]),2) + Math.pow((sector1[i][1]-sector2[j][1]),2));
                if (hyp < shortest) {
                    //System.out.println(Double.toString(hyp) + " shorter than " + Double.toString(shortest));
                    shortest = hyp;
                    result[0] = sector1[i].clone();
                    result[1] = sector2[j].clone();
                }
            }
        }
        return result;
    }

    public int[] pointInTriangle(int P1x, int P1y, int P2x, int P2y, int P3x, int P3y) {
        //Take the triangle corner points P1-P3 as arguments.
        //Shifts inwards to create a buffer near zone edges.
        double bf = 0.03; //buffer fraction. 0.1 = 10%
        int Q1x = (int) (P1x + (bf*((P2x-P1x)+(P3x-P1x))));
        int Q1y = (int) (P1y + (bf*((P2y-P1y)+(P3y-P1y))));
        int Q2x = (int) (P2x + (bf*((P1x-P2x)+(P3x-P2x))));
        int Q2y = (int) (P2y + (bf*((P1y-P2y)+(P3y-P2y))));
        int Q3x = (int) (P3x + (bf*((P1x-P3x)+(P2x-P3x))));
        int Q3y = (int) (P3y + (bf*((P1y-P3y)+(P2y-P3y))));
        Random r = new Random();
        double s = r.nextDouble();
        double t = Math.sqrt(r.nextDouble()); //counters biasing towards P3 due to wedge compression
        double protox = (((1-t)*Q1x) + (t*(((1-s)*Q2x) + (s*Q3x))));
        double protoy = (((1-t)*Q1y) + (t*(((1-s)*Q2y) + (s*Q3y))));
        int x = (int) protox;
        int y = (int) protoy;
        return new int[]{x,y};
    }

    public void paintComponent(Graphics g) {
        //initial background
        g.setColor(Color.black);
        g.fillRect(0,0,1000,1000);

        rand = new Random(0);
        starsize = 7;
        int ystart = 20;
        int yheight = 200;
        int xstart = 20;
        int xwidth = 300;
        int xmid = xwidth/2; //A triangle's base is xwidth; a triangle's area is xmid*yheight.
        int room = 30; //pixels, how far apart stars should be
        double angle = 0.25; //radians, how far apart connecting hyperlanes should be, About 15 degrees.


        int x_secs = 5;
        int y_secs = 4;
        int t_secs = x_secs * y_secs;
        g.setColor(new Color(240,60,140));

        //Sector grid drawing
        for (int i=0; i<=y_secs; i++) { //draw horizontal sector lines
            if (i % 2 == 0) {
                g.drawLine((xstart+xmid),(ystart+(i*yheight)), (xstart+(xmid*(x_secs+((x_secs+1)%2)))),(ystart+(i*yheight)));
            } else {
                g.drawLine(xstart,(ystart+(i*yheight)), (xstart+(xmid*(x_secs+(x_secs%2)))),(ystart+(i*yheight)));
            }
        }
        for (int i=0; i<=x_secs; i++) { //draw diagonal sector lines
            for (int j=0; j<y_secs; j++) {
                if ( j % 2 == 0) { //even j
                    if (i % 2 == 0) { //even i
                        g.drawLine(xstart+(i*xmid),(ystart+yheight+(j*yheight)), (xstart+xmid+(i*xmid)),ystart+(j*yheight));
                    } else { //odd i
                        g.drawLine(xstart+(i*xmid),(ystart+(j*yheight)), (xstart+xmid+(i*xmid)), (ystart+yheight+(j*yheight)));
                    }
                } else { //odd j
                    if (i % 2 == 0) { //even i
                        g.drawLine(xstart+xmid+(i*xmid),(ystart+yheight+(j*yheight)), (xstart+(i*xmid)),(ystart+(j*yheight)));
                    } else { //odd i
                        g.drawLine((xstart+xmid+(i*xmid)),(ystart+(j*yheight)), xstart+(i*xmid),(ystart+yheight+(j*yheight)));
                    }
                }
            }
        }

        int[][][] secs_stars_coords = new int[t_secs][][]; //[n][m][] is {x,y,seed for Lars} to be implemented later.
        //Create stars
        Random rft = new Random(41356);
        for (int i=0; i<x_secs; i++) {
            for (int j=0; j<y_secs; j++) {
                int ij = i+(j*x_secs); // linear number of sector, for array indexing
                System.out.println("Sector number "+ij);
                int[][] sc = new int[3][2]; //sc=sector_corners
                int up = (i%2 == j%2) ? 1 : 0; //1 if triangle 'points' up, 0 if down.
                int down = 1-up;
                sc[0] = new int[]{xstart + (i * xmid), ystart + (yheight * (up+j))};
                sc[1] = new int[]{(xstart + xmid + (i * xmid)), ystart + (yheight * (down + j))};
                sc[2] = new int[]{xstart + xwidth + (i * xmid), ystart + (yheight * (up + j))};
                int secstars_n = rft.nextInt(4)+1; //0-3 +1
                int[][] secstars_c = new int[secstars_n][2];
                for (int n = 0; n < secstars_n; n++) {
                    int[] starcoords = pointInTriangle(sc[0][0], sc[0][1], sc[1][0], sc[1][1], sc[2][0], sc[2][1]);
                    secstars_c[n] = starcoords.clone();
                }
                while (tooCloseInSector(secstars_c, room) || tooLinearInSector(secstars_c, angle)) {
                    for (int n = 0; n < secstars_n; n++) {
                        int[] starcoords = pointInTriangle(sc[0][0], sc[0][1], sc[1][0], sc[1][1], sc[2][0], sc[2][1]);
                        secstars_c[n] = starcoords.clone();
                    }
                }

                g.setColor(new Color(100,200,50));
                for (int n=0; n<secstars_n; n++){
                    g.fillOval(secstars_c[n][0] - starsize / 2, secstars_c[n][1] - starsize / 2, starsize, starsize);
                }
                secs_stars_coords[ij] = secstars_c.clone();
                g.setColor(new Color(20,100,40));
                if (secstars_n == 1) {
                    assert true;
                } else if (secstars_n == 2) {
                    g.drawLine(secstars_c[0][0],secstars_c[0][1],secstars_c[1][0],secstars_c[1][1]);
                } else if (secstars_n == 3) {
                    g.drawLine(secstars_c[0][0],secstars_c[0][1],secstars_c[1][0],secstars_c[1][1]);
                    g.drawLine(secstars_c[0][0],secstars_c[0][1],secstars_c[2][0],secstars_c[2][1]);
                    g.drawLine(secstars_c[1][0],secstars_c[1][1],secstars_c[2][0],secstars_c[2][1]);
                } else if (secstars_n >= 4) {
                    int hub = rft.nextInt(secstars_n);
                    for (int v=0; v<secstars_n; v++) {
                        if (v != hub) { g.drawLine(secstars_c[v][0],secstars_c[v][1],secstars_c[hub][0],secstars_c[hub][1]); }
                    }
                }
            }
        }
        //Create hyperlanes
        for (int i = 1; i < secs_stars_coords.length; i++) {
            if (i%x_secs != 0){ //do not wrap across end of line
                int[][] crossSectorPair = getClosestPair(secs_stars_coords[i-1],secs_stars_coords[i]);
                g.drawLine(crossSectorPair[0][0],crossSectorPair[0][1],crossSectorPair[1][0],crossSectorPair[1][1]);
            }
            int colrowsum = (i%x_secs)+(i/x_secs);
            if (i >= x_secs && (colrowsum%2 == 1)) { //Roughly every 2nd sector should be linked to vertical above sector
                int[][] crossSectorPair = getClosestPair(secs_stars_coords[i-x_secs],secs_stars_coords[i]);
                g.drawLine(crossSectorPair[0][0],crossSectorPair[0][1],crossSectorPair[1][0],crossSectorPair[1][1]);
            }
        }
    }
}