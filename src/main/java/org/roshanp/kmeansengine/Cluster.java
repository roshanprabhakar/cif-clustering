package org.roshanp.kmeansengine;

import java.util.ArrayList;

public class Cluster<P extends Vector<Double>> extends ArrayList<P> {

    private Vector<Double> center;
    private Vector<Double> bounds; //bounds.length is the dimension count for this vector, all dimensions lower bounded at 0

    public Cluster(Vector<Double> bounds) {
        this.bounds = bounds;
        double[] v = new double[bounds.size()];
        for (int i = 0; i < bounds.size(); i++) {
            v[i] = Math.random() * bounds.get(i);
        }
        center = new Vector<>(v);
    }

    public Vector<Double> getCenter() {
        return this.center;
    }

    public Vector<Double> calculatedCenter() {
        double[] a = new double[bounds.size()];
        for (P p : this) {
            for (int j = 0; j < p.size(); j++) {
                a[j] += p.get(j);
            }
        }
        for (int j = 0; j < a.length; j++) {
            a[j] /= size();
        }
        return new Vector<>(a);
    }

    public void recalculateCenter() {
        this.center = calculatedCenter();
    }

    public Vector<Double> calculatedBounds() {
        Vector<Double> newBounds = new Vector<>(bounds.size());
        for (int i = 0; i < bounds.size(); i++) {
            double max = 0;
            for (Vector<Double> p : this) {
                if (p.get(i) > max) max = p.get(i);
            }
            newBounds.set(i, max);
        }
        return newBounds;
    }

    public void recalculateBounds() {
        this.bounds = calculatedBounds();
    }

    public Vector<Double> getBounds() {
        return bounds;
    }

    public static ArrayList<Cluster<Vector<Double>>> cluster(Cluster<Vector<Double>> c, int n) {

        ArrayList<Cluster<Vector<Double>>> clusters = new ArrayList<>();
        if (n == 1) {
            clusters.add(c);
        } else if (n == 2) {

            //init clusters
            for (int i = 0; i < n; i++) {
                clusters.add(new Cluster<>(c.getBounds()));
            }

            //repetition (for now epoch based)
            int EPOCH = 10;
            for (int e = 0; e <= EPOCH - 1; e++) {

                //assign points to clusters
                for (Vector<Double> p : c) {
                    closestCluster(p, clusters).add(p);
                }

                //recalculate centers
                for (Cluster<Vector<Double>> z : clusters) {
                    z.recalculateCenter();
                    z.clear();
                }
            }

            //move all points to new cluster locations
            for (int i = 0; i < c.size(); i++) {
                closestCluster(c.get(i), clusters).add(c.remove(i));
            }

            //calculate new cluster bounds
            for (Cluster<Vector<Double>> z : clusters) {
                z.recalculateBounds();
            }
        } else {
            /**
             * 2 recursive calls:
             *   1            | 2
             *   n* = n / 2   | n* = Math.ceil(n / 2.0)
             */
            clusters.addAll(cluster(c, (n / 2)));
            clusters.addAll(cluster(c, (int) Math.ceil(n / 2.0)));
        }
        return clusters;
    }

    public static Cluster<Vector<Double>> closestCluster(Vector<Double> p, ArrayList<Cluster<Vector<Double>>> c) {

        assert c != null;
        if (c.size() == 1) return c.get(0);

        Cluster<Vector<Double>> closestCluster = c.get(0);
        for (Cluster<Vector<Double>> cluster : c) {
            if (p.distance(cluster.getCenter()) < p.distance(closestCluster.getCenter())) {
                closestCluster = cluster;
            }
        }

        return closestCluster;
    }


}
