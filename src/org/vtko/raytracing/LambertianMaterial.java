package org.vtko.raytracing;

import static org.vtko.raytracing.Util.*;
import static org.vtko.raytracing.Vector3.*;

public class LambertianMaterial extends Material{

    private final Vector3 albedo;

    public LambertianMaterial(Vector3 albedo) {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r_In, HitData hitData, Vector3 attenuation, Ray scattered) {
        Vector3 scatterDirection = add(hitData.normal, randomUnitVec3());

        if (nearZero(scatterDirection)) {
            scatterDirection = hitData.normal;
        }

        scattered.setOrigin(hitData.point);
        scattered.setDirection(scatterDirection);

        // Attenuation poderia ser albedo / p onde p é uma probabilidade fixa.
        attenuation.x = albedo.x;
        attenuation.y = albedo.y;
        attenuation.z = albedo.z;
        return true;
    }
}
