/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

/**
 *
 * @author dpf
 */
public class Interval {

    private final static char INI_CLOSED = '[';
    private final static char END_CLOSED = ']';
    private final static char INI_OPEN = '(';
    private final static char END_OPEN = ')';
    private final static char SEPARATOR = ',';
    private final static String INF = "inf";

    public final static Interval R = new Interval(Double.NEGATIVE_INFINITY, false, Double.POSITIVE_INFINITY, false);

    private double a, b;
    private boolean closed_a, closed_b;
    private double length;

    /**
     * @param a
     * @param b
     * @param closed_a true=[a,... ; false=(a,...
     * @param closed_b true=...,b] ; false=...,b)
     */
    public Interval(double a, boolean closed_a, double b, boolean closed_b) {
        if (a > b) {
            double aux;
            boolean auxb;
            aux = a;
            a = b;
            b = aux;
            auxb = closed_a;
            closed_a = closed_b;
            closed_b = auxb;
        }
        this.a = a;
        this.b = b;
        if (a == Double.POSITIVE_INFINITY || a == Double.NEGATIVE_INFINITY) {
            this.closed_a = false;
        } else {
            this.closed_a = closed_a;
        }
        if (b == Double.POSITIVE_INFINITY || b == Double.NEGATIVE_INFINITY) {
            this.closed_a = false;
        } else {
            this.closed_b = closed_b;
        }
        this.length = b - a;
    }

    /**
     * (a,b)
     *
     * @param a
     * @param b
     */
    public Interval(double a, double b) {
        this(a, false, b, false);
    }

    public boolean contains(double x) {
        if ((x < a) || (x > b)) {
            return false;
        }
        if ((x == a && !closed_a) || (x == b && !closed_b)) {
            return false;
        }
        return true;
    }

    public double getLength() {
        return length;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public boolean isClosed_a() {
        return closed_a;
    }

    public boolean isClosed_b() {
        return closed_b;
    }

    @Override
    public String toString() {
        String s = "";
        if (closed_a) {
            s += INI_CLOSED;
        } else {
            s += INI_OPEN;
        }
        if (a == Double.POSITIVE_INFINITY) {
            s += INF;
        } else if (a == Double.NEGATIVE_INFINITY) {
            s += '-' + INF;
        } else if (Double.isFinite(a)) {
            s += (int) a;
        } else {
            s += a;
        }
        s += SEPARATOR;
        if (b == Double.POSITIVE_INFINITY) {
            s += INF;
        } else if (b == Double.NEGATIVE_INFINITY) {
            s += '-' + INF;
        } else if (Double.isFinite(b)) {
            s += (int) b;
        } else {
            s += b;
        }
        if (closed_b) {
            s += END_CLOSED;
        } else {
            s += END_OPEN;
        }
        return s;
    }

}
