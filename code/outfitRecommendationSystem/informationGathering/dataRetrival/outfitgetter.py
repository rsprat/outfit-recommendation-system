import sys
import libxml2dom
import re
from commongetter import CommonGetter

class SetGetter(CommonGetter):
	def __init__(self,proxy):		
		super(SetGetter,self).__init__(proxy)

	def get_statistics(self,doc):
		statistics ={}
		for element in doc.getElementsByTagName("div"):
			if element.getAttribute("class")=="meta":
				for el in element.getElementsByTagName("div"):								
					if len(el.getElementsByTagName("a"))==0:
						info = el.toString().lower()
						statistics["time_creation"] = self.get_date_days(self.get_time( info))
						statistics["views"]=self.get_views(info)	
						statistics["likes"]=self.get_likes(info)
		return statistics

	def get_member_items_ids(self,doc):
		return self.get_items_ids(doc.getElementById("items"))
	
	def get_author(self,doc):
		for element in doc.getElementsByTagName("div"):
				if element.getAttribute("class")=="meta":
					for el in element.getElementsByTagName("div"):	
						link = el.getElementsByTagName("a")
						if link:
							link = link[0].toString()	
							match =re.search('\">[0-9A-Za-z]+',link).group(0)
							return re.search('[0-9A-Za-z]+',match).group(0)

	def get_data(self,set_id):
		doc = self.get_set(set_id)
		if doc is not None:
			data = {}
			data["author"]=self.get_author(doc)
			data["statistics"]=self.get_statistics(doc) 
			data["items"]=list(self.get_member_items_ids(doc))
			return data