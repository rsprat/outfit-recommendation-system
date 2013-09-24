package rob.ors.garmentssimilarity.textProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import org.apache.log4j.Logger;


import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rob.ors.core.config.Paths;
import rob.ors.core.model.api.ConcreteGarment;
import rob.ors.core.model.api.DBConnection;
import rob.ors.core.model.api.Garment;
import rob.ors.informationGathering.CSVReader.CSVReader;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

public class DescriptionProcessor {
	
	private static Logger LOGGER = Logger.getLogger(DescriptionProcessor.class.getCanonicalName());
	public static List<Element> elements(Node parent) {
	    List<Element> result = new LinkedList<Element>();
	    NodeList nl = parent.getChildNodes();
	    for (int i = 0; i < nl.getLength(); ++i) {
	        if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
	            result.add((Element) nl.item(i));
	    }
	    return result;
	}
	
	public static Map<String,Integer> merge(List<Map<String,Integer>> maps)
	{
		Map<String,Integer> expressions = new HashMap<String,Integer>();
		for(Map<String,Integer> garmentExpressions:maps)
		{			
			for(String expression:garmentExpressions.keySet())
			{
				
				if(expressions.containsKey(expression))
					expressions.put(expression,expressions.get(expression)+garmentExpressions.get(expression));
				else
					expressions.put(expression,garmentExpressions.get(expression));
			}
		}
		return expressions;
	}
	
	public static Map<String,Integer> countExpressions(List<ConcreteGarment> garments, int expressionLen)
	{

		LinkedList<Map<String,Integer>> expressions = new LinkedList<Map<String,Integer>> ();		
		for(ConcreteGarment garment:garments)		
			expressions.push(countExpressions(garment,expressionLen));		
		return merge(expressions);
	}
	
	private static String concatenate(String[] in)
	{
		String out ="";
		for(int i=0;i<in.length-1;i++)out+=in[i]+" ";
		out+=in[in.length-1];
		return out;		
	}
	
	public static Map<String,Integer> countExpressions(ConcreteGarment garment,int expressionLen)
	{
		Map<String,Integer> expressions = new HashMap<String,Integer>();
		//LOGGER.info(garment.getDescription());
		String[] desc = garment.getDescription().split(" ");
		
		for(int i=0;i<desc.length;i++)
		{
			String expr = concatenate(Arrays.copyOfRange(desc, i, Math.min(i+expressionLen, desc.length)));
			if(expressions.containsKey(expr))			
				expressions.put(expr, expressions.get(expr)+1);
			else
				expressions.put(expr, 1);			
		}		
		//printExpressions(expressions,1);
		return expressions;
	}
	
	public static Map<String,Integer> filter(Map<String,Integer> originalMap, int minGarments)
	{
		Map<String,Integer> resultMap = new HashMap<String,Integer>();
		for(String expression: originalMap.keySet())
			if(originalMap.get(expression)>=minGarments)
				resultMap.put(expression, originalMap.get(expression));
		return resultMap;
	}
	
