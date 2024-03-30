package org.vtko.raytracing;

import static org.vtko.raytracing.Vector3.*;

public class Sphere extends Hittable {

    private Vector3 center;
    private float radius;
    private Material material;

    public Sphere(Vector3 center, float radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, Interval rayT, HitData hitData) {

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
        hitData.normal = divide(subtract(hitData.point, center), radius);

        Vector3 outwardNormal = divide(subtract(hitData.point, center), radius);
        hitData.setFaceNormal(ray, outwardNormal);
        hitData.material = this.material;

        return true;
    }
}
