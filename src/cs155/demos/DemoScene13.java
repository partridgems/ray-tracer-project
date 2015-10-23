package cs155.demos;

import cs155.aesthetic.CheckerTexture;
import cs155.aesthetic.DepthFieldCamera3D;
import cs155.aesthetic.ImageTexture;
import cs155.aesthetic.Material;
import cs155.animation.AnimatedScene3D;
import cs155.animation.Animator3D;
import cs155.animation.GifCanvas3D;
import cs155.core.*;
import cs155.objects.Cylinder3D;
import cs155.objects.Plane3D;
import cs155.objects.Sphere3D;

import java.io.IOException;

/**
 * This class draws cylinders and other objects.
 * In PA03 it is used to demonstrate the animation capability by shooting a basketball through a hoop..
 *
 * @author Eden
 */
public class DemoScene13 {

    /**
     * this creates a window to demo the Canvas3D object
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true"); // Prevents stupid window from opening when running the program
        drawOnPNG();
    }

    /**
     * This method uses the alternate PngCanvas3D to draw the scene in a png file for better portability.
     */
    public static void drawOnPNG() {
        AnimatedScene3D scene = new AnimatedScene3D("DemoScene13");
        initScene(scene);
        try {
            RayTracer3D.drawAnimatedGif(scene);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("\nDone.");
    }


    private static void initScene(AnimatedScene3D scene) {

        Material mat1 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d, 0d, 0d), new Color3D(1d, 1d, 1d), new Color3D(1d, 1d, 1d), 200);
        Material mat2 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d, 0d, 0d), new Color3D(1d, 1d, 0d), new Color3D(1d, 1d, 1d), 200);
        Material mat3 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d, 0d, 0d), new Color3D(0d, 1d, 1d), new Color3D(1d, 1d, 1d), 200);
        Material mat4 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d, 0d, 0d), new Color3D(1d, 0d, 1d), new Color3D(1d, 1d, 1d), 200);
        Material mat5 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d, 0d, 0d), new Color3D(1d, 0d, 1d), new Color3D(1d, 1d, 1d), 200);

        ImageTexture ballTexture = new ImageTexture("images/ballTexture.png");
        ballTexture.scale(4000, 2666);
        mat1.setTexture(ballTexture);
        mat1.setTexWeight(0.95); //75;

        ImageTexture basketTexture = new ImageTexture("images/basketTexture.png");
        basketTexture.scale(50, 50);
        mat2.setTexture(basketTexture);
        mat2.setTexWeight(0.95); //75;

        ImageTexture courtTexture = new ImageTexture("images/courtTexture.png");
        courtTexture.scale(50, 50);
        mat3.setTexture(courtTexture);
        mat3.setTexWeight(0.95); //75;


        Sphere3D ball = new Sphere3D(new Point3D(10, 5, -30), 15.0, mat1);
        Cylinder3D basket = new Cylinder3D(new Point3D(50, 30, -200), new Point3D(0, 1, 0), 30.0, 25.0, mat2);
        Cylinder3D post = new Cylinder3D(new Point3D(50, 0, -215), new Point3D(0, 1, 0), 5 , 50, mat5);
        Plane3D court = new Plane3D(new Point3D(0, -1, 0), new Point3D(0, 1, 0), mat3);

        // create the lights
        Light3D light1 = new Light3D(new Point3D(-12, 22, 18), .8);// this light comes from above on the left
        Light3D light2 = new Light3D(new Point3D(30, 15, 65), .6); // this light is from way above the scene

        basket.setInsideMat(mat5); // here we change the material on the inside of cylinder 2

        GifCanvas3D mc = new GifCanvas3D("Basketball", 800, 800);
        Transform3D camTransf = new Transform3D();
        // this transformation takes a few from above and to the right looking down at the cylinders
        // comment it out to see the view from the origin..
        camTransf = camTransf.translate(0, 0, 0).rotateY(20).rotateX(45).translate(0, 50, 60);
        Camera3D cam = new DepthFieldCamera3D(mc, camTransf, 54, 10);

        scene.add(ball);
        scene.add(basket);
        scene.add(post);
        scene.add(court);
        scene.add(cam);

        scene.setAnim(new Animator3D() {    //Implemented Animator3D methods, which allows for animations.
	    int YDir = 1;
            int ZDir = -1;
	    double yDelta = 6 * YDir;
            double zDelta = yDelta * .5;
	    final double originalZ = ball.getCenter().getZ();
	    final double originalX = ball.getCenter().getX();
	    final double basketZ = basket.center.getZ();
            final double distance = Math.abs(basketZ - originalZ);
            double count = 0;
            double frames = distance/zDelta;
	    final double basketX = basket.center.getX();
	    final double deltaX = (basketX - originalX)/frames;
	    double currentZ = originalZ;
	    double correction = 30/frames;

            @Override
            public boolean hasNext() {
                return count < frames + 5;
            }

            @Override
            public Scene3D next() {
		cam.setTransform(Transform3D.compose(cam.getTransform(), new Transform3D().translate(0,.05*yDelta,0.01*yDelta)));
		if (Math.abs(basketZ - currentZ) < Math.abs(originalZ - currentZ)){
			YDir = -1;
		}
		
                ball.applyTrans(new Transform3D().translate(deltaX, yDelta*YDir, (zDelta+correction)*-1));
		currentZ -= zDelta;
                count+=1;
                return scene;
            }
        });

    }

}

