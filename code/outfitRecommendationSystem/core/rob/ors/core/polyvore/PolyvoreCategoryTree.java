package rob.ors.core.polyvore;

import java.util.HashSet;
import org.apache.log4j.Logger;

import rob.ors.core.model.api.*;
import rob.ors.core.model.impl.*;
import rob.ors.garmentssimilarity.imageProcessing.GarmentProportionsExtractor;

public class PolyvoreCategoryTree {
	private static Logger LOGGER = Logger.getLogger(PolyvoreCategoryTree.class.getCanonicalName());
	private static CompoundCategory root = null;
	
	public static CompoundCategory getParent(final Category childCategory)
	{
		class ParentFinder implements CategorizationVisitor
		{
			private CompoundCategory parent = null;
			
			@Override
			public void doVisit(Category category)
			{
				if(category instanceof CompoundCategory)
				{
					if(((CompoundCategory)category).getSubCategories().contains(childCategory)) parent = (CompoundCategory)category;
				}
			}
			public CompoundCategory getParent(){return parent;}
		}
		ParentFinder finder = new ParentFinder();
		getCategoryTreeRoot().visit(finder);
		return finder.getParent();
	}
	
	public static Category getCategoryById(final Integer id)
	{
		if(id==null) return null;
		class CategoryByIdFinder implements CategorizationVisitor
		{
			Category cat = null;
			@Override
			public void doVisit(Category category) 
			{
				if(category.getId().equals(id)) cat = category;				
			}
			
			public Category getCategory(){return cat;}
			
		}
		
		CategoryByIdFinder finder = new CategoryByIdFinder();
		getCategoryTreeRoot().visit(finder);
		return finder.getCategory();
		
	}
	

	public static CompoundCategory getCategoryTreeRoot()
	{
		if( root!=null ) return root;
		
		root = new CompoundCategoryI(0, "root");
				
		CompoundCategory clothing = new CompoundCategoryI(2, "clothing");
		root.addSubCategory(clothing);
		
		
		CompoundCategoryI dress = new CompoundCategoryI(3, "dress");
		clothing.addSubCategory(dress);
		dress.addSubCategory( new CategoryI(5, "cocktail"));
		dress.addSubCategory(new CategoryI(6, "gown"));
		dress.addSubCategory(new CategoryI(4, "day"));
		
		CompoundCategoryI skirt = new CompoundCategoryI(7, "skirt");
		skirt.addSubCategory( new CategoryI(8, "mini"));
		skirt.addSubCategory(new CategoryI(9, "knee length"));
		skirt.addSubCategory(new CategoryI(10, "long"));
		clothing.addSubCategory(skirt);
		
		
		
		CompoundCategoryI tops = new CompoundCategoryI(11, "tops");
		tops.addSubCategory(new CategoryI(17, "blouse"));
		tops.addSubCategory(new CategoryI(18, "cardigan"));
		tops.addSubCategory(new CategoryI(19, "sweater"));
		tops.addSubCategory(new CategoryI(20, "sweatshirt and hoodie"));
		tops.addSubCategory(new CategoryI(104, "tank"));
		tops.addSubCategory(new CategoryI(21, "t-shirt"));
		tops.addSubCategory(new CategoryI(15, "tunic"));
		clothing.addSubCategory(tops);
		
		CompoundCategoryI outwear = new CompoundCategoryI(23, "outwear");
		outwear.addSubCategory( new CategoryI(24, "coat"));
		CompoundCategoryI c = new CompoundCategoryI(25, "jacket");
		c.addSubCategory(new CategoryI(236,"blazers"));
		outwear.addSubCategory(c);
		outwear.addSubCategory(new CategoryI(11, "day"));
		outwear.addSubCategory(new CategoryI(26, "vest"));
		clothing.addSubCategory(outwear);
		
		CompoundCategoryI jeans = new CompoundCategoryI(27, "jeans");
		jeans.addSubCategory( new CategoryI(238, "bootcut"));
		jeans.addSubCategory(new CategoryI(240, "boyfriend"));
		jeans.addSubCategory(new CategoryI(237, "skinny"));
		jeans.addSubCategory(new CategoryI(4452, "flared"));
		jeans.addSubCategory(new CategoryI(310, "straight"));//???
		jeans.addSubCategory(new CategoryI(239, "wide"));
		clothing.addSubCategory(jeans);
		
		CompoundCategoryI pants = new CompoundCategoryI(28, "pants");
		pants.addSubCategory( new CategoryI(332, "cappry & Croped"));
		pants.addSubCategory(new CategoryI(241, "legins"));		
		clothing.addSubCategory(pants);
		
		clothing.addSubCategory(new CategoryI(29, "shorts"));
		
		CompoundCategoryI jsr = new CompoundCategoryI(242, "Jumpsuit & Romper");
		jsr.addSubCategory( new CategoryI(243, "Jumpsuit"));
		jsr.addSubCategory(new CategoryI(241, "legins"));//???
		jsr.addSubCategory(new CategoryI(244, "romper"));
		
		clothing.addSubCategory(jsr);
				
		return root;		
	}	
	
