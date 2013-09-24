package rob.ors.garmentssimilarity.imageProcessing;


import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.CV_8UC1;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
public class NoiseDetection {
	
	/**
	 * Return the percentage of noise in the image.
	 * After using a canny edge detector, summ all white pixels and divide them by the total pixels of the image. 
	 * Return this value times 100.
	 * @param image
	 * @return
	 */
	public static float noiseValue(IplImage image,CvMat mask)
	{
		CvMat edges = cvCreateMat(image.cvSize().height(), image.cvSize().width(),CV_8UC1);
		cvCvtColor(image, edges, CV_BGR2GRAY);				
		
		CvMat blured = cvCreateMat(image.cvSize().height(), image.cvSize().width(),CV_8UC1);
		blur( edges, blured, new CvSize(5,5) ,new CvPoint(),CV_BLUR);
		
		cvCanny(blured, edges, 50, 50,3);		
		
		float noise = (((float)cvCountNonZero(edges)/cvCountNonZero(mask)))*100;
		
		cvReleaseMat(blured);
		cvReleaseMat(edges);

		return noise;
	}	

}
