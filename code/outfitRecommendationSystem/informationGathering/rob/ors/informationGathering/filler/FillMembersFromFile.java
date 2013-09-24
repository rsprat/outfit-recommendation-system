package rob.ors.informationGathering.filler;


import rob.ors.core.config.Paths;
import rob.ors.informationGathering.CSVReader.MemberCSVReader;

public class FillMembersFromFile
{	
	public static void fillFromFile()
	{
		new MemberCSVReader().readFile(Paths.CAPTURED_DATA_FOLDER+"members_data.csv");
	}
	
	
	public static void main(String[] args)
	{	
		fillFromFile();
	}
}

