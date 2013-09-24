package rob.ors.core.model.api;

public interface ConcreteGarment extends Garment, ElementAge {

	
	public abstract AbstractGarment getAbstractGarment();

	public abstract void setAbstractgarment(AbstractGarment abstractGarment);
	
	public abstract java.util.Set<ConcreteOutfit> getOutfits();

	public void removeOutfit(ConcreteOutfit outfit);

	public abstract void addOutfit(ConcreteOutfit outfit);
	
	public void setDescription(String desc);
	public String getDescription();
}