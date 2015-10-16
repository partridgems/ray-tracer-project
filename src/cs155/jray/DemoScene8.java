package cs155.jray;

/**
 * This class performs some very basic tests for Fisheye Cameras
 *
 */
public class DemoScene8 {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("getting ready to draw scene");
        Scene3D scene = initScene2();
        RayTracer3D.drawScene(scene);

        scene.camera.film.refresh();
        System.out.println("drew a sphere!");
    }

    private static Scene3D initScene2() {
    	Scene3D scene = new Scene3D();
        Material mat1 = new Material(Color3D.BLACK, Color3D.BLACK, Color3D.WHITE, Color3D.WHITE, 200);

        // Sphere
        QuarticSurface qs = new QuarticSurface(1, 1, 1, 0, 0, 0, 0, 0, 0, -1);
        TransformedObject3D transformed = new TransformedObject3D(qs);
        transformed.setTransform(Transform3D.translation(0, 0, -5));
        Sphere3D sp1 = new Sphere3D(new Point3D(3, 3, -5), 1.0, Material.defaultMat);

        Light3D light1 = new Light3D(new Point3D(0, 0, 10), 1.0);
        PngCanvas3D mc = new PngCanvas3D(800, 800, "DemoScene8.png");

        // Uncomment one camera to choose which one is active
        Camera3D cam = new Camera3D(mc);
        cam.apply(Transform3D.translation(0, 0, 5));

//        scene.add(transformed);
//        scene.add(sp1);
        scene.add(qs);
        scene.add(light1);
        scene.add(cam);
        return scene;

    }

}
