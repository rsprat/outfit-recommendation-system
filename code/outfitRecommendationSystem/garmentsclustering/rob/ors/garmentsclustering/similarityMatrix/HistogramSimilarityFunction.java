package rob.ors.garmentsclustering.similarityMatrix;

import rob.ors.garmentssimilarity.GarmentComparator;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class HistogramSimilarityFunction extends SimilarityFunction{

	@Override
	public double getSimilarity(int iid1, int iid2)
	{
		/*loadImages(iid1, iid2);
		double val = garmentsComparator.patchHistogramSimilarity(anGarmentImage,anGarmentMask, anotherGarmentImage,anotherGarmentMask, 1);
		if(!Double.isNaN(val)) return  val;
		return 0;*/
		throw new RuntimeException("wrong method");
	}

	@Override
	public double getSimilarity(IplImage anGarmentImage, CvMat anGarmentMask,
			IplImage anotherGarmentImage, CvMat anotherGarmentMask,int iid1, int iid2) {
		double val = GarmentComparator.patchHistogramSimilarity(anGarmentImage,anGarmentMask, anotherGarmentImage,anotherGarmentMask, 1);
		if(!Double.isNaN(val) && val != 0) return  val;
		return 0.001;
	}

	@Override
	public void matrixSliceChange() {
		// TODO Auto-generated method stub
		
	}

}
