package cs155.jray;

import java.util.ArrayList;

/**
 * This class represents a group of Objects. Once a collection of objects are
 * grouped they can be transformed by a single transform operator!
 **/
public class Group3D extends Object3D {

	private ArrayList<Object3D> objs = new ArrayList<Object3D>();

	public Group3D() {
	}

	public Group3D(Object3D[] objs) {
		this.add(objs);
	}

	public void clear() {
		objs = new ArrayList<Object3D>();
	}

	public void add(Object3D obj) {
		objs.add(obj);
	}

	public void add(Object3D[] obj) {
		for (int i = 0; i < obj.length; i++)
			objs.add(obj[i]);
	}

	// x.rayIntersect(ray) -- returns a double, POSITIVE_INFINITY for no
	// intersection, t>0 for intersection at value t
	public RayHit rayIntersect(Ray3D ray) {
		RayHit closestHit = RayHit.NO_HIT;

		for (int i = 0; i < objs.size(); i++) {
			Object3D obj = objs.get(i);
			RayHit hit = obj.rayIntersect(ray);
			if (hit.distance < closestHit.distance) {
				closestHit = hit;
			}
		}

		return closestHit;
	}
	
	public void dump(){
		for(Object3D obj: objs){
			System.out.println(obj);
		}
	}
	
	/**
	 * This pushes the texture of the Group3D object
	 * down onto all of the sub-objects.  It is used to
	 * texture all the triangles in a mesh.
	 */
	public void pushTextures(){
		for(Object3D obj: objs){
			obj.insideMat = this.insideMat;
			obj.outsideMat = this.outsideMat;
		}
	}

}
