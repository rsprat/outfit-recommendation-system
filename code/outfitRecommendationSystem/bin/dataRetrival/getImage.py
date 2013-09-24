import os
import sys
from garmentgetter import ItemGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from sets import Set
images_dir = "/home/robert/Desktop/capturat/"

"""
Captures and stores the image from an item
"""

def get_image(itemid):
	""" Given an item id, captures its image and stores it in images_dir"""
	url = "http://ak1.polyvoreimg.com/cgi/img-thing/size/l/tid/"+str(itemid)+".jpg"
	img = itemgetter.get_image(url)
	if img != None:
		f = open(images_dir+str(itemid)+".jpg",'w')
		f.write(img)
		f.close()
	else:
		print "Image is none!!!"

if __name__ == "__main__":
	proxy = MyProxy(BASE_FOLDER+"proxy/working.csv")
	itemgetter = ItemGetter(proxy)
	get_image(sys.argv[1])

