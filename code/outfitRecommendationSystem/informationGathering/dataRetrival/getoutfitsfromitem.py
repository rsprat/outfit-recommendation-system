import os
import sys
from garmentgetter import ItemGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from sets import Set
from dataFileUtils import format_set_data_CSV
from dataFileUtils import getEntitiesIds
users_file = BASE_FOLDER+"captured"+os.sep+"members_data.csv"
sets_from_items_file = BASE_FOLDER+"captured"+os.sep+"outfits_from_garments_file.csv"
items_file = BASE_FOLDER+"captured"+os.sep+"garments.csv"
visited_items_file = BASE_FOLDER+"captured"+os.sep+"visited_garments.csv"

proxy = MyProxy(BASE_FOLDER+"proxy"+os.sep+"working.csv")
itemgetter = ItemGetter(proxy)


items_ids = getEntitiesIds(items_file)
visited_items_ids = getEntitiesIds(visited_items_file)
items_ids.difference(visited_items_ids)


while items_ids:
	print "visited: ", len(visited_items_ids)
	print "new: ", len(items_ids)
	items_ids = list(items_ids)
	item_id = items_ids.pop(0)
	items_ids = Set(items_ids)

	print "Current: ",item_id 
	visited_items_ids.add(item_id)
	item_data = itemgetter.get_data(item_id)
	f = open(sets_from_items_file,'a')
	print item_data
	for set_id in item_data["sets"]:
		f.write(str(set_id)+"\n")
	f.close()
	f = open(visited_items_file,'a')
	f.write(str(item_id))
	f.close()

