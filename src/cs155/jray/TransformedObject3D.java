package cs155.jray;
/**
 * this class allows any raytraceable object to be transformed
 * by translation, scaling, and rotation simply by changing the
 * intersectRay method so that it uses the transform to modify the
 * ray before intersecting, and to unmodify the resulting hitpoint
 * @author tim
 *
 */
public class TransformedObject3D extends Object3D {
	
	private Transform3D transform;
	private Object3D obj;
	

	public TransformedObject3D(Object3D obj) {
		this.obj = obj;
	}

	/**
	 * to intersect R with T(M) we find the RayHit H for
	 * S(R) with M  (where S is the inverse transform to T)
	 * Then we transform H by T to get T*(H) the RayHit or R with T(M)
	 */
	@Override
	public RayHit rayIntersect(Ray3D ray) {
		// calculate the inverse transform of the ray
		Transform3D invt = transform.inverse();
		Ray3D invRay = ray.applyTransform(invt);
		
		// intersect it with M
		RayHit rh = this.obj.rayIntersect(invRay);
		if (rh==RayHit.NO_HIT)
			return rh;
		
		// transform the RayHit by T (be careful with normals!)
		Point3D p = transform.applyTo(rh.hitPoint);
		Point3D n = transform.applyInvTranspTo(rh.normal);
		//Point3D n = transform.applyTo(rh.normal);
		// we had to remove the normalization of ray direction
		// to get this to work
		double t = rh.distance; ///invRay.d.length();
		RayHit rh2 = new RayHit(p,t,n,rh.obj,rh.tc);
		
		return rh2;
	}
	
	public void setTransform(Transform3D t){
		this.transform = t;
	}
	
	public void applyTrans(Transform3D t){
		transform = Transform3D.compose(transform, t);
	}


}
