class Taxonomy():
	root = None
	
	def __init__(self):
		self.root = Category("root",0,[
							Category("Clothing",2,[
							 Category("Dress",3,[
								Category("Day",4),
								Category("Cocktail",5),
								Category("Grown",6)
								]),

							Category("Skirt",7,[
								Category("Mini",8),
								Category("Knee length",9),
								Category("Long",10)
								]),

							Category("Tops",11,[
								Category("Blouse",17),
								Category("Cardigan",18),
								Category("Sweater",19),
								Category("Sweatshirt and hoodie",20),
								Category("Tank",104),
								Category("T-Shirt",21),
								Category("Tunic",15)
								]),
							Category("Outerwear",23,[
								Category("Coat",24),
								Category("Jacket",25,[Category("Blazers",236)] ),
								Category("Day",11),
								Category("Vest",26)	
							]),

							Category("Jeans",27,[
								Category("Bootcut",238),
								Category("Boyfriend",240),
								Category("Skiny",237),
								Category("Straight",310),
								Category("flared",4452),
								Category("Wide",239)	
							]),

							Category("Pants",28,[
								Category("Cappry & Croped",332),
								Category("Legins",241),
								
							]),
							Category("Shorts",29),
							Category("Jumpsuit & Romper",242,[
								Category("Jumpsuit",243),
								Category("Legins",241),
								Category("Romper",244)
							]),		
						])
					])

	def show(self):
		if self.root is None:
			return
		self.show_subtree(self.root," ")


	def show_subtree(self,node,padding):
		print padding+padding, node.name,":",node.ID#," (",len(node.sub_categories),")"
		for sub_category in node.sub_categories:					
			self.show_subtree(sub_category,padding+padding)

	def getAllIds(self):
		return self.root.getIdsAsList()
		
		
		
	def deeper(self,node,cat):
		if node.ID==cat: return [node.name]		
		
		for sub_category in node.sub_categories:
			print "going"
			result =  self.deeper(sub_category,cat)
			print result
			print "coming"
			if result is not None:
				print result				
				return result.append(node.name)			

	def get_category_taxonomy(self,cat):
		return self.deeper(root,cat)


class Category():
	sub_categories=[]
	name="UNKNOWN"
	ID=0
	def __init__(self,name,ID,sub=[]):
		self.name=name
		self.ID=ID
		self.sub_categories=sub

	def add_sub_category(self,subcategory):
		self.sub_categories.append(subcategory)
	def getIdsAsList(self):
		
		if len(self.sub_categories) == 0:
			return [self.ID]
		else:
			ret =  [self.ID]
			for sub in self.sub_categories:
				ret.extend(sub.getIdsAsList())
			return ret
			
		


"""
romper= Category("Intimate",108)
bra= Category("Bra",245)
camisoles= Category("Camisole",247)
chemessei= Category("Chemessei",248)
hoseiry= Category("Hosiery",68)
socks= Category("Socks",69)
thigs= Category("Thighs",251)

pijamas= Category("Pijamas",249)
pantiesandtongues= Category("Pantiesandtongues",246)
robes= Category("Robes",250)
swimmwear= Category("Swimmwear",31)

bags= Category("ags",25)
backpak= Category("BackPak",259)
handbags= Category("Handbags",318)
clutches= Category("Clutches",38)
shoulders= Category("Shoulders",37)
totes= Category("Totes",36)


messengerBags= Category("258",31)

"""



