package rob.ors.informationGathering.filler;


import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import rob.ors.core.config.Paths;
import rob.ors.informationGathering.CSVReader.OutfitCSVReader;

public class FillOutfitsFromFile
{
	private static final Logger LOGGER = Logger.getLogger(FillOutfitsFromFile.class.getCanonicalName());

	public static void fillFromFile()
	{
		OutfitCSVReader reader = new OutfitCSVReader();
		reader.readFile(Paths.CAPTURED_DATA_FOLDER+"outfits.csv");
		reader.session.getTransaction().commit();
		
		LOGGER.info(reader.noGarments+" outfits without garments");
	}
	
	public static void main(String[] args)
	{	
		fillFromFile();
	}

	
	
}