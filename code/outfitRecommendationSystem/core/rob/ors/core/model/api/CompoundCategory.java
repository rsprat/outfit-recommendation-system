package rob.ors.core.model.api;

public interface CompoundCategory extends Category {
	
	public abstract void addSubCategory(Category category);
		
	public abstract java.util.Set<Category> getSubCategories();



}
