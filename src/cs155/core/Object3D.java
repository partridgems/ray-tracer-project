package cs155.core;

import cs155.aesthetic.Material;

/**
 * This is now an abstract class representing raytraceable objects with inside
 * and outside materials
 **/

public abstract class Object3D {

	private static final double epsilon = 0.0001;

	private Material insideMat = Material.DEFAULT_MAT;
	private Material outsideMat = Material.DEFAULT_MAT;
	
	private Transform3D transform = Transform3D.IDENTITY;

	public static double getEpsilon() {
		return epsilon;
	}

	// x.firstIntersection(ray) -- returns a double, -1 for no intersection, t>0 for
	// intersection at value t
	/**
	 * firstIntersection(r) returns a RayHit object, h, for the intersection of r
	 * with the object where h.distance is the distance to the intersection and
	 * h.normal is the normal at the intersection point If the ray does not
	 * intersect, then h.distance = -1 and h.normal is null. This method must be
	 * implemented by subclasses of objects. They will then be 
	 **/
	protected abstract RayHit rayIntersectObj(Ray3D ray);
	
	public RayHit rayIntersect(Ray3D ray) {
		// calculate the inverse transform of the ray
		Transform3D invt = getTransform().inverse();
		Ray3D invRay = ray.applyTransform(invt);
		
		// intersect it with M
		RayHit rh = this.rayIntersectObj(invRay);
		if (rh==RayHit.NO_HIT)
			return rh;
		
		// transform the RayHit by T (be careful with normals!)
		Point3D p = getTransform().applyTo(rh.getHitPoint());
		Point3D n = getTransform().applyInvTranspTo(rh.getNormal());
		//Point3D n = transform.applyTo(rh.normal);
		// we had to remove the normalization of ray direction
		// to get this to work
		double t = rh.getDistance(); ///invRay.d.length();
		RayHit rh2 = new RayHit(p,t,n, rh.getObj(), rh.getTc());
		
		return rh2;
	}

	
	public void setTransform(Transform3D t){
		this.transform = t;
	}
	
	public void applyTrans(Transform3D t){
		setTransform(Transform3D.compose(getTransform(), t));
	}

	public Material getInsideMat() {
		return insideMat;
	}

	public void setInsideMat(Material insideMat) {
		this.insideMat = insideMat;
	}

	public Material getOutsideMat() {
		return outsideMat;
	}

	public void setOutsideMat(Material outsideMat) {
		this.outsideMat = outsideMat;
	}

	public Transform3D getTransform() {
		return transform;
	}
}
