import csv


from os import listdir
from random import sample


import networkx as nx

def avg_path_len(gr):
	av = 0.0
	i = 0

	lengths = nx.all_pairs_shortest_path_length(gr)

	for k in lengths :
		for j in lengths[k]:
			av += lengths[k][j]
			i += 1

	return av/i



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
	if sa[-1] == "txt" :
		try:
			return int(sa[0]) 
			
		except  ValueError :
			raise ValueError(a+" non e' un file con un nome valido ")
	
	else :
		raise ValueError(a+" non e' un file con un nome valido ")


def clusterng_coefficient(gr) :

	return nx.average_clustering(gr)


def robustness_test(gr,minimum=70.0,maximum=99.0,distance=1.0) :
	repetitions = 20
	res = []
	nodes = gr.nodes()

	points =int((maximum-minimum)/distance)

	for n in range(0,points+1) :
		cluster = 0
		
		for rep in range(0,repetitions) :
			samp = sample(nodes,int(len(nodes)*(minimum+distance*n)/100))
			copy = nx.Graph(gr)
			copy.remove_nodes_from(samp)
			cluster+= nx.number_connected_components(copy)

		cluster = int(cluster/repetitions)
		res.append((minimum+distance*n,cluster))

	return res


def marked_peers(data) :
	signal="dead"

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

	bad_list = marked_peers(data)

	copy = nx.Graph(gr)
	copy.remove_nodes_from(bad_list)

	return nx.number_connected_components(copy)


if __name__ == '__main__':



	lists = [ ['A','B', 'C',],
	        ['B','C', 'D'],
	        ['C','D'],
	        ['D','E'],
	        ['E','F'],
	        ['F','C'] ]





	fl = listdir("../data")
	fl.sort(key=sorter)
	
	for i in fl[-1:]:

		g = create_graph(open_csv("../data/"+i))

		n = int(i.split(".")[0])
		c = clusterng_coefficient(g)
		p = avg_path_len(g)
		#r = robustness_test(g)
		d = degree_distribution(g)
		print(n,p,c,d)


