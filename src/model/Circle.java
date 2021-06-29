package model;

import java.util.List;

public class Circle {	
	public Point cen;
	public float rad;

	public Circle(final Point center, float radius) {
		this.cen = center;
		this.rad = radius;
	}

	public Circle(float x, float y, float radius) {
		cen = new Point(x, y);
		this.rad = radius;
	}
	public Circle(final Point p1, final Point p2) {
		float x = (float) ((p1.x + p2.x) * 0.5);
		float y = (float) ((p1.y + p2.y) * 0.5);
		cen = new Point(x,y);
		rad = cen.distance(p1);
	}

	public Circle(final Point p1, final Point p2, final Point p3) {
		float baY = p2.y - p1.y;
		float cbY=  p3.y - p2.y;
		if (baY == 0.0 || cbY == 0.0) {
			cen = new Point(0, 0);
			rad = 0;
		}
		else {
			float baX = -(p2.x - p1.x) / baY;
			float cbX = -(p3.x - p2.x) / cbY;
			float sub= cbX-baX;
			if (sub == 0.0) {
				cen = new Point(0, 0);
				rad = 0;
			}
			else {
				float p2X = p2.x * p2.x;
				float p2Y = p2.y * p2.y;
				float f1 = (float) ((p2X - p1.x * p1.x + p2Y - p1.y * p1.y) / (2.0 * baY));
				float f2 = (float) ((p3.x * p3.x - p2X + p3.y * p3.y - p2Y) / (2.0 * cbY));
				float pointX = (f1 - f2) / sub;
				float pointY = baX * pointX + f1;
				float dX = p1.x - pointX;
				float dY = p1.y - pointY;
				cen = new Point(pointX, pointY);
				rad = (float) Math.sqrt(dX * dX + dY * dY);
			}

		}
	}

	public boolean isContainsAllPoints(List<Point> points) {
		for (Point p : points) {
			if (p != cen && !isContainsPoint(p))
				return false;
		}
		return true;
	}
	public boolean isContainsPoint(Point p) {
		return p.distanceSquared(cen) <= rad * rad;
	}
	
}