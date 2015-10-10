package cs155.jray;

/**
 * This class stores severalo pieces of information about the intersection of a
 * Ray with an object: the distance to the intersection along the ray, the
 * normal to the object at that intersection point, the object hit, the texture
 * coordinates of the object
 **/
public class RayHit {
	public static RayHit NO_HIT = new RayHit(null, Double.POSITIVE_INFINITY,
			null, null, null);

	public double distance; // distance along ray to the first intersection (or
							// -1 if there is no intersection)
	public Point3D normal; // normal of the object at the intersection point
	public Point3D hitPoint = null; // point of intersection
	public Object3D obj; // innermost primitive object that this ray hits...
	public TextureCoordinate tc;

	/**
	 * Generates a RayHit where x,y are the Texture Coordinates
	 **/
	public RayHit(Point3D hitPoint, double distance, Point3D normal,
			Object3D obj, TextureCoordinate tc) {
		this.hitPoint = hitPoint;
		this.distance = distance;
		this.normal = normal;
		this.obj = obj;

		if (this.normal != null) {
			this.normal = this.normal.normalize();
			this.normal.w = 0;
		}
		this.tc = tc;
	}

	public String toString() {
		return "rayhit(" + hitPoint + "," + distance + "," + normal + "," + obj
				+ "," + tc + ")";
	}

}
