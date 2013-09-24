package rob.ors.garmentssimilarity.imageProcessing;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import rob.ors.core.model.api.Category;
import rob.ors.core.utils.CvMatUtils;
import rob.ors.core.utils.ImagePrinter;



import com.googlecode.javacv.cpp.opencv_core.CvMat;



public abstract class GarmentProportionsExtractor {
	private static Logger LOGGER = Logger.getLogger(GarmentProportionsExtractor.class.getCanonicalName());
	public abstract Set<Category> getAplicableCategories();
	
	public abstract Map<Integer,Integer> getProportions(CvMat mask);
	
	public abstract void printProportions(ImagePrinter printer, CvMat mask);
	
	private static float[] getGarmentWidths(CvMat garmentMask,int garmentTop,int garmentHeight)
	{
		//IplImage image = cvCreateImage(cvGetSize(garmentMask),IPL_DEPTH_8U , 1);		
		IplImage image = new IplImage();		
		cvGetImage(garmentMask, image);
		float[] widths = new float[100];
		for(int i = 0;i<100;i++)
		{
			widths[i] = GarmentProportionsExtractor.widthAtHeigth(image,i,garmentTop,garmentHeight,garmentMask.rows(),garmentMask.cols());				
		}
		return widths;
	}	

	public static float[] getGarmentProportions(CvMat garmentMask)
	{
		int garmentTop = GarmentProportionsExtractor.getTop(garmentMask);
		int garmentHeight = Math.abs(GarmentProportionsExtractor.getBottom(garmentMask)-garmentTop);	
		float[] widths =  getGarmentWidths(garmentMask,garmentTop,garmentHeight);
		float[] proportions = new float[widths.length];
		for(int i = 0;i<100;i++){proportions[i]=widths[i]/garmentHeight;}
		return proportions;
	}
	
	public int getHeight(CvMat mask)
	{
		int top = getTop(mask);
		return getBottom(mask) - top;
	}

	
	public static float widthAtHeigth(IplImage maskImage,int heigthPercentage,int top,int heigth,int rows,int cols)
	{

			int y =(int) (heigth*((float)heigthPercentage/100))+top;
			if(heigth == 0 || heigthPercentage< 0 || heigthPercentage>100)
			{
				return CvMatUtils.invalidMarker;	
			}
			else
			{
				cvSetImageROI(maskImage,new CvRect(0,y,cols-1,1));					
				float val  = cvCountNonZero(maskImage);	 
				cvSetImageROI(maskImage,new CvRect(0,0,cols-1,rows-1));	
				return val;
			}

	}
	
	public static int getBottom(CvMat mask)
	{
		//IplImage image = cvCreateImage(cvGetSize(mask),IPL_DEPTH_8U , 1);		
		IplImage image = new IplImage();
		cvGetImage(mask, image);
		int bottom = CvMatUtils.invalidMarker;
		CvRect rect = cvRect(0, 0, 1, 4);
		for(int x=0;x<mask.cols();x++)
		{
			rect.x(x);
			for(int y=mask.rows();y>0;y--)
			{			
				rect.y(y-4);
				cvSetImageROI(image,rect);
				if(cvCountNonZero(image)==4 && (y>bottom || bottom == CvMatUtils.invalidMarker))
				{
					bottom = y;
					break;
				}	
			}			
		}
		rect.x(mask.cols()-1);
		rect.y(mask.rows()-1);
		cvSetImageROI(image,rect);	

		return bottom;		
	}
	public static int getTop(CvMat mask)
	{
		//IplImage image = cvCreateImage(cvGetSize(mask),IPL_DEPTH_8U , 1);
		IplImage image = new IplImage();
		cvGetImage(mask, image);
		int top = CvMatUtils.invalidMarker;
		CvRect rect = cvRect(0, 0, 1, 4);
		for(int x=0;x<mask.cols();x++)
		{
			rect.x(x);
			for(int y=0;y<mask.rows();y++)
			{			
				rect.y(y);
				cvSetImageROI(image,rect);
				if(cvCountNonZero(image)==4 && (y<top || top == CvMatUtils.invalidMarker))
				{
					top = y;
					break;
				}	
			}			
		}
		rect.x(mask.cols()-1);
		rect.y(mask.rows()-1);
		cvSetImageROI(image,rect);
		return top;		
	}
	
	public static void main(String[] args)
	{

		String garment1Name = "garment_test_hist_simil_11";
		String path = "C:\\Members\\rsprat\\Desktop\\test\\"+garment1Name+".jpg";
		IplImage garment1Image = cvLoadImage(path);	
		//IplImage garment1Image = cvLoadImage(Paths.SMALL_IMAGES_FOLDER+"31568807.jpg");
		//ImagePrinter p = new ImagePrinter(garment1Image);
		//p.print("C:\\Members\\rsprat\\Desktop\\test\\"+garment1Name+"_1.jpg");
		CvMat garment1Mask  = BackgroundExtractor.colorMask(garment1Image);

		GarmentProportionsExtractor extr = new GarmentProportionsExtractor() {

			@Override
			public void printProportions(ImagePrinter printer, CvMat mask) {
			}
			
			@Override
			public Map<Integer, Integer> getProportions(CvMat mask) {
				return null;
			}
			
			@Override
			public Set<Category> getAplicableCategories() {
				return null;
			}
		};
		/*for(int y =1;y<garment1Mask.rows();y++)
		{
			IplImage garmentMaskImage = cvCreateImage(cvGetSize(garment1Mask),8,1);	
			cvGetImage(garment1Mask, garmentMaskImage);
			ImagePrinter p = new ImagePrinter(garmentMaskImage);
			p.print("C:\\Members\\rsprat\\Desktop\\test\\test2\\img_"+y+".jpg");
			int width = extr.getWidthAtAbsolutePoint(garment1Mask.clone(), y);
			LOGGER.info(""+(width));
			//p1.drawWidth(new int[]{y,0,width});
		}*/
		/*IplImage garmentMaskImage = new IplImage();	
		cvGetImage(garment1Mask, garmentMaskImage);
		
		//IplImage binaryImage = null;
		//IplImage h_plane = cvCreateImage(cvGetSize(hsv), 8, 1);
		IplImage binaryImage = cvCreateImage(cvGetSize(garmentMaskImage),8,1);		
		//cvCvtColor(garment1Image, binaryImage, CV_8U);
		cvThreshold(garment1Image, binaryImage, 245, 255, CV_THRESH_BINARY_INV);
		CvMat binary = CvMatUtils.getCvMatFromIplImage(binaryImage);

		*/
		IplImage garmentMaskImage = new IplImage();	
		cvGetImage(garment1Mask, garmentMaskImage);
		IplImage binaryImage = cvCreateImage(cvGetSize(garmentMaskImage),8,1);	
		ImagePrinter p1 = new ImagePrinter(garment1Image);
		int top = GarmentProportionsExtractor.getTop(garment1Mask);
		p1.drawHorzontalLine(top);
		int bottom = GarmentProportionsExtractor.getBottom(garment1Mask);
		p1.drawHorzontalLine(bottom);
		float h = (float)extr.getHeight(garment1Mask);
		for(int y = top+1;y<bottom;y++)
		{
			/*int width = extr.getWidthAtAbsolutePoint(garment1Mask, y);
			LOGGER.info(""+(width/h));
			p1.drawWidth(new int[]{y,0,width});*/
		}
		
		p1.print("C:\\Members\\rsprat\\Desktop\\test\\"+garment1Name+"_tb.jpg");		
		LOGGER.info("top:"+top+" bottom:"+bottom);
	}

	

}
