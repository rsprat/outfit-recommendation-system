package rob.ors.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;


import rob.ors.core.config.Paths;
import rob.ors.core.model.api.DBConnection;

public class GarmentRemover
{
	private static Logger LOGGER = Logger.getLogger(GarmentRemover.class.getCanonicalName());
	public static void removeGarmentFromFile(String path)
	{
		try {
		    BufferedReader in = new BufferedReader(new FileReader(path));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	try{
		        removeGarment(new Integer(str));
		    	}catch(Exception ex){LOGGER.error("Error deleting "+str,ex);}
		    }
		    in.close();
		} catch (IOException e) {
		}
		
		
	}
	/**
	 * Move the garment image from the smallImagesFolder to the toRemoveImagesFolder
	 * @param garmentId
	 */
	private static void moveGarmentImage(int garmentId)
	{
		try
		{			
			File afile =new File(Paths.BIG_IMAGES_FOLDER+garmentId+".jpg");		
			if(afile.exists())
			{
				if(afile.renameTo(new File(Paths.TO_REMOVE_IMAGES_FOLDER+garmentId+".jpg")))
				{
					LOGGER.info(String.format("Moving garment %i image file to deletion folder",garmentId));
				}
				else
				{
					LOGGER.equals(String.format("Failed to move  garment %i image file to deletion folder",garmentId));				
				}
			}
		}catch(Exception e){}
	}
	
	
	/**
	 * Remove the garment with the given id.
	 * REmove it from the DB, add it to the no garments file and add it to the noGarments table.
	 * Move its image to a quarentene folder
	 * @param garmentId
	 */
	public static void removeGarment(int garmentId)
	{
		DBConnection.transaction();
		LOGGER.info("Deleting garment "+garmentId);
		DBConnection.session().createSQLQuery("DELETE FROM outfits_components WHERE garment_id ="+garmentId).executeUpdate();		
		DBConnection.session().createSQLQuery("DELETE FROM garments WHERE id ="+garmentId).executeUpdate();			
		if(!isBlackListed(garmentId))DBConnection.session().createSQLQuery("INSERT INTO no_garments values("+garmentId+")").executeUpdate(); //Add the garment to the black list		
		moveGarmentImage(garmentId);
		//addGarmentToNoGarmentsFile(garmentId);
	}
	
	
	public static void addGarmentToNoGarmentsFile(int garmentId)
	{
	 
		LOGGER.error("Adding to no items file");
		//If already is in file, don't add it again
		try {
			BufferedReader in = new BufferedReader(new FileReader(Paths.garmentS_BLACK_LIST_FILE));
		
	    String str;
	    while ((str = in.readLine()) != null) {
	    	try
	    	{
	    		//Already in the file, do nothig
	    		if(Integer.parseInt(str) == garmentId)
	    		{
	    			 in.close();
	    			 return;	    			
	    		}
	      
	    	}catch(Exception ex){LOGGER.error("Error deleting "+str);}
	    }
	    in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Not in the file, write it
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(Paths.garmentS_BLACK_LIST_FILE));
			out.write(garmentId);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Check if the garment is in the black list
	public static boolean isBlackListed(int iid)
	{
		DBConnection.transaction();
		if(DBConnection.session().createSQLQuery("SELECT * FROM no_garments WHERE garment_id="+iid).list().size()!=0)return true;
		return false;
	}
	
	
	public static List<Integer> getBlackListedGarments()
	{
		DBConnection.transaction();
		List<Object> objects = DBConnection.session().createSQLQuery("SELECT * FROM no_garments").list();
		List<Integer> garments = new LinkedList<Integer>();
		for(int i = 0;i<objects.size();i++)
		{
			int id = (Integer)objects.get(i);
			if(!garments.contains(id))garments.add(id);	
		}
		return garments;
	}
	

	public static void main(String[] argv)
	{		
		removeGarment(13076831);
		//garmentRemover.removeGarmentFromFile(Paths.garmentS_BLACK_LIST_FILE);		
	}
	


}
