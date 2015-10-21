package cs155.demos;

import cs155.aesthetic.FisheyeCamera3D;
import cs155.aesthetic.Material;
import cs155.core.*;
import cs155.objects.Sphere3D;

/**
 * This class uses DemoScene1 to test the Fisheye Camera
 * 
 */
public class DemoScene6 {

	public static void main(String[] args) throws InterruptedException {

		System.setProperty("java.awt.headless", "true"); // Prevents stupid window from opening when running the program
		System.out.println("getting ready to draw scene");
		Scene3D scene = initScene2();

		RayTracer3D.drawScene(scene);

		System.out.println("drew a sphere!");
	}

//	private static Scene3D initScene() {
//
//		Material mat1 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d,
//				0d, 0d), new Color3D(1d, 1d, 1d), new Color3D(1d, 1d, 1d), 200);
//		Sphere3D sp1 = new Sphere3D(new Point3D(0, 0, -5), 2.0, mat1);
//		Sphere3D sp2 = new Sphere3D(new Point3D(6, 0, -5), 5.0, mat1);
//
//		Light3D light1 = new Light3D(new Point3D(-25, 5, -5), 0.5);
//		Light3D light2 = new Light3D(new Point3D(2, -30, -7), 0.5);
//		Film film = new Film(800, 800);
//		Camera3D cam = new Camera3D(film);
//
//		scene.add(sp1);
//		scene.add(sp2);
//		scene.add(light1);
//		scene.add(light2);
//		scene.add(cam);
//		cam.apply(new Transform3D());
//		return scene;
//
//	}

	private static Scene3D initScene2() {
		Scene3D scene = new Scene3D("DemoScene6");

		Material mat1 = new Material(Color3D.BLACK, new Color3D(1d,0d, 0d), Color3D.WHITE, new Color3D(0d, 1d, 0d), 200);
		Material mat2 = new Material(Color3D.BLACK, Color3D.BLACK, new Color3D(1d, 1d, 0d), new Color3D(1d, 1d, 1d), 200);
		Material mat3 = new Material(Color3D.BLACK, Color3D.BLACK, new Color3D(1d, 0d, 0d), Color3D.WHITE, 200);
		Material mat4 = new Material(Color3D.BLACK, Color3D.BLACK, new Color3D(0d, 1d, 0d), Color3D.WHITE, 200);
		Material mat5 = new Material(Color3D.BLACK, Color3D.BLACK, Color3D.WHITE, new Color3D(1d, 0d, 0d), 200);
		Sphere3D sp1 = new Sphere3D(new Point3D(0, 0, -5), 2.0, mat1);
		Sphere3D sp2 = new Sphere3D(new Point3D(6, 0, -5), 5.0, mat2);
		Sphere3D sp3 = new Sphere3D(new Point3D(0, 3, -5), 1.0, mat3);
		Sphere3D sp4 = new Sphere3D(new Point3D(6, 8, -2), 2.0, mat4);
		Sphere3D sp5 = new Sphere3D(new Point3D(0, -10000d, 0), 9950d, mat5);
		Light3D light1 = new Light3D(new Point3D(-25, 5, -5), 0.5);
		// light1.diffuse = new Color3D(1,0,0);
		Light3D light2 = new Light3D(new Point3D(2, 30, -5), 0.5);
		// light2.diffuse= new Color3D(0,0,1);
		PngCanvas3D mc = new PngCanvas3D(800, 800);
//		Camera3D cam = new Camera3D(mc);
		Camera3D cam = new FisheyeCamera3D(mc);

		scene.add(sp1);
		scene.add(sp2);
		scene.add(sp3);
		scene.add(sp4);
		scene.add(sp5);
		scene.add(light1);
		scene.add(light2);
		scene.add(cam);
		cam.apply(new Transform3D().rotateY(45d).rotateX(45d)
				.translate(0d, 5d, 10d));
		return scene;

	}

}
