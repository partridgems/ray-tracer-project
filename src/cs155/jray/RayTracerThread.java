package cs155.jray;

/**
 * One thread of the engine, for rendering
 * @author Michael
 *
 */
public class RayTracerThread implements Runnable{
	
	int threadNum;
	int oversamples;
	Scene3D scene;

	
	public RayTracerThread(int threadNum, int oversamples, Scene3D scene) {
		this.threadNum = threadNum;
		this.oversamples = oversamples;
		this.scene = scene;
	}

	@Override
	public void run() {
		// for each point...
		for (int i = threadNum; i < scene.camera.film.width(); i+=RayTracer3D.THREAD_COUNT) {
			
			if (i%100 == 0 && i > 0) {
				System.out.print(" " + i);
			}
			for (int j = 0; j < scene.camera.film.height(); j++) {

				// Sample a bunch of points near it and average
				Color3D pixelColorSamples[] = new Color3D[oversamples];
				for (int sample = 0; sample < oversamples; sample++) {
					Ray3D r1 = scene.camera.generateRay(i, j); // generate a ray
					if (r1 == Ray3D.NOT_IN_SCENE) {  // This pixel is outside of the lens we are using (used in FishEye)
						pixelColorSamples[sample] = Color3D.BLACK;
					} else {
						pixelColorSamples[sample] = RayTracer3D.computeColor(r1, scene, scene.reflectionDepth); // compute its color
					}
				} // End of antialiasing sampling

				// Combine antialiased samples
				Color3D pixelColor = Color3D.averageSet(pixelColorSamples);

				// Paint pixel
				scene.camera.film.drawPixel(i,j, pixelColor.toColor());
			}
		} // End of painting points
	}

}
