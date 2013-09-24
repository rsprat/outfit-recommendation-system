package rob.ors.informationGathering.getters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import rob.ors.core.config.Paths;
import rob.ors.core.model.api.Category;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.DBConnection;
import rob.ors.core.model.impl.ConcreteGarmentI;
import rob.ors.core.polyvore.PolyvoreCategoryTree;




public class GarmentGetter extends Getter{
	
	private static final Logger LOGGER = Logger.getLogger(GarmentGetter.class.getCanonicalName());
	/* (non-Javadoc)
	 * @see rob.ors.core.informationGathering.getters.impl.garmentGetter#queryWebForGarment(java.lang.Integer)
	 */
	public static String queryWebForGarment(Integer iid)
	{
		LOGGER.info("Quering web for garment ");
		try 
		{ 
			Process p=Runtime.getRuntime().exec("python  "+Paths.PYTHON_SCRIPTS_PATH+"getGarment.py "+iid); 
		
			BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
			p.waitFor(); 
			String garmentData = null;
			String newLine="";
			
			while((newLine=reader.readLine())!=null) 
			{ 
				//LOGGER.info(newLine);
				if(newLine.startsWith("ITEMDATA:")) garmentData = newLine.substring("ITEMDATA:".length());				
			} 
			reader.close();
			return garmentData;
			
			
		} 
		catch(IOException e1) {e1.printStackTrace();} 
		catch(InterruptedException e2) {e2.printStackTrace();} 
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see rob.ors.core.informationGathering.getters.impl.garmentGetter#getGarmentImage(java.lang.Integer)
	 */

	public static void getGarmentImage(Integer iid)
	{
		LOGGER.info("Quering web for image ");
		try 
		{ 
			Process p=Runtime.getRuntime().exec("python  "+Paths.PYTHON_SCRIPTS_PATH+"getImage.py "+iid); 
			p.waitFor(); 
		} 
		catch(IOException e1) {e1.printStackTrace();} 
		catch(InterruptedException e2) {e2.printStackTrace();} 
		return;
	}
	
	/*Get the garment from the db or from the web if its outdated*/
	/* (non-Javadoc)
	 * @see rob.ors.core.informationGathering.getters.impl.garmentGetter#getGarment(java.lang.Integer, boolean, int)
	 */

	public static ConcreteGarment getGarment(Integer iid, boolean update, int deepness)
	{
		//if garment_data is not None and garment_data["category"] is not None and garment_data["category"]!= 0:
		LOGGER.info("Getting garment "+iid);
		DBConnection.transaction();
		List<ConcreteGarment> dbGarments = DBConnection.session().createQuery("from ConcreteGarmentI WHERE id="+iid).list();
		ConcreteGarment garment = null;
		
		if(dbGarments.size()!=0)
		{
			garment = dbGarments.get(0);
			Hibernate.initialize(garment.getOutfits());
		}
		
		/*Check if the garment is in the no garments list*/
		boolean noGarment = false;
		if(DBConnection.session().createSQLQuery("SELECT * FROM no_garments WHERE garment_id="+iid).list().size()!=0)noGarment = true;
		

		/*Get the data from the web*/
		if(noGarment==false && (garment==null || (garment.isOlder(MAX_AGE) && update)))
		{
			String garmentData = queryWebForGarment(iid);	
			if(garmentData == null)
				{LOGGER.info("Data is null ");
				return null;}
			boolean getImage = garment == null; //Get the image only when garment is not in DB
			garment = readGarmentCSV(garmentData, garment, update, deepness);
			if(garment==null) // The garment is not a piece of clothe
			{
				DBConnection.session().createSQLQuery("INSERT INTO no_garments VALUES ("+iid+")").executeUpdate();//If null add the garment to the no garments list
				DBConnection.transaction().commit();
				DBConnection.transaction();
				return null;
			}
			garment.setDate(new Date());
			if(getImage)getGarmentImage(garment.getId());
		}
		if(noGarment == true) LOGGER.info("In no garments list"); //Is in the no garments list

		
		/*Get the garments form the outfit*/
		return garment;
	}
	
	/* (non-Javadoc)
	 * @see rob.ors.core.informationGathering.getters.impl.garmentGetter#readGarmentCSV(java.lang.String, rob.ors.core.model.api.ConcreteGarment, boolean, boolean, int)
	 */

	public static ConcreteGarment readGarmentCSV(String line, ConcreteGarment garment, boolean update, int deepness)
	{
		LOGGER.info(line);
		String[] garmentData = line.split(";");
		
		Category cat = PolyvoreCategoryTree.getCategoryById(integer(garmentData[1]));
		
		if(cat == null)
		{
			LOGGER.info("Category not mapped "+garmentData[1]);
			return null;
		}
		if(garment == null)
		{
			garment = new ConcreteGarmentI(integer(garmentData[0]),cat);
		}
		garment.setDescription(garmentData[5]);
		/**
		 * Get the outfits containing the garment and the similar garments
		 */
		if(deepness>0)
		{
			LOGGER.info("Getting additionalInfo");
			/**
			 * Force to persist the garment to solve duplicated instances
			 */
			DBConnection.session().saveOrUpdate(garment);
			DBConnection.transaction().commit();
			DBConnection.transaction();
			/**
			 * Get the outfits where the garment is used
			 */
			for(String setId : garmentData[6].split(","))
			{
				OutfitGetter.getOutfit(integer(setId), true,deepness-1);				
			}
		}else{DBConnection.session().saveOrUpdate(garment);}
		
		//garment.setSaves(integer(garmentData[2]));
		
		
		
		DBConnection.transaction().commit();
		DBConnection.transaction();
		return garment;
	}

}
