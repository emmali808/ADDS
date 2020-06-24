#ifndef _MATRIX_H
#define _MATRIX_H

#include <vector>

#include "Star.h"
#include "MapGraph.h"

using namespace std;

namespace NUS_EMMA_EDITDISTANCE{
	class CMatrixGraph
	{
	public:
		vector<vector<int> > m_vvecMatrix;  // all edges (vertex to vertex connections)
		vector<int>  m_vecLabels;           // all vertex labels

		void print(FILE* pf);
	public:
		//return the matrix of graph after using map
		static CMatrixGraph getMatrix(const CStarGraph &graph,const CMapGraph &map);

		//fill nodes to make this matrix possess cdt nodes
		class CMatrixGraph refMatrix(size_t cdt) const;

		//return the different of two matrices with identical nodes
		static int diffElements(const CMatrixGraph &a,const CMatrixGraph &b);

		//return the difference of two matrices through map
		static int getDiff(const CStarGraph &a,const CMatrixGraph &b,const CMapGraph &map);

		static int getDiff(const CStarGraph &a,const CMatrixGraph &b,const CMapGraph &map,int relax);

		//return the map to get much better diff
		static int refineDiff(const CMatrixGraph &a,const CMatrixGraph &b,const CMapGraph &map,int est);

	private:
		static void exchange(CMatrixGraph &a,int row, int column);
	};
}

#endif

