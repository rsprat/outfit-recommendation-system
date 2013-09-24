package rob.ors.garmentsclustering.similarityMatrix;

import org.apache.log4j.Logger;

import rob.ors.garmentssimilarity.GarmentComparator;
import rob.ors.garmentssimilarity.imageProcessing.HistogramExtractor;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class NoiseSimilarityFunction extends SimilarityFunction{
	
	@Override
	public double getSimilarity(int iid1, int iid2)
	{
		throw new RuntimeException("wrong method");
	}

	@Override
	public double getSimilarity(IplImage anGarmentImage, CvMat anGarmentMask, IplImage anotherGarmentImage, CvMat anotherGarmentMask,int iid1, int iid2) {
		double val = GarmentComparator.noiseSimilarity(anGarmentImage,anGarmentMask,anotherGarmentImage,anotherGarmentMask);
		if(!Double.isNaN(val) && val != 0) return  val;	
		return 0.01;
	}

	@Override
	public void matrixSliceChange() {
		// TODO Auto-generated method stub
		
	}

}
