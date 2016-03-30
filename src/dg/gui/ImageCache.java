package dg.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import dg.Coordinates;
import dg.Terrain;


public class ImageCache {

	private static final String BASE_DIR = "images/";
	
	public static final String FLOOR = "terrain/Floor01.png";
	public static final String WALL = "terrain/Wall01.png";
	public static final String FOG = "terrain/Fogofwar.png";
	public static final String WALL_5_SIDED = "terrain/Wall02.png"; // 5
	public static final String WALL_4_SIDED = "terrain/Wall03.png"; // 4
	public static final String WALL_3_SIDED = "terrain/Wall04.png"; // 3
	public static final String WALL_2_SIDED = "terrain/Wall05.png"; // 2
	public static final String WALL_1_SIDED = "terrain/Wall06.png"; // 1
	public static final String WALL_3_1_SIDED = "terrain/Wall07.png"; // 4
	public static final String WALL_2_2_SIDED = "terrain/Wall08.png"; // 4
	public static final String WALL_2_1_SIDED = "terrain/Wall10.png"; // 3
	public static final String WALL_1_2_SIDED = "terrain/Wall11.png"; // 3
	public static final String WALL_1_1_SIDED = "terrain/Wall12.png"; // 2
	public static final String WALL_1_1_OPPOSING_SIDED = "terrain/Wall13.png"; // 2
	public static final String WALL_NO_SIDES = "terrain/Wall14.png"; // 0
	public static final String WALL_ALTERNATING = "terrain/Wall15.png"; // 3
	public static final String ROGUERIGO = "roguerigo.png";
	public static final String GUARD = "guard.png";
	public static final String DUMMY = "dummy.png";
	
	private static Hashtable<String, Image> cache = new Hashtable<String, Image>();
	
	private static Hashtable<Coordinates, Image> coordinatesCache = new Hashtable<Coordinates, Image>();
	
	public static Image getImage(String name) {
		if (cache.containsKey(name)) {
			return cache.get(name);
		}
		try {
			BufferedImage image = ImageIO.read(new File(BASE_DIR + name));
			cache.put(name, image);
			return image;
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	static Image getImage(Coordinates coords, Terrain terrain) {
		if (coordinatesCache.containsKey(coords)) {
			return coordinatesCache.get(coords);
		}
		
		Image image = null;
		switch (terrain) {
		case FLOOR:
			image = getImage(ImageCache.FLOOR);
			break;
		case WALL:
			image = getWallImage(coords);
			break;
		default:
			image = null;
			break;
		}
		
		coordinatesCache.put(coords, image);
		return image;
	}
	
	private static Image getWallImage(Coordinates coords) {
		String wallString = GUIUtils.getWallString(coords);
		wallString = GUIUtils.normalizeWallString(wallString); // rotates so that the longest wall is at the start
		
		switch (wallString) {
		case "WWWWWW":
			return getImage(WALL);
		case "WWWWWF":
			return getImage(WALL_5_SIDED);
		case "WWWWFF":
			return getImage(WALL_4_SIDED);
		case "WWWFWF":
			return getImage(WALL_3_1_SIDED);
		case "WWFWWF":
			return getImage(WALL_2_2_SIDED);
		case "WWWFFF":
			return getImage(WALL_3_SIDED);
		case "WWFWFF":
			return getImage(WALL_2_1_SIDED);
		case "WWFFWF":
			return getImage(WALL_1_2_SIDED);
		case "WWFFFF":
			return getImage(WALL_2_SIDED);
		case "WFWFFF":
		case "WFFFWF":
			return getImage(WALL_1_1_SIDED);
		case "WFFWFF":
			return getImage(WALL_1_1_OPPOSING_SIDED);
		case "WFFFFF":
			return getImage(WALL_1_SIDED);
		case "FFFFFF":
			return getImage(WALL_NO_SIDES);
		case "WFWFWF":
			return getImage(WALL_ALTERNATING);
		}
		
		return null;
	}

}
