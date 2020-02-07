package unicus.spacegame.ui.crew;

import de.gurkenlabs.litiengine.graphics.RenderEngine;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import unicus.spacegame.crew.AbstractCrewman;
import unicus.spacegame.crew.CrewSelfID;
import unicus.spacegame.crew.SpaceCrew;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Static class.
 * Generate image files with displaydata for crewman.
 */
public class CrewCard {
    private static Hashtable<String, Image> images = new Hashtable<String, Image>();

    private static final String NO_CREW_IMAGE = "empty.png";
    private static final String UNKNOWN_IMAGE = "unknown.png";
    private static final String GENERIC_IMAGE = "generic.png";
    private static Image fallbackImage;

    public static Image makeIcon(int width, int height, int crewID, NameDisplayMode displayMode, Font font, Color fontColor) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Image displayImage;
        int size = Math.min(width, height);
        String displayText = " - ";

        if(crewID == 0) {
            displayImage = getImage(NO_CREW_IMAGE);
        }
        else {
            AbstractCrewman crewman;
            crewman = SpaceCrew.getInstance().getCrew(crewID);
            if(crewman == null) {
                System.err.println("WARNING: Crewman ID is void : " + crewID);
                displayImage = getImage(NO_CREW_IMAGE);
                displayText = " - ";
            }
            else {
                displayImage = getImage(GENERIC_IMAGE); //TODO: get image based on CrewSelfID and CrewmanGeneData.
                CrewSelfID crewSelfID = crewman.getSelfID();
                if (displayMode == NameDisplayMode.NONE) {
                    displayText = "";
                }
                if (displayMode == NameDisplayMode.FULL) {
                    displayText = crewSelfID.getFullName();
                }
            }
        }
        //scale to fit.
        displayImage = displayImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);

        //test-text:
        //displayText = "HELLO WORLD! I AM HERE!!";

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setFont(font);
        g.drawImage(displayImage, 0, 0, null);
        g.setColor(fontColor);
        int displaylength = g.getFontMetrics().stringWidth(displayText);
        int x = Math.abs((width - displaylength) / 2);
        if(displaylength > width)
        {
            //noinspection IntegerDivisionInFloatingPointContext
            g.scale((float)width / (float)displaylength, 1);
            x = 0;
        }

        g.drawString(displayText, x, height);
        g.dispose();
        return image;
    }
    public static Image makeIcon(int width, int height, int crewID) {
        return makeIcon(width, height, crewID, NameDisplayMode.FULL, new Font(Font.SERIF, Font.ITALIC, 24), Color.lightGray);
    }

    private static Image getImage(String filename) {
        Image img;
        if (images.containsKey(filename)) {
            img = images.get(filename);
        } else {
            try {
                img = ImageIO.read(CrewCard.class.getResource(filename));
            } catch (IOException | IllegalArgumentException err) {
                img = getFallbackImage();
            }
            images.put(filename, img);
        }
        return img;
    }

    private static Image getFallbackImage() {
        if (fallbackImage == null) {
            fallbackImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics g = fallbackImage.getGraphics();
            g.setColor(Color.black);
            g.fillOval(0, 0, 100, 100);
            return fallbackImage;
        }
        return fallbackImage;
    }

    enum NameDisplayMode{NONE,FULL};


}
