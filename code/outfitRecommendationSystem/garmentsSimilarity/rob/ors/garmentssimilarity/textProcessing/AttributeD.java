package rob.ors.garmentssimilarity.textProcessing;
import java.util.HashMap;
import java.util.Map;

public class AttributeD{
	
	
	private int id;
	private String name;
	private Map<Integer,String> idNameMap;
	private Map<String,Integer> nameIdMap;
	private int defaultValueID;
	
	public AttributeD(String name,int id) {
		this.id = id;
		this.name = name;
		idNameMap= new HashMap<Integer, String>();
		nameIdMap = new HashMap<String, Integer>();
		addAttribute(0,"UNKNOWN");
		defaultValueID = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void addAttribute(int id,String name)
	{
		idNameMap.put(id, name);
		nameIdMap.put(name, id);
	}
	public int getDefaultValueId()
	{
		return defaultValueID;
	}
	public void setDefaultValueId(int id)
	{
		defaultValueID = id;
	}
	public String getDefaultValueName()
	{
		return idNameMap.get(defaultValueID);
	}
	public String valueName(int attributeId)
	{
		return idNameMap.get(attributeId);
	}
	
	public int valueId(String valueName)
	{
		return nameIdMap.get(valueName);
	}
	
	public String toString()
	{
		String ret="";
		for(Integer id:idNameMap.keySet())
			ret+=id+":"+idNameMap.get(id)+System.getProperty("line.separator");
		return ret;
	}

	public Map<Integer, String> getIdNameMap() {
		return idNameMap;
	}

	public Map<String, Integer> getNameIdMap() {
		return nameIdMap;
	}
	
	
	
}
