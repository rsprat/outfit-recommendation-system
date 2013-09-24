package rob.ors.core.utils;

import static com.googlecode.javacv.cpp.opencv_core.cvGetImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetMat;
import static com.googlecode.javacv.cpp.opencv_core.cvLine;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImagePrinter {
	private CvMat matrix;	
	
	/**
	 * Create a image printer with an image copy of the given one
	 * @param srcImage
	 */
	public ImagePrinter(final IplImage srcImage)
	{
		matrix = CvMat.createHeader(srcImage.height(), srcImage.width(),srcImage.depth(), srcImage.nChannels());
		cvGetMat(srcImage, matrix,null, 0); 
	}
	public CvMat getMatrix()
	{
		return matrix;
	}
	public void print(String path)
	{
		IplImage image = new IplImage();		
		image = cvGetImage(matrix, image);
		cvSaveImage(path, image);		
	}
	
	
	public void drawWidth(int[] width)
	{
		if(width[0]!=CvMatUtils.invalidMarker && width[1]!=CvMatUtils.invalidMarker && width[2]!=CvMatUtils.invalidMarker)	
			cvLine(matrix,new CvPoint(width[1],width[0]),new CvPoint(width[2]+width[1],width[0]),CvScalar.GREEN,1,8,0);
	}		
	
	public void drawHorzontalLine(int y)
	{		
		cvLine(matrix,new CvPoint(0,y),new CvPoint(matrix.cols(),y),CvScalar.RED,1,8,0);
	}

	public void put(CvMat image,int y,int x,CvScalar color)
	{
		for(int c = 0; c<image.channels();c++)
		{			
			if(x!=CvMatUtils.invalidMarker && y != CvMatUtils.invalidMarker) image.put(y*image.step()/image.elemSize()+x*image.channels() + c, color.getVal(c));
		}
	}

}
