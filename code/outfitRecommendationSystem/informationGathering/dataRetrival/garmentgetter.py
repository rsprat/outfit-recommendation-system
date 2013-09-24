import sys
import libxml2dom
import re
from commongetter import CommonGetter


class ItemGetter(CommonGetter):
	def __init__(self,proxy):		
		super(ItemGetter,self).__init__(proxy)

	def get_description(self,doc):
		description = " "
		for element in doc.getElementsByTagName("div"):
			if element.getAttribute("class")=="tease_container ":
				for el in element.getElementsByTagName("div"):
					if el.getAttribute("class")=="tease":
						m = re.search('(?<=\>).+(?=\<)', el.toString())
						return m.group(0)
		
	def get_statistics(self,doc):

		statistics ={}
		#statistics["saves"]=self.get_saves(doc.toString())		
		
		for element in doc.getElementsByTagName("div"):
			if element.getAttribute("class")=="meta":
				for el in element.getElementsByTagName("div"):				
					if len(el.getElementsByTagName("a"))==0:					
						info = el.toString().lower()				
						print "++++",info
						statistics["time_creation"] = self.get_time(info)	
						statistics["views"]=self.get_views(info)	
						statistics["saves"]=self.get_saves(info)

		return statistics

	def get_item_category(self,doc):
		category = "UNKNOWN"
		try:
			for element in doc.getElementsByTagName("div"):
				if element.getAttribute("class")=="breadcrumb unit":				
					for element in element.getElementsByTagName("a"):		
						link =element.getAttribute("href") # Gets the url
						m = re.search('(?<=category_id=)[0-9]{1,5}$', link) # Tryes to match the regular expression to find the id of the product
						if m is not None: #if matches
							category = int(m.group(0)) 

		except Exception as e:
			 print "\tUnable to get category id",e
		return category

		
	def get_data(self,item_id):
		try:
			data = {}
			doc = self.get_item(item_id)
			data["description"] = self.get_description(doc)
			data["category"] = self.get_item_category(doc)
			#data["statistics"]=self.get_statistics(doc)	
			#data["items"]=list(self.get_similar_ids(doc))
			data["sets"]=list(self.get_sets_used_in(doc))
			return data
	
		except Exception as e:
			print "Unable to get info from item",e
