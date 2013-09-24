import os
import sys
from sets import Set

"""
This file contains serveral functions to work with the CSV files used to store captured data

"""
def getEntitiesIds(fileName):
	f = open(fileName,"r")
	s = Set()
	for line in f:
		try:
			if line.find(';')!=-1:
				s.add(int(line.split(";")[0]))
			else:
				s.add(int(line.strip("\n")))
		except Exception:
			pass
	return s
	
	
def loadNoItems(no_items_file):
	noItems = Set()
	f=open(no_items_file,'r')
	for el in f:
		try:
			noItems.add(int(el.split('\n')[0]))
		except:
			pass
	return noItems

def purgeNoItems(sets,noItems):
	for setId in sets.keys():
		sets[setId]["items"]=list(Set(sets[setId]["items"]).difference(noItems))
		
def format_item_data_CSV(iid,data):
		cat ="UNKNOWN"
		try:
			cat = data["category"]
		except Exception:
			pass
		time_creation = "UNKNOWN"
		try:
			time_creation = data["statistics"]["time_creation"]
		except Exception:
			pass
			
		views = "UNKNOWN"
		try:
			views = data["statistics"]["views"]
		except Exception:
			pass

		saves ="UNKNOWN"
		try:
			saves = data["statistics"]["saves"]
		except Exception:
			pass

		description ="UNKNOWN"
		try:
			description = data["description"]
		except Exception:
			pass

		elems = [iid,cat,time_creation,views,saves,description]
		return reduce(lambda x,y: str(x)+";"+str(y), elems)+"\n"

def format_user_data_CSV(uid,data):
	sets="UNKNOWN"		
	try:
		sets = reduce(lambda x,y: str(x)+","+str(y), data["user_sets"])
	except Exception:
		pass

	liked ="UNKNOWN"
	try:
		liked = reduce(lambda x,y: str(x)+","+str(y), data["liked_sets"])
	except Exception:
		pass

	following ="UNKNOWN"
	try:
		d = [str(d[0])+":"+str(d[1]) for d in data["following"] ]
		following =reduce(lambda x,y: str(x)+","+str(y), d)
	except Exception:
		pass

	followers ="UNKNOWN"	
	try:
		d = [str(d[0])+":"+str(d[1]) for d in data["followers"] ]
		followers =reduce(lambda x,y: str(x)+","+str(y), d)	
	except Exception:
		pass
	elems = [uid,sets,liked,following,followers]
	return reduce(lambda x,y: str(x)+";"+str(y), elems)+"\n"
		
def format_set_data_CSV(sid,data):
	time_creation="UNKNOWN"		
	try:	
		time_creation = str(setgetter.get_date_days(data["statistics"]["time_creation"]))
	except Exception:
		try:
			time_creation = str(data["statistics"]["time_creation"])
		except Exception:
			pass

	views ="UNKNOWN"
	try:
		views =str(data["statistics"]["views"])
	except Exception:
		pass

	likes ="UNKNOWN"
	try:
		likes = str(data["statistics"]["likes"])
	except Exception:
		pass

	items ="UNKNOWN"
	try:
		items =reduce(lambda x,y: str(x)+","+str(y), data["items"])	
	except Exception:
		pass
		
	elems = [sid,time_creation,views,likes,items]
	return reduce(lambda x,y: str(x)+";"+str(y), elems)+"\n"

def cleanDescription(des):	
	remove = [';',',','.',':','/','\n']
	des =  (''.join(char for char in des if char not in  remove)).lower()
	if des == "none": return "UNKNOWN"
	return des

def loadItems(itemsFile):
	items ={}
	f = open(itemsFile,'r')
	for item in f:
		data = {}
		vals = item.split(";")
		try:
			id = int(vals[0])
		except Exception:
			id=0
		data["id"]=id
		try:
			data["category"] = int(vals[1])
		except Exception:
			data["category"]=0

		data["statistics"] = {}

		try:
			data["statistics"]["time_creation"] = int(vals[2])
		except Exception:
			data["statistics"]["time_creation"]=0

		try:
			data["statistics"]["views"] = int(vals[3])
		except Exception:
			data["statistics"]["views"]=0

		try:
			data["statistics"]["saves"] = int(vals[4])
		except Exception:
			data["statistics"]["saves"]=0
		try:			
			data["description"] = cleanDescription("".join([vals[i] for i in range(5,len(vals))]))
		except Exception:
			data["description"]=" "
		items[id]=data
	f.close()

	return items	

def loadSets(setsFile):
	sets ={}
	f = open(setsFile,'r')
	for set in f:
		data = {}
		vals = set.split(";")
		try:
			id = int(vals[0])
		except Exception:
			id=0
		data["id"]=id

		data["statistics"] = {}
		try:
			data["statistics"]["time_creation"] = int(vals[1])
		except Exception:
			data["statistics"]["time_creation"]=0

		try:
			data["statistics"]["views"] = int(vals[2])
		except Exception:
			data["statistics"]["views"]=0

		try:
			data["statistics"]["likes"] = int(vals[3])
		except Exception:
			data["statistics"]["likes"]=0
		try:
			data["items"] = [int(el) for el in vals[4].split(",")]
		except Exception:
				data["items"]=[]

		sets[id]=data

	f.close()

	return sets

def loadUsers(usersFile):
	users ={}
	f = open(usersFile,'r')
	for set in f:
		data = {}
		vals = set.split(";")
		try:
			id = int(vals[0])
		except Exception:
			id=0
		data["id"]=id

		
		try:
			data["sets"] = [int(el) for el in vals[1].split(",")]
		except Exception:
			data["sets"] = []

		try:
			data["liked"] = [int(el) for el in vals[2].split(",")]
		except Exception:
			data["liked"] = []

		try:
			data["following"] = [int(el.split(":")[1]) for el in vals[3].split(",")]
		except Exception:
			data["following"] = []
		print data["following"] 
		try:
			data["followers"] = [int(el) for el in vals[4].split(",")]
		except Exception:
			data["followers"] = []

		users[id]=data

	f.close()

	return users
	
def removeNoItems():
	sets = loadSets("outfits.csv")
	noItems = loadNoItems("nogarments.csv")
	purgeNoItems(sets,noItems)
	f = open("clean_outfits.csv",'w')
	for id in sets:
		f.write(format_set_data_CSV(id,sets[id]))
	f.close()
"""
items = loadItems("items.txt")
f = open("clean_items.txt",'w')
for id in items:
	if(items[id]["description"]!=" " and items[id]["description"]!="UNKNOWN" ):
		f.write(format_item_data_CSV(id,items[id]))
f.close()
"""
