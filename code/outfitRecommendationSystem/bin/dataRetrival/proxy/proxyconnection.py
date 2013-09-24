import urllib2
import proxyfileutils
import sys
import time
import socket
class MyProxy():
	provider = None
	num_threads = 0
	max_num_threads = 10
	def __init__(self,f):
		self.provider = ProxyProvider(proxyfileutils.read_proxy_file(f))

	def make_request_trough_proxy(self,proxy,request):	
		print self.num_threads
		"""
		while(MyProxy.max_num_threads<=self.num_threads):
			print "sleep"			
			time.sleep(10)
		self.num_threads+=1
		"""
		proxy_support = urllib2.ProxyHandler({"http" : proxy})
		opener = urllib2.build_opener(proxy_support)
		urllib2.install_opener(opener)
		socket.setdefaulttimeout(10)	
		f = urllib2.urlopen(request)
		buf = f.read()
		f.close()
		self.num_threads-=1
		return buf

	def make_request_without_proxy(self,request):
		print request
		#print "there are "+str(self.num_threads)+" out of "+str(MyProxy.max_num_threads)
		"""
		while(MyProxy.max_num_threads<=self.num_threads):
			#print "there are "+str(self.num_threads)+" out of "+str(MyProxy.max_num_threads)+" sleep"	
			time.sleep(2)
		self.num_threads+=1
		"""
		print "NOPROXY!"
		f = urllib2.urlopen(request)
		buf = f.read()
		f.close()
		self.num_threads-=1
		return buf
	
	def make_request(self,request):
		while True:
			proxy=self.provider.use()
			print "Trying proxy",proxy
			try:				
				#ret = self.make_request_trough_proxy(proxy,request)	
				ret = self.make_request_without_proxy(request)	
				self.provider.ret(proxy,True)	
				return ret		
			except Exception , e:
				self.provider.ret(proxy,False)	
				print request	
				print e
				try:
					if e.code ==	404: # If the web wasn't found						
						return
				except Exception:
					self.num_threads-=1
					print "\tProxy doesn't work!"
			

		print "Culdn't find a working proxy"

class ProxyProvider():
	"""
	This class is in charge of providing proxies to other services.
	This class handles a list of proxies, from which a client can borrow one (method "use") making that proxy not avalible for other users.
	When a client whants to release a proxy, calls the method "ret" with the proxy and a boolean saying weather the proxy worked or not.
	When a proxy is returned saying that it worked "ret(proxy,true)", it's inmediatly added to the list of ready proxies
	In contrast, when a proxy is returned but wasn't useful "ret(proxy,false)", its ranking gets decremented by one and the proxy goes directly to the
	punished list where it will be for a number of periods directly proportional to it's ranking.

	Once the ProxyProvided is no longer used, it stores the rank values in "proxyranking.txt"
	"""
	ready = []
	punished = []
	rank = {}

	def __init__(self,proxys):	
		"""
		Initialize the provider with the given list of proxies.
		Reads the ranking file to obtain previous ranking data
		"""	
		self.ready = proxys
		self.punished = []
		self.rank = {}
		for proxy in self.ready:
			self.rank[proxy]=0		
		try:
			ranking = open("proxy\proxyranking.csv",'r')
			for line in ranking:
				line = line.split()
				self.rank[line[0]]=int(line[1].strip("\n"))	
			ranking.close()		
		except IOError:
			print "No ranking file found"	
		self.ready =  sorted(self.ready, key=lambda proxy: self.rank[proxy], reverse=True)	
		for p in self.ready:
			print p		
	

	def __del__(self):
		"""	Stores the ranking data in a file """
		ranking = open("proxyranking.csv",'w')
		for item in  sorted(self.rank.items(), key=lambda proxy: proxy[1]):
			ranking.write(str(item[0])+" "+str(item[1])+"\n")				
		ranking.close()

	def use(self):
		""" Borrows the first proxy in the ready queue, if empty, borrows the first from the punished list"""
		self.update_list()
		if len(self.ready) > 0:	
			return self.ready.pop(0)
		if len(self.punished)>0:				
			return sorted(self.punished, key=lambda proxy: proxy[1]).pop(0)[0]
		return "00.00.00.00:00"
		

	def update_list(self):
		""" Update the counter of the proxies in the punished list.
		For each proxy in this list, increments it's tick counter with one.
		If a proxy has a tick counter of 0, it is returned to the ready list
		"""
		for proxy in self.punished:
			if proxy[1] == 0:
				self.ready.append(proxy[0])
				self.punished.remove(proxy)
			else:
				proxy[1]+=1

	def ret(self,proxy,ok):
		""" Return a proxy to the proxy provider.
		If ok=true menas that the proxy whas working and so, it enters into the ready queue inmediately.
		If ok=false means that the proxy was not working and so, it enters into the punished list with the tick counter set to twice its rank
		"""
		if ok:
			self.ready.append(proxy)
		else:	
			self.rank[proxy]-=1
			self.punished.append([proxy,-self.rank[proxy]*2])


