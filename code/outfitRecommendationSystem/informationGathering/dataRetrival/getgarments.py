import os
import sys
from garmentgetter import ItemGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+os.sep+"proxy")
from proxyconnection import *
from sets import Set
from taxonomy import Taxonomy
from dataFileUtils import format_item_data_CSV
from dataFileUtils import getEntitiesIds
no_items_file=BASE_FOLDER+"captured"+os.sep+"nogarments.csv"
no_items_file_output =BASE_FOLDER+"captured"+os.sep+"nogarments"+sys.argv[1]+".csv"
items_file = BASE_FOLDER+"captured"+os.sep+"garments.csv"
items_file_output = BASE_FOLDER+"captured"+os.sep+"garments"+sys.argv[1]+".csv"
sets_file = BASE_FOLDER+"captured"+os.sep+"outfits.csv"
images_dir = BASE_FOLDER+"captured"+os.sep+"images"+os.sep
proxy = MyProxy(BASE_FOLDER+"proxy"+os.sep+"working.csv")
itemgetter = ItemGetter(proxy)
import urllib
import random
from time import sleep
"""
	Capture and store the infromation from the items used in the already captured sets
"""

def store_data(iid,data):
		""" Store an item represented with a map in a file """		
		f = open(items_file_output,'a')
		f.write(format_item_data_CSV(iid,data))
		f.close()


def load_sets_items():
	""" Returns a list with all the id's of items from the captured sets. Loads the sets from the sets_file file.
	"""
	items = Set()
	f = open(sets_file,'r')
	for set in f:
		for item_id in set.split(";")[4].split(","):
			try:
				items.add(int(item_id))
			except:
				pass
	f.close()
	return items	

def get_image(itemid):
		url = "http://embed.polyvoreimg.com/cgi/img-thing/size/l/tid/"+str(itemid)+".jpg"
		image=urllib.URLopener()
		image.retrieve(url,images_dir+os.sep+str(itemid)+".jpg")





"""
For each pending item (not already visited) searches its description. If the item it's a clothe, (not a 
decorative image) stores the gatheret infromation in a file and its picture in a folder.
Else, store the ID of the item in the no_items_file
"""

visited_items = getEntitiesIds(items_file)
visited_items = visited_items.union(getEntitiesIds(no_items_file))
valid_categories = Taxonomy().getAllIds()
sets_items = load_sets_items()
print len(sets_items)
new_items = sets_items.difference(visited_items)
print len(new_items)

new_items = list(new_items)
random.shuffle(new_items)
print len(new_items)

while new_items:
	#sleep(randint(1,5))
	print "visited: ", len(visited_items)
	print "new: ", len(new_items)
	item_id = new_items.pop(0)
	
	print "Current: ",item_id 
	visited_items.add(item_id)
	item_data = itemgetter.get_data(item_id)
	print item_data
	if item_data is not None and item_data["category"] is not None and (item_data["category"] in valid_categories) and item_data["category"]!= "UNKNOWN" and item_data["description"]!= None:
		
		store_data(item_id,item_data)
		get_image(item_id)
	else:
		if item_id in new_items:
			new_items.remove(item_id)
		f = open(no_items_file_output,'a')
		f.write(str(item_id)+"\n")
		f.close()
		print "it's not an item"

