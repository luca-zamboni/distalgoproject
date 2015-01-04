import csv

from pygraph.classes.graph import graph
from pygraph.algorithms.heuristics.chow import chow
from pygraph.algorithms.minmax import shortest_path

def calcAvPathLength(gr):
	av = 0.0
	i = 0

	for start in gr :
		dj = shortest_path(gr,start)[1]
		for end in dj :
			if start != end :
				l = dj[end]
				av+= l
				i +=1

	return av/i



	#for n in nodes:
		#for n2 in nodes:
		#	if(n!=n2):
		#		print(n +" " + n2)
		#		#av += len(find_shortest_path(nodes,n,n2))
		#		i+=1

	#av = av / i
	#print(av)

			
#def find_shortest_path(graph, start, end, path=[]):
#	path = path + [start]
#	if start == end:
#		return path
#	if not graph.has_key(start):
#		return None
#	shortest = None
#	for node in graph[start]:
#		if node not in path:
#			newpath = find_shortest_path(graph, node, end, path)
#			if newpath:
#				if not shortest or len(newpath) < len(shortest):
#					shortest = newpath
#	return shortest



def open_csv(path):
	with open(path,"r") as d :
		return list(csv.reader(d))	

def prepare_graph(l) :

	#------------------------ adding nodes ot the graph
	graph = {}
	for peer  in l :
		graph[peer[0]] = set()
	
	#-------------------- adding edges
	for peer in l :
		for neighbor in peer[1:] :
			graph[peer[0]].add(neighbor)
			graph[neighbor].add(peer[0])

	#-------------------- converting from set to lists

	for node in graph :
		if node in graph[node] : #if the key is in its value remove it
			graph[node].remove(node)
		graph[node] = list(graph[node])

	return graph


	

def create_graph(l) :
	gr = graph()

	for peer in l :
		gr.add_node(peer[0])

	for peer in l :
		for neighbor in peer[1:] :
			if not gr.has_edge((peer[0],neighbor)) :
				gr.add_edge((peer[0],neighbor))



	return gr



if __name__ == '__main__':

	gr = {'A': ['B', 'C'],
			'B': ['A','C', 'D'],
			'C': ['D','A','B','F'],
			'D': ['E','C','B'],
			'E': ['F','D'],
			'F': ['C','E']}

	lists = [ ['A','B', 'C',],
	        ['B','C', 'D'],
	        ['C','D'],
	        ['D','E'],
	        ['E','F'],
	        ['F','C'] ]



	#print find_shortest_path(gr, 'A', 'E')
	# print(calcAvPathLength(gr))

	# g= prepare_graph(lists)

	
	g = create_graph(open_csv("../data/39.txt"))



	# print(p)


	#g = create_graph(lists)




	#c= chow("B")
	#c.optimize(q)
	#print(c.centers)
	#print(c('A','B'))

	#print(q.neighbors("C"))
	
	p = calcAvPathLength(g)

	print(p)
