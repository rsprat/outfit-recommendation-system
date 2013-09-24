package rob.ors.core.model.api;

import java.util.Set;


public interface Category {

	public abstract Integer getId();

	public abstract void setId(Integer id);

	public abstract String getName();

	public abstract void setName(String name);
	
	public void addGarment(ConcreteGarment garment);

	public abstract Set<ConcreteGarment> getGarments();	

	public void removeGarment(ConcreteGarment garment);
	
	public abstract void setParentCategory(CompoundCategory category);
	
	public abstract CompoundCategory getParentCategory();
	

	public void addAbstractGarment(AbstractGarment garment);
	
	public void removeAbstractGarment(AbstractGarment garment);
	
	public abstract Set<AbstractGarment> getAbstractGarments();	
	
	
	public abstract void visit(CategorizationVisitor visitor);
	
	
}