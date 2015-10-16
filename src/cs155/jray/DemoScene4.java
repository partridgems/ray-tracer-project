package cs155.jray;

/**
 * This class tests out the methods in PA01
 * 
 * @author tim
 * 
 */
public class DemoScene4 {

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

		System.out.println("getting ready to draw scene");
		Thread.sleep(200L);
		Scene3D scene = Demos.demo3();

		RayTracer3D.drawScene(scene);

		scene.camera.film.refresh();
	}




}
