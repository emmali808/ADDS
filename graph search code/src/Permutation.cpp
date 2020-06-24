#include "Permutation.h"

using namespace NUS_EMMA_EDITDISTANCE;

void CPermutation::setBase(unsigned int base)
{
	m_base=base;
	m_current=0,m_max=1;
	for (unsigned int i=2;m_base>=i;i++)
		m_max*=i;

	m_vecCurrent.clear();
	m_vecCurrent.resize(base,0);
}

bool CPermutation::getNext(vector<int> &map)
{
	if (m_max==m_current)
		return false;

	getPert(map);
	incIndex();

	return true;
}

void CPermutation::getPert(vector<int> &map)
{
	map.clear();
	map.resize(m_base,0);	

	bool _left=true;
	unsigned int _pos=0,_num=0;
	int j=0;

	for (unsigned int i=m_base;i>1;i--){
		_pos=m_base-i;

		if (i==2) _left=true;
		else {
			if (1==i%2){
				if (1==m_vecCurrent[_pos+1]%2)		
					_left=false;
				else _left=true;
			}else {
				if (1==(m_vecCurrent[_pos+1]+m_vecCurrent[_pos+2])%2)
					_left=false;
				else _left=true;
			}
		}
		_num=0;
		if (_left){
			for (j=m_base-1;j>=0;j--){
				if (map[j]==0) _num++;

				if (_num==m_vecCurrent[_pos]+1){
					map[j]=i-1;
					break;
				}
			}
		} else {
			for (j=0;j<m_base;j++){
				if (map[j]==0) _num++;

				if (_num==m_vecCurrent[_pos]+1){
					map[j]=i-1;
					break;
				}
			}
		}
	}

	for (j=0;j<map.size();j++)
		if (map[j]==0){
			map[j]=0;
			break;
		}
}

void CPermutation::incIndex()
{
	m_current++;

	m_vecCurrent[0]++;

	for (unsigned int i=0;i<m_base-1;i++){
		if (m_vecCurrent[i]==(m_base-i))
			m_vecCurrent[i]=0,m_vecCurrent[i+1]++;
	}
}

void CPermutation::printMediate(FILE* pf)
{
	for (int i=m_vecCurrent.size()-1;i>=0;i--)
		fprintf(pf,"%d,",m_vecCurrent[i]);
	fprintf(pf,"\n");
}
