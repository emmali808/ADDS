#Makefile for similarity search in graph databases
CC=g++ -DNDEBUG -O3
CFLAGS=-c
#Change BIN_HOME to your local binary folder
BIN_HOME=/home/xiaoli/graph/segos_published/segos_nopipe/bin/
OFILES=star.o list.o db.o init.o time.o hungarian.o map.o matrix.o permu.o

all:rangeexp

rebuild:clean all
	
clean:
	rm -f *.o
	rm bin/rangeexp

rangeexp:${OFILES} rangeexp.o
	${CC} -o ${BIN_HOME}$@ ${OFILES} rangeexp.o

# ------------------------------------------
rangeexp.o:rangeExp.cpp
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}


# COMMON OBJECT FILES

star.o:Star.cpp Star.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

list.o:List.cpp List.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

db.o:Database.cpp Database.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

init.o:Init.cpp Init.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

time.o:Time.cpp Time.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

hungarian.o:Hungarian.cpp Hungarian.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

map.o:MapGraph.cpp MapGraph.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

matrix.o:MatrixGraph.cpp MatrixGraph.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}

permu.o:Permutation.cpp Permutation.h
	${CC} ${CFLAGS} $< -o $@ ${INCLUDE}
