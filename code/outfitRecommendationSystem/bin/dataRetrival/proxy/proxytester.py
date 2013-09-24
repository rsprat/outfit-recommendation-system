import urllib2
from proxyfileutils import *
import socket
from proxyconnection import *



def test_proxys(proxys):
	

	test_web="http://www.uct2.net/"
	f = urllib2.urlopen(test_web)
	base_answer = f.read()
	f.close()

	f2 = open("base_answer.html",'w')
	f2.write(base_answer )
	f2.close()

	socket.setdefaulttimeout(6)
	good_proxys=[]

	p = MyProxy(proxys)
	for index in range(len(proxys)):
		proxy=p.provider.use()
		print "("+str(index)+"/"+str(len(proxys))+") Trying to conect with proxy",proxy
		try:
			buf = p.make_request_trough_proxy(proxy,test_web)
			print "\tGot something from the web"
			if buf == base_answer:
				good_proxys.append(proxy)
				print "\t:) Proxy works!"
			else:
				print "\t:( Proxy doesn't work!"		
		
		except Exception:
			print "\tCuldn't connect!"
		
	return good_proxys


def get_elite_proxys():
	proxys = proxyfileutils.get_filtered_proxys("proxylist.csv",["Anon.","Transp."])
	good = test_proxys(proxys)
	proxyfileutils.write_proxy_file(good,"eliteproxys2.csv")
