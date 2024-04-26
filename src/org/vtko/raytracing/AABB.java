package org.vtko.raytracing;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AABB {
    public Interval x;
    public Interval y;
    public Interval z;

    public AABB() {
    }

    public AABB(Interval x, Interval y, Interval z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AABB(Vector3 a, Vector3 b) {
        this.x = new Interval(min(a.x, b.x), max(a.x, b.x));
        this.y = new Interval(min(a.y, b.y), max(a.y, b.y));
        this.z = new Interval(min(a.z, b.z), max(a.z, b.z));
    }

    public Interval axis(int n) {
        if (n == 1) return y;
        if (n == 2) return z;
        return x;
    }

    public boolean hit(Ray r, Interval ray_T) {

        float t0x = min(
                (axis(0).min - r.getOrigin().x) / r.getDirection().x,
                (axis(0).max - r.getOrigin().x) / r.getDirection().x);

        float t1x = max(
                (axis(0).min - r.getOrigin().x) / r.getDirection().x,
                (axis(0).max - r.getOrigin().x) / r.getDirection().x);

        float t0y = min(
                (axis(1).min - r.getOrigin().y) / r.getDirection().y,
                (axis(1).max - r.getOrigin().y) / r.getDirection().y);
        float t1y = max(
                (axis(1).min - r.getOrigin().y) / r.getDirection().y,
                (axis(1).max - r.getOrigin().y) / r.getDirection().y);
        float t0z = min(
                (axis(2).min - r.getOrigin().z) / r.getDirection().z,
                (axis(2).max - r.getOrigin().z) / r.getDirection().z);
        float t1z = max(
                (axis(2).min - r.getOrigin().z) / r.getDirection().z,
                (axis(2).max - r.getOrigin().z) / r.getDirection().z);

        ray_T.min = max(t0x, ray_T.min);
        ray_T.max = min(t1x, ray_T.min);

        if(ray_T.max <= ray_T.min) {
            return false;
        }

        ray_T.min = max(t0y, ray_T.min);
        ray_T.max = min(t1y, ray_T.min);

        if(ray_T.max <= ray_T.min) {
            return false;
        }

        ray_T.min = max(t0z, ray_T.min);
        ray_T.max = min(t1z, ray_T.min);

        return !(ray_T.max <= ray_T.min);
    }
}
