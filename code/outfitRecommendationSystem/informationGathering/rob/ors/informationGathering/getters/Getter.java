package rob.ors.informationGathering.getters;

import org.apache.log4j.Logger;

import rob.ors.informationGathering.filler.FillOutfitsFromFile;

public class Getter {
	
	static int MAX_AGE = 0; //The maximum age of an entry
	private static final Logger LOGGER = Logger.getLogger(Getter.class.getCanonicalName());
	public static Integer integer(String val)
	{
		try
		{
			return new Integer(val);
		}catch(NumberFormatException e){
			LOGGER.info(val+" is not an integer");
			return null;
		}
		
	}

}
