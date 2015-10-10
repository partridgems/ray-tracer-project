package cs155.jray;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This class tests out the methods in PA01
 * 
 * @author tim
 * 
 */
public class DemoScene4 {
	private static Scene3D scene = new Scene3D();
	static int N = 800;
	private static NewRayCanvas3D mc = new NewRayCanvas3D(scene, N,N);

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
		mc.scene = Demos.demo3();

		RayTracer3D.drawScene(mc.scene);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
					mc.refresh();
			}
		});
		// mc.refresh();
		// mc.invalidate();
//		System.out.println("drew a circle!");
	}

	/*
	 * here we create a window, add the canvas, set the window size and make it
	 * visible!
	 */
	private static void createAndShowGUI() {

		JFrame f = new JFrame("PA06 Demo");

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(mc);
		f.setSize(N,N);
		f.setVisible(true);
	}




}
