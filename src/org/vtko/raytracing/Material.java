package org.vtko.raytracing;

public abstract class Material {
    public abstract boolean scatter(Ray r_In, HitData hitData, Vector3 attenuation, Ray scattered);
}
