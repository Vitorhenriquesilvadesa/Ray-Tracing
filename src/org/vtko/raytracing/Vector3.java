package org.vtko.raytracing;

import static java.lang.Math.abs;

public class Vector3 {

    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Vector3(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public static Vector3 zero() {
        return new Vector3(0f, 0f, 0f);
    }

    public static Vector3 normalize(Vector3 vector) {
        float length = vector.length();

        if (length != 0.0f) {
            vector.x /= length;
            vector.y /= length;
            vector.z /= length;
        }

        return vector;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSquared() {
        float length = length();
        return length * length;
    }

    public static Vector3 unitVector(Vector3 vector) {
        return Vector3.divide(vector, vector.length());
    }

    public static Vector3 reflect(Vector3 vector, Vector3 normal) {
        float dotProduct = dot(vector, normal);
        return subtract(vector, multiply(normal, 2 * dotProduct));
    }

    public static boolean nearZero(Vector3 vector) {
        float s = 1E-8f;
        return (abs(vector.x) < s) && (abs(vector.y) < s) && (abs(vector.z) < s);
    }

    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3 negate(Vector3 vector) {
        return new Vector3(-vector.x, -vector.y, -vector.z);
    }

    public static Vector3 subtract(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3 multiply(Vector3 vector, float scalar) {
        return new Vector3(vector.x * scalar, vector.y * scalar, vector.z * scalar);
    }

    public static Vector3 multiply(Vector3 a, Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static Vector3 divide(Vector3 vector, float scalar) {

        if (scalar != 0.0f) {
            return new Vector3(vector.x / scalar, vector.y / scalar, vector.z / scalar);
        } else {
            return zero();
        }
    }

    public static Vector3 cross(Vector3 a, Vector3 b) {
        float x = a.y * b.z - a.z * b.y;
        float y = a.z * b.x - a.x * b.z;
        float z = a.x * b.y - a.y * b.x;
        return new Vector3(x, y, z);
    }

    public static Vector3 divide(Vector3 a, Vector3 b) {
        if (b.x != 0 && b.y != 0 && b.z != 0) {
            return new Vector3(a.x / b.x, a.y / b.y, a.z / b.z);
        } else {
            return Vector3.zero();
        }
    }

    public static float dot(Vector3 a, Vector3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3 forward() {
        return new Vector3(0f, 0f, -1f);
    }

    public static Vector3 back() {
        return new Vector3(0f, 0f, 1f);
    }

    public static Vector3 left() {
        return new Vector3(-1f, 0f, 0f);
    }

    public static Vector3 right() {
        return new Vector3(1f, 0f, 0f);
    }

    public static Vector3 up() {
        return new Vector3(0f, 1f, 0f);
    }

    public static Vector3 down() {
        return new Vector3(0f, -1f, 0f);
    }

    @Override
    public String toString() {
        return "Vector3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    public Vector3 copy() {
        return new Vector3(x, y, z);
    }
}
