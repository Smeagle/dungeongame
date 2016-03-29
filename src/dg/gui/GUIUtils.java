package dg.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dg.Coordinates;
import dg.GameState;
import dg.Gameboard;
import dg.Terrain;

public class GUIUtils {

	private static Map<Coordinates, Integer> rotationCache = new HashMap<Coordinates, Integer>();
	
	public static Dimension getFullScreenBounds() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		return new Dimension(screenWidth, screenHeight);
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
		double r = Shapes.HEX_RADIUS;
		double h = GUIUtils.getTriangleHeight(r);
		double dqx = 2 * h;
		double dqy = 0;
		double drx = h;
		double dry = r + r / 2;
		
		double tx = c.r * drx + c.q * dqx;
		double ty = c.r * dry + c.q * dqy;
		
		return new Point2D.Double(tx, ty);
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
