package org.roshanp;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cube {

    private Point[][][] CUBE;

    public Cube(BufferedImage image) {
        this();
        init(image);
    }

    public Cube() {
        CUBE = new Point[255][255][255];
    }

    public void init(BufferedImage image) {

        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {

                Color pixel = new Color(image.getRGB(c, r));

                if (CUBE[pixel.getRed()][pixel.getGreen()][pixel.getBlue()] == null)
                    CUBE[pixel.getRed()][pixel.getGreen()][pixel.getBlue()] = new Point(c, r);

            }
        }
    }

    public Point get(int r, int g, int b) {
        return CUBE[r][g][b];
    }

}
