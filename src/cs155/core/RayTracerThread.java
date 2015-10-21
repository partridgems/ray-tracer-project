package cs155.core;

/**
 * One thread of the engine, for rendering
 * @author Michael
 *
 */
public class RayTracerThread implements Runnable{
	
	private int threadNum;
	private int oversamples;
	private Scene3D scene;

	
	public RayTracerThread(int threadNum, int oversamples, Scene3D scene) {
		this.threadNum = threadNum;
		this.oversamples = oversamples;
		this.scene = scene;
	}

	@Override
	public void run() {
		// for each point...
		for (int i = threadNum; i < scene.getCamera().getFilm().width(); i+=RayTracer3D.THREAD_COUNT) {
			
			if (i%100 == 0 && i > 0) {
				System.out.print(" " + i);
			}
			for (int j = 0; j < scene.getCamera().getFilm().height(); j++) {

				// Sample a bunch of points near it and average
				Color3D pixelColorSamples[] = new Color3D[oversamples];
				for (int sample = 0; sample < oversamples; sample++) {
					Ray3D r1 = scene.getCamera().generateRay(i, j); // generate a ray
					if (r1 == Ray3D.NOT_IN_SCENE) {  // This pixel is outside of the lens we are using (used in FishEye)
						pixelColorSamples[sample] = Color3D.BLACK;
					} else {
						pixelColorSamples[sample] = RayTracer3D.computeColor(r1, scene, scene.getReflectionDepth()); // compute its color
					}
				} // End of antialiasing sampling

				// Combine antialiased samples
				Color3D pixelColor = Color3D.averageSet(pixelColorSamples);

				// Paint pixel
				scene.getCamera().getFilm().drawPixel(i, j, pixelColor.toColor());
			}
		} // End of painting points
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getOversamples() {
		return oversamples;
	}

	public void setOversamples(int oversamples) {
		this.oversamples = oversamples;
	}

	public Scene3D getScene() {
		return scene;
	}

	public void setScene(Scene3D scene) {
		this.scene = scene;
	}
}
