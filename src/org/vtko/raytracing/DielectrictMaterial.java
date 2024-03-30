package org.vtko.raytracing;

import static org.vtko.raytracing.Util.randomNumber;
import static org.vtko.raytracing.Util.refract;
import static org.vtko.raytracing.Vector3.*;

public class DielectrictMaterial extends Material {

    public final float indexOfRefraction;

    public DielectrictMaterial(float indexOfRefraction) {
        this.indexOfRefraction = indexOfRefraction;
    }

    @Override
    public boolean scatter(Ray r_In, HitData hitData, Vector3 attenuation, Ray scattered) {
        attenuation.x = 1f;
        attenuation.y = 1f;
        attenuation.z = 1f;

        float refractionRatio = hitData.isFrontFace ? (1f / indexOfRefraction) : indexOfRefraction;

        Vector3 unitDirection = unitVector(r_In.getDirection());

        float cosTheta = Math.min(dot(negate(unitDirection), hitData.normal), 1.0f);
        float sinTheta = (float) Math.sqrt(1f - cosTheta * cosTheta);

        boolean cannotRefract = refractionRatio * sinTheta > 1.0f;
        Vector3 direction;

        if (cannotRefract || reflectance(cosTheta, refractionRatio) > randomNumber()) {
            direction = reflect(unitDirection, hitData.normal);
        } else {
            direction = refract(unitDirection, hitData.normal, refractionRatio);
        }

        scattered.setOrigin(hitData.point);
        scattered.setDirection(direction);
        return true;
    }

    private float reflectance(float cosine, float refractionIndex) {
        float r0 = (1f - refractionIndex) / (1f + refractionIndex);
        r0 = r0 * r0;
        return r0 + (1 - r0) * (float) Math.pow((1 - cosine), 5);
    }
}
