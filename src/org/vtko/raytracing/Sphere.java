package org.vtko.raytracing;

import static org.vtko.raytracing.Vector3.*;

public class Sphere extends Hittable {

    private Vector3 center1;
    private float radius;
    private Material material;
    private boolean isMoving;
    private Vector3 centerVector;

    public Sphere(Vector3 center, float radius, Material material) {
        this.center1 = center;
        this.radius = radius;
        this.material = material;
        this.isMoving = false;
    }

    public Sphere(Vector3 center1, Vector3 center2 , float radius, Material material) {
        this.radius = radius;
        this.center1 = center1;
        this.material = material;
        this.isMoving = true;
        this.centerVector = subtract(center2, center1);
    }

    public Vector3 center(float time) {
        return add(center1, multiply(centerVector, time));
    }

    @Override
    public boolean hit(Ray ray, Interval rayT, HitData hitData) {
        Vector3 center = isMoving ? center(ray.getTime()) : center1;
        Vector3 oc = subtract(ray.getOrigin(), center);
        float a = ray.getDirection().lengthSquared();
        float halfB = dot(oc, ray.getDirection());
        float c = oc.lengthSquared() - radius * radius;
        float discriminant = halfB * halfB - a * c;

        if(discriminant < 0) {
            return false;
        }

        float sqrtDiscriminant = (float)Math.sqrt(discriminant);


        // Find the nearest root that lies in the acceptable range.
        float root = (-halfB - sqrtDiscriminant) / a;

        if(!rayT.surrounds(root)) {
            root = (-halfB + sqrtDiscriminant) / a;
            if (!rayT.surrounds(root)) {
                return false;
            }
        }

        hitData.t = root;
        hitData.point = ray.at(hitData.t);
        hitData.normal = divide(subtract(hitData.point, center1), radius);

        Vector3 outwardNormal = divide(subtract(hitData.point, center1), radius);
        hitData.setFaceNormal(ray, outwardNormal);
        hitData.material = this.material;

        return true;
    }
}
