package rob.ors.core.model.api;

public interface AbstractGarment extends Garment {

	public abstract java.util.Set<ConcreteGarment> getGarments();
	
	public void addGarment(ConcreteGarment garment);
	
	public void removeGarment(ConcreteGarment garment);

	public abstract java.util.Set<AbstractOutfit> getOutfits();

	public abstract void addOutfit(AbstractOutfit outfit);
	
	public void removeOutfit(AbstractOutfit outfit);
	
	public void removeReferences();

}