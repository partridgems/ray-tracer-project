package cs155.demos;

import java.io.IOException;

import cs155.aesthetic.DepthFieldCamera3D;
import cs155.aesthetic.ImageTexture;
import cs155.aesthetic.Material;
import cs155.animation.AnimatedScene3D;
import cs155.animation.Animator3D;
import cs155.animation.GifCanvas3D;
import cs155.core.Camera3D;
import cs155.core.Color3D;
import cs155.core.Light3D;
import cs155.core.Point3D;
import cs155.core.RayTracer3D;
import cs155.core.Scene3D;
import cs155.core.Transform3D;
import cs155.objects.Cylinder3D;
import cs155.objects.Plane3D;
import cs155.objects.Triangle3D;

/**
 * Mike's Depth of Field Demo
 * The camera simulates walking through a small structure made of columns
 * and a roof.
 *
 */
public class DemoScene15 {
    

    public static void main(String[] args) {
    	AnimatedScene3D scene = new AnimatedScene3D("DemoScene15"); initScene(scene);
    	
    	try {
			RayTracer3D.drawAnimatedGif(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	System.out.println("\nDone.");
    }


	private static void initScene(AnimatedScene3D scene){
		// Camera and canvas setup
		GifCanvas3D mc = new GifCanvas3D("DemoScene15", 800, 700);
		Camera3D cam = new DepthFieldCamera3D(mc, 8, 12);
		cam.apply(new Transform3D().translate(0, 5, 8).rotateX(15));
		scene.add(cam);
		
		// Columns along each side
		Cylinder3D[] leftWall = new Cylinder3D[5];
		for (int i = 0; i < 5; i++) {
			Material colMat = new Material();
			colMat.setSpecular(Color3D.WHITE);
			colMat.setHardness(5);
			colMat.setReflect(0);
			colMat.setRefract(0);
			ImageTexture colTex = new ImageTexture("images/greek_pillar.jpg");
			colTex.scale(120, 70);
			colMat.setTexture(colTex);
			colMat.setTexWeight(.5);
			colTex.translate(Math.random()*1000, Math.random()*1000);
			leftWall[i] = new Cylinder3D(new Point3D(-4, 0, 2*i-4), new Point3D(0, 1, 0), .8, 10, colMat);
			scene.add(leftWall[i]);
		}
		Cylinder3D[] rightWall = new Cylinder3D[5];
		for (int i = 0; i < 5; i++) {
			Material colMat = new Material();
			colMat.setSpecular(Color3D.WHITE);
			colMat.setHardness(5);
			colMat.setReflect(0);
			colMat.setRefract(0);
			ImageTexture colTex = new ImageTexture("images/greek_pillar.jpg");
			colTex.scale(120, 70);
			colMat.setTexture(colTex);
			colMat.setTexWeight(.5);
			colTex.translate(Math.random()*1000, Math.random()*1000);
			rightWall[i] = new Cylinder3D(new Point3D(4, 0, 2*i-4), new Point3D(0, 1, 0), .8, 10, colMat);
			scene.add(rightWall[i]);
		}
		
		// A floor
		Plane3D floor = new Plane3D(new Point3D(0, 0, 0), new Point3D(0, 1, 0));
		Material floorMat = new Material(Color3D.BLACK, new Color3D(188f/256, 143f/256, 143f/256).scale(.7), new Color3D(.6, .6, .6), Color3D.BLACK, 0);
		floorMat.setReflect(0);
		floorMat.setRefract(0);
		floor.setOutsideMat(floorMat);
		scene.add(floor);
		
		// A roof
		Triangle3D frontLeft = new Triangle3D(new Point3D(-5, 10, 5), new Point3D(-5, 10, -5), 
				new Point3D(5, 10, 5));
		Triangle3D backRight = new Triangle3D(new Point3D(-5, 10, -5), new Point3D(5, 10, -5), 
				new Point3D(5, 10, 5));
		scene.add(frontLeft); scene.add(backRight);
		
		// The front sky
		Plane3D sky = new Plane3D(new Point3D(1000, 0, -1000), new Point3D(0, 0, 1));
		Material skyMat = new Material(Color3D.WHITE, Color3D.BLACK, Color3D.BLACK, Color3D.BLACK, 0);
		skyMat.setReflect(0); skyMat.setRefract(0);
		ImageTexture skyTex = new ImageTexture("images/sky.jpg");
		skyTex.scale(.5, .5);
		skyTex.translate(1080, 85);
		skyMat.setTexture(skyTex);
		skyMat.setTexWeight(1);
		sky.setOutsideMat(skyMat);
		scene.add(sky);
		
		// The back sky
		Plane3D skyb = new Plane3D(new Point3D(0, 0, 1000), new Point3D(0, 0, -1));
		Material skyMatb = new Material(Color3D.WHITE, Color3D.BLACK, Color3D.BLACK, Color3D.BLACK, 0);
		ImageTexture skyTexb = new ImageTexture("images/sky.jpg");
		skyTexb.scale(.1, .1);
		skyTexb.translate(1080, 85);
		skyMatb.setTexture(skyTexb);
		skyMatb.setTexWeight(1);
		skyb.setOutsideMat(skyMatb);
		scene.add(skyb);
		
		// The sun
		Light3D sun = new Light3D(new Point3D(0, 100, 0));
		sun.ambient = new Color3D(0.3, 0.3, 0.35);
		sun.intensity = .5;
		sun.applyTrans(new Transform3D().rotateZ(60));
		scene.add(sun);
		
		scene.setAnim(new Animator3D() {    //Implemented Animator3D methods, which allows for animations.
            int frameNum = 0; 
            int totalFrames = 24*10;  // 10 seconds * 24 FPS
            double frameSpeed = 1;	// Set higher to 'skip' frames to speed up rendering or animation

            @Override
            public boolean hasNext() {
                return frameNum < totalFrames;
            }

            @Override
            public Scene3D next() {

            	
            	// Moves the sun right to left across the sky
            	sun.applyTrans(new Transform3D().rotateZ((float)-40/totalFrames*frameSpeed));
            	// Moves the sun a little south and back (northern hemisphere
            	double sunZshift = -1*Math.cos((double)frameNum/totalFrames/2*Math.PI);
            	sun.applyTrans(new Transform3D().translate(0, 0, sunZshift));
            	// Brightens the sun near the beginning of the day and darkens it at the end
            	if (frameNum <= .2 * totalFrames) { // Morning
            		sun.intensity = .8 + ((double)frameNum)/totalFrames*1;
            	} else if (frameNum >= .8 * totalFrames) { // Evening
            		sun.intensity = .8 + ((double)totalFrames - frameNum)/totalFrames*1;
            	} else {
            		sun.intensity = 1.0;
            	}
                // Moves the clouds
                skyTex.translate(1.0/5, -.2/5);
                
                // Move the camera into the scene
                cam.setTransform(new Transform3D().translate(0, 5, 8).rotateX(5)); // Start the camera here
                double distance = 6.0 * frameNum/totalFrames;
                double leftRightAmp = .15;
                double upDownAmp = .25;
                double walkPace = 4.5;
                Transform3D walk = new Transform3D().translate( leftRightAmp * Math.cos(walkPace*distance + Math.PI/2),
                		upDownAmp * Math.sin(Math.PI/2 - 2*walkPace*distance), -distance );
                
                // A different way of modeling the camera 'walking'
//                double t = distance * 2.5 * Math.PI;
//                Transform3D walk = new Transform3D().translate(0, .2 * (1 - Math.cos(t)), -.2 * (t - Math.sin(t)));
                
                cam.apply(walk);
                
            	frameNum += frameSpeed;
                return scene;
            }
        });
	}

}

