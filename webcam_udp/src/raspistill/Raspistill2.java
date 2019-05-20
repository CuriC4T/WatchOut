package raspistill;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

//This class is a very simple Java wrapper for the raspistill executable,
//which makes it easier to take photos from a Java application. Note that
//there are considerably more parameters available for raspistill which
//could be added to this class. (e.g. Shutter Speed, ISO, AWB, etc.) 

public class Raspistill2 {
// Define the path to the raspistill executable.
	private final String _raspistillPath = "/opt/vc/bin/raspistill";
// Define the amount of time that the camera will use to take a photo.
	private final int _picTimeout = 5000;
// Define the image quality.
	private final int _picQuality = 100;

// Specify a default image width.
	private int _picWidth = 1024;
// Specify a default image height.
	private int _picHeight = 768;
// Specify a default image name.
	private String _picName = "example.jpg";
// Specify a default image encoding.
	private String _picType = "jpg";

	Image image;

// Default class constructor.
	public Raspistill2() {
		// Do anything else here. For example, you could create another
		// constructor which accepts an alternate path to raspistill,
		// or defines global parameters like the image quality.
	}

// Default method to take a photo using the private values for name/width/height.
// Note: See the overloaded methods to override the private values.
	public BufferedImage TakePicture() {
		Process pros;
		BufferedImage bufferedImage =null;
		try {
			// Determine the image type based on the file extension (or use the default).
			if (_picName.indexOf('.') != -1)
				_picType = _picName.substring(_picName.lastIndexOf('.') + 1);

			// Create a new string builder with the path to raspistill.
			StringBuilder sb = new StringBuilder(_raspistillPath);

			// Add parameters for no preview and burst mode.
			sb.append(" -n -bm");
			// Configure the camera timeout.
			sb.append(" -t " + _picTimeout);
			// Configure the picture width.
			sb.append(" -w " + _picWidth);
			// Configure the picture height.
			sb.append(" -h " + _picHeight);
			// Configure the picture quality.
			sb.append(" -q " + _picQuality);
			// Specify the image type.
			sb.append(" -e " + _picType);
			// Specify the name of the image.
			sb.append(" -o " + _picName);

			// Invoke raspistill to take the photo.
			pros = Runtime.getRuntime().exec(sb.toString());
			InputStream is = pros.getInputStream();
			image = ImageIO.read(is);
			bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			// Pause to allow the camera time to take the photo.
			Thread.sleep(_picTimeout);
		} catch (Exception e) {
			// Exit the application with the exception's hash code.
			System.exit(e.hashCode());
		}

		return bufferedImage;
	}

// Overloaded method to take a photo using specific values for the name/width/height.
//	public void TakePicture(String name, int width, int height) {
//		_picName = name;
//		_picWidth = width;
//		_picHeight = height;
//		TakePicture();
//	}
//
//// Overloaded method to take a photo using a specific value for the image name.
//	public void TakePicture(String name) {
//		TakePicture(name, _picWidth, _picHeight);
//	}
//
//// Overloaded method to take a photo using specific values for width/height.
//	public void TakePicture(int width, int height) {
//		TakePicture(_picName, width, height);
//	}

}
