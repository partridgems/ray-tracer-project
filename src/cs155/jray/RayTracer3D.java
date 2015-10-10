package cs155.jray;

/**
 * This consists of static methods for drawing a scene on a canvas.
 **/

public class RayTracer3D {

	/**
	 * Compute the color of the pixel corresponding to the intersection of the
	 * ray r with the first object in the scene s
	 **/
	public static Color3D computeColor(Ray3D r, Scene3D s, int depth) {

		/*
		 * first we intersect the ray with the scene and get the RayHit object
		 */
		RayHit hit = s.objs.rayIntersect(r);

		// if the ray hits no object, return the background color
		if (hit == RayHit.NO_HIT)
			return s.backgroundColor;

		/*
		 * if the ray hits the inside of the object (as determined by the
		 * outward facing normal) then flip the normal to also point inward and
		 * use the inside material
		 */
		Material m;
		boolean outside = (hit.normal.dot(r.d) < 0);
		if (!outside) {
			m = hit.obj.insideMat;
			hit.normal = hit.normal.scale(-1d); // flip the normal when the ray
												// hits on the inside
		} else
			m = hit.obj.outsideMat;

		// calculate the initial color of the pixel by setting it to the
		// emissive color
		Color3D pixelColor = m.emissive;

		// next calculate the ambient color of the pixel by multiplying
		// and add it to the pixel color, ambient is independent of the lights

		Color3D ambientColor = s.ambient.times(m.ambient);
		pixelColor = pixelColor.add(ambientColor);

		// next, for each light which is not obscured at the hitPoint
		// calculate its contribution to the pixel color and add it to the
		// pixelcolor
		for (int k = 0; k < s.numLights; k++) {
			if (isObscured(s.light[k], hit.hitPoint, hit.obj, s.objs))
				continue;

			Color3D localColor = calcColorForLight(r, hit.normal, hit.hitPoint,
					m, s.light[k]);
			pixelColor = pixelColor.add(localColor);
		}

		// finally, calculate the texture color
		Color3D textureColor = m.texture.getColor(hit.tc);
		// and average it with the pixel color using mat.texWeight
		pixelColor = pixelColor.averageIn(textureColor, m.texWeight);

		if ((m.reflect > 0) && (depth > 0)) {
			Color3D reflectColor = Color3D.BLACK;
			/*
			 * calculate the reflection ray and move the starting point a little
			 * above the surface to avoid self-intersection. Calculate the color
			 * corresponding to that ray (with depth-1 level of recursion)
			 * average in the reflectColor with the current pixel color using
			 * the constant m.reflect. So m.reflect=0 means you would ignore the
			 * color for the reflect ray and 0.5 would mean you average the
			 * material color and the reflected ray color.
			 */

			// put in a couple of lines of code here for PA05!!
			Ray3D reflectRay = calcReflectionRay(r,hit);
			reflectColor = computeColor(reflectRay,s,depth-1);

			pixelColor = pixelColor.averageIn(reflectColor, m.reflect);
		}

		return (pixelColor);
	}

	/**
	 * calculate the ray r' that you obtain by reflecting the ray r off of the
	 * hitpoint h
	 * 
	 * @param r
	 * @param h
	 * @return
	 */
	private static Ray3D calcReflectionRay(Ray3D r, RayHit h) {

		// put in a few lines of code HERE for PA05!!
		Point3D z = h.normal.scale(h.normal.dot(r.d));
		Point3D d1 = r.d.subtract(z.scale(2));
		Point3D p1 = h.hitPoint.add(d1.scale(0.0001));
		return new Ray3D(p1,d1);
	}

	/**
	 * return true if the ray starting at the point and heading toward the light
	 * hits an object on its way there, you'll need to be careful about the
	 * intersection of the point with the object it is on (with distance zero or
	 * close to zero!) that one shouldn't count as obscuring the light...
	 * 
	 * @param light
	 * @param point
	 * @param objs
	 * @return
	 */
	private static boolean isObscured(Light3D light, Point3D point,
			Object3D obj, Group3D objs) {
		Point3D lightVec = light.location.subtract(point);
		double distToLight = lightVec.length();
		Point3D lightVecNorm = lightVec.normalize();
		Point3D p2 = point.add(lightVecNorm.scale(0.01));
		Ray3D r = new Ray3D(p2, lightVecNorm);
		RayHit hit = objs.rayIntersect(r);
		if (hit == RayHit.NO_HIT)
			return false;
		double distToObj = (hit.hitPoint.subtract(point)).length();
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
	 * @return
	 */
	public static Color3D calcColorForLight(Ray3D r, Point3D n, Point3D p,
			Material m, Light3D light) {

		// Find the normalized light vector
		Point3D lightPos = light.location;
		Point3D lightVec = lightPos.subtract(p);
		lightVec = lightVec.normalize(); // vector from point to the light

		// calculate the local ambient pixel color contribution
		Color3D localAmbient = m.ambient.times(light.ambient);

		// calculate the local diffuse pixel color contribution
		Color3D localDiffuse = calculateDiffuse(n, m.diffuse, light.diffuse,
				lightVec);

		// calculate the local specular pixel color contribution
		// which requires the vector from the eye to the intersection point
		Color3D localSpecular = calculateSpecular(r, n, p, m.specular,
				m.hardness, light.specular, lightVec);

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
	 * @param n
	 * @param matColor
	 * @param lightColor
	 * @param lightVec
	 * @return
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
	 * @return
	 */
	private static Color3D calculateSpecular(Ray3D r, Point3D n, Point3D p,
			Color3D matSpecular, int matHardness, Color3D lightSpecular,
			Point3D lightVec) {
		Color3D localSpecular;
		Point3D eyeVec = r.p.subtract(p).normalize();
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
	public static void drawScene(Scene3D s) {
		double h = s.camera.film.height(), w = s.camera.film.width();
		System.out.println("h= " + h + " w= " + w);
		s.depth = 15;

		for (int i = 0; i < w; i++) {
			if ((i % 100) == 0) // print a message every 100 columns
				System.out.print(" " + i);

			for (int j = 0; j < h; j++) {
				Ray3D r1 = s.camera.generateRay(i, j); // generate a ray
				Color3D pixelColor;
				if (r1 == Ray3D.NOT_IN_SCENE) {  // This pixel is outside of the lens we are using
					pixelColor = Color3D.BLACK;
				} else {
					pixelColor = computeColor(r1, s, s.depth); // compute its color
				}
												
				s.camera.film.drawPixel(i, j, pixelColor.toColor()); // draw it
																		// on
																		// film
			} // close for i
		} // close for j
	} // close drawScene

	public static void main(String[] args) {
		RayHit h = new RayHit(null, 0, null, null, null);
		h.hitPoint = new Point3D(0, 0, 0);
		h.normal = new Point3D(0, 1, 0);
		Ray3D r = new Ray3D(new Point3D(-1, 1, -1), new Point3D(1, -1, 1));
		Ray3D s = calcReflectionRay(r, h);
		System.out.println("the reflection is " + s);
	}
}
