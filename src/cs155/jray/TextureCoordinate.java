package cs155.jray;

/**
 * A pair of doubles returned as part of a RayHit object which represent the
 * location of the intersection in 2D texture space
 **/
public class TextureCoordinate {
	double x, y; // raw coordinates

	public TextureCoordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "tc(" + x + "," + y + ")";
	}
	
	public static TextureCoordinate interpolate(double b1, double b2, 
			TextureCoordinate tc0,
			TextureCoordinate tc1,
			TextureCoordinate tc2){
		TextureCoordinate tc = new TextureCoordinate(0,0);
		tc.x = tc0.x*(1-b1-b2)+ tc1.x*b1 + tc2.x*b2;
		tc.y = tc0.y*(1-b1-b2)+ tc1.y*b1 + tc2.y*b2;
		return tc;
		
	}

}
