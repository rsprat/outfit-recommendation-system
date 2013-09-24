#!/usr/bin/python
import sys
import urllib
import libxml2dom
import re
import os
import string
import random
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
import proxyconnection


class CommonGetter(object):
	""" Base class for any other class aimed to collect information from the site. Offers basic
		functionalities that are comonly used by all the other classes
	"""
	proxy = None

	def __init__(self,proxy):		
		self.proxy = proxy

	def get_image(self,url):
		""" Get an image hosted in the given url. If successful, returns the image, if not, returns None."""
		try:
			img = self.proxy.make_request(url)
			return img
		except IOError:
			print "unable to connect to server "


	def get_element(self,url):
		""" Get an HTML element hosted in the given url. If successful, returns the DOM of the element, if not, returns 			None.
		"""
	
		try:

			#doc_string = self.proxy.make_request_without_proxy(url)
			doc_string = self.proxy.make_request(url)
			if doc_string != None:
				return libxml2dom.parseString(doc_string, html=1)
		except IOError:
			print "unable to connect to server "
	

	def get_set(self,set_id):
		""" Get the set page of the set with the given ID. If successful, returns the DOM of the element, if not, 				returns None.
		"""
		url="http://www.polyvore.com/chuck_bass-tard_you_are_man/set?id="+str(set_id)
		return self.get_element(url)

	def get_item(self,item_id):		
		""" Get the item page of the item with the given ID. If successful, returns the DOM of the element, if not, 			returns None.
		"""
		randomName = ''.join(random.choice(string.ascii_uppercase) for x in range(20))
		#url="http://www.polyvore.com/macy_home_decor_waterford_evolution/thing?id="+str(item_id)+"&tab=sets"	
		url="http://www.polyvore.com/"+randomName+"/thing?id="+str(item_id)+"&tab=sets"	
		return self.get_element(url)


	def get_set_fan_page(self,set_id,page=1):
		""" Get the set fans page of the set with the given ID. If successful, returns the DOM of the element, if not, 				returns None.
		"""
		url="http://www.polyvore.com/cgi/set-fans?id="+str(item_id)+"&page="+str(page)	
		return self.get_element(url)

	def get_item_fan_page(self,item_id,page=1):
		""" Get the item fans page of the item with the given ID. If successful, returns the DOM of the element, if 			not, returns None.
		"""

		url="http://www.polyvore.com/cgi/thing-fans?id="+str(item_id)+"&page="+str(page)	
		return self.get_element(url)




	def save(self,data,file_name):
		try:
			f = open(file_name,'w')
			f.write(data.toString())
			f.close()
		except IOError:
			print "unable to write file"

	def load(self,file_name):
		try:
			f = open(file_name,'r')
			doc_string = reduce(lambda x,y:x+y,f)
			f.close()

			return  libxml2dom.parseString(doc_string, html=1)
		except IOError:
			print "unable to read file ",file_name

	def save_item(self,item_id,file_name):
			try:
				f = open(file_name,'w')
				f.write(self.get_item(item_id).toString())
				f.close()
			except IOError:
				print "unable to write file"

	numerals = {
		"one":1,
		"two":2,
		"three":3,
		"four":4,
		"five":5,
		"six":6,
		"seven":7,
		"eight":8,
		"nine":9,
		"ten":10,
		"eleven":11,
		"twelve":12,
		"thirteen":13,
		"fourteen":14,	
	}


	def numeral_to_int(self,numeral):
		""" Returns the integer represented by the given numeral (from 1 to 14). Otherwhise returns the same numeral"""
		if str(numeral).lower() in self.numerals:
			return self.numerals[str(numeral).lower()]
		return numeral
		
	
	def get_time(self,info):	
		""" Given some text containing an elapsed time, returns an integer representing de quantity and a string 				with the units (day,month or year).
			The elapsed time may be expressed in one of this ways:
				* integer units (i.e. 10 days)
				* numeral units (i.e. ten days)
			If no elapsed time is found, returns -1 as time and "None" as units.
		"""
		time = -1
		actual_temporalitzation = "None"

		for temporalitzation in ["day","month","year"]:
			matching = re.search('[0-9]{1,12} '+temporalitzation, info)
			if matching is not None:
				time = int(re.search('[0-9]+', matching.group(0)).group(0))		
				actual_temporalitzation = temporalitzation

			else:
				matching = re.search('[a-z]+ '+temporalitzation, info)
				if matching is not None:
					numeral = re.search('[a-z]+', matching.group(0)).group(0)
					time = self.numeral_to_int(numeral)
					actual_temporalitzation = temporalitzation

		return time, actual_temporalitzation

	factor = {
	"day":1,
	"month":30,
	"year":365
	}

	def get_date_days(self,date):
		""" Given an array representing a date, with the quantity as an integer in the first position
			and the units ("day","month","year") in the second one, returns an integer representing the
			date in days.
		"""
		return int(date[0])*self.factor[date[1]]
	

	def get_views(self,info):
		""" Given a text with the views counter on it, returns it's value.
			The view counter may be expressed in one of this ways:
				* integer views (i.e. 10 views)
				* numeral views (i.e. ten views)
			If no views counter is found, returns 0.
		"""
		views = 0
		match = re.search('[0-9]{1,12},*[0-9]{0,12} view',info)
		if match is not None:
			views= re.search('[0-9]{1,12},*[0-9]{0,12}',match.group(0))
			views= int(views.group(0).replace(",",""))
		else:
				matching = re.search('[a-z]+ view', info)
				if matching is not None:
					numeral = re.search('[a-z]+', matching.group(0)).group(0)
					views =self. numeral_to_int(numeral)				

		return views

	def has_next(self,page):
		""" Given an HTML DOM element, searches for a link to the next page. If it's found returns true, false 				otherwise
		"""

		for link in page.getElementsByTagName("a"):	
			if link.getAttribute("class")=="next":
				return True
		return False


	def next_fan_page(self,fan_page):
		""" Given a DOM element of a fan page, returns the number of the next page. If not found returns None """
		right = fan_page.getElementById("right")
		for link in right.getElementsByTagName("a"):	
			if link.getAttribute("class")=="next":				
				num = re.search('page=[0-9]+',link.toString())		
				num = re.search('[0-9]+',num.group(0))
			
				ID = re.search('id=[0-9]+',link.toString())		
				ID = re.search('[0-9]+',ID.group(0))	
				return [ID.group(0),num.group(0)]
		return None


	def get_fans(self,fan_page):
		""" Given a DOM element of a fan page, returns a list with all the fans names found on the page """	
		fans = []
		right = fan_page.getElementById("right")
		for div in right.getElementsByTagName("div"):	
			if div.getAttribute("class")=="fan":		
				for div in div.getElementsByTagName("div"):
					if div.getAttribute("class")=="right meta":		
						try:			
							link = div.getElementsByTagName("a")[0].toString()
							match =re.search('\">[0-9A-Za-z]+',link).group(0)
							fans.append(re.search('[0-9A-Za-z]+',match).group(0))
						except Exception:
							pass

		next = self.next_fan_page(fan_page)
		if next:
			fans.extend(self.get_fans(self.get_item_fan_page(next[0],next[1])))
			return fans

		return fans


	def get_likes(self,info):
		""" Given a text with a likes counter on it, returns it's value.
			The likes counter may be expressed in one of this ways:
				* integer likes (i.e. 10 likes)
				* numeral likes (i.e. ten likes)
			If no likes counter is found, returns 0.
		"""
		likes = 0
		match = re.search('[0-9]{1,12},*[0-9]{0,12} like',info)
		if match is not None:
			likes= re.search('[0-9]{1,12},*[0-9]{0,12}',match.group(0))
			likes= int(likes.group(0).replace(",",""))
		else:
				matching = re.search('[a-z]+ like', info)
				if matching is not None:
					numeral = re.search('[a-z]+', matching.group(0)).group(0)
					likes = self.numeral_to_int(numeral)		
		return likes


	def get_saves(self,info):
		""" Given a text with a saves counter on it, returns it's value.
			The saves counter may be expressed in one of this ways:
				* integer saves (i.e. 10 saves)
				* numeral saves (i.e. ten saves)
			If no saves counter is found, returns 0.
		"""
		saves = 0
		info = info.lower()
		match = re.search('[0-9,]{1,12} sav',info)

		if match is not None:
			saves= re.search('[0-9,]{1,12}',match.group(0))
			saves= int(saves.group(0).replace(",",""))

		else:
				matching = re.search('[a-z]+ sav', info)
				if matching is not None:
					numeral = re.search('[a-z]+', matching.group(0)).group(0)
					saves = self.numeral_to_int(numeral)	
		if saves == 'i':
			return 0
		return saves



	def get_items_ids(self,doc):
		""" Given the DOM element of a set, returns a set with all the items id's found """
		try:
			set_items_ids = set([])
			# Gets the id's of the clothes present in the set
			for element in doc.getElementsByTagName("a"): # For each link in the div "items"
				link =element.getAttribute("href") # Gets the url
				m = re.search('&id=[0-9]{4,20}$', link) # Tryes to match the regular expression to find the id of the product
				if m is not None: #if matches
					set_items_ids.add(int(re.search('[0-9]{4,20}$', m.group(0)).group(0))) # Adds the id to the id's set
			return set_items_ids
		except Exception:
			print "Unable to get set items id's"
		return {}



	def get_sets_used_in(self,doc):
		""" Given the DOM element of a set, returns a set with all the set id's found """
		set_ids = set([])
		# Gets the id's of the sets
		for element in doc.getElementsByTagName("a"): # For each link in the div "items"
			link =element.getAttribute("href") # Gets the url
			if link is not None:
				m = re.search('set\?id=[0-9]{4,20}$', link) # Tryes to match the regular expression to find the id of the set
				if m is not None: #if matches
					set_ids.add(int(re.search('[0-9]{4,20}$', m.group(0)).group(0))) # Adds the id to the id's set
	
		return set_ids 

		#return {}

