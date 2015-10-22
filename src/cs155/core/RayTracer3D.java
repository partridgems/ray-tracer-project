package cs155.core;

import cs155.aesthetic.Material;
import cs155.animation.AnimatedScene3D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This consists of static methods for drawing a scene on a canvas.
 **/

public class RayTracer3D {

	protected static final int THREAD_COUNT = 3 * Runtime.getRuntime().availableProcessors();
	protected static final int ANTI_ALIASING = 1;

	/**
	 * Compute the color of the pixel corresponding to the intersection of the
	 * ray r with the first object in the scene s
	 **/
	public static Color3D computeColor(Ray3D r, Scene3D s, int depth) {

		/*
		 * first we intersect the ray with the scene and get the RayHit object
		 */
		RayHit hit = s.firstIntersection(r);

		// if the ray hits no object, return the background color
		if (hit == RayHit.NO_HIT)
			return s.getBackgroundColor();

		/*
		 * if the ray hits the inside of the object (as determined by the
		 * outward facing normal) then flip the normal to also point inward and
		 * use the inside material
		 */
		Material m;
		boolean outside = (hit.getNormal().dot(r.getDirection()) < 0);
		if (!outside) {
			m = hit.getObj().getInsideMat();
			hit.setNormal(hit.getNormal().scale(-1d)); // flip the normal when the ray
												// hits on the inside
		} else {
			m = hit.getObj().getOutsideMat();
		}

		// calculate the initial color of the pixel by setting it to the
		// emissive color
		Color3D pixelColor = m.getEmissive();

		// next calculate the ambient color of the pixel by multiplying
		// and add it to the pixel color, ambient is independent of the lights

		Color3D ambientColor = s.getAmbient().times(m.getAmbient());
		pixelColor = pixelColor.add(ambientColor);

		// next, for each light which is not obscured at the hitPoint
		// calculate its contribution to the pixel color and add it to the
		// pixelcolor
		for (int k = 0; k < s.getLights().size(); k++) {
			if (isObscured(s.getLights().get(k), hit.getHitPoint(), hit.getObj(), s)) {
				continue;
			}

			Color3D localColor = calcColorForLight(r, hit.getNormal(), hit.getHitPoint(),
					m, s.getLights().get(k));
			pixelColor = pixelColor.add(localColor);
		}


		// finally, calculate the texture color
		Color3D textureColor = m.getTexture().getColor(hit.getTc());
		// and average it with the pixel color using mat.TEX_WEIGHT
		pixelColor = pixelColor.averageIn(textureColor, m.getTexWeight());

		if ((m.getReflect() > 0) && (depth > 0)) {
			Color3D reflectColor;
			/*
			 * calculate the reflection ray and move the starting point a little
			 * above the surface to avoid self-intersection. Calculate the color
			 * corresponding to that ray (with depth-1 level of recursion)
			 * average in the reflectColor with the current pixel color using
			 * the constant m.REFLECT. So m.REFLECT=0 means you would ignore the
			 * color for the REFLECT ray and 0.5 would mean you average the
			 * material color and the reflected ray color.
			 */

			// put in a couple of lines of code here for PA05!!
			Ray3D reflectRay = calcReflectionRay(r,hit);
			reflectColor = computeColor(reflectRay,s,depth-1);

			pixelColor = pixelColor.averageIn(reflectColor, m.getReflect());
		}

		return pixelColor;
	}

	/**
	 * calculate the ray r' that you obtain by reflecting the ray r off of the
	 * hitpoint h
	 * 
	 * @param r the ray
	 * @param h the ray hit
	 * @return a reflected ray
	 */
	private static Ray3D calcReflectionRay(Ray3D r, RayHit h) {

		// put in a few lines of code HERE for PA05!!
		Point3D z = h.getNormal().scale(h.getNormal().dot(r.getDirection()));
		Point3D d1 = r.getDirection().subtract(z.scale(2));
		Point3D p1 = h.getHitPoint().add(d1.scale(0.0001));
		return new Ray3D(p1,d1);
	}

	/**
	 * return true if the ray starting at the point and heading toward the light
	 * hits an object on its way there, you'll need to be careful about the
	 * intersection of the point with the object it is on (with distance zero or
	 * close to zero!) that one shouldn't count as obscuring the light...
	 * 
	 * @param light the light coming in
	 * @param point the point of intersection
	 * @param scene the scene in question
	 * @return boolean determines whether the point is obscured
	 */
	private static boolean isObscured(Light3D light, Point3D point,
			Object3D obj, Scene3D scene) {
		Point3D lightVec = light.getLocation().subtract(point);
		double distToLight = lightVec.length();
		Point3D lightVecNorm = lightVec.normalize();
		Point3D p2 = point.add(lightVecNorm.scale(0.01));
		Ray3D r = new Ray3D(p2, lightVecNorm);
		RayHit hit = scene.firstIntersection(r);
		if (hit == RayHit.NO_HIT)
			return false;
		double distToObj = (hit.getHitPoint().subtract(point)).length();
		return (distToLight > distToObj);
	}

