package dg.gui;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shapes {

	public static final double HEX_RADIUS = 50;
	public static final double HEX_TRIANGLE_HEIGHT = GUIUtils.getTriangleHeight(HEX_RADIUS);
	
	private static final double VIEW_DIRECTION_TRIANGLE_SIZE = 20;
	private static final double VIEW_DIRECTION_TRIANGLE_HEIGHT = GUIUtils.getTriangleHeight(VIEW_DIRECTION_TRIANGLE_SIZE);
	
	public static final int HEX = 0;
	public static final int VIEW_TRIANGLE = 1;
	
	private static Map<Integer, Shape> cache = new HashMap<Integer, Shape>();
	
	public static Shape getShape(int id) {
		if (!cache.containsKey(id)) {
			switch (id) {
			case 0:
				cache.put(id, getHexPolygon());
				break;
			case 1:
				cache.put(id, getViewDirectionPolygon());
				break;
			default: 
				throw new IllegalArgumentException("unknown shape id");
			}
		}
		return cache.get(id);
	}
	
	private static Shape getViewDirectionPolygon() {
		// calculate points
		double h = HEX_TRIANGLE_HEIGHT;
		
		double r2 = VIEW_DIRECTION_TRIANGLE_SIZE;
		double h2 = VIEW_DIRECTION_TRIANGLE_HEIGHT;
		
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D.Double(-r2 / 2, -h + h2));
		points.add(new Point2D.Double(r2 / 2, -h + h2));
		points.add(new Point2D.Double(0, -h));
		
		return buildShape(points);
	}

	private static Shape getHexPolygon() {
		// calculate points
		List<Point2D> points = new ArrayList<Point2D>();
		Point2D center = new Point2D.Double(0, 0);
		for (int i = 0; i < 6; i++) {
			points.add(hexCorner(center, HEX_RADIUS, i));
		}

		return buildShape(points);
	}
	
	private static Shape buildShape(List<Point2D> points) {
		Path2D path = new Path2D.Double();

		Point2D p = points.get(0);
		path.moveTo(p.getX(), p.getY());
		for (int i = 1; i < points.size(); i++) {
			p = points.get(i);
			path.lineTo(p.getX(), p.getY());
		}
		path.closePath();
		return path;
	}
	
	private static Point2D hexCorner(Point2D center, double size, int i) {
	    double angle_deg = 60 * i;
	    double angle_rad = Math.PI / 180 * angle_deg;
	    return new Point2D.Double(center.getX() + size * Math.cos(angle_rad), center.getY() + size * Math.sin(angle_rad));
	}
	
}
