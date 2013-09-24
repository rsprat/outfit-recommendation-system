package rob.ors.garmentsclustering.similarityMatrix;

public abstract class SimilarityFunctionBuilder
{
	public abstract void setParameters(Object[] args);
	public abstract SimilarityFunction build();
}
