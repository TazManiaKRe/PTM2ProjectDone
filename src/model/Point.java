package model;

public class Point {
	public final float x,y;
	
	public Point(float x, float y) {
		this.x=x;
		this.y=y;
	}
	public float distance(final Point p) {
		return (float) Math.sqrt(distanceSquared(p));
	}


	public float distanceSquared(final Point p) {
		float disX = this.x - p.x;
		float disY = this.y - p.y;
		
		return (float) (Math.pow(disX,2) + Math.pow(disY,2));
	}
	public static boolean are(final Point p1, final Point p2, final Point p3) {
		float t1 = p2.y - p3.y;
		float t2 = p3.y - p1.y;
		float t3 = p1.y - p2.y;
		return Math.abs(p1.x * t1 + p2.x * t2 + p3.x * t3) == 0.0;
	}
	
}
