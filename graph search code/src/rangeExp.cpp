#include <stdlib.h>
#include <stdio.h>
#include <vector>
#include <cstring>
#include <ostream>

#include "Database.h"
#include "Query.h"
#include "Init.h"
#include "Time.h"

using namespace std;
using namespace NUS_EMMA_EDITDISTANCE;

CGraphDB gl_db;
vector<CQuery> gl_vecQuery;
vector<double> gl_qtime;

int gl_topk=100;
int gl_depth=1000;
int gl_nDist=2;

void usage()
{
	fprintf(stdout,"**************************************************\n");
	fprintf(stdout,"RANGEEXP 1.0 Copyright 2010, NUS, Singapore\n");
	fprintf(stdout,"Range query for CA from threshold 0 to 20\n");
	fprintf(stdout,"-----------------------------------\n\n");
	fprintf(stdout,"Usage: rangeexp [OPTION] ${INPUTDB} ${QUERY}\n");
	fprintf(stdout," -k\t--integer\tk value (default by 100).\n");
	fprintf(stdout," -h\t--integer\tlist processing depth (default by 1000).\n");
	fprintf(stdout," -d\t--integer\tedit distance threshold (default by 2).\n");
	fprintf(stdout,"\nAuthor : Xiaoli Wang, SOC, NUS, Singapore.\n");
	fprintf(stdout,"GMail  : xiaolistue@gmail.com\n");
	fprintf(stdout,"**************************************************\n");
}

void parseOptions(int argc, char* argv[])
{
	if (argc > 3){
		for (unsigned int i=1;argc-2>i;i++){
			if (strncmp(argv[i],"-k",2) ==0){
				if (1 != sscanf(argv[++i],"%d",&gl_topk)){
					fprintf(stdout,"Error occured while parsing the value of k.\n");
					exit(-1);
				}
				if (gl_topk<=0){
					fprintf(stdout,"The h value error : %d.\n",gl_topk);
					exit(-1);
				}
			}

			if (strncmp(argv[i],"-h",2) ==0){
				if (1 != sscanf(argv[++i],"%d",&gl_depth)){
					fprintf(stdout,"Error occured while parsing the value of h.\n");
					exit(-1);
				}
				if (gl_depth<=0){
					fprintf(stdout,"The k value error : %d.\n",gl_depth);
					exit(-1);
				}
			}

			if (strncmp(argv[i],"-d",2) ==0){
				if (1 != sscanf(argv[++i],"%d",&gl_nDist)){
					fprintf(stdout,"Error occured while parsing the edit distance threshold.\n");
					exit(-1);
				}
				if (gl_nDist<0){
					fprintf(stdout,"Distance threshold value error : %d.\n",gl_nDist);
					exit(-1);
				}
			}

		}
	}
}

int main(int argc,char* argv[])
{
	if (argc < 3){
		usage();
		exit(-1);
	}

	parseOptions(argc,argv);
	gl_db.m_topk=gl_topk;
	gl_db.m_depth=gl_depth;
	
	fprintf(stdout,"Begin to build the index, Please wait...\n");
	init(argc,argv);
	fprintf(stdout,"Finish building the index!\n");
	fprintf(stdout,"Total time to index: %-10.6f(sec)\n",gl_qtime[0]);

	int _qnum=gl_vecQuery.size();
	int _candNum=0;
	int _acNum=0;
	double _qtime=0.0;
	int _rcount=0;

	
	fprintf(stdout,"Begin to excute the query...\n");

	for (int i=0;_qnum>i;i++){
		class TSINGHUA_CLIPSE_UTIL::TimeRecorder time;
		int gl_ncomp=gl_db.rangeQuery(gl_vecQuery[i],gl_nDist, true);
		time.check();
		_candNum+=gl_ncomp;
		_acNum+=gl_db.m_accessNum;
		_qtime+=time.diffTime(0,1);
	}

	fprintf(stdout,"Avg. Access Num(K)\tAvg. Candidate Num(K)\tAvg. Query Time(sec)\n%-10.6f\t%-10.6f\t%-10.6f\n",(float)_acNum/(float)(_qnum*1000),(float)_candNum/(float)(_qnum*1000),_qtime/_qnum+gl_qtime[1]);

	return 0;
}

