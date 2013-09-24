package rob.ors.garmentssimilarity.imageProcessing;
import org.apache.log4j.Logger;


import rob.ors.garmentsclustering.similarityMatrix.NoiseSimilarityFunction;

import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
// SRC: http://www.activovision.com/octavi/doku.php?id=javacv_opencv_7-1_hs_histogram
// SRC https://groups.google.com/forum/?fromgroups=#!topic/javacv/7v_rf9vfuEI
public class HistogramExtractor {
	private static Logger LOGGER = Logger.getLogger(HistogramExtractor.class.getCanonicalName());
	private static CvHistogram getHSHistogram(IplImage src, CvMat mask )
	{
		return getHSHistogram(src,mask,30,32);
	}
	
	private static CvHistogram getHSHistogram(IplImage src, CvMat mask, int hbuckets, int sbuckets )
	{
		IplImage hsv = cvCreateImage(cvGetSize(src), 8, 3);
		cvCvtColor(src, hsv, CV_BGR2HSV);
		IplImage h_plane = cvCreateImage(cvGetSize(hsv), 8, 1);
		IplImage s_plane = cvCreateImage(cvGetSize(hsv), 8, 1);
		IplImage v_plane = cvCreateImage(cvGetSize(hsv), 8, 1);
		cvSplit(hsv, h_plane, s_plane, v_plane, null);
		cvReleaseImage(hsv); 
		
		int hist_size[] = {hbuckets,sbuckets};
		float h_ranges[] = {0, 180};
		float s_ranges[] = {0, 255};
		float[][] ranges = {h_ranges,s_ranges};	 	      
		
		CvHistogram hist = cvCreateHist(2, hist_size, CV_HIST_ARRAY, ranges,1);		
		cvCalcHist(new IplImage[]{h_plane,s_plane}, hist, 0, mask);	   
		cvNormalizeHist( hist, 1.0f );
		cvReleaseImage(h_plane);
		cvReleaseImage(s_plane);
		cvReleaseImage(v_plane);
		
		return hist;		
	}
	
	public static CvHistogram[][] getHSPatchHistogram(IplImage src, CvMat garmentMask, float relativePatchSize)
	{
		if(relativePatchSize<=0) relativePatchSize = 0.1f;
		if(relativePatchSize>1)relativePatchSize = 1f;
		
		int numCols = (int) (1/relativePatchSize);
		int numRows = numCols;

		CvHistogram[][] histograms = new CvHistogram[numRows][numCols];
		for(int r=0; r<numRows; r++)
		{
			for(int c=0; c<numCols; c++)
			{
				histograms[r][c]  = getHSHistogram(src,garmentMask);	
			}
		}
		return histograms;
	}
	
