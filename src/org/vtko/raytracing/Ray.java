package org.vtko.raytracing;

public class Ray {

    private Vector3 origin;
    private Vector3 direction;
    private float time;

    public Ray() {

    }

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
        this.time = 0f;
    }

    public Ray(Ray other) {
        this.origin = other.origin;
        this.direction = other.direction;
        this.time = other.time;
    }

    public Ray(Vector3 origin, Vector3 direction, float time) {
        this.origin = origin;
        this.direction = direction;
        this.time = time;
    }

    Vector3 at(float t) {
        return Vector3.add(origin, Vector3.multiply(direction, t));
    }


    public Vector3 getOrigin() {
        return origin;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public float getTime() {
        return time;
    }

    public void setOrigin(Vector3 origin) {
        this.origin = origin;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
