package org.vtko.raytracing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.tan;
import static org.vtko.raytracing.Util.*;
import static org.vtko.raytracing.Vector3.*;
import static org.vtko.raytracing.Vector3.multiply;

public class Camera {


    // Image
    public float aspectRatio = 16f / 9f;
    public int imageWidth = 400;
    public int samplesPerPixel = 10;
    private int imageHeight;
    public int maxDepth = 10;
    public float vFov = 90f;
    public Vector3 lookFrom = new Vector3(0f, 0f, -1f);
    public Vector3 lookAt = new Vector3(0f, 0f, -1f);
    public Vector3 viewUp = new Vector3(0f, 1f, 0f);
    public float antiAliasingSquareSize = 1f;
    public float defocusAngle = 0f;
    public float focusDistance = 10f;
    private Vector3 center;
    private Vector3 u, v, w;
    private Vector3 pixel00Location;
    private Vector3 pixelDeltaU;
    private Vector3 pixelDeltaV;
    private Vector3 defocusDiskU;
    private Vector3 defocusDiskV;
    private long startTime;

    public void render(HittableList world) {

        initialize();

        PrintWriter writer;

        try {
            writer = new PrintWriter(new FileWriter("/home/vitor/Github/Java/RayTracer/src/" + System.currentTimeMillis() + ".ppm"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.println("P3\n" + imageWidth + ' ' + imageHeight + "\n255\n");

        System.out.println("Complete render of " + imageWidth * imageHeight * samplesPerPixel * maxDepth + " iterations" + ": ");

        startTime = System.currentTimeMillis();

        for (int j = 0; j < imageHeight; ++j) {

            //System.out.print("\r" + "Completed render: " + (int)((float)j / imageHeight * 100f) + "%.");
            for (int i = 0; i < imageWidth; ++i) {
                Vector3 color = zero();

                printProgressBar((float) j / imageHeight, j * imageWidth + i, imageHeight * imageWidth, startTime);

                for (int sample = 0; sample < samplesPerPixel; ++sample) {
                    Ray r = getRay(i, j);
                    color = add(color, rayColor(r, maxDepth, world));
                }
                writeColor(writer, color, samplesPerPixel);
            }
        }

        System.out.print("\r" + "Done!");
    }

    private void printProgressBar(float progress, int currentIteration, int totalIterations, long startTime) {
        int width = 50;
        int blocks = (int) (progress * width);
        String cyanColor = "\u001B[36m";
        String resetColor = "\u001B[0m";

        StringBuilder progressBar = new StringBuilder("\r[");
        for (int i = 0; i < width; i++) {
            if (i < blocks) {
                progressBar.append(cyanColor).append("#");
            } else {
                progressBar.append(" ");
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long usedMemory = totalMemory - runtime.freeMemory();

        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long seconds = (elapsedTime / 1000) % 60;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        String totalIterationsStr = Integer.toString(totalIterations * samplesPerPixel * maxDepth);
        String iterationCountStr = Integer.toString(currentIteration * samplesPerPixel * maxDepth);
        int totalIterationsLength = totalIterationsStr.length();

        progressBar.append(resetColor).append("] ")
                .append(String.format("%5.2f%%", progress * 100)).append(resetColor)
                .append(" Used memory: ").append(String.format("%3d", usedMemory / (1024 * 1024))).append("MB").append(" / ")
                .append(String.format("%3d", totalMemory / (1024 * 1024))).append("MB | Elapsed time: ").append(formattedTime)
                .append(" Iteration ").append(" ".repeat(totalIterationsLength - iterationCountStr.length())).append(iterationCountStr).append(" of ").append(totalIterationsStr);

        System.out.print(progressBar);
    }


    private Ray getRay(int i, int j) {
        Vector3 pixelCenter = add(pixel00Location, add(multiply(pixelDeltaU, i), multiply(pixelDeltaV, j)));
        Vector3 pixelSample = add(pixelCenter, pixelSampleSquare());

        Vector3 rayOrigin = (defocusAngle <= 0) ? center : defocusDiskSample();
        Vector3 rayDirection = subtract(pixelSample, rayOrigin);
        float rayTime = randomNumber();
        return new Ray(rayOrigin, rayDirection, rayTime);
    }

    private Vector3 defocusDiskSample() {
        Vector3 point = randomInUnitDisk();
        return add(add(multiply(defocusDiskU, point.x), multiply(defocusDiskV, point.y)), center);
    }

    private Vector3 pixelSampleSquare() {
        float px = -0.5f * antiAliasingSquareSize + randomNumber() * antiAliasingSquareSize;
        float py = -0.5f * antiAliasingSquareSize + randomNumber() * antiAliasingSquareSize;

        return add(multiply(pixelDeltaU, px), multiply(pixelDeltaV, py));
    }

    private void initialize() {
        imageHeight = (int) (imageWidth / aspectRatio);
        imageHeight = Math.max(imageHeight, 1);

        center = lookFrom;

        float theta = (float) Math.toRadians(vFov);
        float h = (float) tan(theta / 2);
        float viewportHeight = 2f * h * focusDistance;
        float viewportWidth = viewportHeight * ((float) imageWidth / imageHeight);

        w = unitVector(subtract(lookFrom, lookAt));
        u = unitVector(cross(viewUp, w));
        v = cross(w, u);

        Vector3 viewportU = multiply(u, viewportWidth);
        Vector3 viewportV = multiply(negate(v), viewportHeight);

        pixelDeltaU = divide(viewportU, imageWidth);
        pixelDeltaV = divide(viewportV, imageHeight);
        Vector3 deltaVPlusDeltaU = add(pixelDeltaV, pixelDeltaU);
        Vector3 deltaVMinusDeltaU = subtract(pixelDeltaV, pixelDeltaU);
        Vector3 viewportUpperLeft = subtract(subtract(center, multiply(w, focusDistance)), deltaVMinusDeltaU);
        viewportUpperLeft = subtract(viewportUpperLeft, divide(viewportU, 2));
        viewportUpperLeft = subtract(viewportUpperLeft, divide(viewportV, 2));
        pixel00Location = add(viewportUpperLeft, multiply(deltaVPlusDeltaU, 0.5f));

        float defocusRadius = focusDistance * (float) tan(degreesToRadians(defocusAngle / 2f));
        defocusDiskU = multiply(u, defocusRadius);
        defocusDiskV = multiply(v, defocusRadius);
    }

    private Vector3 rayColor(Ray ray, int depth, Hittable world) {

        HitData hitData = new HitData();

        if (depth <= 0) {
            return new Vector3(0f, 0f, 0f);
        }

        if (world.hit(ray, new Interval(0.001f, infinity()), hitData)) {
            Ray scattered = new Ray();
            Vector3 attenuation = new Vector3();
            if (hitData.material.scatter(ray, hitData, attenuation, scattered)) {
                return multiply(attenuation, rayColor(scattered, depth - 1, world));
            }
            return Vector3.zero();
        }

        Vector3 unitDirection = unitVector(ray.getDirection());
        float a = 0.5f * (unitDirection.y + 1.0f);

        return add(multiply(new Vector3(1.0f, 1.0f, 1.0f), 1 - a), multiply(new Vector3(0.5f, 0.7f, 1.0f), a));
    }
}
