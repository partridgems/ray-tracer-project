package cs155.jray;

/**
 * This is now an abstract class representing raytraceable objects with inside
 * and outside materials
 **/

public abstract class Object3D {

	public static final double epsilon = 0.0001;

	public Material insideMat = Material.defaultMat,
			outsideMat = Material.defaultMat;
	
	public Transform3D transform = null;

	// x.rayIntersect(ray) -- returns a double, -1 for no intersection, t>0 for
	// intersection at value t
	/**
	 * rayIntersect(r) returns a RayHit object, h, for the intersection of r
	 * with the object where h.distance is the distance to the intersection and
	 * h.normal is the normal at the intersection point If the ray does not
	 * intersect, then h.distance = -1 and h.normal is null
	 **/
	public abstract RayHit rayIntersect(Ray3D ray);

	
}
