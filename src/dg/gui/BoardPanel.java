package dg.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;

import dg.Agent;
import dg.Coordinates;
import dg.GameState;
import dg.Terrain;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final double HEX_RADIUS = 50;
	
	private static final boolean DRAW_TEXTS = false;
	private static final boolean DRAW_MOUSE = false;
	
	private static final boolean DRAW_MOUSEOVER = true;
	private static final Color MOUSEOVER_COLOR = Color.white;
	private static final Stroke MOUSEOVER_STROKE = new BasicStroke(1);
	
	private static final Color SELECTION_COLOR = Color.orange;
	private static final Stroke SELECTION_STROKE = new BasicStroke(3);
	
	private static BoardPanel instance = null;
	
	static double translateX = 0;
	static double translateY = 0;
	static double scale = 1;

	public static int mouseX = 0;
	public static int mouseY = 0;
	
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
		
	    AffineTransform viewportTransform = new AffineTransform();
	    viewportTransform.translate(translateX, translateY);
	    viewportTransform.scale(scale, scale);
		
		Path2D hex = getHexPolygon();
		
		Hashtable<Coordinates, Terrain> grid = GameState.getBoard().getGrid();
		for (Coordinates c : grid.keySet()) {
			Point2D hexOffset = getHexOffset(c);
			AffineTransform hexTransform = getHexTransform(c, hexOffset);
			
			// hex texture
			Image image = ImageCache.getImage(c, grid.get(c));
			if (image != null) {
				g2.setTransform(hexTransform);
				g2.setClip(hex);
				double r = HEX_RADIUS;
				double h = getTriangleHeight(HEX_RADIUS);
				g2.drawImage(image, (int) -h - 1, (int) -r - 1, (int) (2 * h) + 2, (int) (2 * r) + 2, null);
				g2.setTransform(new AffineTransform());
				g2.setClip(null);
			}

			// grid
			g2.setColor(Color.black);
			g2.draw(hexTransform.createTransformedShape(hex));
			
			if (DRAW_TEXTS) {
				AffineTransform textTransform = getHexTextTransform(hexOffset);
				g2.setTransform(textTransform);
				
				g2.setColor(Color.white);
				String string = c.toString();
				Rectangle2D stringRect = g2.getFontMetrics().getStringBounds(string, null);
				g2.drawString(string, (int) (-stringRect.getWidth() / 2), (int) (stringRect.getHeight() / 2));
				
				g2.setTransform(new AffineTransform());
			}
		}

		// mouseover
		if (DRAW_MOUSEOVER) {
			Coordinates c = GameState.getMouseoverCoordinates();
			if (c != null) {
				Point2D hexOffset = getHexOffset(c);
				AffineTransform hexTransform = getHexTransform(c, hexOffset);
				g2.setColor(MOUSEOVER_COLOR);
				g2.setStroke(MOUSEOVER_STROKE);
				g2.draw(hexTransform.createTransformedShape(hex));
			}
		}
		
		// selection
		Coordinates c = GameState.getSelectionCoordinates();
		if (c != null) {
			Point2D hexOffset = getHexOffset(c);
			AffineTransform hexTransform = getHexTransform(c, hexOffset);
			g2.setColor(SELECTION_COLOR);
			g2.setStroke(SELECTION_STROKE);
			g2.draw(hexTransform.createTransformedShape(hex));
		}
		
		// agents
		for (Agent agent : GameState.getBoard().getAgents()) {
			Image image = ImageCache.getImage(agent.getImage());
			if (image != null) {
				Point2D hexOffset = getHexOffset(agent.getPosition());
				AffineTransform t = getAgentTransform(hexOffset);
				g2.setTransform(t);
				double r = HEX_RADIUS;
				double h = getTriangleHeight(HEX_RADIUS);
				g2.drawImage(image, (int) -h - 1, (int) -r - 1, (int) (2 * h) + 2, (int) (2 * r) + 2, null);
				g2.setTransform(new AffineTransform());
			}
		}
		
		// mouse
		if (DRAW_MOUSE) {
			g2.setColor(Color.white);
			g2.drawLine(mouseX - 10, mouseY, mouseX + 10, mouseY);
			g2.drawLine(mouseX, mouseY - 10, mouseX, mouseY + 10);
		}
	};
	
	static void updateMouseoverCoordinates() {
		Path2D hex = BoardPanel.getHexPolygon();
		Hashtable<Coordinates, Terrain> grid = GameState.getBoard().getGrid();
		for (Coordinates c : grid.keySet()) {
			if (BoardPanel.getTransformedHex(c, hex).contains(BoardPanel.mouseX, BoardPanel.mouseY)) {
				GameState.setMouseoverCoordinates(c);
				return;
			}
		}
		GameState.setMouseoverCoordinates(null);
	}
	
	static Shape getTransformedHex(Coordinates c, Path2D hex) {
		Point2D hexOffset = getHexOffset(c);
		AffineTransform hexTransform = getHexTransform(c, hexOffset);
		return hexTransform.createTransformedShape(hex);
	}
	
	/**
	 * Ermittelt die Höhe h eines gleichseitigen Dreiecks mit der Kantenlänge r.
	 */
	private static double getTriangleHeight(double r) {
		return Math.sqrt(r * r - r * r / 4);
	}

	static AffineTransform getHexTransform(Coordinates c, Point2D hexOffset) {
		AffineTransform hexTransform = new AffineTransform();
		hexTransform.translate(translateX, translateY);
		hexTransform.scale(scale, scale);
		hexTransform.translate(hexOffset.getX(), hexOffset.getY());
		hexTransform.rotate(GUIUtils.getWallRotation(c) * (2 * Math.PI / 6));
		return hexTransform;
	}
	
	private AffineTransform getHexTextTransform(Point2D hexOffset) {
		AffineTransform textTransform = new AffineTransform();
		textTransform.translate(translateX, translateY);
		textTransform.scale(scale, scale);
		textTransform.translate(hexOffset.getX(), hexOffset.getY());
		textTransform.scale(1 / scale, 1 / scale);
		return textTransform;
	}
	
	private AffineTransform getAgentTransform(Point2D hexOffset) {
		AffineTransform transform = new AffineTransform();
		transform.translate(translateX, translateY);
		transform.scale(scale, scale);
		transform.translate(hexOffset.getX(), hexOffset.getY());
		return transform;
	}
	
	private static Point2D getHexOffset(Coordinates c) {
		double r = HEX_RADIUS;
		double h = getTriangleHeight(r);
		double dqx = 2 * h;
		double dqy = 0;
		double drx = h;
		double dry = r + r / 2;
		
		double tx = c.r * drx + c.q * dqx;
		double ty = c.r * dry + c.q * dqy;
		
		return new Point2D.Double(tx, ty);
	}
	
	static Path2D getHexPolygon() {
		// calculate points
		List<Point2D> points = new ArrayList<Point2D>();
		Point2D center = new Point2D.Double(0, 0);
		for (int i = 0; i < 6; i++) {
			points.add(hexCorner(center, HEX_RADIUS, i));
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
