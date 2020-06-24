#ifndef _PERMU_H_
#define _PERMU_H_

#include <vector>
#include <fstream>

using namespace std;

namespace NUS_EMMA_EDITDISTANCE{
	class CPermutation
	{
	private:
		unsigned int m_base;
		unsigned long m_current;
		unsigned long m_max;
		vector<unsigned int> m_vecCurrent;

		void incIndex();
		void getPert(vector<int> &map);

	public:
		void setBase(unsigned int base);
		bool getNext(vector<int> &map);
		void printMediate(FILE* pf);
	};
}

#endif

