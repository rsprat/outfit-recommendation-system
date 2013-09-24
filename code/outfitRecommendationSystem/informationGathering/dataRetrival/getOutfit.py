import os
import sys
from outfitgetter import SetGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from sets import Set
from dataFileUtils import format_set_data_CSV

"""
	Script for integration with Java code, recieves a set id as input and outputs the info for that set gathered from Polyvore
"""
if __name__ == "__main__":
	proxy = MyProxy(BASE_FOLDER+"proxy"+os.sep+"working.csv")
	setgetter = SetGetter(proxy)
	set_id = int(sys.argv[1])
	set_data = setgetter.get_data(set_id)
	print "SETDATA:"+format_set_data_CSV(set_id,set_data)


