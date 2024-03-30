package org.vtko.raytracing;

import static org.vtko.raytracing.Util.infinity;

public class Interval {
    public float min, max;

    public static final Interval empty = new Interval(infinity(), -infinity());
    public static final Interval universe = new Interval(-infinity(), infinity());

    public Interval() {
        min = Float.MIN_VALUE;
        max = Float.MAX_VALUE;
    }

    public Interval(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(float x) {
        return min <= x && x <= max;
    }

    public boolean surrounds(float x) {
        return min < x && x < max;
    }

    public float clamp(float x) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }
}
