package rob.ors.core.polyvore;

import java.util.HashSet;

import rob.ors.core.model.api.Category;
import rob.ors.core.model.api.Garment;
import rob.ors.core.model.api.CategorizationVisitor;

public class AllGarmentsCollector implements CategorizationVisitor{
	
	private java.util.Set<Garment> garments;
	
	public AllGarmentsCollector()
	{garments = new HashSet<Garment>();			
	}
	
	@Override
	public void doVisit(Category category) {
		garments.addAll(category.getGarments());			
	}
	public void clean(){
		garments = new HashSet<Garment>();			
	}
	public java.util.Set<Garment> getGarments()
	{
		return garments;
	}

}
