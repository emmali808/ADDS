#ifndef _MAPGRAPH_H
#define _MAPGRAPH_H

#include <vector>

#include "Matrix.h"

using namespace std;

namespace NUS_EMMA_EDITDISTANCE{

	class CMapGraph
	{
	public:
		vector<int> m_vecMap; //start with 0
		int getMap(size_t index) const;


		void getMapping(Matrix<double> &matrix);
	};
}

#endif

