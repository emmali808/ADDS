#ifndef _INIT_H_
#define _INIT_H_

#include "Query.h"
#include "Database.h"

extern NUS_EMMA_EDITDISTANCE::CGraphDB 		gl_db;
extern vector<NUS_EMMA_EDITDISTANCE::CQuery> 	gl_vecQuery;
extern vector<double> gl_qtime;

void init(int argc,char* argv[]);

#endif

