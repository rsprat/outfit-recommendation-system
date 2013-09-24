package rob.ors.core.utils;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.text.DecimalFormat;
import java.util.List;
import org.apache.log4j.Logger;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import rob.ors.core.config.Paths;
public class ImageReduction {
	
	private static final String BIG_IMAGES_FOLDER = Paths.BIG_IMAGES_FOLDER;
	private static final Logger LOGGER = Logger.getLogger(ImageReduction.class.getCanonicalName());
	private static final int SIDE = 80;
	
	
	public static void resizeImage(int id)
	{
		try
		{
			IplImage bigImage = cvLoadImage(BIG_IMAGES_FOLDER+id+".jpg");		
			if(bigImage == null)
			{
				LOGGER.info("garment "+id+" image not found");
				return;
			}
			
			float proportion = bigImage.width()/bigImage.height();
			IplImage smallImage=IplImage.create(SIDE,(int)(SIDE*proportion), bigImage.depth(), bigImage.nChannels());
			
			cvResize(bigImage, smallImage);
			cvSaveImage(BIG_IMAGES_FOLDER+id+".jpg", smallImage);		
			
			
			if(bigImage!=null)cvReleaseImage(bigImage);
			if(smallImage!=null)cvReleaseImage(smallImage);
		}catch(Exception e){e.printStackTrace();}
		
	}

	public static void main(String[] args)
	{		
		List<String> big = DirectoryReader.getFileNames(Paths.BIG_IMAGES_FOLDER, "jpg");
		List<String> small = DirectoryReader.getFileNames(Paths.BIG_IMAGES_FOLDER, "jpg");
		LOGGER.info("big: "+big.size()+" small:"+small.size());		
		big.removeAll(small);
		LOGGER.info("remaining: "+big.size());		
		
		
		class ResizeImageThread implements Runnable
		{
			List<String> fileNames;
			private DecimalFormat df = new DecimalFormat("#.##");
			public ResizeImageThread(List<String> fileNames)
			{
				this.fileNames = fileNames;
				
			}
			@Override
			public void run()
			{
				int i=0;
				for(String fileName : fileNames)    	
		    	{      	    	
					String fileId = fileName.substring(0, fileName.length()-("jpg".length()+1));
		    		ImageReduction.resizeImage(Integer.parseInt(fileId));	
		    		if(++i%100==0)
		    		{
		    			double completion = ((float)i/fileNames.size())*100;
		    			LOGGER.info("processed: "+i+"/"+fileNames.size()+"("+df.format(completion)+"%)");	
		    		}
		    	}				
			}
			
		}
		
		Thread[] threads = new Thread[3];
		int garmentsPerThread = big.size()/threads.length;
		for(int i=0;i<threads.length;i++)
		{
			int startIndex = garmentsPerThread*i;
			int endIndex;
			if(i<threads.length-1) endIndex = startIndex+garmentsPerThread;
			else endIndex = big.size()-1;
			threads[i]= new Thread(new ResizeImageThread(big.subList(startIndex, endIndex)));
			threads[i].start();
		}
		for(int i=0;i<threads.length;i++)
		{
			try {
				threads[i].wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
