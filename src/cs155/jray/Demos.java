package cs155.jray;

/**
 * this class contains several static methods for creating
 * interesting scenes
 * @author tim
 *
 */
public class Demos {

	public Demos() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * this is a simple demo of materials, basic shapes and camera
	 * transformations
	 * @return
	 */
	public static Scene3D demo1(){
		Scene3D scene = new Scene3D();
		
		// create the materials
		Material mat1 = new Material();		
		CheckerTexture tex = new CheckerTexture();
		tex.scale(100, 100);
		mat1.texture = tex;
		mat1.texWeight = 0.1;
		mat1.diffuse = new Color3D(1,1,0);  // yellow
		mat1.reflect=0.5;

		Material mat2 = new Material();
		mat2.diffuse = new Color3D(1.0,0,0);
		mat2.specular = new Color3D(0,0,1);
		mat2.reflect=0;
		
		Material mat3 = new Material();
		mat3.texture = mat1.texture;
		mat3.texWeight = 0.25;
		mat3.diffuse = mat1.diffuse;
		mat3.reflect=0;
		
		
		// create three objects with specified materials
		Plane3D plane = new Plane3D(new Point3D(0, 0, 0), new Point3D(0, 1, 0), mat3);

		Cylinder3D cylinder = 
				new Cylinder3D(new Point3D(1, 0, -15), new Point3D(0,1,0), 10.0, 15.0, mat1);
		cylinder.insideMat = mat2;

		Sphere3D sphere = new Sphere3D(new Point3D(0, 0, 5), 5.0, mat2);
		



		// create the lights
		//light1 comes from above on the left
		Light3D light1 = new Light3D(new Point3D(0, 15, 15), 0.5);
		//light2 comes from directlyh above
//		Light3D light2 = new Light3D(new Point3D(0, 100, 0), 0.3);


		PngCanvas3D canvas = new PngCanvas3D(800, 800, "demo1.png");
		Transform3D camTransf = new Transform3D();
		camTransf = camTransf.translate(0, 10, 0).rotateY(-60).rotateX(2).translate(0, 0, 20);
		Camera3D cam = new Camera3D(canvas, camTransf);

        scene.add(cylinder);
        scene.add(sphere);
        scene.add(plane);
        scene.add(light1);
        //scene.add(light2);
        scene.add(cam);
		
		return scene;

	}
	
	/**
	 * This creates a scene which demonstrates the use of Object Transforms
	 * @return
	 */
	public static Scene3D demo2(){
		Scene3D scene = new Scene3D();
		

		Material mat1 = new Material();
		CheckerTexture tex = new CheckerTexture();
		tex.scale(10, 10);
		mat1.texture = tex;
		mat1.texWeight = 0.25;
	
		Material mat2 = new Material();
		ImageTexture tex2 = new ImageTexture("images/daisies.png");
		tex2.scale(5000, 50);
		mat2.texture = tex2;
		mat2.texWeight = 0.5;
		mat2.reflect=0;

		
		Plane3D pl1 = new Plane3D(new Point3D(0, 0, 0), new Point3D(0, 1, 0), mat1);

		Cylinder3D cyl2 = new Cylinder3D(new Point3D(0,0,0), new Point3D(0, 1, 0), 20.0, 10.0, mat2);

		Sphere3D sp2 = new Sphere3D(new Point3D(0, 0, 0), 20.0, Material.defaultMat);


		// create the lights
		Light3D light1 = new Light3D(new Point3D(15, 25, 15), 0.5);
		Light3D light2 = new Light3D(new Point3D(2, 90, -15), 0.3); 



		PngCanvas3D canvas = new PngCanvas3D(800, 800, "demo2.png");
		Transform3D camTransf;

		// move the camera ...
		camTransf = 
				Transform3D.IDENTITY
				.translate(0, 10, 0)
				.rotateY(0)
				.rotateX(25)
				.translate(0, 0, 75);
		Camera3D cam = new Camera3D(canvas, camTransf);

		
		// move the cylinder
		TransformedObject3D cyl2a = new TransformedObject3D(cyl2);
		  cyl2a.setTransform(Transform3D
				  .IDENTITY
				  .rotateY(45)
				  .scale(3, 1, 2)
				  .rotateY(10));

		  // transform the sphere
		  TransformedObject3D sp2a = new TransformedObject3D(sp2);
		  sp2a.setTransform(Transform3D
				  .IDENTITY
				  .translate(0,0,0)
				  .rotateY(30)
				  .scale(0.05,1,0.25)
				  .translate(0,0,0)
				  .rotateY(0)
				  .translate(0,0,0)
				  );
		  
		  scene.add(cyl2a);
		  scene.add(sp2a); 
		  scene.add(pl1); 
		  scene.add(light1);
		  scene.add(light2);

		  scene.add(cam);
	
		  return scene;
	}
	
