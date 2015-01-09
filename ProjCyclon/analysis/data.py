from analysis import *
import csv
import networkx as nx



if __name__ == '__main__':
	




	folder = "C:/Users/Liuga/Google Drive/magistrale/Algo dist/csv/normal/m20000"

	fl = listdir(folder)
	for x in reversed(range(0,len(fl))):
		if not ".csv" in fl[x] :
			del fl[x]
	fl.sort(key=sorter)
	
	
	f = open("self_cleaning_capacity.tsv","w")
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




	for i in fl[9:] :

		opening = open_csv(folder+"/"+i)
		
		n = int(i.split(".")[0])
		
		#g = create_graph(opening)

		#a = attack_resistance(g,opening)

		s =self_cleaning_capacity(opening)
		normal.writerow([n-9,s])
		print(n,s)

	
	#gr =nx.gnm_random_graph(20000,len(g.edges()))


	#r = robustness_test(gr,minimum=60.0,distance=0.5)
	#d = degree_distribution(gr)
	
	#for i in d :

	
	#------------------------------------------
	#d = degree_distribution(g)


	#--------------------------------------------






	#opening = open_csv(folder+"/50.csv")
		

		
	#g = create_graph(opening)

	#print(attack_resistance(g,opening))