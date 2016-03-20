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
	
	static final String FLOOR = "terrain/Floor01.png";
	static final String WALL = "terrain/Wall01.png";
	
	private static Hashtable<String, Image> cache = new Hashtable<String, Image>();
	
	static Image getImage(String name) {
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
		switch (terrain) {
		case FLOOR:
			return getImage(ImageCache.FLOOR);
		case WALL:
			return getImage(ImageCache.WALL);
		default:
			return null;
		}
	}
	
}
