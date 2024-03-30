package org.vtko.raytracing;

import static org.vtko.raytracing.Vector3.*;

public class HitData {

    public Vector3 point = new Vector3();
    public Vector3 normal = new Vector3();
    public float t = 0;
    public boolean isFrontFace;
    public Material material;

    public HitData() {
        material = new LambertianMaterial(new Vector3(1f, 1f, 0f));
    }

    public HitData(Vector3 point, Vector3 normal, float t, Material material) {
        this.point = point;
        this.normal = normal;
        this.t = t;
        this.material = material;
    }

    public void setFaceNormal(Ray ray, Vector3 outwardNormal) {
        isFrontFace = dot(ray.getDirection(), outwardNormal) < 0;
        normal = isFrontFace ? outwardNormal : subtract(zero(), outwardNormal);
    }


}
