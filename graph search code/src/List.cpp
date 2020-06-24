#include "List.h"

#include <algorithm>

using namespace std;
using namespace NUS_EMMA_EDITDISTANCE;

bool CEntry::operator< (const CEntry &other) const
{
	return (m_nInf<other.m_nInf);
}

bool CList::hasMore()
{
	return (m_index<(int)m_vecGraph.size());
}

CEntry CList::getNext()
{
	return m_vecGraph[m_index++];
}

void CList::normalize()
{
	sort(m_vecGraph.begin(),m_vecGraph.end());
}

void CList::clear()
{
	m_vecGraph.clear();
}

void CList::print(FILE* pf)
{	
	for (unsigned int i=0;m_vecGraph.size()>i;i++)
		fprintf(pf," %d,%d",m_vecGraph[i].m_nId,m_vecGraph[i].m_nInf);
}
