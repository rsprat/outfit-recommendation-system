import os
import sys
from sets import Set
from dataFileUtils import *
if len(sys.argv)<4:
	print sys.argv[0]+" items_file sets_file no_items_file"
	sys.exit()
	
	
BASE_FOLDER = "\\".join(os.path.realpath(__file__).split("\\")[0:-1])+"\\"
items_file = BASE_FOLDER+sys.argv[1]
sets_file = BASE_FOLDER+sys.argv[2]
no_items_file=BASE_FOLDER+sys.argv[3]
def write(ids,file):
	fo = open(file,"w")
	for item in ids:
		fo.write(str(item)+"\n")

def storeItems(items,file):
	f = open(file,'w');
	for item in items.values():
		f.write(format_item_data_CSV(item["id"],item))
	f.close()

def reatainOnlyItems(sets,items):
	itemsSet = Set(items.keys())
	for set in sets.values():
		set["items"] = list(Set(set["items"]).intersection(itemsSet))
	
	
def meanItemOccurrences(sets):
	oc = countItemOccurrences(sets)
	return (sum(oc.values())+0.0)/len(oc)

	
def meanItemSaves(items):
	total = 0.0
	for item in items.values():
		total += item["statistics"]["saves"]
	return total/len(items)

def countItemsPerSet(sets):
	total = 0.0
	for set in sets.values():
		total +=len(set["items"])
	return total	
def getItemsFromSets(sets):
	ret = []
	for set in sets.values():
		ret.extend(set["items"])
	return Set(ret)	

def countItemOccurrences(sets):
	count = {}
	for set in sets.values():
		for itemId in set["items"]:
			try:
				count[itemId]+=1
			except:
				count[itemId]=1
	return count

def getFullCharacterizedSetsIds(sets,items):
	itemsSet = Set(items.keys())
	s = Set()
	total = 0
	for set in sets.values():
		if(len(Set(set["items"]).intersection(itemsSet))==len(Set(set["items"]))):
			print set["id"],set["items"]
			s.add(set["id"])
		total+=1	
	return s

	
def countLostItems(sets,items):
	itemsSet = Set(items.keys())
	count = 0
	total = len(sets)
	lost = Set()
	for set in sets.values():
		lost = lost.union(Set(set["items"]).difference(itemsSet))	
	return lost
	
def getSetsWithItems(sets,items):
	itemsSet = Set(items.keys())
	count = 0
	total = len(sets)
	ret = []
	for set in sets.values():
		if len(set["items"])!=0 and  len(Set(set["items"]).difference(itemsSet))==0:
			ret.append(set)
	return ret

def pSetSize(sets):
	p = {}
	for set in sets.values():
			try:
				p[len(set["items"])]+=1
			except:
				p[len(set["items"])]=1.0	

	n = len(p)
	return [(id,p[id]/n) for id in p.keys()]	

def plot(x_label,y_label,data):
	""" Create a plot """
	plots = []
	legends = []
	for elem in data:
		legends.append(elem[0]	)
		plots.append(plt.plot(elem[1], elem[2],"o-"))

	plt.xlabel(x_label)
	plt.ylabel(y_label)
	plt.legend(plots, legends)
	plt.show()



def plotPSetSize(sets):
	p = pSetSize(sets)
	x = [el[0] for el in p]
	y = [el[1] for el in p]
	plot("# items per set","provability",[["",x,y]])
items = loadItems(items_file)
sets = loadSets(sets_file)
noitems = loadNoItems(no_items_file)
#storeItems(items,cleanedItemsFile)
"""
print "# items:",len(items)
print "# sets:",len(sets)
print "meanItemsPerSet", countItemsPerSet(sets)/len(sets)
print "meanItemOccurrences ", meanItemOccurrences(sets)
print "meanItemSaves ", meanItemSaves(items)
"""
print "\n\n\n"
purgeNoItems(sets,noitems)
#reatainOnlyItems(sets,items)
print "# items:",len(items)
print "# sets:",len(sets)
print "meanItemsPerSet", countItemsPerSet(sets)/len(sets)
print "num used items: ",len(Set(countItemOccurrences(sets).keys()).intersection(items))
print "meanItemOccurrences ", meanItemOccurrences(sets)
#print plotPSetSize(sets)
print "lost:",len(countLostItems(sets,items))
fullCharacterizedSets = getFullCharacterizedSetsIds(sets,items)
#count = len(fullCharacterizedSets)
#print count, float(count)/len(sets.keys())
print "Sets with items: ",len(getSetsWithItems(sets,items))

#write(getItemsFromSets(sets),"out.txt")
"""
orig_sets_file = open(sets_file,'r')
dest_sets_file = open("dest_sets.txt",'w')
for org_set in orig_sets_file:
	id = int(org_set.split(";")[0])
	if id in fullCharacterizedSets:
		dest_sets_file.write(org_set)
		
"""