	public static void main(String[] args)
	{
		
		/**
		 * Compute the mask for two images, the edge image and compute the distance
		 */
		/*String name = "garment_5";
		IplImage garment = cvLoadImage("C:\\Members\\rsprat\\Desktop\\"+name+".jpg");*/
		
		
		String garmentName = "garment_test_hist_simil_7";
		IplImage garment = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garmentName+".jpg");	
		CvMat garmentMask  = BackgroundExtractor.colorMask(garment);
		
		
		String garmentName1 = "garment_test_hist_simil_12";
		IplImage garment1 = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garmentName1+".jpg");	
		CvMat garmentMask1  = BackgroundExtractor.colorMask(garment1);
		
		NoiseSimilarityFunction s = new NoiseSimilarityFunction();
		
		
		//cvOutfitImageCOI(noiseImage,1);
		//cvOutfitImageCOI(garmentMaskImage,1);		
		//*LOGGER.info("Noise white pixels:"+	cvCountNonZero(noiseImage));
		//LOGGER.info("Mask wite piexels:"+	cvCountNonZero(garmentMaskImage));*/
		//LOGGER.info(s.getSimilarity(garment, garmentMask, garment1, garmentMask1));

		
		
		/**
		 * Test the mask when computing a histogram. both garments are the same with the background color changed, the
		 * mask has been prior caluclated
		 */
		/*String garment1Name = "garment_5_3";
		IplImage garment1Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\"+garment1Name+".jpg");	
		IplImage garment2Mask = cvLoadImage("C:\\Members\\rsprat\\Desktop\\garment_5_mask.png");	
		//Convert from bgr to gray, create a mat to contain the data and make te conversion
		CvMat  mask = CvMat.create(garment2Mask.height(), garment2Mask.width(),CV_8UC1, 1);	
		cvCvtColor(garment2Mask, mask, CV_BGR2GRAY);		
		CvHistogram garment1Hist = getHSHistogram(garment1Image,mask);
		String garment2Name = "garment_5_2";
		IplImage garment2Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\"+garment2Name+".jpg");
		CvHistogram garment2Hist = getHSHistogram(garment2Image,mask);
		LOGGER.info(cvCompareHist(garment1Hist, garment2Hist, 2));*/
	
		
		
		/**
		 * HSV vs HS histogram comparision
		 */
		/*String garment1Name = "garment_test_hist_simil_1";
		IplImage garment1Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\"+garment1Name+".jpg");	
		CvMat garment1Mask  = BackgroundExtractor.colorMask(CvMatUtils.getCvMatFromIplImage(garment1Image),new CvScalar(255, 255, 255, 255), 10);
		CvHistogram garment1Hisths = getHSHistogram(garment1Image,garment1Mask);		
		CvHistogram garment1Histhsv = getHSVHistogram(garment1Image,garment1Mask,30,32,50);	
		
		String garment2Name = "garment_test_hist_simil_3";
		IplImage garment2Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\"+garment2Name+".jpg");
		CvMat garment2Mask  = BackgroundExtractor.colorMask(CvMatUtils.getCvMatFromIplImage(garment2Image),new CvScalar(255, 255, 255, 255), 10);
		CvHistogram garment2Hisths = getHSHistogram(garment2Image,garment2Mask);
		CvHistogram garment2Histhsv = getHSVHistogram(garment2Image,garment1Mask,30,32,50);	
		LOGGER.info(garment1Name+"-"+garment2Name+": "+cvCompareHist(garment2Hisths, garment1Hisths, 2)*100);
		LOGGER.info(garment1Name+"-"+garment2Name+": "+cvCompareHist(garment2Histhsv, garment1Histhsv, 3)*100);*/
		
		
		/*IplImage garment1Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment1Name+".jpg");	
		CvMat garment1Mask  = BackgroundExtractor.colorMask(CvMatUtils.getCvMatFromIplImage(garment1Image),new CvScalar(255, 255, 255, 255), 10);
		CvHistogram garment1Hist = getHSHistogram(garment1Image,garment1Mask);		
		
		String garment2Name = "garment_test_hist_simil_4";
		IplImage garment2Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment2Name+".jpg");
		CvMat garment2Mask  = BackgroundExtractor.colorMask(CvMatUtils.getCvMatFromIplImage(garment2Image),new CvScalar(255, 255, 255, 255), 10);
		CvHistogram garment2Hist = getHSHistogram(garment2Image,garment2Mask);
		
		String garment3Name = "garment_test_hist_simil_11";
		IplImage garment3Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment3Name+".jpg");
		CvMat garment3Mask  = BackgroundExtractor.colorMask(CvMatUtils.getCvMatFromIplImage(garment3Image),new CvScalar(255, 255, 255, 255), 10);
		CvHistogram garment3Hist = getHSHistogram(garment3Image,garment3Mask);

		
		String garment4Name = "garment_test_hist_simil_9";
		IplImage garment4Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment4Name+".jpg");
		CvMat garment4Mask  = BackgroundExtractor.colorMask(CvMatUtils.getCvMatFromIplImage(garment4Image),new CvScalar(255, 255, 255, 255), 10);
		CvHistogram garment4Hist = getHSHistogram(garment4Image,garment4Mask);*/
		
		
		
		String garment1Name = "garment_test_hist_simil_3";
		IplImage garment1Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment1Name+".jpg");	
		CvMat garment1Mask  = BackgroundExtractor.colorMask(garment1Image);
		CvHistogram garment1Hist = getHSHistogram(garment1Image,garment1Mask);		
		
		String garment2Name = "garment_test_hist_simil_4";
		IplImage garment2Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment2Name+".jpg");
		CvMat garment2Mask  = BackgroundExtractor.colorMask(garment2Image);
		CvHistogram garment2Hist = getHSHistogram(garment2Image,garment2Mask);
		
		String garment3Name = "garment_test_hist_simil_11";
		IplImage garment3Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment3Name+".jpg");
		CvMat garment3Mask  = BackgroundExtractor.colorMask(garment3Image);
		CvHistogram garment3Hist = getHSHistogram(garment3Image,garment3Mask);

		
		String garment4Name = "garment_test_hist_simil_9";
		IplImage garment4Image = cvLoadImage("C:\\Members\\rsprat\\Desktop\\test\\"+garment4Name+".jpg");
		CvMat garment4Mask  = BackgroundExtractor.colorMask(garment4Image);
		CvHistogram garment4Hist = getHSHistogram(garment4Image,garment4Mask);
		
		
		
		
		
		
		
		
		
		LOGGER.info("CV_COMP_CORREL\n");
		LOGGER.info(garment1Name+"-"+garment2Name+": "+cvCompareHist(garment1Hist, garment2Hist, CV_COMP_CORREL)*100);
		LOGGER.info(garment1Name+"-"+garment3Name+": "+cvCompareHist(garment1Hist, garment3Hist, CV_COMP_CORREL)*100);
		LOGGER.info(garment1Name+"-"+garment4Name+": "+cvCompareHist(garment1Hist, garment4Hist, CV_COMP_CORREL)*100);
		LOGGER.info(garment2Name+"-"+garment3Name+": "+cvCompareHist(garment2Hist, garment3Hist, CV_COMP_CORREL)*100);
		LOGGER.info(garment2Name+"-"+garment4Name+": "+cvCompareHist(garment2Hist, garment4Hist, CV_COMP_CORREL)*100);
		LOGGER.info(garment3Name+"-"+garment4Name+": "+cvCompareHist(garment3Hist, garment4Hist, CV_COMP_CORREL)*100);
		LOGGER.info("CV_COMP_CHISQR\n");
		LOGGER.info(garment1Name+"-"+garment2Name+": "+cvCompareHist(garment1Hist, garment2Hist, CV_COMP_CHISQR )*100);
		LOGGER.info(garment1Name+"-"+garment3Name+": "+cvCompareHist(garment1Hist, garment3Hist, CV_COMP_CHISQR )*100);
		LOGGER.info(garment1Name+"-"+garment4Name+": "+cvCompareHist(garment1Hist, garment4Hist, CV_COMP_CHISQR )*100);
		LOGGER.info(garment2Name+"-"+garment3Name+": "+cvCompareHist(garment2Hist, garment3Hist, CV_COMP_CHISQR )*100);
		LOGGER.info(garment2Name+"-"+garment4Name+": "+cvCompareHist(garment2Hist, garment4Hist, CV_COMP_CHISQR )*100);
		LOGGER.info(garment3Name+"-"+garment4Name+": "+cvCompareHist(garment3Hist, garment4Hist, CV_COMP_CHISQR )*100);
		LOGGER.info("CV_COMP_INTERSECT\n");
		LOGGER.info(garment1Name+"-"+garment2Name+": "+cvCompareHist(garment1Hist, garment2Hist, CV_COMP_INTERSECT  )*100);
		LOGGER.info(garment1Name+"-"+garment3Name+": "+cvCompareHist(garment1Hist, garment3Hist, CV_COMP_INTERSECT  )*100);
		LOGGER.info(garment1Name+"-"+garment4Name+": "+cvCompareHist(garment1Hist, garment4Hist, CV_COMP_INTERSECT  )*100);
		LOGGER.info(garment2Name+"-"+garment3Name+": "+cvCompareHist(garment2Hist, garment3Hist, CV_COMP_INTERSECT  )*100);
		LOGGER.info(garment2Name+"-"+garment4Name+": "+cvCompareHist(garment2Hist, garment4Hist, CV_COMP_INTERSECT  )*100);
		LOGGER.info(garment3Name+"-"+garment4Name+": "+cvCompareHist(garment3Hist, garment4Hist, CV_COMP_INTERSECT  )*100);
		LOGGER.info("CV_COMP_BHATTACHARYYA\n");
		LOGGER.info(garment1Name+"-"+garment2Name+": "+cvCompareHist(garment1Hist, garment2Hist, CV_COMP_BHATTACHARYYA   )*100);
		LOGGER.info(garment1Name+"-"+garment3Name+": "+cvCompareHist(garment1Hist, garment3Hist, CV_COMP_BHATTACHARYYA   )*100);
		LOGGER.info(garment1Name+"-"+garment4Name+": "+cvCompareHist(garment1Hist, garment4Hist, CV_COMP_BHATTACHARYYA   )*100);
		LOGGER.info(garment2Name+"-"+garment3Name+": "+cvCompareHist(garment2Hist, garment3Hist, CV_COMP_BHATTACHARYYA   )*100);
		LOGGER.info(garment2Name+"-"+garment4Name+": "+cvCompareHist(garment2Hist, garment4Hist, CV_COMP_BHATTACHARYYA   )*100);
		LOGGER.info(garment3Name+"-"+garment4Name+": "+cvCompareHist(garment3Hist, garment4Hist, CV_COMP_BHATTACHARYYA   )*100);
		//getHHistogram(anotherGarmentImageAux,null);

		//CvHistogram [][] firstGarmentHistMatrix = getHSPatchHistogram(anGarmentImageAux,1f);
		//CvHistogram [][] secondHistMatrix = getHSPatchHistogram(anotherGarmentImageAux,1f);	
		//LOGGER.info(cvCompareHist(firstGarmentHistMatrix[0][0], secondHistMatrix[0][0], 1));
		//LOGGER.info(cvCompareHist(firstGarmentHistMatrix[0][0], secondHistMatrix[0][0], 2));
		//LOGGER.info(cvCompareHist(firstGarmentHistMatrix[0][0], secondHistMatrix[0][0], 3));
		
//	    	CvHistogram [][] secondGarmentHistMatrix = getHSPatchHistogram(32986271,0.3f);
//	    	double accumulated = 0;
//	    	for(int i = 0; i<firstGarmentHistMatrix.length; i++)
//	    	{
//	    		for(int j=0;j<firstGarmentHistMatrix[i].length;j++)
//	    		{
//	    			double val = cvCompareHist(firstGarmentHistMatrix[i][j], secondGarmentHistMatrix[i][j], CV_COMP_CORREL);
//	    			accumulated+=val;
//	    			LOGGER.info("["+i+"]["+j+"]= "+val);
//	    		}
//	    	}	
//	    	LOGGER.info("mean:"+accumulated/(firstGarmentHistMatrix.length*firstGarmentHistMatrix[0].length));
//
    }


}
