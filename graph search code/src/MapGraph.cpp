#include "MapGraph.h"
#include "assert.h"

using namespace NUS_EMMA_EDITDISTANCE;

int CMapGraph::getMap(size_t index) const
{
	if (m_vecMap.size()<=index)
		return -1;
	else return m_vecMap[index];
}

void CMapGraph::getMapping(Matrix<double> &matrix)
{
	assert(matrix.rows()==matrix.columns());
	int i=0,j=0;
	int _rows=matrix.rows();
	for(i=0;i<_rows;i++) 
		for(j=0;j<_rows;j++){
			if (0==matrix(i,j))
				m_vecMap[i]=j;	
		}//end for j
}

