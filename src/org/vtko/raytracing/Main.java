package org.vtko.raytracing;


import java.util.Vector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.vtko.raytracing.Util.*;
import static org.vtko.raytracing.Vector3.multiply;
import static org.vtko.raytracing.Vector3.subtract;

public class Main {

    public static void main(String[] args) {

        HittableList world = new HittableList();
        //Material materialGround = new LambertianMaterial(new Vector3(0.8f, 0.8f, 0f));
        //Material materialCenter = new LambertianMaterial(new Vector3(0.3f, 0.2f, 0.8f));
        //Material materialLeft = new DielectrictMaterial(1.5f);
        //Material materialRight = new MetallicMaterial(new Vector3(0.8f, 0.6f, 0.2f), 1.0f);
//
        //// World
        //HittableList world = new HittableList();
        //world.add(new Sphere(new Vector3(0f, -100.5f, -1f), 100f, materialGround));
        //world.add(new Sphere(new Vector3(0f, 0f, -1f), 0.5f, materialCenter));
        //world.add(new Sphere(new Vector3(-1.0f, 0.0f, -1.0f), -0.4f, materialLeft));
        ////world.add(new Sphere(new Vector3(0.5f, 0f, -1f), 0.5f, materialCenter2));
        //world.add(new Sphere(new Vector3(-1f, 0f, -1f), 0.5f, materialLeft));
        //world.add(new Sphere(new Vector3(1f, 0f, -1f), 0.5f, materialRight));

//        Material materialGround = new LambertianMaterial(new Vector3(0.8f, 0.8f, 0.0f));
//        Material materialCenter = new LambertianMaterial(new Vector3(0.1f, 0.2f, 0.5f));
//        Material materialLeft = new DielectrictMaterial(1.5f);
//        Material materialRight = new MetallicMaterial(new Vector3(0.8f, 0.6f, 0.2f), 0.0f);
//
//        world.add(new Sphere(new Vector3(0f, -100.5f, -1f), 100f, materialGround));
//        world.add(new Sphere(new Vector3(0f, 0f, -1f), 0.5f, materialCenter));
//        world.add(new Sphere(new Vector3(-1f, 0f, -1f), 0.5f, materialLeft));
//        world.add(new Sphere(new Vector3(-1f, 0f, -1f), -0.4f, materialLeft));
//        world.add(new Sphere(new Vector3(1f, 0f, -1f), 0.5f, materialRight));
//
//        Camera cam = new Camera();
//        cam.aspectRatio = 16f / 9f;
//        cam.imageWidth = 720;
//        cam.samplesPerPixel = 100;
//        cam.maxDepth = 50;
//        cam.antiAliasingSquareSize = 1f;
//
//        cam.vFov = 20f;
//        cam.lookFrom = new Vector3(-2f,2f,1f);
//        cam.lookAt = new Vector3(0f, 0f, -1f);
//        cam.viewUp = new Vector3(0f, 1f, 0f);
//
//        cam.defocusAngle = 10f;
//        cam.focusDistance = 3.4f;
//
//        cam.render(world);

        var ground_material = new LambertianMaterial(new Vector3(0.5f, 0.5f, 0.5f));
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, ground_material));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                var choose_mat = randomNumber();
                Vector3 center = new Vector3(a + 0.9f * randomNumber(), 0.2f, b + 0.9f * randomNumber());

                if (subtract(center, new Vector3(4f, 0.2f, 0f)).length() > 0.9f) {
                    Material sphere_material;

                    if (choose_mat < 0.8) {
                        // diffuse
                        var albedo = multiply(randomUnitVec3(), randomVec3());
                        sphere_material = new LambertianMaterial(albedo);
                        world.add(new Sphere(center, 0.2f, sphere_material));
                    } else if (choose_mat < 0.95) {
                        // metal
                        var albedo = randomVec3(0.5f, 1f);
                        var fuzz = randomNumber(0, 0.5f);
                        sphere_material = new MetallicMaterial(albedo, fuzz);
                        world.add(new Sphere(center, 0.2f, sphere_material));
                    } else {
                        // glass
                        sphere_material = new DielectrictMaterial(1.5f);
                        world.add(new Sphere(center, 0.2f, sphere_material));
                    }
                }
            }
        }

        var material1 = new DielectrictMaterial(1.5f);
        world.add(new Sphere(new Vector3(0, 1, 0), 1.0f, material1));

        var material2 = new LambertianMaterial(new Vector3(0.4f, 0.2f, 0.1f));
        world.add(new Sphere(new Vector3(-4, 1, 0), 1.0f, material2));

        var material3 = new MetallicMaterial(new Vector3(0.7f, 0.6f, 0.5f), 0.0f);
        world.add(new Sphere(new Vector3(4, 1, 0), 1.0f, material3));

        Camera cam = new Camera();

        cam.aspectRatio = 16.0f / 9.0f;
        cam.imageWidth = 3120;
        cam.samplesPerPixel = 50;
        cam.maxDepth = 20;

        cam.vFov = 20;
        cam.lookFrom = new Vector3(13, 2, 3);
        cam.lookAt = new Vector3(0, 0, 0);
        cam.viewUp = new Vector3(0, 1, 0);

        cam.defocusAngle = 0.6f;
        cam.focusDistance = 10.0f;

        cam.render(world);
    }
}
