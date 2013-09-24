from sets import Set
import sys
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
		
		
		
if len(sys.argv)<4:
	print sys.argv[0]+" ids_file src_folder ds_folder"
	sys.exit()
		
import os		
ids = read(sys.argv[1])
for id in ids:
	try:
		os.rename(sys.argv[2]+"/"+str(id)+".jpg",sys.argv[3]+"/"+str(id)+".jpg")
	except:
		pass
"""
items_clean = open("items_clean.txt","w")

	
items = Set()
f = open("items.txt","r")
for line in f:
	if int(line.split(";")[0]) in items:
		pass
		#print line.split(";")[0]
	else:
		items_clean.write(line)
	try:		
		items.add(int(line.split(";")[0]))
	except Exception:
		print line.split(";")[0]
		
print len(items)	
images = read("img.txt")
		
inter = items.intersection(images)		
print len(inter)
write(inter)
"""


