package rob.ors.garmentsclustering.similarityMatrix;

import java.util.HashMap;

import rob.ors.garmentssimilarity.GarmentComparator;
import rob.ors.garmentssimilarity.imageProcessing.GarmentProportionsExtractor;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class WidthSimilarityFunction extends SimilarityFunction {

	
	private HashMap<Integer,Float[]> garmentsProportions;
	
	public WidthSimilarityFunction()
	{
		garmentsProportions = new HashMap<Integer, Float[]>();
	}
	@Override
	public double getSimilarity(int iid1, int iid2) {
		{
			throw new RuntimeException("wrong method");		
		}
	}

	private float[] getGarmentProportions(CvMat mask,int iid)
	{
		Integer id = new Integer(iid);
		if(garmentsProportions.containsKey(id))
		{
			Float[] vals = garmentsProportions.get(id);
			float[] ret = new float[vals.length];
;			for(int i =0;i<vals.length;i++){ret[i]=(float)vals[i];};	
			return ret;
		}
		else
		{
			float[] vals =GarmentProportionsExtractor.getGarmentProportions(mask);
			Float[] ret = new Float[vals.length];
			for(int i =0;i<vals.length;i++){ret[i]=new Float(vals[i]);};	
			garmentsProportions.put(id,ret);			
			return vals;
		}
	}
	
	@Override
	public double getSimilarity(IplImage anGarmentImage, CvMat anGarmentMask,
			IplImage anotherGarmentImage, CvMat anotherGarmentMask,int iid1, int iid2) {
		try{
		
			float[] anGarmentProportions = getGarmentProportions(anGarmentMask,iid1);
			float[] anotherGarmentProportions = getGarmentProportions(anotherGarmentMask,iid2);
			double val = GarmentComparator.widthsSimilarity(anGarmentProportions, anotherGarmentProportions);
		//double val = garmentsComparator.widthsSimilarity(anGarmentMask, anotherGarmentMask);
		
		
		
		if(!Double.isNaN(val) && val != 0) return  val;
		}catch(Exception e){}
		return 0.01;
	}

	@Override
	public void matrixSliceChange() {
		garmentsProportions = new HashMap<Integer, Float[]>();
	}

}
