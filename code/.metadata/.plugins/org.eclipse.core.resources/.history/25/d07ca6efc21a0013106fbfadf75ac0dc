package rob.ors.garmentsclustering.similarityMatrix;

import static com.googlecode.javacv.cpp.opencv_core.cvReleaseMat;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCloneImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import rob.ors.core.config.Paths;
import rob.ors.core.utils.Utils;
import rob.ors.garmentssimilarity.GarmentComparator;
import rob.ors.garmentssimilarity.imageProcessing.BackgroundExtractor;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public abstract class SimilarityFunction
{

	protected Logger LOGGER = Logger.getLogger(SimilarityFunction.class.getCanonicalName());
	protected static final DecimalFormat df = new DecimalFormat("#.##");
	private static final String IMAGES_SRC_PATH = Paths.BIG_IMAGES_FOLDER;
	protected static IplImage blackImage=  cvLoadImage(IMAGES_SRC_PATH+"black.jpg");

	protected Map<Integer,IplImage> images;
	protected Map<Integer,CvMat> masks;
	

	protected SimilarityFunction()
	{
		images = new HashMap<Integer,IplImage>();
		masks = new HashMap<Integer,CvMat>();
	}
	 
	/*private void loadImage(IplImage garmentImage, CvMat garmentMask, int garmentId)
	{
		garmentImage = garmentsComparator.getGarmentImage(garmentId);
		if(garmentImage==null)garmentImage = blackImage;//TODO: CAUTION!! REFERENCE TO THE BLACK IMAGE MAY CAUSE PROBLEMS
		garmentMask = BackgroundExtractor.colorMask(CvMatUtils.getCvMatFromIplImage(garmentImage),CV_RGB(255,255,255),10);
	}*/
	
	public void loadImages(int[] garmentsIds)
	{
		//LOGGER.info("LOAD "+garmentsIds.length+" images");
		for(int garmentId : garmentsIds)
		{
			//images.put(garmentId, GarmentComparator.getGarmentImage(garmentId));
			images.put(garmentId, Utils.loadImage(garmentId));
			if(images.get(garmentId)==null)
			{
				images.put(garmentId, blackImage.clone());			
			}			
			IplImage back = BackgroundExtractor.colorMaskIpl(images.get(garmentId));
			masks.put(garmentId,back.asCvMat().clone());
			back.release();
		}
	}
	
	public void clearImages()
	{		
		//LOGGER.info("RELEASING IMAGES");
		int counter = 0;
		for(CvMat mask : masks.values())
		{		
			if(mask!=null){
					mask.release();	
					counter++;
				}		
		}
		//LOGGER.info("released "+counter+" masks");
		 counter = 0;
		for(IplImage image : images.values())
		{			
			if(image!=null){
				image.release();
				counter++;
				
			}
			
		}
		
		//LOGGER.info("released "+counter+" images");
		images = new HashMap<Integer,IplImage>();
		masks = new HashMap<Integer,CvMat>();
	}	

	public abstract double getSimilarity(int iid1,int iid2);
	public abstract double getSimilarity(IplImage anGarmentImage,CvMat anGarmentMask,IplImage anotherGarmentImage,CvMat anotherGarmentMask,int iid1,int iid2);

	/**
	 * Called when the slice of the matrix being computed changes. This call should be used by the similarity functions to clear all the stored data for the 
	 * processed garments
	 */
	public abstract void matrixSliceChange();
}