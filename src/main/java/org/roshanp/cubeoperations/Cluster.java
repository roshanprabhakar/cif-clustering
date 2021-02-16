package org.roshanp.cubeoperations;

import java.awt.*;
import java.util.ArrayList;

public class Cluster<P extends Point> extends ArrayList<P> {

    private Point center;

    private int xBound;
    private int yBound;

    public Cluster() {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public Cluster(int maxX, int maxY) {
        center = new Point((int)(Math.random() * maxX), (int)(Math.random() * maxY));
        xBound = maxX;
        yBound = maxY;
    }

    public Point getCenter() {
        return this.center;
    }

    public Point calculatedCenter() {

        int ax = 0;
        int ay = 0;
        for (int i = 0; i < size(); i++) {
            ax += get(i).getX();
            ay += get(i).getY();
        }
        return new Point(ax / size(), ay / size());
    }

    public void setCenter(Point p) {
        this.center = p;
    }

    public ArrayList<Cluster<Point>> clusterize(Cluster<Point> c, int nClusters) {

        ArrayList<Cluster<Point>> clusters = new ArrayList<>();
        for (int i = 0; i < nClusters; i++) {
            clusters.add(new Cluster<>());
        }

        for (int p = 0; p < size(); p++) {
            closestCluster(get(p), clusters).add(remove(p));
        }

        return clusters;
    }

    private Cluster<Point> closestCluster(Point p, ArrayList<Cluster<Point>> c) {

        assert c != null;
        if (c.size() == 1) return c.get(0);

        Cluster<Point> closestCluster = c.get(0);
        for (Cluster<Point> cluster : c) {
            if (p.distance(cluster.getCenter()) < p.distance(closestCluster.getCenter())) {
                closestCluster = cluster;
            }
        }

        return closestCluster;
    }


}
