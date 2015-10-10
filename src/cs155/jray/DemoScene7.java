package cs155.jray;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This class performs some very basic tests for Fisheye Cameras
 * 
 */
public class DemoScene7 {
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


	private static Scene3D initScene2() {

		Material mat1 = new Material(Color3D.BLACK, Color3D.BLACK, Color3D.WHITE, new Color3D(0d, 1d, 0d), 200);

		Sphere3D sp1 = new Sphere3D(new Point3D(5, 5, -5), 2.0, mat1);
		Sphere3D sp2 = new Sphere3D(new Point3D(5, -5, -5), 2.0, mat1);
		Sphere3D sp3 = new Sphere3D(new Point3D(-5, 5, -5), 2.0, mat1);
		Sphere3D sp4 = new Sphere3D(new Point3D(-5, -5, -5), 2.0, mat1);
		Light3D light1 = new Light3D(new Point3D(0, 5, -2), 1.0);
		Film film = new Film(800, 800);
		
		// Uncomment one camera to choose which one is active
//		Camera3D cam = new Camera3D(film);
		Camera3D cam = new FisheyeCamera3D(film);

		scene.add(sp1);
		scene.add(sp2);
		scene.add(sp3);
		scene.add(sp4);
		scene.add(light1);
		scene.add(cam);
		return scene;

	}

}
