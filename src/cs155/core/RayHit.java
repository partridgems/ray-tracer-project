package cs155.core;

import cs155.aesthetic.TextureCoordinate;

/**
 * This class stores severalo pieces of information about the intersection of a
 * Ray with an object: the distance to the intersection along the ray, the
 * normal to the object at that intersection point, the object hit, the texture
 * coordinates of the object
 **/
public class RayHit {
	public static final RayHit NO_HIT = new RayHit(null, Double.POSITIVE_INFINITY,
			null, null, null);

	private double distance; // distance along ray to the first intersection
	private Point3D normal; // normal of the object at the intersection point
	private Point3D hitPoint; // point of intersection
	private Object3D obj; // innermost primitive object that this ray hits...
	private TextureCoordinate tc;

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
			this.normal.setW(0);
		}
		this.tc = tc;
	}

	public String toString() {
		return "rayhit(" + hitPoint + "," + distance + "," + normal + "," + obj
				+ "," + tc + ")";
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Point3D getNormal() {
		return normal;
	}

	public void setNormal(Point3D normal) {
		this.normal = normal;
	}

	public Point3D getHitPoint() {
		return hitPoint;
	}

	public void setHitPoint(Point3D hitPoint) {
		this.hitPoint = hitPoint;
	}

	public Object3D getObj() {
		return obj;
	}

	public void setObj(Object3D obj) {
		this.obj = obj;
	}

	public TextureCoordinate getTc() {
		return tc;
	}

	public void setTc(TextureCoordinate tc) {
		this.tc = tc;
	}
}
