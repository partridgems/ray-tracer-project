package cs155.demos;

import cs155.aesthetic.CheckerTexture;
import cs155.aesthetic.DepthFieldCamera3D;
import cs155.aesthetic.ImageTexture;
import cs155.aesthetic.Material;
import cs155.core.*;
import cs155.objects.Cylinder3D;
import cs155.objects.Plane3D;
import cs155.objects.Sphere3D;

import java.io.IOException;

/**
 * This class draws cylinders and other objects.
 * In PA03 it is used to demonstrate the depth of field camera.
 * @author Michael
 *
 */
public class DemoScene9 {
    
    /**
     * this creates a window to demo the Canvas3D object
     * @param args command line arguments
     */
    public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true"); // Prevents stupid window from opening when running the program
    	drawOnPNG();
    }
    
    /**
     * This method uses the alternate PngCanvas3D to draw the scene in a png file for better portability.
     */
    public static void drawOnPNG() {
    	Scene3D scene = new Scene3D("DemoScene9");
    	initScene(scene);
    	RayTracer3D.drawScene(scene);
    	System.out.println("\nDone.");
    }


	private static void initScene(Scene3D scene){

		Material mat1 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(1d,1d,1d), new Color3D(1d,1d,1d),200);
		Material mat2 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(1d,1d,0d), new Color3D(1d,1d,1d),200);
		Material mat3 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(0d,1d,1d), new Color3D(1d,1d,1d),200);
		Material mat4 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(1d,0d,1d), new Color3D(1d,1d,1d),200);
		Material mat5 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(0d,0d,1d), new Color3D(1d,1d,0d),200);

		CheckerTexture tex = new CheckerTexture();
		tex.scale(10,10);
		mat1.setTexture(tex);
		mat1.setTexWeight(0.25);
		CheckerTexture tex2 = new CheckerTexture();
		tex2.scale(1000,100);
		mat2.setTexture(tex2);
		mat2.setTexWeight(0.25);
		CheckerTexture tex3 = new CheckerTexture();
		tex3.scale(1000,1000);
		mat3.setTexture(tex3);
		mat3.setTexWeight(0.25);
		mat3.setReflect(0.0);
		
		ImageTexture tex4 = new ImageTexture("images/daisies.png");
		tex4.scale(5,5);
		mat4.setTexture(tex4);
		mat4.setTexWeight(0.0); //75;
		mat4.setReflect(0.75);
		
		ImageTexture tex5 = new ImageTexture("images/daisies.png");
		tex5.scale(50,50);
		mat5.setTexture(tex5);
		mat5.setTexWeight(0.95);
		
//		Cylinder3D cyl1 = new Cylinder3D(new Point3D(1,-1,-15),new Point3D(0,1,0),3.0,15.0,mat4);
		Cylinder3D cyl2 = new Cylinder3D(new Point3D(25, -5,-15),new Point3D(0,1,0),20.0,25.0,mat4);
		Cylinder3D cyl3 = new Cylinder3D(new Point3D(-13, 36,38),new Point3D(0,1,-.3),1.0,4.0,mat1);
		Sphere3D sp2 = new Sphere3D(new Point3D(-15,5,-20),20.0,mat4);
		Sphere3D sp3 = new Sphere3D(new Point3D(-15,50,-20),20.0,mat3);
		Plane3D pl1 = new Plane3D(new Point3D(0,-1,0),new Point3D(0,1,0),mat1);
		Plane3D pl2 = new Plane3D(new Point3D(-50,0,0),new Point3D(1,0,0.5),mat5);
		
		// create the lights
		Light3D light1 = new Light3D(new Point3D(-20,22,18),.8);// this light comes from above on the left
		Light3D light2 = new Light3D(new Point3D(30,15,55),.6); // this light is from way above the scene
//		Light3D light3 = new Light3D(new Point3D(0,2,-13),1.0); // this light is near the openings of both cylinders
//		Light3D light4 = new Light3D(new Point3D(-2,13,-4),1.0); // this light is inside cylinder 1, toward the far end
		
		cyl2.setInsideMat(mat1); // here we change the material on the inside of cylinder 2
		
    	PngCanvas3D mc = new PngCanvas3D(600, 600);
		Transform3D camTransf = new Transform3D();
		// this transformation takes a few from above and to the right looking down at the cylinders
		// comment it out to see the view from the origin..
		camTransf = camTransf.rotateY(20).rotateX(45).translate(0,0,60);
//		Camera3D cam = new Camera3D(mc,camTransf);
//		Camera3D cam = new FisheyeCamera3D(mc, camTransf);
		Camera3D cam = new DepthFieldCamera3D(mc, camTransf, 12, 10);
//		scene.add(cyl1);
		scene.add(cyl2);
		scene.add(cyl3);
		scene.add(sp2);
		scene.add(sp3);
		scene.add(pl1);
		scene.add(pl2);
		scene.add(light1);
		scene.add(light2);
//		scene.add(light3);
//		scene.add(light4);
		scene.add(cam);

	}

}

