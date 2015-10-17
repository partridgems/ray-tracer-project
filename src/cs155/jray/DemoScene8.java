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
    }

    public static Scene3D initScene2() {
    	Scene3D scene = new Scene3D();
        Material mat1 = new Material(Color3D.BLACK, Color3D.BLACK, Color3D.WHITE, Color3D.WHITE, 200);

        // Sphere
        QuadricSurface qs = new QuadricSurface(.5, .3333, -.85, 0, 0, 0, 0, 0, 0, -1);
        qs.setBounds(new Point3D(Integer.MIN_VALUE, Integer.MIN_VALUE, -1.5),
                new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE, 1.5));
        TransformedObject3D transformed = new TransformedObject3D(qs);
        transformed.setTransform(new Transform3D().translate(0, 0, -5).rotateX(100d));
        Sphere3D sp1 = new Sphere3D(new Point3D(0, 0, -1), 1.0, Material.defaultMat);

        Light3D light1 = new Light3D(new Point3D(5, 5, 10), 1.0);
        PngCanvas3D mc = new PngCanvas3D(400, 400, "DemoScene8.png");

        // Uncomment one camera to choose which one is active
        Camera3D cam = new Camera3D(mc);
        // cam.apply(new Transform3D().translate(0, 0, 5));
        //cam.apply(new Transform3D().rotateY(45d).rotateX(90d).rotateZ(90d)
        //       .translate(0d, 5d, 10d));

//        scene.add(transformed);
//        scene.add(sp1);
        scene.add(transformed);
        scene.add(light1);
        scene.add(cam);
        return scene;

    }

}
