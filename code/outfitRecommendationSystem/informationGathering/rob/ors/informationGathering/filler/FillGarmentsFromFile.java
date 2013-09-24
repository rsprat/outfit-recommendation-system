package rob.ors.informationGathering.filler;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import rob.ors.core.config.Paths;
import rob.ors.core.model.api.DBConnection;
import rob.ors.core.model.api.Garment;
import rob.ors.core.model.impl.ConcreteGarmentI;
import rob.ors.informationGathering.CSVReader.CSVReader;
import rob.ors.informationGathering.CSVReader.GarmentCSVReader;
import rob.ors.informationGathering.getters.GarmentGetter;

public class FillGarmentsFromFile {
	private static final Logger LOGGER = Logger.getLogger(FillGarmentsFromFile.class.getCanonicalName());
	public static void fillFromFile()
	{

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
		
		SimpleReader simpleReader = new SimpleReader();
		Set<Integer> noGarmentsReferences = simpleReader.parseFile(Paths.garmentS_BLACK_LIST_FILE);
		GarmentCSVReader garmentReader = new GarmentCSVReader();
		garmentReader.excludeReferences(noGarmentsReferences);
		Set<Garment> garments = garmentReader.parseFile(Paths.CAPTURED_DATA_FOLDER+"garments.csv");
		DBConnection.transaction();
		int numGarments = 0;
		LOGGER.info("number of garments: "+garments.size());
		int nonUnique = 0;
		for(Garment garment:garments)
		{
			if(DBConnection.session().get(ConcreteGarmentI.class,garment.getId())!=null)//The garment is already in the db
			{
				//LOGGER.info("already in the DB");
				nonUnique++;
				continue;				
			}
					
			DBConnection.session().save(garment);
		
			if(numGarments==300)//Commit the changed every 300 garments
			{
				DBConnection.session().flush();
				DBConnection.session().clear();					
				numGarments = 0;				
				LOGGER.info("Commit");
			}
			numGarments++;				
			
		}	
		DBConnection.session().getTransaction().commit();
		LOGGER.info("number of non unique: "+nonUnique+" totalCreated:"+(garments.size()-nonUnique));
	}
	public static void main(String[] args)
	{	
		fillFromFile();
	}

}
