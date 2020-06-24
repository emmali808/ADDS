SEGOS
------------

This package is a graph query tool based on a novel index structure.
The package can support similarity range queries.


CONTAINS
--------

readme.txt    -> This file


HOW TO USE
----------
1. Similarity range queries

    ./rangeexp [OPTION] ${INPUTDB} ${QUERY}

    [OPTION]:
   	-d --integer distance treshold (default by 2)
	-k --integer k value (default by 100)
	-h --integer h value (default by 1000)
   
To see the usage, run the programs with no arguments.
   
   
FILE FORMAT
-----------------
  
1. INPUTDB
  
   t #graph0              ; graph ID
   v 0 4                    ; label of each vertex
   v 1 38
   v 2 38
   v 3 23
   e 0 1                     ; end points of each edge
   e 1 2
   e 2 3
   e 0 3
 
   t #graph1
   ...

2. QUERY

   Same as INPUTDB.
   A query_file may have multiple queries.
          
---------
Xiaoli Wang
7/13/2011