	public static void printExpressions(Map<String,Integer> expressions,int numGarments)
	{
		LinkedList<Object[]> list = new LinkedList<Object[]>();
		for(String expression:expressions.keySet()) list.add(new Object[]{expression,expressions.get(expression)});
		
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {				
				return ((Integer)((Object[])o1)[1])-((Integer)((Object[])o2)[1]);
			}
		});
		float factor = 100.0f/numGarments;
		
		for(Object[] obj:list)
		{
			LOGGER.info(obj[0].toString());
			//LOGGER.info(obj[0]+"=>"+obj[1]+"=>"+(((Integer)obj[1]).floatValue()*factor));
		}
	}
	
	public static void findWords(String wordsFileName, final Map<String,Integer> terms)
	{
		//final Set<String> terms = new HashSet<String>();
		CSVReader reader = new CSVReader() {
			
			@Override
			protected void process(String[] line) {
				for(String term:line)
					if(terms.containsKey(term))
						LOGGER.info(term+"=>"+terms.get(term));
			}
		};
	}
	
	public static List<String> createSynonyms(String base, List<Element> autoSynonyms)
	{
		LinkedList<String> vals = new LinkedList<String>();
		vals.push(" "+base+" ");
		
		for(Element autoSynonym: autoSynonyms)
		{	
			String toAppend = autoSynonym.getAttribute("term");
			vals.push(" "+base+toAppend);
//			switch(autoSynonym.getNodeName())
//			{
//				case "append":
//					String toAppend = autoSynonym.getAttribute("term");
//					vals.push(" "+base+toAppend);
//					break;
//			}
			
		}
		return vals;
	}

	private static boolean match(String haystack, Collection<String> needels)
	{
		for(String needel:needels)
			if(haystack.contains(needel)) return true;
		return false;
				
	}
	
	private static boolean hasValue(ConcreteGarment garment, Element value, List<Element> autoSynonyms)
	{
		if(garment.getDescription() == null || garment.getDescription().equals("None")) return false;
		String desc = garment.getDescription().toLowerCase();
		desc = desc.replaceAll("-", " ");
		desc = desc.replaceAll("\\.", " ");
		desc = desc.replaceAll(",", " ");
		desc = desc.replaceAll(";", " ");
		desc = desc.replaceAll(":", " ");
		desc = " "+desc+" ";//add two whitespaces at the begining and end to allow matching the first and last word
		List<String> synonyms = createSynonyms(value.getAttribute("name"), autoSynonyms);
		for(Element synonym : elements(value))
		{
			
			String val = synonym.getAttribute("name");
			synonyms.addAll(createSynonyms(val, autoSynonyms));
		}	
		return match(desc,synonyms);
	}
	
	
	
	
	
	
	public static void getAttributesFromDescription(Garment garment)
	{
		try
		{	
			Document doc = getAttributesDocument();
			doc.getDocumentElement().normalize();
			
	
				NodeList attributes = doc.getElementsByTagName("attribute");
				for(int i=0;i<attributes.getLength();i++)
				{
					Element attribute = (Element)attributes.item(i);
					if(attribute.getAttribute("source")!="" && attribute.getAttribute("source")!="description") //Its not a description attribute
						continue;
					List<Element> autoSynonym =  elements(attribute.getElementsByTagName("automatic-synonyms").item(0));
					List<Element> values = elements(attribute.getElementsByTagName("values").item(0));
					for(int j=0;j<values.size();j++)
					{
					
						int valueId = Integer.parseInt(attribute.getAttribute("defaultId"));
						if(hasValue((ConcreteGarment)garment,values.get(j),autoSynonym))							
						{
							valueId = new Integer(values.get(j).getAttribute("id"));
							
							String attributeName = attribute.getAttribute("name");
							Integer attributeId = new Integer(attribute.getAttribute("id"));
							String attributeValue = values.get(j).getAttribute("name");
							
							String select = attribute.getAttribute("select");
							if(select=="")select="longest";
							//String currentValue = garment.getAttributes().get(attributeName);
							garment.getAttributes().put(attributeId, valueId);
							/*switch(select)
							{
							
							case "longest":								
								if(currentValue==null || attributeValue.length()>currentValue.length())
									garment.getAttributes().put(attributeName, attributeValue);
								break;	
							case "first":
								if(currentValue==null || garment.getDescription().indexOf(currentValue)>garment.getDescription().indexOf(attributeValue))
									garment.getAttributes().put(attributeName, attributeValue);
								break;
							case "multiple":
								if(currentValue==null)currentValue="";
								garment.getAttributes().put(attributeName, currentValue+attributeValue+", ");
								break;
							default:
								garment.getAttributes().put(attributeId, valueId);
							}*/
						}
					}	
				}
			}catch(Exception e){e.printStackTrace();}
			
	}
	

	
	public static void print(ConcreteGarment garment,AttributesDictionary dict)
	{
		LOGGER.info("garment: "+garment.getDescription());
		for(Integer key: garment.getAttributes().keySet())
		{
			String attrName = dict.attributeName(key);	
			String valueName = dict.valueName(key, garment.getAttributes().get(key));
			LOGGER.info(attrName+"->"+valueName);
		}
		LOGGER.info("====================");
	}
	
	public static Document getAttributesDocument()
	{
		Document doc = null;
		try{
			File attributesFile = new File(Paths.ATTRIBUTES_XML);
			//File attributesFile = new File(ConfigReader.getAttribute("attributesFile"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			dbFactory.setIgnoringElementContentWhitespace(true);
			dbFactory.setNamespaceAware(true);
			final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
			/*dbFactory.setValidating(true);  
			File attributesSchema = new File("E:/MyDocuments/get-dressed/code/informationGathering/src/descProcess/attributesSchema.xsd");
			//File attributesSchema = new File(ConfigReader.getAttribute("attributesSchema"));
			final Schema schema = sf.newSchema(attributesSchema);
			dbFactory.setSchema(schema);*/
			doc = dBuilder.parse(attributesFile);		
		}catch(Exception e){e.printStackTrace();}		
		return doc;
	}
	
	
	public static AttributesDictionary buildDictionary()
	{
		AttributesDictionary attributesDict = new AttributesDictionary();	
		Document doc = getAttributesDocument();
		NodeList attributes = doc.getElementsByTagName("attribute");
		
		for(int i=0;i<attributes.getLength();i++)
		{
			
			Element attribute = (Element)attributes.item(i);
			Integer attrId = new Integer(attribute.getAttribute("id"));
			String attrName = attribute.getAttribute("name");
			String type = attribute.getAttribute("type");

			AttributeD valuesDict = new AttributeD(attrName,attrId);
			valuesDict.setDefaultValueId(Integer.parseInt(attribute.getAttribute("defaultId")));
			List<Element> values = elements(attribute.getElementsByTagName("values").item(0));
			for(Element value: values)					
				valuesDict.addAttribute(new Integer(value.getAttribute("id")), value.getAttribute("name"));
			attributesDict.addAttribute(attrId, attrName, valuesDict);
		}
		return attributesDict;
	}
	
	/*public static Map<Integer,String> getAttributesIdName()
	{
		Map<Integer,String> map = new HashMap<Integer,String>();	
		Document doc = getAttributesDocument();
		NodeList attributes = doc.getElementsByTagName("attribute");
		for(int i=0;i<attributes.getLength();i++)
		{
			Element attribute = (Element)attributes.garment(i);		
			map.put(new Integer(attribute.getAttribute("id")),attribute.getAttribute("name"));
		}
		return map;
	}
	
	public static Map<Integer,String> getAttributeValuesIdName(int attributeId)
	{
		Map<Integer,String> map = new HashMap<Integer,String>();	
		Document doc = getAttributesDocument();
		NodeList attributes = doc.getElementsByTagName("attribute");
		Element attribute = (Element)attributes.garment(attributeId);
		map.put(new Integer(attribute.getAttribute("id")),attribute.getAttribute("name"));
		List<Element> values = elements(attribute.getElementsByTagName("values").garment(0));
		for(Element value: values)
		{			
			map.put(new Integer(value.getAttribute("id")), value.getAttribute("name"));
		}	
		return map;		
	}*/
	
				
	public static void main(String[] args)
	{
		/*AttributesDictionary dict = buildDictionary();	
		DBConnection.session().beginTransaction();
		List<ConcreteGarment> garments = (List<ConcreteGarment>) DBConnection.session().createQuery("from ConcreteGarmentI where (category_id = 2 OR category_id = 3 OR category_id = 4 OR category_id = 5 OR category_id = 6)").list();
		//List<ConcreteGarment> garments = (List<ConcreteGarment>) DBConnection.session().createQuery("from ConcreteGarmentI ").list();
		FastVector fvWekaAttributes = new FastVector( ModelInstanceConverter.ATTRIBUTES.keySet().size());
		for(Attribute attribute : ModelInstanceConverter.ATTRIBUTES.values())
		{
			fvWekaAttributes.addElement(attribute);
		}
		Instances instances = new Instances("garments",fvWekaAttributes,garments.size());
		try {
		File file = new File(Paths.OUTPUT_FOLDER+"descriptProcessorTest.txt");
		FileOutputStream fos;
		
			fos = new FileOutputStream(file);
		
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int garmentsCount = 0;
		
		for(garment garment: garments)
		{			
			String desc = ((ConcreteGarment)garment).getDescription();
			if(desc == null || desc.equals("None"))continue;
			garmentsCount++;
			getAttributesFromDescription(garment);
			print((ConcreteGarment) garment,dict);		
			//Instance inst = ModelInstanceConverter.toInstance(garment, instances, dict);
			//ModelInstanceConverter.printInstance(inst, dict);
			//LOGGER.info(desc);
			LOGGER.info("***************************************");
			LOGGER.info("***************************************");
			//PolyvoreCategoryTree.getCategoryById(garment.getId()).getGarments().add((ConcreteGarment)garment);
		}
		LOGGER.info("\n\n\nNumber of garments : "+garmentsCount+" from "+garments.size()+" \n\n\n");
		System.setOut(System.out);


		/*LinkedList<	Map<String,Integer>> expressions = new LinkedList<	Map<String,Integer>>();
		LinkedList<garment> evalGarments = new LinkedList<garment>(PolyvoreCategoryTree.getSubtreeGarments(3));
		for(int i=1;i<=4;i++)
			expressions.push(countExpressions(evalGarments,i));
		
		
		Map<String,Integer> expr = merge(expressions);
		expressions.clear();

		printExpressions(filter(expr,3),evalGarments.size());	
		LOGGER.info("--------->"+evalGarments.size());
		//PolyvoreCategoryTree.displayGarmentsPerCategory();
		 * 
		 */
	}
	

}
