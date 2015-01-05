import csv


from os import listdir
from random import sample
from datetime import timedelta,datetime
from sys import stdout


import networkx as nx

def avg_path_len(gr,approx=None):

	if approx == None :
		return nx.average_shortest_path_length(gr)
	
	try : 
		a =int(approx)
		if a <1 :
			raise ValueError("approx has to be a integer >0 on None")

		avg = 0.0
		v = gr.nodes()
		for i in range(0,a) :
			s = sample(v,2)
			avg += nx.shortest_path_length(gr,s[0],s[1])

		return avg/a

	except :
		raise ValueError("approx has to be a integer >0 on None")




def open_csv(path):
	with open(path,"r") as d :
		return list(csv.reader(d))	



def create_graph(l) :
	gr = nx.Graph()

	for peer in l :
		gr.add_node(peer[0])

	for peer in l :
		for neighbor in peer[1:] :
			if not gr.has_edge(peer[0],neighbor) :
				gr.add_edge(peer[0],neighbor)


	return gr


def sorter(a) :
	sa = a.split(".")

	if len(sa) !=2 :
		raise ValueError(a+" non e' un file con un nome valido ")
	if sa[-1] == "csv" :
		try:
			return int(sa[0]) 
			
		except  ValueError :
			raise ValueError(a+" non e' un file con un nome valido ")
	
	else :
		raise ValueError(a+" non e' un file con un nome valido ")


def clusterng_coefficient(gr) :

	return nx.average_clustering(gr)


def robustness_test(gr,minimum=70.0,maximum=99.0,distance=1.0) :
	repetitions = 10
	print("repetitions",repetitions)

	res = []
	nodes = gr.nodes()

	points =int((maximum-minimum)/distance)

	for n in range(0,points+1) :
		cluster = 0
		print(minimum+n*distance,end=": ")
		for rep in range(0,repetitions) :
			print("#",end="")
			stdout.flush()
			samp = sample(nodes,int(len(nodes)*(minimum+distance*n)/100))
			copy = nx.Graph(gr)
			copy.remove_nodes_from(samp)
			cluster+= nx.number_connected_components(copy)
		print("\n",end="")
		cluster = int(cluster/repetitions)
		res.append((minimum+distance*n,cluster))

	return res


def marked_peers(data,signal="dead") :

	marked_set = set()

	for i in data :
		if i[-1] == signal :
			marked_set.add(i[0])

	return marked_set	

def self_cleaning_capacity(data) :

	dead_peers = marked_peers(data)

	founded_peers = set()

	for i in data :
		if not i[0] in dead_peers :
		
			for j in i[1:] :
				if j in dead_peers :
					founded_peers.add(j)

	return len(founded_peers)


def degree_distribution(gr) :

	deg_count = {}

	for d in gr.degree().values() :
		if d in deg_count :
			deg_count[d] +=1
		else :
			deg_count[d] = 1

	l = list(deg_count.keys())
	l.sort()
	for i in range(0,len(l)) :
		l[i]= (l[i],deg_count[l[i]])

	return l
		
def attack_resistance(gr,data) :

	bad_list = marked_peers(data,signal="malicious")

	copy = nx.Graph(gr)
	copy.remove_nodes_from(bad_list)

	return nx.number_connected_components(copy)





def testing() :

	lists = [ ['A','B', 'C',],
	        ['B','C', 'D'],
	        ['C','D'],
	        ['D','E'],
	        ['E','F'],
	        ['F','C'] ]



	folder = "C:/Users/Liuga/Google Drive/magistrale/Algo dist/csv/normal/explode"

	fl = listdir(folder)
	for x in reversed(range(0,len(fl))):
		if not ".csv" in fl[x] :
			del fl[x]
	fl.sort(key=sorter)
	
	
	for i in fl[3:] :


		t1 = datetime.now()
		#print("pre opening")
		opening = open_csv(folder+"/"+i)
		
		t2 = datetime.now()

		#print("post opening, pre creation")
		g = create_graph(opening)
		

		#print("post creation,pre clustering")

		n = int(i.split(".")[0])

		t3 = datetime.now()
		c = clusterng_coefficient(g)
		
		t4 = datetime.now()

		#print("post clustering, pre avg")

	
		



		#p2 = avg_path_len(g,approx=int(len(g.nodes())/2))
		p2 = avg_path_len(g,approx=1000)
		
		t5 = datetime.now()

		#p = avg_path_len(g)
		
		t6 = datetime.now()


		#r = robustness_test(g)
		#d = degree_distribution(g)
		print(n,p2,c)



		#print("opening",t2-t1)
		#print("creation of graph",t3-t2)
		#print("clustering",t4-t3)
		#print("avg_approximated",t5-t4)
		#print("avg normal",t6-t5)



if __name__ == '__main__':
	testing()
