from sets import Set
import urllib2
import sys
import urllib

def getImages(ids,dest):
	for itemid in ids:
		url = "http://embed.polyvoreimg.com/cgi/img-thing/size/t/tid/"+str(itemid)+".jpg"
		image=urllib.URLopener()
		image.retrieve(url,dest+"/"+str(itemid)+".jpg")
		"""
		f = urllib2.urlopen(url)
		img = f.read()
		f.close()
		if img != None:
			f = open(dest+"/"+str(itemid)+".jpg",'w')
			f.write(img)
			f.close()
		else:
			print "Image is none!!!"
		"""
		
def read(fileName):
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
	
def write(ids,file):
	fo = open(file,"w")
	for item in ids:
		fo.write(str(item)+"\n")

		
if len(sys.argv)<4:
	print sys.argv[0]+" ids_file1 ids_file2 (diff|intersec) [diff_file]"
	sys.exit()
		
		
s1 = read(sys.argv[1])
s2 = read(sys.argv[2])
diff = Set()
if(sys.argv[3] == "diff"):
	diff = s1.difference(s2)
if(sys.argv[3] == "intersec"):
	diff = s1.intersection(s2)
	
if(len(sys.argv)==4):
		print len(diff)
if(len(sys.argv)==5):
	write(diff,sys.argv[4])
if(len(sys.argv)==6):
	tmp = Set()
	for i in range(int(sys.argv[4])):
		tmp.add(diff.pop())
	getImages(tmp,sys.argv[5])

