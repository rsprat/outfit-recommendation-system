package rob.ors.core.utils;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import rob.ors.core.config.Paths;


public class Utils
{
	private static String IMAGES_SRC_PATH = Paths.BIG_IMAGES_FOLDER;
	public static int[] toArray(Collection<Integer> collection)
	{
		if(collection==null)return new int[0];
		int[] aux = new int[collection.size()];
		Iterator<Integer> it = collection.iterator();
		int i = 0;
		while(it.hasNext())
		{
			Integer current = it.next();
			if(current!=null)aux[i++]=current;
			else aux[i++]=Integer.MIN_VALUE;
		}
		return aux;		
	}
	
	/**
	 * Checks if an image exists for the garment with the given iid
	 * @param iid
	 * @return
	 */
	public static boolean hasImage(int iid)
	{
		File f = new File((IMAGES_SRC_PATH+iid+".jpg"));
		return f.exists();
	}
	
	public static IplImage loadImage(int iid)
	{
		final float FACTOR  = 0.53f;
		BufferedImage img;
		try {
			img = ImageIO.read(new File((IMAGES_SRC_PATH+iid+".jpg")));
			int scaleX = (int) (img.getWidth() * FACTOR);
			int scaleY = (int) (img.getHeight() * FACTOR);
			Image image = img.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
			BufferedImage buffered = new BufferedImage(scaleX, scaleY, BufferedImage.TYPE_INT_BGR);
			buffered.getGraphics().drawImage(image, 0, 0 , null);
			IplImage ret = IplImage.createFrom( buffered);
			buffered.getGraphics().dispose();
			return  ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return null;
	}
	

}
