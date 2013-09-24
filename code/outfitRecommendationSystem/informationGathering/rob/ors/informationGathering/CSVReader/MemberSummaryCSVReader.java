package rob.ors.informationGathering.CSVReader;

import java.util.HashMap;

import rob.ors.core.model.api.Member;
import rob.ors.core.model.impl.MemberI;

/***
 * Class for reading member's summaries CSV files
 * @author rsprat
 *
 */
public class MemberSummaryCSVReader extends CSVReader
{
	public HashMap<Integer,Member> members = new HashMap<Integer,Member>();
	
	

	/***
	 * Read a summary of an member and stores it in the members map.
	 */
	@Override
	protected void process(String[] line)
	{
		Member member = new MemberI(parseInteger(line[0]),"");	
		member.setNumOutfitViews( parseInteger(line[1]));
		member.setNumOutfitLikes(parseInteger(line[2]));
		member.setNumTrophies(parseInteger(line[3]));		
		member.setNumFollowers(parseInteger(line[4]));
		members.put(member.getId(), member);
	}
	

}
