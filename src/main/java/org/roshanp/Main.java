package org.roshanp;

import org.roshanp.kmeansengine.Cluster;
import org.roshanp.kmeansengine.Vector;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Vector<Double>> list = Vector.createList(new double[]{0,0,1,1,0,1,1,0,100,100,101,101,100,101,101,100}, 2);
        Cluster<Vector<Double>> c = new Cluster<>(new Vector<>(new double[]{1000,1000}));
        c.addAll(list);

        System.out.println(c.size());

        ArrayList<Cluster<Vector<Double>>> clusters = Cluster.cluster(c, 2);
        for (Cluster<Vector<Double>> cluster : clusters) {
            System.out.println("NEW CLUSTER");
            System.out.println(cluster.size());
            for (Vector<Double> v : cluster) {
                System.out.println(v);
            }
        }
    }
}
