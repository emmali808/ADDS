#ifndef _QUERY_H_
#define _QUERY_H_

#include <vector>

#include "Star.h"
#include "MatrixGraph.h"
#include "List.h"

using namespace std;

namespace NUS_EMMA_EDITDISTANCE{

	/************************************************************************/
	/* Query Graph                                                          */
	/************************************************************************/
	class CQuery
	{
	public:
		class CStarGraph m_starGraph;
		class CMatrixGraph m_matrix;

		vector<int> m_index;               // positions of different stars
		vector<int> m_starHash;
		vector<int> m_qMax;
		vector<vector<list_pair> > m_topk; // top-k similar stars to each star
		vector<int> m_maxDeg;              // the para of max degree with all graphs
		int m_nDeg;

		CQuery(void){m_nDeg=0;};

		void clear(){
			m_starGraph.clear();
			m_index.clear();
			m_starHash.clear();
			m_qMax.clear();
			m_topk.clear();
			m_maxDeg.clear();
		};
	};
};


#endif

