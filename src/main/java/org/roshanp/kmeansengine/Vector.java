package org.roshanp.kmeansengine;

import java.util.ArrayList;

public class Vector<V extends Double> extends ArrayList<V> {

    public Vector() {
    }

    public Vector(double[] v) {
        for (Double i : v) {
            add((V) i);
        }
    }

    public Vector(int n) {
        for (int i = 0; i < n; i++) {
            add((V) new Double(0));
        }
    }

    public double distance(Vector<V> u) {

        ArrayList<V> big = this;
        ArrayList<V> small = u;

        if (u.size() > this.size()) {
            big = u;
            small = this;
        }

        double d = 0;
        for (int i = 0; i < big.size(); i++) {
            if (i < small.size()) {
                d += ((Double) big.get(i) - (Double) small.get(i)) * ((Double) big.get(i) - (Double) small.get(i));
            } else {
                d += (Double) big.get(i) * (Double) big.get(i);
            }
        }

        return Math.sqrt(d);
    }

    public static ArrayList<Vector<Double>> createList(double[] l, int d) {
        ArrayList<Vector<Double>> out = new ArrayList<>();
        for (int i = 0; i <= l.length - d; i += d) {
            Vector<Double> v = new Vector<>();
            for (int j = 0; j < d; j++) {
                v.add(l[i + j]);
            }
            out.add(v);
        }
        return out;
    }

    public boolean equals(Vector<V> other) {
        if (this.size() != other.size()) return false;
        for (int i = 0; i < size(); i++) {
            if (!get(i).equals(other.get(i))) return false;
        }
        return true;
    }
}
