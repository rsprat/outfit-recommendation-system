import os
import sys
from membergetter import UserGetter
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from sets import Set
from dataFileUtils import format_user_data_CSV
data_file = BASE_FOLDER+"captured"+os.sep+"members_data.csv"
summary_file = BASE_FOLDER+"captured"+os.sep+"members_summary.csv"


def store_data(uid,data):
	f = open(data_file,'a')
	f.write(format_user_data_CSV(uid,data))
	f.close()

def store_summary(uid,summary):
	
	f = open(summary_file,'a')
	elems = [uid,summary["set_views"],summary["set_likes"],summary["trophies"],summary["followers"]]
	f.write(reduce(lambda x,y: str(x)+";"+str(y), elems)+"\n")
	f.close()

def compute_index(summary):
	score = 0
	score += summary["set_views"]
	score += summary["set_likes"]
	score += summary["trophies"]*20
	score += summary["followers"]*40
	return score

def load_summarized(new_users):
	f = open(summary_file,'r')
	v = []
	for summarized in f:
		summary_list = summarized.split(";")
		uid = int(summary_list[0])
		summary={}
		summary["set_views"]= int(summary_list[1])
		summary["set_likes"]= int(summary_list[2])
		summary["trophies"]= int(summary_list[3])
		summary["followers"]= int(summary_list[4])
		index = compute_index(summary)
		if uid not in new_users:
			v.append(uid)
			new_users[uid] = index
	print len(v)
	print len(set(v))

def load_visited(visited_set):
	v = []
	visited_file = open(data_file,'r')
	for visited in visited_file:
		v.append(int(visited.split(";")[0]))
		visited_set.add(int(visited.split(";")[0]))	
	visited_file.close()	
	print len(v)
	print len(set(v))

proxy = MyProxy(BASE_FOLDER+"proxy/working.csv")
usergetter = UserGetter(proxy)

visited_users = Set([])
load_visited(visited_users)

new_users = {}
load_summarized(new_users)


if len(new_users)==0:
	ID="349251"
	new_users[ID]=1

while new_users:

	print "visited: ", len(visited_users)
	print "new: ", len(new_users)
	user_id = max(new_users, key=new_users.get)
	del new_users[user_id]

	while user_id in visited_users:
		print user_id, "already viisted"
		user_id = max(new_users, key=new_users.get)
		del new_users[user_id]

	print "Current: ",user_id 
	visited_users.add(user_id)
	user_data = usergetter.get_data(user_id)
	store_data(user_id,user_data)
	print "Elegible for new users:",len(user_data["following"])
	for following in user_data["following"]:
		user_id = following[1]
		user_name = following[0]
		print user_id
		if user_id not in visited_users and user_id != "UNKNOWN" and int(user_id) != 27 and user_id not in new_users:	
			print "get summary ",user_name
			user_summary = usergetter.get_summary(user_name)
			store_summary(user_id,user_summary)
			index = compute_index(user_summary)
			new_users[user_id]=index

	print "----------------"
