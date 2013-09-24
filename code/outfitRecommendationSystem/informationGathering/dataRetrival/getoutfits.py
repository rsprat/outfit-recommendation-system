import os
import sys
from outfitgetter import SetGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from sets import Set
from dataFileUtils import format_set_data_CSV
from dataFileUtils import getEntitiesIds
users_file = BASE_FOLDER+"captured"+os.sep+"members_data.csv"
set_file = BASE_FOLDER+"captured"+os.sep+"outfits.csv"
proxy = MyProxy(BASE_FOLDER+"proxy"+os.sep+"working.csv")
setgetter = SetGetter(proxy)

def load_users_sets():
	sets = Set()
	f = open(users_file,'r')
	for user in f:
		for setId in user.split(";")[1].split(","):
			try:
				sets.add(int(setId))
			except:
				pass
	f.close()
	return sets	


visited_sets = getEntitiesIds(set_file)
#user_sets = load_users_sets()
remaining_sets = getEntitiesIds( BASE_FOLDER+"captured"+os.sep+"outfits_from_garments_file.csv")
new_sets = remaining_sets.difference(visited_sets)

while new_sets:
	print "visited: ", len(visited_sets)
	print "new: ", len(new_sets)
	new_sets = list(new_sets)
	set_id = new_sets.pop(0)
	new_sets = Set(new_sets)

	print "Current: ",set_id 
	visited_sets.add(set_id)
	set_data = setgetter.get_data(set_id)
	print set_data
	f = open(set_file,'a')
	f.write(format_set_data_CSV(set_id,set_data))
	f.close()

