package cs155.jray;

/**
 * This class creates the various orientations of the scene for the skybox images
 *
 */
public class DemoScene5 {
    

    public static void main(String[] args) {
    	Scene3D scene = new Scene3D(); initScene(scene);
    	RayTracer3D.drawScene(scene);
    	scene.getCamera().film.refresh();
    	System.out.println("\nDone.");
    }


	private static void initScene(Scene3D scene){

		Material mat1 = new Material(new Color3D(0d,0d,0d), new Color3D(0.3d,0.7d,0.7d), new Color3D(1d,1d,1d), new Color3D(1d,1d,1d),200);
		Material mat2 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(1d,1d,0d), new Color3D(1d,1d,1d),200);
		Material mat3 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(0d,1d,1d), new Color3D(1d,1d,1d),200);
		Material mat4 = new Material(new Color3D(0d,0d,0d), new Color3D(0d,0d,0d), new Color3D(1d,0d,1d), new Color3D(1d,1d,1d),200);

		mat1.reflect = 0.1;
		
		CheckerTexture tex2 = new CheckerTexture();
		tex2.scale(1000,80);
		mat2.texture = tex2;
		mat2.texWeight = 0.25;
		
		CheckerTexture tex3 = new CheckerTexture();
		tex3.scale(500,20);
		mat3.texture = tex3;
		mat3.texWeight = 0.25;
		
		ImageTexture tex4 = new ImageTexture("images/daisies.png");
		tex4.scale(500,-45);
		tex4.translate(-10, 0);  // Hides the seam
		mat4.texture = tex4;
		mat4.texWeight = 1;
		mat4.reflect = 0.0;		


		// Create the objects
		Cylinder3D cyl1 = new Cylinder3D(new Point3D(-35, 15,-35),new Point3D(0,1,0.5), 20.0, 25.0, mat2);
		Cylinder3D cyl2 = new Cylinder3D(new Point3D(35,9,-25),new Point3D(-.2,1,.3), 35.0, 12.0, mat3);
		Cylinder3D cyl3 = new Cylinder3D(new Point3D(30, -5,-15),new Point3D(0,1,0), 20.0, 25.0, mat4);
		Plane3D pl1 = new Plane3D(new Point3D(0,-1,0),new Point3D(0,1,0),Material.defaultMat);
		
		// create the lights
		Light3D light1 = new Light3D(new Point3D(15,15,15),1.0);// this light comes from above on the left
		Light3D light2 = new Light3D(new Point3D(2,90,-15),0.3); // this light is from way above the scene
		
		cyl3.insideMat = mat2; // here we change the material on the inside of cylinder 3
		cyl2.insideMat = mat1;
		
    	PngCanvas3D mc = new PngCanvas3D(800, 800, "DemoScene5forward.png");
		Transform3D camTransf = new Transform3D();
		// this transformation takes a few from above and to the right looking down at the cylinders
		// comment it out to see the view from the origin..
		camTransf = camTransf.translate(0,0,0).rotateY(20).rotateX(45).translate(-3,5,30).rotateX(0);
		Camera3D cam = new Camera3D(mc,camTransf);
		
		// Add objects to scene
		scene.add(cyl2);
		scene.add(cyl1);
		scene.add(cyl3);
		scene.add(pl1);
		scene.add(light1);
		scene.add(light2);
		scene.add(cam);
	}

}

