package rob.ors.garmentssimilarity.textProcessing;
import java.util.HashMap;
import java.util.Map;


public class AttributesDictionary {

	private Map<String,Integer> nameIdMap;
	private Map<Integer,AttributeD> attributes;

	
	public AttributesDictionary()
	{
		nameIdMap = new HashMap<String,Integer>();
		attributes = new HashMap<Integer,AttributeD>();
	}
	
	public void addAttribute(int id,String name,AttributeD attr)
	{

		nameIdMap.put(name, id);
		attributes.put(id, attr);
	}
	
	public String attributeName(int id)
	{
		return attributes.get(id).getName();
	}
	
	public int attributeId(String attributeName)
	{
		return nameIdMap.get(attributeName);
	}
	
	
	public String valueName(int attributeId, int valueId)
	{
		return ((AttributeD)attributes.get(attributeId)).valueName(valueId);
	}
	

	public int valueId(String attributeName, String valueName)
	{
		return ((AttributeD)attributes.get(nameIdMap.get(attributeName))).valueId(valueName);
	}
	
	
	
	public AttributeD getCategoricalAttribute(String attributString)
	{
		return (AttributeD)attributes.get((nameIdMap.get(attributString)));
	}
	
	public AttributeD getCategoricalAttribute(int attrId)
	{
		return (AttributeD)attributes.get(attrId);
	}


	

	public Map<Integer, AttributeD> getAttributes() {
		return attributes;
	}

	public Map<String, Integer> getNameIdMap() {
		return nameIdMap;
	}

	
	public String toString()
	{
		String ret="";
		for(Integer id:attributes.keySet())
		{
			ret+=id+":"+attributes.get(id).getName()+System.getProperty("line.separator");
			//String values = valuesDictionarys.get(id).toString().replace(System.getProperty("line.separator"), System.getProperty("line.separator")+"\t");
			ret+="\t"+attributes.get(id).toString();
			ret+="******************************************"+System.getProperty("line.separator");
		}
		return ret;
	}
	
}
