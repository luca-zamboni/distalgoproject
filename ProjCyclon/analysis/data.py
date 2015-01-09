from analysis import *
import csv
import networkx as nx



if __name__ == '__main__':
	




	folder = "F:"

	fl = listdir(folder)
	for x in reversed(range(0,len(fl))):
		if not ".csv" in fl[x] :
			del fl[x]
	fl.sort(key=sorter)
	
	
	f = open("degree_distribution.tsv","w")
	normal = csv.writer(f,delimiter="\t",lineterminator="\n")



	#----------------------------------------------
	#for i in fl :



	#	opening = open_csv(folder+"/"+i)
		

	#	g = create_graph(opening)
		


	#	n = int(i.split(".")[0])

	#	c = clusterng_coefficient(g)

	#	p2 = avg_path_len(g,approx=1500)

	#	normal.writerow([n,p2,c])


	
	#------------------------------

	#start = 30

	#for i in fl[start:] :

	#	opening = open_csv(folder+"/"+i)

	#	#print(len(marked_peers(opening)))
		
	#	n = int(i.split(".")[0])
		
	#	g = create_graph(opening)

	#	a = attack_resistance(g,opening)

	#	#s =self_cleaning_capacity(opening)
	#	normal.writerow([n-start,a])
	#	print(n-start,a)



	#opening = open_csv(folder+"/30.csv")
		
		
	#g = create_graph(opening)


	## r = robustness_test(gr,minimum=60.0,distance=0.5)
	#d = degree_distribution(gr)
	#print(d)
	
	#for i in d :
	#	normal.writerow(list(i))

	
	#------------------------------------------
	opening = open_csv(folder+"/41.csv")
	g = create_graph(opening,directed=True)
	#gr =nx.gnm_random_graph(5000,len(g.edges()),directed=True)
	d = degree_distribution(g)
	for i in d :
		normal.writerow(list(i))
		print(i)


	#--------------------------------------------






	#opening = open_csv(folder+"/50.csv")
		

		
	#g = create_graph(opening)

	#print(attack_resistance(g,opening))
