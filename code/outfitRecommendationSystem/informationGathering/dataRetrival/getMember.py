#!/usr/bin/env python
import os
import sys
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from membergetter import UserGetter
from dataFileUtils import format_user_data_CSV
from sets import Set

data_file = BASE_FOLDER+"captured/members_data.csv"
summary_file = BASE_FOLDER+"captured/members_summary.csv"

"""
	Script for integration with Java code, recieves a user id as input and outputs the info for that user gathered from Polyvore
"""

def print_data(uid,data):
	print "USERDATA:"+format_user_data_CSV(uid,data)
	print_summary(uid,data["summary"])

def print_summary(uid,summary):
	
	elems = [uid,summary["set_views"],summary["set_likes"],summary["trophies"],summary["followers"]]
	print "SUMMARY:"+reduce(lambda x,y: str(x)+";"+str(y), elems)+"\n"

if __name__ == "__main__":
	user_id = int(sys.argv[1])
	proxy = MyProxy(BASE_FOLDER+"proxy"+os.sep+"working.csv")
	usergetter = UserGetter(proxy)
	
	user_data = usergetter.get_data(user_id)

	print_data(user_id,user_data)