	public static void displayGarmentsPerCategory()
	{

		AllGarmentsCollector collector = new AllGarmentsCollector();
		getCategoryTreeRoot().visit(collector);
		int total = collector.getGarments().size();
		printChildData(0,getCategoryTreeRoot(),total);
		
		
	}
	
	private static void printChildData(int level,Category category,int total)
	{
		
		String indentaion = "";
		for(int i=0;i<level;i++) indentaion+="--";
		
		if(category instanceof CompoundCategory)
		{
			AllGarmentsCollector collector = new AllGarmentsCollector();
			category.visit(collector);
			int subtreeGarmentCount = collector.getGarments().size();
			LOGGER.info(indentaion+">"+category.getName()+" : "+category.getGarments().size()+" ("+(float)category.getGarments().size()*100/total+"%) "+
			"| "+subtreeGarmentCount+" ("+(float)subtreeGarmentCount*100/total+"%) ");
			int count = 0;

			for(Category childCat: ((CompoundCategory) category).getSubCategories()) printChildData(level+1,childCat,total);
		}
		else
		{
			LOGGER.info(indentaion+">"+category.getName()+" : "+category.getGarments().size()+"("+(float)category.getGarments().size()*100/total+"%)");
			
		}
		
	}
	
	public static void printGarmentsInCompoundCategory()
	{
		class garmentsInCompoundCategoryFinder implements CategorizationVisitor
		{
			

			@Override
			public void doVisit(Category category) {
				if(!(category instanceof CompoundCategory)) return;
				for(Garment garment: category.getGarments()) LOGGER.info(garment.getId().toString());
				
			}
			
			
		}
		
		CategorizationVisitor visitor = new garmentsInCompoundCategoryFinder();
		getCategoryTreeRoot().visit(visitor);
		
	}
	
	public static java.util.Set<Category> getSubtreeAsSet(Integer categoryId)
	{
		
		class CategoryCollector implements CategorizationVisitor
		{
			
			private java.util.Set<Category> categories = new HashSet<Category>();

			@Override
			public void doVisit(Category category)
			{
				categories.add(category);				
			}
			public java.util.Set<Category> getCategories(){return categories;}
		}

		CategoryCollector collector = new CategoryCollector();
		getCategoryById(categoryId).visit(collector);
		return collector.getCategories();
		
	}
	
	
	public static java.util.Set<Garment> getSubtreeGarments(Integer categoryId)
	{
		java.util.Set<Category> categories = getSubtreeAsSet(categoryId);
		java.util.Set<Garment> garments = new HashSet<Garment>();		
		for(Category cat: categories) garments.addAll(cat.getGarments());
		return garments;
		
	}
}