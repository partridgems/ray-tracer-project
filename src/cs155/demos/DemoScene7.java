package cs155.demos;

import cs155.aesthetic.FisheyeCamera3D;
import cs155.aesthetic.Material;
import cs155.core.*;
import cs155.objects.Sphere3D;

/**
 * This class performs some very basic tests for Fisheye Cameras
 * 
 */
public class DemoScene7 {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("java.awt.headless", "true"); // Prevents stupid window from opening when running the program
		System.out.println("getting ready to draw scene");
		Scene3D scene = initScene2();

		RayTracer3D.drawScene(scene);

		System.out.println("drew a sphere!");
	}


	private static Scene3D initScene2() {
		Scene3D scene = new Scene3D("DemoScene7");

		Material mat1 = new Material(Color3D.BLACK, Color3D.BLACK, Color3D.WHITE, new Color3D(0d, 1d, 0d), 200);

		Sphere3D sp1 = new Sphere3D(new Point3D(5, 5, -5), 2.0, mat1);
		Sphere3D sp2 = new Sphere3D(new Point3D(5, -5, -5), 2.0, mat1);
		Sphere3D sp3 = new Sphere3D(new Point3D(-5, 5, -5), 2.0, mat1);
		Sphere3D sp4 = new Sphere3D(new Point3D(-5, -5, -5), 2.0, mat1);
		Light3D light1 = new Light3D(new Point3D(0, 5, -2), 1.0);
		PngCanvas3D mc = new PngCanvas3D(800, 800);
		
		// Uncomment one camera to choose which one is active
//		Camera3D cam = new Camera3D(mc);
		Camera3D cam = new FisheyeCamera3D(mc);

		scene.add(sp1);
		scene.add(sp2);
		scene.add(sp3);
		scene.add(sp4);
		scene.add(light1);
		scene.add(cam);
		return scene;

	}

}
