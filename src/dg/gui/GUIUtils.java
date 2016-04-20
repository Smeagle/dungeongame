package dg.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dg.Coordinates;
import dg.Direction;
import dg.GameState;
import dg.Gameboard;
import dg.Terrain;

public class GUIUtils {

	public static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();
	
	private static Map<Coordinates, Integer> rotationCache = new HashMap<Coordinates, Integer>();
	
	private static Map<Coordinates, Point2D> hexOffsetCache = new HashMap<Coordinates, Point2D>();
	
	private static RenderingHints renderingHints = null;
	
	public static void setRenderingHints(Graphics2D g) {
		if (renderingHints == null) {
			renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
			renderingHints.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
		}
		
	    g.setRenderingHints(renderingHints);
	}
	
	public static Dimension getFullScreenBounds() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		return new Dimension(screenWidth, screenHeight);
	}
	
	public static double getDirectionRotation(Direction dir) {
		double d = 2 * Math.PI / 12;
		
		int i = 0;
		
		switch (dir) {
		case BOTTOMLEFT:
			i = 7;
			break;
		case BOTTOMRIGHT:
			i = 5;
			break;
		case LEFT:
			i = 9;
			break;
		case RIGHT:
			i = 3;
			break;
		case TOPLEFT:
			i = 11;
			break;
		case TOPRIGHT:
			i = 1;
			break;
		default:
			return 0;
		}
		
		return (i + 1) * d;
	}
	
	/**
	 * Ermittelt die Höhe h eines gleichseitigen Dreiecks mit der Kantenlänge r.
	 */
	public static double getTriangleHeight(double r) {
		return Math.sqrt(r * r - r * r / 4);
	}
	
	/**
	 * Ermittelt x und y des Mittelpunkts eines Hexfeldes.
	 */
	public static Point2D getHexOffset(Coordinates c) {
		if (hexOffsetCache.containsKey(c)) {
			return hexOffsetCache.get(c);
		}
		
		double r = Shapes.HEX_RADIUS;
		double h = Shapes.HEX_TRIANGLE_HEIGHT;
		double dqx = r + r / 2;
		double dqy = h;
		double drx = 0;
		double dry = 2 * h;
		
		double tx = c.r * drx + c.q * dqx;
		double ty = c.r * dry + c.q * dqy;
		
		Point2D p = new Point2D.Double(tx, ty);
		hexOffsetCache.put(c, p);
		
		return p;
	}
	
	//---------------------------------------------------------------------------------------
	// wall string stuff
	static int getWallRotation(Coordinates coords) {
		if (rotationCache.containsKey(coords)) {
			return rotationCache.get(coords);
		}
		
		String wallString = getWallString(coords);
		
		String[] imageWallStrings = new String[] {
				"WWWWWW", "WWWWFW", "WWWWFF", "FWWWFF",
				"FFWWFF", "FFFWFF", "FWWWFW", "WFWWFW",
				"FFWWFW", "WFFWFW", "FFFWFW", "FWFFWF", 
				"FFFFFF", "FWFWFW"
		};
		Arrays.sort(imageWallStrings);
		
		int rotation = 0;
		for (int i = 0; i < wallString.length(); i++) {
			if (Arrays.binarySearch(imageWallStrings, wallString) >= 0) {
				rotation = i;
				break;
			}
			wallString = wallString.substring(1) + wallString.charAt(0);
		}

		rotation++;
		
		rotationCache.put(coords, rotation);
		return rotation;
	}
	
	/**
	 * Returns a string like "WWFWWF" depending on neighbor wall fields
	 */
	static String getWallString(Coordinates coords) {
		Gameboard board = GameState.getBoard();
		String wallString = "";
		for (Coordinates neighbor : Coordinates.getAdjacent(coords)) {
			if (!board.isInBounds(neighbor) || board.getTerrain(neighbor) != Terrain.WALL) {
				wallString += "W";
			}
			else {
				wallString += "F";
			}
		}
		return wallString;
	}
	
	/**
	 * "Dreht" die Fläche so, dass der String mit der längsten Wand anfängt
	 */
	static String normalizeWallString(String wallString) {
		int maxWallLength = getMaxWallLength(wallString);
		for (int i = 0; i < wallString.length(); i++) {
			if (getStartingWallLength(wallString) == maxWallLength) {
				break;
			}
			wallString = wallString.substring(1) + wallString.charAt(0);
		}
		return wallString;
	}

	private static int getMaxWallLength(String wallString) {
		int maxWallLength = 0;
		int wallLength = 0;
		boolean wallStarted = false;
		for (int i = 0; i < wallString.length() * 2; i++) {
			if (wallString.charAt(i % wallString.length()) == 'W') {
				if (wallStarted) {
					wallLength++;
				}
				else {
					wallLength = 1;
					wallStarted = true;
				}
			}
			else {
				if (wallStarted) {
					wallStarted = false;
					if (wallLength > maxWallLength) {
						maxWallLength = wallLength;
					}
				}
			}
		}
		if (wallStarted) {
			if (wallLength > maxWallLength) {
				maxWallLength = wallLength;
			}
		}
		if (maxWallLength > wallString.length()) {
			return wallString.length();
		}
		return maxWallLength;
	}

	private static int getStartingWallLength(String wallString) {
		int length = 0;
		for (int i = 0; i < wallString.length(); i++) {
			if (wallString.charAt(i) == 'W') {
				length++;
			}
			else {
				break;
			}
		}
		return length;
	}
	
}
