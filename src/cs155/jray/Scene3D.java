package cs155.jray;

/**
 * This represents a 3d scene that can be drawn on a Canvas3D object. It
 * consists of an array of lights and a group of objects It also has an ambient
 * light, a background color, and a camera object.
 * 
 * REFACTOR -- We should rewrite this using ArrayLists!
 **/

public class Scene3D {

	/** Here we create the scene elements as instance variables **/
	public Group3D objs = new Group3D();
	public int numObj = 0;

	public Light3D light[] = new Light3D[100];
	public int numLights = 0;

	public int reflectionDepth = 2;

	/**
	 * this is the level and color of initial ambient light in the scene
	 * independent of the other lights in the scene
	 */
	public Color3D ambient = new Color3D(0.1, 0.1, 0.1);

	/**
	 * this is the color of pixels whose rays hit no objects
	 */
	public Color3D backgroundColor = new Color3D(0d, 0d, 0.4d); // this is the
																// color of the
																// "sky"
	/** this is the default camera **/
	public Camera3D camera;

	public Scene3D() {
	}

	public void add(Camera3D x) {
		camera = x;
	}

	public void add(Object3D x) {
		objs.add(x);
	}

	public void add(Light3D x) {
		light[numLights++] = x;
	}

	public void clear() {
		objs.clear();
		numLights = 0;
	}

}
