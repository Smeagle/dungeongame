package dg.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import dg.Coordinates;
import dg.GameState;
import dg.Gameboard;
import dg.Terrain;

public class GUIUtils {

	public static Dimension getFullScreenBounds() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		return new Dimension(screenWidth, screenHeight);
	}
	
	
	//---------------------------------------------------------------------------------------
	// wall string stuff
	static double getWallRotation(Coordinates coords) {
		String wallString = getWallString(coords);
		
		String[] imageWallStrings = new String[] {
				"WWWWWW", "WWWWFW", "WWWWFF", "FWWWFF",
				"FFWWFF", "FFFWFF", "FWWWFW", "WFWWFW",
				"FFWWFW", "WFFWFW", "FFFWFW", "FWFFWF", 
				"FFFFFF", "FWFWFW"
		};
		Arrays.sort(imageWallStrings);
		
		for (int i = 0; i < wallString.length(); i++) {
			if (Arrays.binarySearch(imageWallStrings, wallString) >= 0) {
				return i;
			}
			wallString = wallString.substring(1) + wallString.charAt(0);
		}
		
		return 0;
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
