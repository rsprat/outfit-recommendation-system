#!/usr/bin/env python
import sys
import os
import re
BASE_FOLDER = os.sep.join(os.path.realpath(__file__).split(os.sep)[0:-1])+os.sep
sys.path.append(BASE_FOLDER+"proxy")
from proxyconnection import *
from commongetter import CommonGetter
import threading
max_pages = 10

class UserGetter(CommonGetter):
	partial_uid_list=[]
	def __init__(self,proxy):		
		super(UserGetter,self).__init__(proxy)

	def get_followers(self,uname):
		try:
			num=1
			user_ids=[]
			more = True
			while more and num<max_pages+1:
				print "followers pg",num
				followers_page = self.get_element("http://"+uname+".polyvore.com/?filter=followers")
				plain_followers_page=followers_page.toString()	
				for div in followers_page.getElementsByTagName("div"):
					if div.getAttribute("class")=="rec_follow clearfix":
						for li in div.getElementsByTagName("li"):
							if li.getAttribute("class")=="name":
								name = li.getElementsByTagName("a")[0].childNodes[0].data
						#for span in div.getElementsByTagName("span"):
						#	if span.getAttribute("class")=="clickable":								
						#		ID=re.search("(?<=[0-9a-zA-Z]{3}\",\")[0-9]+",span.toString()).group(0)		
						print name
						user_ids.append([name,self.get_user_id(name)])			
				#more = self.has_next(followers_page)	
				more = False
				num+=1
			return user_ids
		except Exception , e:
			print "\tUnable to get followers ", e
		return []

	def get_user_id(self,name):		
		print "get user id for ", name
		user_page = self.get_element("http://www."+name+".polyvore.com")
		id=re.search("(?<=\/cgi\/profile\?id=)[0-9]+",user_page.toString()).group(0)
		return id

	class ThreadGetUID (threading.Thread):
		"""Thread class for getting a user id from an user name"""

		def __init__(self, uname,num,func):  
			threading.Thread.__init__(self)  
			self.num = num  
			self.uname = uname
			self.func = func

		
		
		def run(self):  
		 	UserGetter.partial_uid_list[self.num]=self.get_user_id(self.uname)		

	def thr_get_user_ids(self,unames):
		UserGetter.partial_uid_list=["UNKNOWN"]*len(unames) #Initialize the list with UNKNOWN values for every position
		i = 0
		threads = []
		for uname in unames:
			t = self.ThreadGetUID(uname,i,self.get_element)
			t.start()
			threads.append(t) #Store the thread
			i+=1
		# Wait every thread termination
		for t in threads:
			try:
				t.join()#If the thread already finished, it might throw an exception
			except:
				pass
		return list(UserGetter.partial_uid_list)#Return a copy
	
	class ThreadGetFollowing (threading.Thread):
		"""Thread class for getting the sets from a certain page """
		def __init__(self, uname,page_num,get_func,next_func,get_uid_func,results,more):  
			threading.Thread.__init__(self)  
			self.results = results #List where to store the results
			self.more = more #Flag array to indicate by the thread if there are more pages
			self.page_num = page_num  
			self.uname = uname
			self.get_func = get_func
			self.get_uid_func = get_uid_func
			self.next_func = next_func
	
			
		def run(self):  
			print "user following pg",str(self.page_num)
			following_page = self.get_func("http://"+self.uname+".polyvore.com/?filter=following")
			plain_following_page=following_page.toString()	
			for div in following_page.getElementsByTagName("div"):
				if div.getAttribute("class")=="rec_follow clearfix":
					for li in div.getElementsByTagName("li"):
						if li.getAttribute("class")=="name":
							name = li.getElementsByTagName("a")[0].childNodes[0].data
							self.results.append([name,self.get_uid_func(name)])		

					
			self.more[0]= self.next_func(following_page)
			
	def get_following(self,uname):
		try:
			num=1
			following=[]
			t = self.ThreadGetFollowing(uname,0,self.get_element,self.has_next,self.get_user_id,following,[False]) #Its the last page in the row, send the more flag
			t.start()
			try:
				t.join()#If the thread already finished, it might throw an exception
			except:
				pass
			return following
			""" obsolete
			more = [True] #Flag set to true when there are more pages to visit. It's encapsulated in an array as that container is mutable and can be used as in/out param for thread
			while more[0] and num<max_pages+1:
				more[0] = False;
				i = 0
				threads = []
				pages_per_row = 10 #How many pages are queried at a time
				for i in range(num,pages_per_row+num):					
					if(i == pages_per_row+num-1):
						t = self.ThreadGetFollowing(uname,i,self.get_element,self.has_next,following,more) #Its the last page in the row, send the more flag
					else:				
						t = self.ThreadGetFollowing(uname,i,self.get_element,self.has_next,following, [False]) #We don't care about the more flag, as it's not the last page in the row
					t.start()
					threads.append(t) #Store the thread
					i+=1
				# Wait every thread termination
				for t in threads:
					try:
						t.join()#If the thread already finished, it might throw an exception
					except:
						pass
				num+=pages_per_row 		
			return following
			"""
		except Exception as e:
			print "\tUnable to get user following",e
		return []	 	
		 	
	class ThreadGetLikedSet (threading.Thread):
		"""Thread class for getting the liked sets from a certain page """
		def __init__(self, uname,page_num,get_func,next_func,results,more):  
			threading.Thread.__init__(self)  
			self.results = results #List where to store the results
			self.more = more #Flag array to indicate by the thread if there are more pages
			self.page_num = page_num  
			self.uname = uname
			self.get_func = get_func
			self.next_func = next_func
			
		def run(self):  
			print "liked sets pg",str(self.page_num)
			set_page = self.get_func("http://"+self.uname+".polyvore.com/?filter=likes")
			plain_set_page=set_page.toString()	
			for match in re.findall("(?<=img-set\/cid\/)[0-9]+",str(plain_set_page)):					
					self.results.append(match)
			self.more[0]= self.next_func(set_page)
	
	class ThreadGetUserSet (threading.Thread):
		"""Thread class for getting the sets from a certain page """
		def __init__(self, uname,page_num,get_func,next_func,results,more):  
			threading.Thread.__init__(self)  
			self.results = results #List where to store the results
			self.more = more #Flag array to indicate by the thread if there are more pages
			self.page_num = page_num  
			self.uname = uname
			self.get_func = get_func
			self.next_func = next_func
			
		def run(self):  
			print "user sets pg",str(self.page_num)
			set_page = self.get_func("http://"+self.uname+".polyvore.com/?filter=sets")			

			for element in set_page.getElementsByTagName("div"):
				if element.getAttribute("class")=="title":
					for el in element.getElementsByTagName("a"):				
						for match in re.findall("(?<=set\?id=)[0-9]+",el.toString()):
							self.results.append(match)	
			#self.more[0]= self.next_func(set_page)
		 	
	def get_sets(self,uname,worker):
		try:
			num=1
			set_ids=[]
			t = worker(uname,0,self.get_element,self.has_next,set_ids,[False]) #Its the last page in the row, send the more flag
			t.start()
			try:
				t.join()#If the thread already finished, it might throw an exception
			except:
				pass
			""" OBSOLETE
			more = [True] #Flag set to true when there are more pages to visit. It's encapsulated in an array as that container is mutable and can be used as in/out param for thread
			while more[0] and num<max_pages+1:
				more[0] = False;
				i = 0
				threads = []
				pages_per_row = 1 #How many pages are queried at a time
				for i in range(num,pages_per_row+num):					
					if(i == pages_per_row+num-1):
						t = worker(uname,i,self.get_element,self.has_next,set_ids,more) #Its the last page in the row, send the more flag
					else:				
						t = worker(uname,i,self.get_element,self.has_next,set_ids, [False]) #We don't care about the more flag, as it's not the last page in the row
					t.start()
					threads.append(t) #Store the thread
					i+=1
				# Wait every thread termination
				for t in threads:
					try:
						t.join()#If the thread already finished, it might throw an exception
					except:
						pass
				num+=pages_per_row 	
			"""				
			return set_ids
		except Exception as e:
			print "\tUnable to get user sets"
		return []	 	
		 	
	def get_user_sets(self,uname):
		return self.get_sets(uname,self.ThreadGetUserSet)	 	
		 	
	def get_liked_sets(self,uname):
		return self.get_sets(uname,self.ThreadGetLikedSet)	 	

	def get_name(self,uid):	
		set_page = self.get_element("http://www.polyvore.com/cgi/browse.sets?page=1&uid="+str(uid))	
		for div in set_page.getElementsByTagName("div"):
			if div.getAttribute("id")=="buddyicon":
				return div.getElementsByTagName("img")[0].getAttribute("alt")
				div.toString()

	def get_user_summary(self,uname):
		try:		
			user_page = self.get_element("http://"+uname+".polyvore.com/")

			summary={"set_views":0,"set_likes":0,"trophies":0,"followers":0}		
			for ul in user_page.getElementsByTagName("ul"):			
				if ul.getAttribute("class")=="activity_summary":
					for li in ul.getElementsByTagName("li"):					
						data = li.toString()			
						try:
							result = re.search("(?<=</div>).{1,10} view",data).group(0)
							summary["set_views"]=int(self.numeral_to_int(result.split()[0].replace(",","")))
						except Exception:
							pass
						try:
							result =  re.search("(?<=</div>).{1,20} like",data).group(0)
							summary["set_likes"]= int(self.numeral_to_int(result.split()[0].replace(",","")))
						except Exception:
							pass

						try:
							result= re.search("(?<=</div>).{1,10} trophie",data).group(0)
							summary["trophies"]= int(self.numeral_to_int(result.split()[0].replace(",","")))
						except Exception:
							pass					

						try:
							result = re.search("(?<=</div>).{1,10} follower",data).group(0)
							summary["followers"]=int( self.numeral_to_int(result.split()[0].replace(",","")))
						except Exception:
							pass
			
		except Exception as e:
			print "Unable to get user summary",e
		return summary			
					
	def get_summary(self,uname):
		return self.get_user_summary(uname)
		
	def get_data(self,uid):			
		info={}	

		info["name"] = self.get_name(uid)		
		info["summary"] = self.get_user_summary(info["name"])		
		
		info["user_sets"]=self.get_user_sets(info["name"])	
		info["liked_sets"]=self.get_liked_sets(info["name"])
		info["following"]=self.get_following(info["name"])	
		#info["followers"]=self.get_followers(info["name"])
		
		return info
