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
