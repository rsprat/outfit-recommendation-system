package rob.ors.garmentssimilarity.imageProcessing;
import org.apache.log4j.Logger;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
public class BackgroundExtractor {
	private static Logger LOGGER = Logger.getLogger(BackgroundExtractor.class.getCanonicalName());
	/**
	 * 
	 * @param image BGR image of the garment
	 * @return
	 */
	public static CvMat colorMask(IplImage src)
	{
		/*IplImage image = cvCloneImage(src);	
		IplImage grayImage = cvCreateImage(cvGetSize(image),IPL_DEPTH_8U,1);
		IplImage binaryImage = cvCreateImage(cvGetSize(image),IPL_DEPTH_8U,1);
		cvCvtColor(image,grayImage,CV_BGR2GRAY);
		cvThreshold(grayImage, binaryImage, 245, 255, CV_THRESH_BINARY_INV);
		CvMat auxMat = cvCreateMat(binaryImage.height(), binaryImage.width(),CV_8U);		
		CvMat mat =  cvGetMat(binaryImage,auxMat,null, 0);
		cvReleaseImage(image);
		cvReleaseImage(grayImage);		
		cvReleaseImage(binaryImage);
		cvReleaseMat(mat);
		cvReleaseMat(auxMat);*/
		
		
		IplImage image = src.clone();
		//IplImage image = cvCloneImage(src);		
		IplImage grayImage = IplImage.create(cvGetSize(image),IPL_DEPTH_8U,1);
		IplImage binaryImage = IplImage.create(cvGetSize(image),IPL_DEPTH_8U,1);
		cvCvtColor(image,grayImage,CV_BGR2GRAY);
		cvThreshold(grayImage, binaryImage, 245, 255, CV_THRESH_BINARY_INV);		
		CvMat mat = binaryImage.asCvMat();
		image.release();
		grayImage.release();		
		binaryImage.release();
		//mat.release();
		//cvReleaseImage(binaryImage);
		//cvReleaseMat(returnMat);
		return mat.clone();
	}
	
	public static IplImage colorMaskIpl(IplImage src)
	{
		IplImage image = src.clone();		
		IplImage grayImage = IplImage.create(cvGetSize(image),IPL_DEPTH_8U,1);
		IplImage binaryImage = IplImage.create(cvGetSize(image),IPL_DEPTH_8U,1);
		cvCvtColor(image,grayImage,CV_BGR2GRAY);
		cvThreshold(grayImage, binaryImage, 245, 255, CV_THRESH_BINARY_INV);
		image.release();
		grayImage.release();	
		return binaryImage;
	}

	public static void main(String[] args)
	{			
		String garmentName = "garment_test_hist_simil_7";
		IplImage garment = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garmentName+".jpg");
	
		for(double i = 0;i<Double.MAX_VALUE;i++)
			if(i%100 == 0)System.out.println(i);
		{
			CvMat garmentMask  = BackgroundExtractor.colorMask(garment);	
			cvReleaseMat(garmentMask);
			//cvReleaseImage(xx);
			//System.gc();
			
		}
		
	}
}
