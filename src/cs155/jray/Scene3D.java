package cs155.jray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This represents a 3d scene that can be drawn on a Canvas3D object. It
 * consists of an array of lights and a group of objects It also has an ambient
 * lights, a background color, and a camera object.
 * 
 * REFACTOR -- We should rewrite this using ArrayLists!
 **/

public class Scene3D {

	private int numObj;
	private List<Light3D> lights;
	private int reflectionDepth;
    private List<Object3D> objs;
	private Color3D ambient;
	private Color3D backgroundColor;
	private Camera3D camera;
	private int sceneNum;
	private String name;

	private Scene3D() {
        setObjs(new ArrayList<Object3D>());
        setLights(new ArrayList<>());
        setReflectionDepth(2);
        setAmbient(new Color3D(0d, 0d, 0.4d));
        setBackgroundColor(new Color3D(0d, 0d, 0.4d));
        setSceneNum(1);
	}
	
	public Scene3D(String name) {
		this();
		this.name = name;
	}

	public void add(Camera3D x) {
		setCamera(x);
	}

	public void add(Object3D ... objs) {
		this.getObjs().addAll(Arrays.asList(objs));
	}

	public void add(Light3D x) {
        lights.add(x);
	}

    // x.firstIntersection(ray) -- returns a double, POSITIVE_INFINITY for no
    // intersection, t>0 for intersection at value t
    public RayHit firstIntersection(Ray3D ray) {
        RayHit closestHit = RayHit.NO_HIT;
        for (int i = 0; i < getObjs().size(); i++) {
            Object3D obj = getObjs().get(i);
            RayHit hit = obj.rayIntersect(ray);
            if (hit.distance < closestHit.distance) {
                closestHit = hit;
            }
        }
        return closestHit;
    }

    /**
     * This pushes the passed textures
     * down onto all of the sub-objects.  It is used to
     * texture all the triangles in a mesh.
     */
    public void pushTextures(Material inside, Material outside){
        for(Object3D obj: getObjs()){
            obj.insideMat = inside;
            obj.outsideMat = outside;
        }
    }

	public void clear() {
		getObjs().clear();
	}

    public List<Object3D> getObjs() {
        return objs;
    }
    
    /**
     * Causes the image on the camera to be stored to the named file
     * supplied when the scene was created or updated by the RayTracer
     * for animated images.
     */
    public void save() {
    	this.camera.film.refresh(this.name);
    }

    /** Here we create the scene elements as instance variables **/
    public int getNumObj() {
        return numObj;
    }

    public List<Light3D> getLights() {
        return lights;
    }

    public int getReflectionDepth() {
        return reflectionDepth;
    }

    public void setReflectionDepth(int reflectionDepth) {
        this.reflectionDepth = reflectionDepth;
    }

    /**
     * this is the level and color of initial ambient lights in the scene
     * independent of the other lights in the scene
     */
    public Color3D getAmbient() {
        return ambient;
    }

    /**
     * this is the color of pixels whose rays hit no objects
     */
    public Color3D getBackgroundColor() {
        return backgroundColor;
    }

    /** this is the default camera **/
    public Camera3D getCamera() {
        return camera;
    }
    
    public String getName() {
    	return this.name;
    }

    public int getSceneNum() {
        return sceneNum;
    }

    public void incremementSceneNum() {
        this.setSceneNum(this.getSceneNum() + 1);
    }

    public void setSceneNum(int sceneNum) {
        this.sceneNum = sceneNum;
    }

    public void setNumObj(int numObj) {
        this.numObj = numObj;
    }

    public void setLights(List<Light3D> lights) {
        this.lights = lights;
    }

    public void setObjs(List<Object3D> objs) {
        this.objs = objs;
    }

    public void setAmbient(Color3D ambient) {
        this.ambient = ambient;
    }

    public void setBackgroundColor(Color3D backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setCamera(Camera3D camera) {
        this.camera = camera;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
}
