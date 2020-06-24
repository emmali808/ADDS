#include "Init.h"

#include "stdlib.h"
#include "assert.h"
#include "stdio.h"

#include <map>

#include "Time.h"

using namespace std;
using namespace NUS_EMMA_EDITDISTANCE;

void init(int argc, char* argv[])
{
	class TSINGHUA_CLIPSE_UTIL::TimeRecorder time;

	{
		/**
		 * Build the index structure
		 **/

		ifstream _fs;
        _fs.open(argv[argc-2],ios::in);
		
        if (_fs.fail()) {
                fprintf(stderr,"Error:Failed to open the dataset file - %s\n",argv[argc-2]);
                fprintf(stderr,"Please check the file name and restart this program.\n");
                fprintf(stderr,"The program will be terminated!\n");
                exit(-1);
        }
			
		gl_db.parseData(_fs);
		gl_db.buildIndex();
		_fs.close();
	}

	time.check();
	gl_qtime.push_back(time.diffTime(0,1));

	{
		/**
		 * Initialize the query patterns of graphs
		 **/
		ifstream _fs;
        _fs.open(argv[argc-1],ios::in);
		
        if (_fs.fail()) {
                fprintf(stderr,"Error:Failed to open the query file - %s\n",argv[argc-1]);
                fprintf(stderr,"Please check the file name and restart this program.\n");
                fprintf(stderr,"The program will be terminated!\n");
                exit(-1);
        }
		
		CGraphDB _graphs;
		_graphs.parseData(_fs);
		_fs.close();
 
		for (unsigned int i=0;_graphs.m_vecStars.size()>i;i++){
			CQuery _query;
			CStarGraph &_graph=_graphs.m_vecStars[i];

			{
				_query.m_nDeg=_graph.m_maxDeg+1;
				if (gl_db.m_nDeg > _query.m_nDeg){
					_query.m_nDeg=0;

					// calculate the max degree parameter
					for (int i=0;gl_db.m_vecStars.size()>i;i++){
						int _min=gl_db.m_vecStars[i].m_maxDeg>_graph.m_maxDeg?gl_db.m_vecStars[i].m_maxDeg:_graph.m_maxDeg;// update here for max degree
						if (_min<3)	
							_min=4;	
						else _min+=1;
						_query.m_maxDeg.push_back(_min);
					}
				}
				else{
					// calculate the max degree parameter
					for (int i=0;gl_db.m_vecStars.size()>i;i++){
						int _min=_graph.m_maxDeg;// update here for max degree
						if (_min<3)	
							_min=4;	
						else _min+=1;
						_query.m_maxDeg.push_back(_min);
					}
				}
			}

			_query.m_starGraph=_graph;
			CMapGraph _map;
			for (unsigned int i=0;_graph.m_vecStars.size()>i;i++)
				_map.m_vecMap.push_back(i);					
			_query.m_matrix=CMatrixGraph::getMatrix(_graph,_map);

			if (gl_db.m_topk > 0){
				unsigned int j=0;
				vector<list_pair> _vecSimiStar;
				for (;_graph.m_vecStars.size()>j;j++){
					if (j != 0 && _graph.m_vecStars[j] == _graph.m_vecStars[j-1]){
						_query.m_starHash.push_back(_query.m_starHash.back());
						continue;
					}

					_query.m_index.push_back(j);
					_query.m_qMax.push_back(_graph.m_vecStars[j].getElements());

					gl_db.findSimilarStars(_vecSimiStar,_graph.m_vecStars[j]);
					if (_vecSimiStar.back().m_dist == 0)
						_query.m_starHash.push_back(_vecSimiStar.back().m_sid);

					_query.m_topk.push_back(_vecSimiStar);
					_vecSimiStar.clear();
				}
				_query.m_index.push_back(j);
			}

			gl_vecQuery.push_back(_query);
		}
	}

	time.check();
	gl_qtime.push_back(time.diffTime(1,2));
}

