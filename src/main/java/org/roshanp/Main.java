package org.roshanp;

import org.roshanp.kmeansengine.Cluster;
import org.roshanp.kmeansengine.Vector;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Vector<Double>> list = Vector.createList(new double[]{0, 0, 1, 1, 0, 1, 1, 0, 100, 100, 101, 101, 100, 101, 101, 100, 60, 60, 60, 61, 61, 60, 61, 61}, 2);
        Cluster<Vector<Double>> c = new Cluster<>(new Vector<>(new double[]{0, 101, 0, 101}));
        for (Vector<Double> v : list) {
            c.safeAdd(v);
        }

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
