package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class FileManager {
	public static BufferedImage loadImage(String imgFileName) {
		URL imgUrl = FileManager.class.getClassLoader().getResource(imgFileName);
		if (imgUrl == null) {
			System.err.println("File not found:" + imgFileName);
			return null;
		} else {
			try {
				BufferedImage img = ImageIO.read(imgUrl);
				return img;
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}
}