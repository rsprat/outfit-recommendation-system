package rob.ors.core.model.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import rob.ors.core.model.api.AbstractGarment;
import rob.ors.core.model.api.AbstractOutfit;
import rob.ors.core.model.api.Category;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.ElementAge;
import rob.ors.core.model.api.Garment;
import rob.ors.core.model.api.ConcreteOutfit;


// Generated Jul 13, 2012 11:58:30 AM by Hibernate Tools 3.4.0.CR1

/**
 * garment generated by hbm2java
 */
public class ConcreteGarmentI extends GarmentI implements java.io.Serializable, ConcreteGarment, ElementAge {


	protected AbstractGarment abstractGarment;
	protected java.util.Set<ConcreteOutfit> outfits = new HashSet<ConcreteOutfit>(0);
	protected Date date;
	protected String description;
	protected ConcreteGarmentI(){}

	public ConcreteGarmentI(Integer id, Category category)
	{		
		super(category);	
		this.id = id;
		category.getGarments().add(this);
		date = new Date();
	}

	
	private void setCategory(Category category)
	{		
		this.category = category;
	}

	@Override
	public AbstractGarment getAbstractGarment()
	{
		return this.abstractGarment;
	}

	/**
	 * Only for hibernate use with reflection
	 * @param abstractOutfit
	 */	
	protected void setAbstractGarment(AbstractGarment newAbstractGarment)
	{
		if(this.abstractGarment!=null) this.abstractGarment.getGarments().remove(this);
		if(newAbstractGarment == null)
		{
			this.abstractGarment = null;
			return;
		}
		this.abstractGarment = newAbstractGarment;
	}
	
	@Override
	public void setAbstractgarment(AbstractGarment newAbstractGarment)
	{
		
		
		if(this.abstractGarment!=null) this.abstractGarment.getGarments().remove(this);
		
		this.abstractGarment = newAbstractGarment;
		if(newAbstractGarment == null) return;

		newAbstractGarment.getGarments().add(this);
		
	
	
	}

	
	@Override
	public java.util.Set<ConcreteOutfit> getOutfits()
	{
		return outfits;
	}

	

	private void setOutfits(java.util.Set<ConcreteOutfit> outfits)
	{
		this.outfits = outfits;
	}

	@Override
	public void addOutfit(ConcreteOutfit outfit)
	{
		
		if(outfit == null ) throw new IllegalArgumentException("Set is null");
		this.outfits.add(outfit);
		outfit.getGarments().add(this);
	}
	
	@Override
	public void removeOutfit(ConcreteOutfit outfit)
	{
		if(outfit == null ) throw new IllegalArgumentException("Set is null");
		this.outfits.remove(outfit);
		outfit.getGarments().remove(this);
	}

	public Date getDate()
	{
		return date;
	}


	public void setDate(Date newDate)
	{
		this.date = newDate;		
	}


	public boolean isOlder(int days)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTime((Date) date.clone());
		cal.add(Calendar.DAY_OF_YEAR, days);
		if(cal.compareTo(new GregorianCalendar())<0) return true;
		return false;
	}

	public String getDescription(){return description;};
	
	public void setDescription(String desc){this.description=desc;};
	
}