	/**
	 * this demonstrates loading in a wavefront OBJ file and
	 * transforming it ...
	 * @return
	 */
	public static Scene3D demo3(){
		Scene3D scene = new Scene3D();
	

		Material mat1 = new Material();
		CheckerTexture tex = new CheckerTexture();
		tex.scale(10, 10);
		mat1.texture = tex;
		mat1.texWeight = 0.25;
		mat1.reflect=0.5;
	
		Material mat2 = new Material();
		ImageTexture tex2 = new ImageTexture("images/daisies.png");
		tex2.scale(1000, 1000);
		mat2.texture = tex2;
		mat2.texWeight = 0.1;
		

		
		Plane3D pl1 = new Plane3D(new Point3D(0, -1, 0), new Point3D(0, 1, 0), mat1);



		// create the lights
		Light3D light1 = new Light3D(new Point3D(15, 15, 15), 0.5);
		Light3D light2 = new Light3D(new Point3D(2, 90, -15), 0.3); 
		Light3D light3 = new Light3D(new Point3D(0, 0, 15), 0.5);


		int N = 800;
		PngCanvas3D canvas = new PngCanvas3D(N, N, "demo3.png");
		Transform3D camTransf;

		// move the camera ...
		camTransf = 
				Transform3D.IDENTITY
				.translate(0, 10, 0)
				.rotateY(-30)
				.rotateX(15)
				.translate(0, 0, 30);
		Camera3D cam = new Camera3D(canvas, camTransf);

		//ObjReader r = new ObjReader("obj/cubesds.obj");
		ObjReader r = new ObjReader("obj/cubesd.obj");
		//ObjReader r = new ObjReader("obj/suzanne2.obj");
			Group3D obj = r.toGroup3D();
			TransformedObject3D objT = new TransformedObject3D(obj);
		    obj.insideMat = obj.outsideMat = mat2;
			obj.pushTextures();
			objT.setTransform(Transform3D
					.IDENTITY
					.scale(10,10,10)
					.translate(0,1,0));
	      scene.add(objT);

		  
		  //scene.add(cyl2a);
		  //scene.add(sp2a); 
		  scene.add(pl1); 
		  scene.add(light1);
		  scene.add(light2);
		  scene.add(light3);

		  scene.add(cam);
	
		return scene;
	}
	
	

	/**
	 * generate a cylinder directly by dividing a circle 
	 * in the xy plane into k=5 parts (where k can be changed...)
	 * and then extruding each of those segments along the z-axis
	 * to form a rectangle with two triangles.
	 * @return
	 */
	public static Group3D createTube() {
		Group3D g = new Group3D();
		int k = 5;
		for (int i = 0; i <= k; i++) {
			double x1 = Math.cos(2 * Math.PI / k * i);
			double y1 = Math.sin(2 * Math.PI / k * i);
			double x2 = Math.cos(2 * Math.PI / k * (i + 1));
			double y2 = Math.sin(2 * Math.PI / k * (i + 1));
			Point3D p10 = new Point3D(x1, y1, -10);
			Point3D p11 = new Point3D(x1, y1, -20);
			Point3D p20 = new Point3D(x2, y2, -10);
			Point3D p21 = new Point3D(x2, y2, -20);
			Point3D n1 = new Point3D(x1, y1, 0);
			Point3D n2 = new Point3D(x2, y2, 0);
			Triangle3D t1 = new Triangle3D(p10, p20, p21);
			Triangle3D t2 = new Triangle3D(p21, p11, p10);
			t1.n0 = n1;
			t1.n1 = n2;
			t1.n2 = n2;
			t2.n0 = n2;
			t2.n1 = n1;
			t2.n2 = n1;
			g.add(t1);
			g.add(t2);
		}
		return (g);
	}

}
