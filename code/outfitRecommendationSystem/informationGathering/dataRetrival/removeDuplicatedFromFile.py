from sets import Set

def readIdsCSV(fileName):
	ids = Set()
	f = open(fileName,"r")
	for line in f:	
		try:		
			ids.add(int(line.split(";")[0]))
		except Exception:
			pass
	return ids
			
def read(fileName):
	f = open(fileName,"r")
	s = Set()
	for line in f:
		try:
			s.add(int(line.strip("\n")))
		except Exception:
			pass
	return s
	
def write(ids):
	fo = open("diff.txt","w")
	for item in ids:
		fo.write(str(item)+"\n")
		
		
"""
small = read("small.txt")
items = readIdsCSV("items.txt")

"""
"""
noitems = read("noitems.txt")
items_clean = open("items_clean.txt","w")
items = Set()
f = open("items.txt","r")
count  = 0
i = Set()
for line in f:
	id = int(line.split(";")[0])
	if id not in i and id not in noitems:	
		items_clean.write(line)
		i.add(id)
items_clean.close()
"""

clean = read("noitems.txt")
write(clean)



"""

#write(small.intersection(readIdsCSV("items_clean.txt")))
print "items",len(items)

print "small",len(small)
print "items",len(items)
print "noitems",len(noitems)
print "imatges noitesm",len(noitems.intersection(small))
print "items en noitems",len(items.intersection(noitems))
print "imatges sense item", len((small-items)-noitems)
print "items sense imatge ", len(items-small)

#write(blacklist.union(noitems))



validCat = Set([0,2,3,5,6,4,7,8,9,10,11,17,18,19,20,104,21,15,23,24,25,236,11,26,27,238,240,237,237,239,28,332,241,29,242,243,241,244]);


items_clean = open("items_clean_id.txt","w")
itemsc = readIdsCSV("items_clean.txt")
for i in itemsc:
	items_clean.write(str(i)+"\n")
	

eliminar =read("noitems.txt")
items_clean = open("items_clean.txt","w")
items = Set()
f = open("items.txt","r")
count  = 0
for line in f:
	id = int(line.split(";")[0])
	catId = -1
	try:
		catId = int(line.split(";")[1])
	except:
		pass 
	if catId not in validCat:
		count+=1
	if catId in validCat and id not in eliminar:	
		items_clean.write(line)
print count
	
"""