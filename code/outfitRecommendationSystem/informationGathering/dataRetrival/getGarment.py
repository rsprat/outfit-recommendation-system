import os
import sys
from garmentgetter import ItemGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from sets import Set
no_items_file=BASE_FOLDER+"captured"+os.sep+"nogarments.csv"
items_file = BASE_FOLDER+"captured"+os.sep+"garments.csv"
sets_file = BASE_FOLDER+"captured"+os.sep+"outfits.csv"
from dataFileUtils import format_item_data_CSV
"""
	Script for integration with Java code, recieves a item id as input and outputs the info for that item gathered from Polyvore
"""


if __name__ == "__main__":

	proxy = MyProxy(BASE_FOLDER+"proxy/working.csv")
	itemgetter = ItemGetter(proxy)
	item_id = int(sys.argv[1])
	item_data = itemgetter.get_data(item_id)
	print "ITEMDATA:"+format_item_data_CSV(item_id,item_data)