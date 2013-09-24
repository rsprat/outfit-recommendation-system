package rob.ors.informationGathering.CSVReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import rob.ors.core.config.Paths;
import rob.ors.core.model.api.Category;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.ConcreteOutfit;
import rob.ors.core.model.api.Garment;

import rob.ors.core.model.impl.ConcreteGarmentI;
import rob.ors.core.model.impl.ConcreteOutfitI;

import rob.ors.core.polyvore.PolyvoreCategoryTree;
import rob.ors.core.utils.ImageReduction;
import rob.ors.core.utils.GarmentRemover;
import rob.ors.informationGathering.filler.FillOutfitsFromFile;


/**
 * Class for reading Outfits CSVs
 * @author rsprat
 *
 */
public class OutfitCSVReader extends CSVReader
{
	private static final Logger LOGGER = Logger.getLogger(OutfitCSVReader.class.getCanonicalName());
	private int numGarments = 0;
	public int noGarments = 0;
	public Session session;
	Set<Integer> noGarmentsReferences;
	public OutfitCSVReader()
	{
		
		LOGGER.info("Connecting to DB");
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
		
		/**
		 * Inner class for reading CSV files with only an integer value per line
		 * @author rsprat
		 */
		class SimpleReader extends CSVReader
		{
			private Set<Integer> references = new HashSet<Integer>(0);

			public  Set<Integer> parseFile(String file)
			{
				readFile(file);
				return references;
				
			}
			@Override
			protected void process(String[] line) {
				references.add(parseInteger(line[0]));
			}				
		}
		//Read the ids form the noGarments file
		SimpleReader simpleReader = new SimpleReader();
		noGarmentsReferences = simpleReader.parseFile(Paths.CAPTURED_DATA_FOLDER+"no_garments.csv");

		
		noGarmentsReferences.addAll(GarmentRemover.getBlackListedGarments());
		//Read all garments in the garments file, excluding all the garments in the noGarments file and noGarment table
		GarmentCSVReader garmentReader = new GarmentCSVReader();
		garmentReader.excludeReferences(noGarmentsReferences);		
		garmentReader.parseFile(Paths.CAPTURED_DATA_FOLDER+"garments.csv");			
		noGarmentsReferences.addAll(garmentReader.unCategorized);//Add all the uncategorized garments to the no garments list
		session.beginTransaction();
	}

	@Override
	protected void process(String[] line)
	{
		
		Integer sid = parseInteger(line[0]);
		//LOGGER.info(""+sid);
		ConcreteOutfit outfit = getOutfit(sid);
		outfit.setAge(parseInteger(line[1]));
		outfit.setViews( parseInteger(line[2]));
		outfit.setLikes(parseInteger(line[3]));
		int noGarments = 0;
		try
		{
			for(String garment:line[4].split(","))//For each garment that composes the outfit
			{
				Integer iid = parseInteger(garment);
				if( session.createQuery("from ConcreteGarmentI where id="+iid).list().size()!=0)//If the garment is in the system
				{
					int n = outfit.getGarments().size();
					outfit.addGarment(getGarment(iid));
					if(outfit.getGarments().size()==n)throw new RuntimeException("same garments as before");
	
				}
				else
				{noGarments++;}
				
				if(outfit.getGarments().size()==0)
				{					
					noGarments++;
					return;
					
				}
			}
		
			session.saveOrUpdate(outfit);
			numGarments+=1;
			if(numGarments==300)//Commit the changed every 300 garments
			{
				
				session.getTransaction().commit();
				session.clear();
				session.beginTransaction();
				numGarments = 0;
				LOGGER.info("Commit");
			}

			LOGGER.info("==============Set filled with "+outfit.getGarments().size()+" out of  "+line[4].split(",").length+" garments wherer "+noGarments+" where no garments");
			if(outfit.getGarments().size()==0)noGarments+=1;//If the outfit doesn't have garments, increment the counter
		}catch(RuntimeException e){
			e.printStackTrace();
			//LOGGER.info("garment not found");
			}
		
		
	}
	
	/*Get an instance of outfit either creating it or retrieving it from the DB*/
	private ConcreteOutfit getOutfit(Integer sid)
	{
		List<ConcreteOutfitI> outfits = session.createQuery("from ConcreteOutfitI where id="+sid).list();
		if(outfits.size()!=0)return outfits.get(0);
		ConcreteOutfitI s = new ConcreteOutfitI(sid,null);
		return s;
	}
	
	
	private ConcreteGarment getGarment(Integer iid)
	{
		List<ConcreteGarment> garments = session.createQuery("from ConcreteGarmentI where id="+iid).list();
		if(garments.size()!=0)return garments.get(0);
		throw new RuntimeException("garment not found "+iid);
		
	}
	
}