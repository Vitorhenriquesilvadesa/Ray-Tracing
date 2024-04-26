package org.vtko.raytracing;

import static org.vtko.raytracing.Util.randomUnitVec3;
import static org.vtko.raytracing.Vector3.*;

public class MetallicMaterial extends Material {

    public final Vector3 albedo;
    public final float fuzz;

    public MetallicMaterial(Vector3 albedo, float fuzz) {
        this.albedo = albedo;
        this.fuzz = fuzz;
    }

    @Override
    public boolean scatter(Ray r_In, HitData hitData, Vector3 attenuation, Ray scattered) {
        Vector3 reflected = reflect(unitVector(r_In.getDirection()), hitData.normal);
        scattered.setOrigin(hitData.point);
        scattered.setDirection(add(reflected, multiply(randomUnitVec3(), fuzz)));
        scattered.setTime(r_In.getTime());
        attenuation.x = albedo.x;
        attenuation.y = albedo.y;
        attenuation.z = albedo.z;
        return dot(scattered.getDirection(), hitData.normal) > 0;
    }
}