	/**
	 * Calculate the color of a pixel corresponding to a specified light. This
	 * method calculates the diffuse, specular, and ambient components of the
	 * lighting and adds them together to get the final result
	 * 
	 * @param r
	 *            the ray corresponding to the pixel
	 * @param n
	 *            the normal to the surface at the intersection point
	 * @param p
	 *            the point of where the ray intersects the object
	 * @param m
	 *            the material of the object
	 * @param light
	 *            the light being considered
	 * @return a new Color3D object denoting color
	 */
	public static Color3D calcColorForLight(Ray3D r, Point3D n, Point3D p,
			Material m, Light3D light) {

		// Find the normalized light vector
		Point3D lightPos = light.getLocation();
		Point3D lightVec = lightPos.subtract(p);
		lightVec = lightVec.normalize(); // vector from point to the light

		// calculate the local ambient pixel color contribution
		Color3D localAmbient = m.getAmbient().times(light.ambient);

		// calculate the local diffuse pixel color contribution
		Color3D localDiffuse = calculateDiffuse(n, m.getDiffuse(), light.diffuse,
                lightVec);

		// calculate the local specular pixel color contribution
		// which requires the vector from the eye to the intersection point
		Color3D localSpecular = calculateSpecular(r, n, p, m.getSpecular(),
				m.getHardness(), light.specular, lightVec);

		// now combine all of the local colors (ambient, diffuse, specular)
		Color3D localColor = localAmbient.add(localDiffuse).add(localSpecular);

		// and scale by the intensity of the light
		localColor = localColor.scale(light.intensity);
		return localColor;
	}

	/**
	 * calculate the diffuse color, by calculating the diffuse intensity and
	 * multiplying it by the diffuse material color and the diffuse lighting
	 * color
	 * 
	 * @param n the point to calculate diffusion at
	 * @param matColor the mat color
	 * @param lightColor the light color
	 * @param lightVec the light vector
	 * @return the Color3D object denoting the color
	 */
	private static Color3D calculateDiffuse(Point3D n, Color3D matColor,
			Color3D lightColor, Point3D lightVec) {
		Color3D localDiffuse;
		double diffuseIntensity = Light3D.diffuse(lightVec, n);
		localDiffuse = matColor.times(lightColor).scale(diffuseIntensity);
		return localDiffuse;
	}

	/**
	 * calculate the specular color, by calculating the specular intensity and
	 * multiplying it by the diffuse material color and the diffuse lighting
	 * color
	 * 
	 * @param r
	 *            the ray from the camera (i.e. the eye)
	 * @param n
	 *            the normal
	 * @param p
	 *            the point of intersection
	 * @param matSpecular
	 *            the specular material color
	 * @param matHardness
	 *            the specular hardness coefficient
	 * @param lightSpecular
	 *            the specular color of the light
	 * @param lightVec
	 *            the vector pointing to the light
	 * @return Color3D object denoting color
	 */
	private static Color3D calculateSpecular(Ray3D r, Point3D n, Point3D p,
			Color3D matSpecular, int matHardness, Color3D lightSpecular,
			Point3D lightVec) {
		Color3D localSpecular;
		Point3D eyeVec = r.getPoint().subtract(p).normalize();
		double specularIntensity = Light3D.specular(lightVec, n, eyeVec,
				matHardness);
		localSpecular = matSpecular.times(lightSpecular).scale(
				specularIntensity);
		return localSpecular;
	}

	/**
	 * draw a view of the scene on a canvas. The method should get the width and
	 * height of the film from the camera then it should loop through each pixel
	 * on the film and do the following - use the camera to generate a ray for
	 * that pixel - compute the color corresponding to that ray - draw that
	 * color to the corresponding pixel on the film
	 **/
	public static void drawScene(Scene3D scene) {
		scene.setReflectionDepth(15);

		// Start up THEAD_COUNT threads to do rendering and join on them
		Thread[] tarray = new Thread[THREAD_COUNT];
		for (int tnum = 0; tnum < THREAD_COUNT; tnum++) {
			tarray[tnum] = new Thread(new RayTracerThread(tnum, ANTI_ALIASING, scene));
			tarray[tnum].start();
		}
		
		for (Thread t : tarray) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		scene.save();
	} // close drawScene
	
	/**
	 * Draw a series of scenes, numbering them and saving them in a folder
	 * @throws IOException 
	 */
	public static void drawSceneSeries(Scene3D scene, int numScenes) throws IOException {
		// Get the scene name for the folder
		String sceneName = scene.getName();
	    if (Files.exists(Paths.get(sceneName))){
            Files.delete(Paths.get(sceneName));
        }
		Files.createDirectory(Paths.get(sceneName));
		
		for (; scene.getSceneNum() <= numScenes; scene.incremementSceneNum()) {
			scene.setName(sceneName + "/" + scene.getSceneNum());
			drawScene(scene);
		}
	}

	/**
	 * Draw an animated gif of scenes, calling the animate() method on a scene to get to the next scene
	 * @throws IOException
	 */
	public static void drawAnimatedGif(AnimatedScene3D scene) throws IOException {
		// Get the scene name for the folder
		long start = System.currentTimeMillis();
		int frameNum = 0;

		while (scene.getAnim().hasNext()) {  //Keeps drawing the scene as long as the animator can do more.
			drawScene(scene.getAnim().next());
			System.out.println("     Rendered frame " + ++frameNum + ".");
		}
        scene.done();
        
        long elapsed = System.currentTimeMillis() - start;
		System.out.printf("Time to render: %.0f seconds.", (double) elapsed / 1000);

	}

	public static void main(String[] args) {
		RayHit h = new RayHit(null, 0, null, null, null);
		h.setHitPoint(new Point3D(0, 0, 0));
		h.setNormal(new Point3D(0, 1, 0));
		Ray3D r = new Ray3D(new Point3D(-1, 1, -1), new Point3D(1, -1, 1));
		Ray3D s = calcReflectionRay(r, h);
		System.out.println("the reflection is " + s);
	}
}
