package org.roshanp.kmeansengine;

import java.util.ArrayList;

public class Cluster<P extends Vector<Double>> extends ArrayList<P> {

    private Vector<Double> center;
    private Vector<Double> bounds; //bounds.length / 2 is the dimension count for this vector

    public Cluster(Vector<Double> bounds) {
        this.bounds = bounds;
        double[] v = new double[bounds.size() / 2];
        for (int i = 0; i <= bounds.size() - 2; i += 2) {
            v[i / 2] = bounds.get(i) + (Math.random() * (bounds.get(i + 1) - bounds.get(i)));
        }
        center = new Vector<>(v);
    }

    public Cluster(Vector<Double> bounds, Vector<Double> center) {
        this.bounds = bounds;
        this.center = center;
    }

    public Vector<Double> getCenter() {
        return this.center;
    }

    public Vector<Double> calculatedCenter() {
        double[] a = new double[bounds.size() / 2];
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
        for (int i = 0; i < bounds.size() / 2; i++) {
            double max = Integer.MIN_VALUE;
            double min = Integer.MAX_VALUE;
            for (Vector<Double> p : this) {
                if (p.get(i) > max) max = p.get(i);
                if (p.get(i) < min) min = p.get(i);
            }
            newBounds.set(2 * i, min);
            newBounds.set(2 * i + 1, max);
        }
        return newBounds;
    }

    public void recalculateBounds() {
        this.bounds = calculatedBounds();
    }

    public Vector<Double> getBounds() {
        return bounds;
    }

    public void safeAdd(P p) {
        add(p);
        recalculateBounds();
    }

    //TODO safety lock to check if safeAdd been called at least once
    public static ArrayList<Cluster<Vector<Double>>> cluster(Cluster<Vector<Double>> c, int n) {

        //safety
        if (c.size() != 0) c.safeAdd(c.remove(0));

        ArrayList<Cluster<Vector<Double>>> clusters = new ArrayList<>();
        if (n == 0) {
            return null;
        } else if (n == 1) {
            clusters.add(c);

        } else {
            //add two clusters between two random members of c
            if (c.size() < 2) {
                for (int i = 0; i < 2; i++) {
                    clusters.add(new Cluster<>(c.getBounds()));
                }
            } else {
                Vector<Double> p1 = c.get(0);
                Vector<Double> p2 = c.farthest(p1);

                Vector<Double> c1 = new Vector<>(); //closer to p1
                for (int d = 0; d < c.center.size(); d++) {
                    c1.add((p1.get(d) - p2.get(d)) * (1/3.0) + p2.get(d));
                }
                clusters.add(new Cluster<>(c.bounds, c1));

                Vector<Double> c2 = new Vector<>(); //closer to p2
                for (int d = 0; d < c.center.size(); d++) {
                    c2.add((p1.get(d) - p2.get(d)) * (2/3.0) + p2.get(d));
                }
                clusters.add(new Cluster<>(c.bounds, c2));
            }


            descendGradient(clusters, c);

            if (n > 2) {

                Cluster<Vector<Double>> c1 = clusters.remove(0);
                Cluster<Vector<Double>> c2 = clusters.remove(0);

                if (c1.size() > c2.size()) {
                    clusters.addAll(cluster(c2, n / 2));
                    clusters.addAll(cluster(c1, (int) Math.ceil(n / 2.0)));
                } else {
                    clusters.addAll(cluster(c1, n / 2));
                    clusters.addAll(cluster(c2, (int) Math.ceil(n / 2.0)));
                }
            }
        }
        return clusters;
    }

    public static void descendGradient(ArrayList<Cluster<Vector<Double>>> clusters, Cluster<Vector<Double>> c) {
        //repetition (for now epoch based)

        Vector<Double>[] oldCenters = null;
        Vector<Double>[] centers = getCenters(clusters);
        boolean iterA = true;

        while (iterA || hasMoved(oldCenters, centers)) {

            //single iteration of descent
            //------------------------------

            //assign points to clusters
            for (Vector<Double> p : c) {
                closestCluster(p, clusters).add(p);
            }

            //recalculate centers
            for (Cluster<Vector<Double>> z : clusters) {
                z.recalculateCenter();
                z.clear();
            }

            //------------------------------
            //end decent iteration

            oldCenters = centers.clone();
            centers = getCenters(clusters);
            iterA = false;

        }

        //move all points to new cluster locations
        int bound = c.size();
        for (int i = 0; i < bound; i++) {
            closestCluster(c.get(i), clusters).add(c.remove(i));
            bound--;
            i--;
        }

        //calculate new cluster bounds
        for (Cluster<Vector<Double>> z : clusters) {
            z.recalculateBounds();
        }
    }

    //TODO change this to list output, cycle through list systematically (clusters will need a <i>picked</i> indicator)
    public static Cluster<Vector<Double>> closestCluster(Vector<Double> p, ArrayList<Cluster<Vector<Double>>> c) {

        assert c != null;
        if (c.size() == 1) return c.get(0);

        Cluster<Vector<Double>> closestCluster = c.get(0);
        for (Cluster<Vector<Double>> cluster : c) {
            if (p.distance(cluster.getCenter()) < p.distance(closestCluster.getCenter())) {
                closestCluster = cluster;
            } else if (p.distance(cluster.getCenter()) == p.distance(closestCluster.getCenter())) {
                if (Math.random() > 0.5) closestCluster = cluster;
            }
        }

        return closestCluster;
    }

    private static Vector[] getCenters(ArrayList<Cluster<Vector<Double>>> clusters) {
        Vector[] centers = new Vector[clusters.size()];
        for (int i = 0; i < clusters.size(); i++) {
            centers[i] = clusters.get(i).center;
        }
        return centers;
    }

    private static boolean hasMoved(Vector<Double>[] c1, Vector<Double>[] c2) {
        assert c1.length == c2.length;
        for (int i = 0; i < c1.length; i++) {
            if (!c1[i].equals(c2[i])) return true;
        }
        return false;
    }

    public Vector<Double> farthest(Vector<Double> u) {
        double max = 0;
        Vector<Double> farthest = u;
        for (Vector<Double> v : this) {
            if (u.distance(v) > max) {
                max = u.distance(v);
                farthest = v;
            }
        }
        return farthest;
    }
}
