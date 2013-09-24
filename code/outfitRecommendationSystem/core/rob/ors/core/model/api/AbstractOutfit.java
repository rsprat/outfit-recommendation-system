package rob.ors.core.model.api;

import java.util.Set;

public interface AbstractOutfit extends rob.ors.core.model.api.Outfit{

	public abstract Set<rob.ors.core.model.api.ConcreteOutfit> getOutfits();
	
	public abstract void addOutfit(rob.ors.core.model.api.ConcreteOutfit outfit);
	
	public void  removeOutfit(rob.ors.core.model.api.ConcreteOutfit outfit);
	
	public abstract void addGarment(AbstractGarment garment);
	
	public void removeGarment(AbstractGarment garment);
	
	public abstract java.util.List<AbstractGarment> getGarments(); //Must be a list, because repeated elements are allowed i.e a outfit with two jackets
	
	public void removeReferences();

}