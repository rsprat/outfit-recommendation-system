package rob.ors.core.model.impl;


// Generated Jul 13, 2012 11:58:30 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import rob.ors.core.model.api.AbstractGarment;
import rob.ors.core.model.api.Category;
import rob.ors.core.model.api.CompoundCategory;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.Garment;
import rob.ors.core.model.api.CategorizationVisitor;

/**
 * Category generated by hbm2java
 */
public class CompoundCategoryI extends CategoryI implements java.io.Serializable, CompoundCategory {

	private Set<Category> subCategories = new HashSet<Category>(0);
	protected CompoundCategoryI()
	{
		super();
	}

	public CompoundCategoryI(Integer id, String name)
	{
		super(id,name);	
	}

	@Override
	public void addSubCategory(Category category) {
		subCategories.add(category);
		category.setParentCategory(this);		
	}

	@Override
	public void visit(CategorizationVisitor visitor) {
		visitor.doVisit(this);
		for(Category cat: subCategories)
		{
			cat.visit(visitor);
		}		
	}

	@Override
	public Set<Category> getSubCategories() {
		return subCategories;
	}

}
