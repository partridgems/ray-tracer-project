package cs155.jray;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This class tests out the methods in PA01
 * 
 * @author tim
 * 
 */
public class DemoScene1 {
	private static Scene3D scene = new Scene3D();
	private static NewRayCanvas3D mc = new NewRayCanvas3D(scene, 800, 800);

	/**
	 * this creates a window to demo the Canvas3D object
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		/*
		 * This is the preferred way to create a GUI. It avoid thread problems
		 * by creating the GUI in the EventDispatch thread.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

		System.out.println("getting ready to draw scene");
		Thread.sleep(200L);
		Scene3D scene = initScene2();

		RayTracer3D.drawScene(scene);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mc.refresh();
			}
		});

		System.out.println("drew a sphere!");
	}

	/*
	 * here we create a window, add the canvas, set the window size and make it
	 * visible!
	 */
	private static void createAndShowGUI() {

		JFrame f = new JFrame("PA03 Demo");

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(mc);
		f.setSize(800, 800);
		f.setVisible(true);
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

		Material mat1 = new Material(new Color3D(0d, 0d, 0d), new Color3D(1d,
				0d, 0d), new Color3D(1d, 1d, 1d), new Color3D(0d, 1d, 0d), 200);
		Material mat2 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d,
				0d, 0d), new Color3D(1d, 1d, 0d), new Color3D(1d, 1d, 1d), 200);
		Material mat3 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d,
				0d, 0d), new Color3D(1d, 0d, 0d), new Color3D(1d, 1d, 1d), 200);
		Material mat4 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d,
				0d, 0d), new Color3D(0d, 1d, 0d), new Color3D(1d, 1d, 1d), 200);
		Material mat5 = new Material(new Color3D(0d, 0d, 0d), new Color3D(0d,
				0d, 0d), new Color3D(1d, 1d, 1d), new Color3D(1d, 0d, 0d), 200);
		Sphere3D sp1 = new Sphere3D(new Point3D(0, 0, -5), 2.0, mat1);
		Sphere3D sp2 = new Sphere3D(new Point3D(6, 0, -5), 5.0, mat2);
		Sphere3D sp3 = new Sphere3D(new Point3D(0, 3, -5), 1.0, mat3);
		Sphere3D sp4 = new Sphere3D(new Point3D(6, 8, -2), 2.0, mat4);
		Sphere3D sp5 = new Sphere3D(new Point3D(0, -10000d, 0), 9950d, mat5);
		Light3D light1 = new Light3D(new Point3D(-25, 5, -5), 0.5);
		// light1.diffuse = new Color3D(1,0,0);
		Light3D light2 = new Light3D(new Point3D(2, 30, -5), 0.5);
		// light2.diffuse= new Color3D(0,0,1);
		Film film = new Film(800, 800);
		Camera3D cam = new Camera3D(film);

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
