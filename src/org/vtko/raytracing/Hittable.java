package org.vtko.raytracing;

public abstract class Hittable {

    public Hittable() {}
    public abstract boolean hit(Ray ray, Interval rayT, HitData hitData);
}
