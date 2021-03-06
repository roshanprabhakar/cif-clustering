package org.roshanp;

import org.roshanp.kmeansengine.Cluster;
import org.roshanp.kmeansengine.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert image != null;

        Cluster<Vector<Double>> cimage = new Cluster<>(new Vector<>(new double[]{Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE}));
        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {
                Color p = new Color(image.getRGB(c, r));
                cimage.add(new Vector<>(new double[]{p.getRed(), p.getBlue(), p.getBlue()}));
            }
        }

        long start = System.currentTimeMillis();
        ArrayList<Cluster<Vector<Double>>> iclusters = Cluster.cluster(cimage, 5);
        System.out.println("cluster time: " + (System.currentTimeMillis() - start) / 1000);

        System.exit(0);

        ArrayList<Vector<Double>> list = Vector.createList(new double[]{0, 0, 1, 1, 0, 1, 1, 0, 100, 100, 101, 101, 100, 101, 101, 100, 60, 60, 60, 61, 61, 60, 61, 61}, 2);
        Cluster<Vector<Double>> c = new Cluster<>(new Vector<>(new double[]{0, 101, 0, 101}));
        c.addAll(list);
        c.safeAdd(list.remove(0));

        System.out.println(c);
        System.out.println(c.getCenter());
        System.out.println(c.getBounds());

        System.out.println("---------------");

        ArrayList<Cluster<Vector<Double>>> clusters = Cluster.cluster(c, 3);

        System.out.println("----");
        assert clusters != null;
        for (Cluster<Vector<Double>> cluster : clusters) {
            System.out.println(cluster);
        }
    }
}
