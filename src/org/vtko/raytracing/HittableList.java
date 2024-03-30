package org.vtko.raytracing;

import java.util.ArrayList;
import java.util.List;

public class HittableList extends Hittable {

    private final List<Hittable> objects;

    public HittableList() {
        objects = new ArrayList<>();
    }

    public HittableList(Hittable object) {
        objects = new ArrayList<>();
        objects.add(object);
    }

    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray ray, Interval rayT, HitData hitData) {

        HitData tempHitData = new HitData();
        boolean hitAnything = false;
        float closestSoFar = rayT.max;

        for (Hittable object : objects) {
            if (object.hit(ray, new Interval(rayT.min, closestSoFar), tempHitData)) {
                hitAnything = true;
                closestSoFar = tempHitData.t;
                hitData.point = tempHitData.point;
                hitData.isFrontFace = tempHitData.isFrontFace;
                hitData.t = tempHitData.t;
                hitData.normal = tempHitData.normal;
                hitData.material = tempHitData.material;
            }
        }

        return hitAnything;
    }
}
