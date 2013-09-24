package rob.ors.informationGathering;
import java.util.HashSet;
import java.util.Set;
//import org.apache.log4j.Logger;


import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.bytecode.buildtime.spi.ExecutionException;
import org.hibernate.cfg.Configuration;


import rob.ors.core.config.Paths;
import rob.ors.core.model.api.Category;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.DBConnection;
import rob.ors.core.model.api.Garment;

import rob.ors.core.model.api.Member;
import rob.ors.core.model.impl.CategoryI;
import rob.ors.core.model.impl.CompoundCategoryI;
import rob.ors.core.model.impl.ConcreteGarmentI;
import rob.ors.core.model.impl.ConcreteOutfitI;
import rob.ors.core.model.impl.MemberI;
import rob.ors.core.polyvore.AllGarmentsCollector;
import rob.ors.core.polyvore.PolyvoreCategoryTree;


import rob.ors.informationGathering.CSVReader.CSVReader;
import rob.ors.informationGathering.CSVReader.GarmentCSVReader;
import rob.ors.informationGathering.filler.FillGarmentsFromFile;
import rob.ors.informationGathering.filler.FillOutfitsFromFile;
import rob.ors.informationGathering.filler.FillMembersFromFile;
import rob.ors.informationGathering.getters.GarmentGetter;
import rob.ors.informationGathering.getters.MemberGetter;
import rob.ors.informationGathering.getters.OutfitGetter;



public class Main {
	private static Logger LOGGER = Logger.getLogger(Main.class.getCanonicalName());
	public static void printGarmentsId(Integer categoryId)
	{
		
		Set<ConcreteGarment> garments = PolyvoreCategoryTree.getCategoryById(categoryId).getGarments();
		for(ConcreteGarment garment:garments)LOGGER.info(""+garment.getId());
		
	}
	private static Session connect()
	{
		LOGGER.info("Connecting to DB");
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
		
	}

		
	public static void getGarments()
	{
		Session session = connect();
    	FillGarmentsFromFile.fillFromFile();	
	}
	
	public static void getOutfits()
	{
		FillOutfitsFromFile f = new FillOutfitsFromFile();
	}
	
	public static void getMembers()
	{		
		 new FillMembersFromFile();
	}
	
	public static void main(String[] args)	
	{

		//getOutfits();		
		getGarments();
		//getMembers();
		
		
		//MemberGetter.getMember(2316559, false, 2);
		//GarmentGetter.getGarment(29540367, true, 2);
		//OutfitGetter.getOutfit(12345, true, 2);
//		
//		DBConnection.transaction();
//		MemberGetter.getMember(567, true,0);		
//		DBConnection.session().close();
		
	}
	
	

}
