package cs155.jray;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ObjReader {
	String filename;
	File infile;
	ArrayList<Point3D> vertex = new ArrayList<Point3D>();
	ArrayList<Point3D> normal = new ArrayList<Point3D>();
	ArrayList<TextureCoordinate> textcoord = new ArrayList<TextureCoordinate>();
	ArrayList<Triangle3D> face = new ArrayList<Triangle3D>();

	public ObjReader(String filename) {
		this.filename = filename;
		this.infile = new File(filename);
		// now we add a fake element to each ArrayList
		// since the indexing should start with 1 not 0
		Point3D p0 = new Point3D(0, 0, 0);
		vertex.add(p0);
		normal.add(p0);
		textcoord.add(new TextureCoordinate(0, 0));
		face.add(new Triangle3D(p0, p0, p0));

	}

	public Group3D toGroup3D() {
		Group3D g = new Group3D();
		Scanner scanner = null;
		// open file
		// read all data into ArrayLists for
		// vertices, normals, texture coords, and faces
		// add faces to a Group3D object and
		try {
			scanner = new Scanner(this.infile);

			scanner.useDelimiter("\\n");
			while (scanner.hasNext()) {
				String s = scanner.next();
				System.out.println("processing "+s);
				Scanner line = new Scanner(s);
				if (!line.hasNext())
					continue;
				String firstToken = line.next();
				if (firstToken.equals("v")) {
					processVertex(line);
				} else if (firstToken.equals("vt")) {
					processTextureCoord(line);
				} else if (firstToken.equals("vn")) {
					processVertexNormal(line);
				} else if (firstToken.equals("f")) {
					processFace(g,line);
				} 
				line.close();

			}

		} catch (FileNotFoundException e) {
			System.out.println("***** ERROR in ObjReader ****");
			e.printStackTrace();
		}
		
		updateVertexNormals();

		return g;
	}
	
	/*
	 * traverse through all vertices v and triangles t containing v
	 * and replace the normal corresponding to v with the average of 
	 * the normals of all triangles touching v. Maybe we can automate this
	 * by initializing the vertex normals to (0,0,0) and then adding the
	 * triangle normal to each vertex as we process each triangle???
	 * This will require us to create a map from vertex indices to normals...
	 */
	private void updateVertexNormals(){
		
	}

	private void processVertex(Scanner s) {
		double d1 = s.nextDouble();
		double d2 = s.nextDouble();
		double d3 = s.nextDouble();
		Point3D p = new Point3D(d1, d2, d3);
		this.vertex.add(p);
		System.out.println("p "+p);
	}

	private void processTextureCoord(Scanner s) {
		double d1 = s.nextDouble();
		double d2 = s.nextDouble();
		TextureCoordinate tc = new TextureCoordinate(d1, d2);
		this.textcoord.add(tc);
		System.out.println("tc "+tc);
	}

	private void processVertexNormal(Scanner s) {
		double d1 = s.nextDouble();
		double d2 = s.nextDouble();
		double d3 = s.nextDouble();
		Point3D p = new Point3D(d1, d2, d3);
		this.normal.add(p);
		System.out.println("n "+p);
	}

	private static int[] readVertexData(String s){
		int[] data = new int[3];
		int n1 = s.indexOf("/");
		int n2 = s.indexOf("//");
		if (n1==-1){
			data[0] = Integer.parseInt(s);
			data[1]=data[2]=0;
		}else if (n2==-1){
			int n3 = s.indexOf('/',n1+1);
			data[0] = Integer.parseInt(s.substring(0,n1));
			if (n3==-1){
				data[1] = Integer.parseInt(s.substring(n1+1));
				data[2] = 0;
			}else {
				data[1] = Integer.parseInt(s.substring(n1+1,n3));
				data[2] = Integer.parseInt(s.substring(n3+1));
			}
		}else {
			data[0] = Integer.parseInt(s.substring(0,n2));
			data[1]=0;
			data[2] = Integer.parseInt(s.substring(n2+2));
		}
		
		return data;
	}
	
	private void processFace(Group3D g, Scanner s) {
		/*
		 * we need to read the 1,2, or 3 integer indices
		 * and store them in an array of 3 ints....
		 */
		int[]d0,d1,d2,d3;
		String v0 = s.next();
		d0 = readVertexData(v0);
		String v1 = s.next();
		d1 = readVertexData(v1);
		String v2 = s.next();
		d2 = readVertexData(v2);
		d3 = new int[3];


		Point3D p0 = this.vertex.get(d0[0]);
		Point3D p1 = this.vertex.get(d1[0]);
		Point3D p2 = this.vertex.get(d2[0]);
		Triangle3D t = new Triangle3D(p0, p1, p2);
		// add the TextureCoordinates to the vertices, if any
		if (d0[1]>0) t.tc0 = this.textcoord.get(d0[1]);
		if (d1[1]>0) t.tc1 = this.textcoord.get(d1[1]);
		if (d2[1]>0) t.tc2 = this.textcoord.get(d2[1]);
		// add the Normals to the vertices, if any
		if (d0[2]>0) t.n0 = this.normal.get(d0[2]);
		if (d1[2]>0) t.n1 = this.normal.get(d1[2]);
		if (d2[2]>0) t.n2 = this.normal.get(d2[2]);
		
		this.face.add(t);
		g.add(t);
//		int n4=0;
		Triangle3D t2 = null;
		if (s.hasNext()){
			String v3 = s.next();
			d3 = readVertexData(v3);			
			Point3D p3 = this.vertex.get(d3[0]);
			t2 = new Triangle3D(p0,p2,p3);
			// add the TextureCoordinates to the vertices, if any
			if (d0[1]>0) t2.tc0 = this.textcoord.get(d0[1]);
			if (d2[1]>0) t2.tc1 = this.textcoord.get(d2[1]);
			if (d3[1]>0) t2.tc2 = this.textcoord.get(d3[1]);
			// add the Normals to the vertices, if any
			if (d0[2]>0) t2.n0 = this.normal.get(d0[2]);
			if (d2[2]>0) t2.n1 = this.normal.get(d2[2]);
			if (d3[2]>0) t2.n2 = this.normal.get(d3[2]);
			
			this.face.add(t2);
			g.add(t2);
		} else t2=t;
		// next handle the surface normals!!
		
		System.out.println("t("
		+d0[0]+"/"+d0[1]+"/"+d0[2]+","
		+d1[0]+"/"+d1[1]+"/"+d1[2]+","
		+d2[0]+"/"+d2[1]+"/"+d2[2]+","
		+d3[0]+"/"+d3[1]+"/"+d3[2]+") = \n"+t+"---"+t2+"\n");
	}

	public static void main(String[] args) {
		//ObjReader r = new ObjReader("obj/cube2.obj");
		//r.toGroup3D();
		String s;
		int[] d;
		s="5";
		d= readVertexData(s);
		System.out.println("s="+s+"  d=["+d[0]+","+d[1]+","+d[2]+"]");
	
		s="5/7";
		d= readVertexData(s);
		System.out.println("s="+s+"  d=["+d[0]+","+d[1]+","+d[2]+"]");
		
		s="5/7/9";
		d= readVertexData(s);
		System.out.println("s="+s+"  d=["+d[0]+","+d[1]+","+d[2]+"]");
		
		s="5//9";
		d= readVertexData(s);
		System.out.println("s="+s+"  d=["+d[0]+","+d[1]+","+d[2]+"]");
			
	}

}
