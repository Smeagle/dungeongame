package dg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;

import dg.Coordinates;
import dg.GameState;
import dg.Terrain;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final boolean DRAW_TEXTS = true;
	
	private static BoardPanel instance = null;
	
	static double translateX = 0;
	static double translateY = 0;
	static double scale = 1;
	
	public static BoardPanel getInstance() {
		if (instance == null) {
			instance = new BoardPanel();
		}
		return instance;
	}
	
	private BoardPanel() {
		this.setBackground(Color.decode("#6F4E37")); // coffee brown!
		
		Dimension fullscreenDim = GUIUtils.getFullScreenBounds();
		translateX = fullscreenDim.getWidth() / 2;
		translateY = fullscreenDim.getHeight() / 2;
		
		this.addMouseMotionListener(BoardMouseListener.getInstance());
		this.addMouseListener(BoardMouseListener.getInstance());
		this.addMouseWheelListener(BoardMouseListener.getInstance());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHints(rh);
		
		g2.translate(translateX, translateY);
		g2.scale(scale, scale);
		
		double r = 50;
		double h = Math.sqrt(r * r - r * r / 4);
		double dqx = 2 * h;
		double dqy = 0;
		double drx = h;
		double dry = r + r / 2;
		
		Path2D hex = getHexPolygon(r);
		
		Hashtable<Coordinates, Terrain> grid = GameState.getBoard().getGrid();
		for (Coordinates c : grid.keySet()) {
			double tx = c.r * drx + c.q * dqx;
			double ty = c.r * dry + c.q * dqy;
			
			AffineTransform transform = g2.getTransform(); // save transform
			
			g2.translate(tx, ty);
			
			// coord image
			Image image = ImageCache.getImage(c, grid.get(c));
			if (image != null) {
				g2.setClip(hex);
				g2.rotate(GUIUtils.getWallRotation(c) * (2 * Math.PI / 6));
				g2.drawImage(image, (int) -h - 1, (int) -r - 1, (int) (2 * h) + 2, (int) (2 * r) + 2, null);
			}
			g2.setClip(null);
			
			// grid
			g2.setColor(Color.black);
			g2.draw(hex);
			
			if (DRAW_TEXTS) {
				// text
				g2.scale(1 / scale, 1 / scale);
				g2.setColor(Color.white);
				String string = c.toString();
				Rectangle2D stringRect = g2.getFontMetrics().getStringBounds(string, null);
				g2.drawString(string, (int) (-stringRect.getWidth() / 2), (int) (stringRect.getHeight() / 2));
			}
			
			g2.setTransform(transform); // restore transform
		}
	};
	
	private static Path2D getHexPolygon(double r) {
		// calculate points
		List<Point2D> points = new ArrayList<Point2D>();
		Point2D center = new Point2D.Double(0, 0);
		for (int i = 0; i < 6; i++) {
			points.add(hexCorner(center, r, i));
		}

		// build path
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
	    double angle_deg = 60 * i + 30;
	    double angle_rad = Math.PI / 180 * angle_deg;
	    return new Point2D.Double(center.getX() + size * Math.cos(angle_rad), center.getY() + size * Math.sin(angle_rad));
	}

}
