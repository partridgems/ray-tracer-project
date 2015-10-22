package cs155.demos;

import cs155.animation.AnimatedScene3D;
import cs155.animation.Animator3D;
import cs155.animation.GifCanvas3D;
import cs155.core.*;
import cs155.objects.QuadricSurface;
import cs155.objects.Sphere3D;

import java.io.IOException;

/**
 * This class performs some very basic tests for Fisheye Cameras
 *
 */
public class ExpandingHyperbloid {

    public static void main(String[] args) throws InterruptedException, IOException {

        System.setProperty("java.awt.headless", "true"); // Prevents stupid window from opening when running the program
        System.out.println("getting ready to draw scene");
        AnimatedScene3D scene = initScene2();
        RayTracer3D.drawAnimatedGif(scene);
    }

    public static AnimatedScene3D initScene2() {
    	AnimatedScene3D scene = new AnimatedScene3D("DemoScene8");
//        Material mat1 = new Material(Color3D.BLACK, Color3D.BLACK, Color3D.WHITE, Color3D.WHITE, 200);

        // Sphere
        QuadricSurface q1 = new QuadricSurface(.5, .3, -.8, 0, 0, 0, 0, 0, 0, -1);
        QuadricSurface q2 = new QuadricSurface(.5, .3, -.8, 0, 0, 0, 0, 0, 0, -1);
        QuadricSurface q3 = new QuadricSurface(.5, .5, -1, 0, 0, 0, 0, 0, 0, -1);
        q1.setBounds(new Point3D(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE),
                new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE, 1.5));
        q2.setBounds(new Point3D(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE),
                new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE, 1.5));
        q3.setBounds(new Point3D(Integer.MIN_VALUE, Integer.MIN_VALUE, -1.5),
                     new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE, 1.5));
        q1.setTransform(new Transform3D().translate(5, -5, -10).rotateX(60d));
        q2.setTransform(new Transform3D().translate(-5, 5, -10).rotateX(-60d));
        q3.setTransform(new Transform3D().translate(0, 0, -10).rotateX(60d));
        Light3D light1 = new Light3D(new Point3D(5, 5, 10), 1.0);
        GifCanvas3D mc = new GifCanvas3D("ExpandingHyperbloid", 400, 400);

        // Uncomment one camera to choose which one is active
        Camera3D cam = new Camera3D(mc);
        // cam.apply(new Transform3D().translate(0, 0, 5));
        //cam.apply(new Transform3D().rotateY(45d).rotateX(90d).rotateZ(90d)
        //       .translate(0d, 5d, 10d));

//        scene.add(transformed);
//        scene.add(sp1);
//        scene.add(q1);
//        scene.add(q2);
        Sphere3D s = new Sphere3D(new Point3D(0, 0, -10), 1);
        scene.add(s);
        scene.add(q3);
        scene.add(light1);
        scene.add(cam);

        scene.setAnim(new Animator3D() {

            int currFrame = 0;
            int NUM_SECS = 3;
            int FPS = 24;
            int NUM_FRAMES = FPS * NUM_SECS;

            @Override
            public boolean hasNext() {
                return currFrame < NUM_FRAMES;
            }

            @Override
            public Scene3D next() {
                // First second of scene
                System.out.println("Scene # = " + currFrame / FPS);
                double expansionRate = 1.0 / FPS;
                double sphereRiseRate = 1.0 / FPS;
                if (currFrame / FPS < 1) {
                    q3.changeBounds(new Point3D(0, 0, -currFrame * expansionRate), new Point3D(0, 0, currFrame * expansionRate));
                    System.out.println("q3 bounds = " + q3.getMinBounds() + "\t" + q3.getMaxBounds());
                    s.applyTrans(new Transform3D().translate(0, 2 * sphereRiseRate, 0));
                } else if (currFrame / FPS < 2) {
                    double degreesToRotate = 45.0 / FPS;
                    s.applyTrans(new Transform3D().translate(expansionRate, expansionRate, expansionRate));
                    q3.applyTrans(new Transform3D().rotateX(degreesToRotate));
                }
                ++currFrame;
                return scene;
            }
        });
        return scene;

    }

}
