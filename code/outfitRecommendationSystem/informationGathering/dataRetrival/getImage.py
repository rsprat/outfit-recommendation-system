import os
import sys
import sys
import libxml2dom
import urllib
from garmentgetter import ItemGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from sets import Set

images_dir = BASE_FOLDER+"captured"+os.sep+"images"+os.sep

"""
Captures and stores the image from an item
"""

def get_image(itemid):
	""" Given an item id, captures its image and stores it in images_dir"""
	url = "http://embed.polyvoreimg.com/cgi/img-thing/size/l/tid/"+str(itemid)+".jpg"
	image=urllib.URLopener()
	image.retrieve(url,images_dir+os.sep+str(itemid)+".jpg")


if __name__ == "__main__":
	proxy = MyProxy(BASE_FOLDER+"proxy/working.csv")
	itemgetter = ItemGetter(proxy)
	get_image(sys.argv[1])

