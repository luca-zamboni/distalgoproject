import csv

def calcAvPathLength(nodes):
	av = 0.0
	i = 0
	for start in nodes :
		for end  in nodes :
			if start != end :
				l = find_shortest_path(nodes,start,end)
				if l == None :
					raise ValueError("graph isn't connected!")
				av+= len(l)
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

			
def find_shortest_path(graph, start, end, path=[]):
    path = path + [start]
    if start == end:
        return path
    if not graph.has_key(start):
        return None
    shortest = None
    for node in graph[start]:
        if node not in path:
            newpath = find_shortest_path(graph, node, end, path)
            if newpath:
                if not shortest or len(newpath) < len(shortest):
                    shortest = newpath
    return shortest

# for i in range(0,82):
# 	nodes = {}
# 	with open(str(i)+".txt","rb") as f:
# 		reader = csv.reader(f)
# 		for row in reader:
# 			n = row.pop(0)
# 			nodes[n] = row
# 	calcAvPathLength(nodes);


#graph = {'A': ['B', 'C'],
        # #'B': ['C', 'D'],
      #   'C': ['D'],
      #   'D': ['C'],
      #   'E': ['F'],
      #   'F': ['C']}

#print(find_shortest_path(graph,'A','D'))
# ['A', 'B', 'D']
#print(len(find_shortest_path(graph,'A','D')))
# 3
if __name__ == '__main__':

	graph = {'A': ['B', 'C'],
	        'B': ['A','C', 'D'],
	        'C': ['D','A','B','F'],
	        'D': ['E','C','B'],
	        'E': ['F','D'],
	        'F': ['C','E']}

	#print find_shortest_path(graph, 'A', 'E')
	print calcAvPathLength(graph)
