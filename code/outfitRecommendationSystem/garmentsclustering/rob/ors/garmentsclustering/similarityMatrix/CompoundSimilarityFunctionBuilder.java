package rob.ors.garmentsclustering.similarityMatrix;

import java.util.LinkedList;

public class CompoundSimilarityFunctionBuilder extends SimilarityFunctionBuilder
{
	private LinkedList<SimilarityFunctionBuilder> builders;
	private LinkedList<Double> sfWeights;
	
	public CompoundSimilarityFunctionBuilder()
	{
		builders = new LinkedList<SimilarityFunctionBuilder>();
		sfWeights = new LinkedList<Double>();
	}
	
	public LinkedList<SimilarityFunctionBuilder> getSimilarityFunctionBuilders(){ return builders;}
	public void addSimilarityFunction(SimilarityFunctionBuilder builder, double sfw)
	{
		builders.add(builder);
		sfWeights.add(new Double(sfw));
	}
	
	public double[] getSimilarityFunctionWeights()
	{
		double[] weights = new double[sfWeights.size()];
		for(int i=0;i<sfWeights.size();i++)weights[i]=sfWeights.get(i);
		return weights;
	}
	
	public void setSimilarityFunctionWeight(double[] weights)
	{
		sfWeights.clear();
		for(int i=0;i<builders.size();i++) sfWeights.add(weights[i]);		
	}

	public SimilarityFunction build()
	{
		SimilarityFunction sf[] = new SimilarityFunction[builders.size()];
		double[] weights = new double[builders.size()];
		
		for(int i=0;i<builders.size();i++)
		{
			sf[i]=builders.get(i).build();
			weights[i]=sfWeights.get(i);
		}
		
		return new CompoundSimilarityFunction(sf, weights);
	}
	@Override
	public void setParameters(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
