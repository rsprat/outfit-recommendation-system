package rob.ors.garmentsclustering.similarityMatrix;

import java.util.HashMap;
import java.util.Iterator;

import javax.management.RuntimeErrorException;


import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


import rob.ors.core.model.api.DBConnection;
import rob.ors.core.model.impl.CategoryI;
import rob.ors.core.model.impl.ConcreteGarmentI;
import rob.ors.garmentssimilarity.textProcessing.AttributesDictionary;
import rob.ors.garmentssimilarity.textProcessing.DescriptionProcessor;
import weka.core.Instance;

public class DescriptionSimilarityFunction extends SimilarityFunction {
	
	private HashMap<Integer,ConcreteGarmentI> garments;
	
	public DescriptionSimilarityFunction()
	{
		garments = new HashMap<Integer, ConcreteGarmentI>();
		
	}
	
	private ConcreteGarmentI getGarmentWithAttributes(int iid)
	{
		Integer id = new Integer(iid);
		if(!garments.keySet().contains(id))
		{
			DBConnection.transaction();
			String desc = (String)DBConnection.session().createSQLQuery("SELECT description FROM garments where id="+iid).list().get(0);
			ConcreteGarmentI garment =  new ConcreteGarmentI(iid,new CategoryI(-1, "DUMMY"));
			garment.setDescription(desc);
			DescriptionProcessor.getAttributesFromDescription(garment);
			garments.put(id, garment);
			return garment;
		}
		
		return garments.get(id);
	
		
	}

	private static AttributesDictionary dict = DescriptionProcessor.buildDictionary();
	@Override
	public double getSimilarity(int iid1, int iid2) {
		{
				ConcreteGarmentI i1 =  getGarmentWithAttributes(iid1);
				ConcreteGarmentI i2 =  getGarmentWithAttributes(iid2);

				Iterator<Integer> it = dict.getAttributes().keySet().iterator();	
				int score = 0;
				while(it.hasNext())
				{
					int attributeId = it.next();
					if(i1.getAttributes().get(attributeId)==null || i2.getAttributes().get(attributeId)==null) continue;
					if(i1.getAttributes().get(attributeId)==i2.getAttributes().get(attributeId)) score++;				
				}
				return (((float)score)/dict.getAttributes().size())*100+1;
		}
		
	}

	@Override
	public double getSimilarity(IplImage anGarmentImage, CvMat anGarmentMask,
			IplImage anotherGarmentImage, CvMat anotherGarmentMask,int iid1, int iid2) {
		throw new RuntimeException("Wrong method");
	}


	@Override
	public void matrixSliceChange() {
		garments = new HashMap<Integer, ConcreteGarmentI>();
		
	}

}
