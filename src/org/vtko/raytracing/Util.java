package org.vtko.raytracing;

import java.io.PrintWriter;
import java.util.Random;

import static org.vtko.raytracing.Vector3.*;

public final class Util {
    public static void writeColor(PrintWriter out, Vector3 pixelColor) {

        out.print((int) (pixelColor.x * 255f) + " " + (int) (pixelColor.y * 255f) + " " + (int) (pixelColor.z * 255f) + " " + "\n");

        System.out.println((pixelColor.x * 255f) + " " + (pixelColor.y * 255f) + " " + (pixelColor.z * 255f) + " " + "\n");
    }

    public static void writeColor(PrintWriter out, Vector3 pixelColor, int samplesPerPixel) {

        float scale = 1f / samplesPerPixel;
        float r = pixelColor.x;
        float g = pixelColor.y;
        float b = pixelColor.z;

        r = r * scale;
        g = g * scale;
        b = b * scale;

        r = linearToGamma(r);
        g = linearToGamma(g);
        b = linearToGamma(b);

        Interval intensity = new Interval(0.0f, 0.999f);

        out.print((int) (intensity.clamp(r) * 256f) + " " + (int) (intensity.clamp(g) * 256f) + " " + (int) (intensity.clamp(b) * 256f) + " " + "\n");

    }

    public static float infinity() {
        return Float.MAX_VALUE;
    }

    public static float pi() {
        return 3.1415926535897932385f;
    }

    public static float degreesToRadians(float degrees) {
        return degrees * pi() / 180f;
    }

    public static float randomNumber() {
        return new Random().nextFloat(0f, 1f);
    }

    public static float randomNumber(float min, float max) {
        return new Random().nextFloat(min, max);
    }

    public static Vector3 randomVec3() {
        return new Vector3(randomNumber(), randomNumber(), randomNumber());
    }

    public static Vector3 randomVec3(float min, float max) {
        return new Vector3(randomNumber(min, max), randomNumber(min, max), randomNumber(min, max));
    }

    public static Vector3 randomInUnitSphere() {
        while (true) {
            Vector3 p = randomVec3(-1f, 1f);
            if (p.lengthSquared() < 1) {
                return p;
            }
        }
    }

    public static Vector3 randomUnitVec3() {
        return unitVector(randomInUnitSphere());
    }

    public static Vector3 randomOnHemisphere(Vector3 normal) {
        Vector3 onUnitSphere = randomUnitVec3();
        if(dot(onUnitSphere, normal) > 0.0) {
            return onUnitSphere;
        } else {
            return negate(onUnitSphere);
        }
    }

    public static float linearToGamma(float linearComponent) {
        return (float)Math.sqrt(linearComponent);
    }

    public static Vector3 refract(Vector3 uv, Vector3 n, float etaIOverEtaT) {
        float cosTheta = (float)Math.min(dot(negate(uv), n), 1.0);
        Vector3 rOutPerp = multiply(add(uv, multiply(n, cosTheta)), etaIOverEtaT);
        Vector3 rOutParallel = multiply(n, (float)-Math.sqrt(Math.abs(1f - rOutPerp.lengthSquared())));
        return add(rOutPerp, rOutParallel);
    }

    public static Vector3 randomInUnitDisk() {
        while (true) {
            Vector3 point = new Vector3(randomNumber(-1f, 1f), randomNumber(-1f, 1f), 0f);
            if(point.length() < 1) {
                return point;
            }
        }
    }
}
