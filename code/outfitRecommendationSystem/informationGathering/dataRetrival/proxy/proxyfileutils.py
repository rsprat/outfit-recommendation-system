import sys
def read_raw_file(f):
	f = open(f,'r')
	proxys = []
	count = 0
	for line in f:
		if count!=0:
			data = line.split("\t")
			try:
				proxys.append(data[0])
			except Exception:
				pass
		count+=1
	f.close()
	return proxys

def read_proxy_file(f):
	try:
		f = open(f,'r')
		proxys = []
		for line in f:					
				try:						
					proxys.append(line.strip("\n"))
				except Exception:
					pass	
		f.close()
		return proxys
	except Exception:
		print "Error reading the file ",f,"\n\t", sys.exc_info()

def write_proxy_file(proxys,f):
	out = open(f,'w')
	for proxy in proxys:
		out.write(proxy+"\n")
	out.close()


def do_filter(proxys,filters):
	final_list=[]
	for proxy in proxys:
		if proxy[1] not in filters:
			final_list.append(proxy)
	return final_list

def get_filtered_proxys(f,filters):
	return  do_filter(read_proxy_file(f),filters)


