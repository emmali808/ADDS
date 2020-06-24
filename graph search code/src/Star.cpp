#include "Star.h"

#include <algorithm>

using namespace NUS_EMMA_EDITDISTANCE;

template <class InputIterator1, class InputIterator2>
int set_intersection(InputIterator1 first1, InputIterator1 last1,
                     InputIterator2 first2, InputIterator2 last2)
{
	int _count=0;
	while (first1!=last1 && first2!=last2){
		if (*first1<*first2)
			++first1;
		else if (*first2<*first1)
			++first2;
		else{
			_count++;
			first1++;
			first2++;
		}
	}

	return _count;
}


CStar::CStar()
{
	m_nRoot=0;
	m_vecLeaf.clear();
	m_vecLabels.clear();
	m_labelCount.clear();
}

CStar::~CStar()
{
}

bool CStar::operator< (const CStar &other) const
{
	if (m_nRoot<other.m_nRoot)
		return true;

	if (m_nRoot>other.m_nRoot)
		return false;

	if (0==other.m_vecLabels.size())
		return false;

	if (0==m_vecLabels.size())
		return true;

	return (std::lexicographical_compare(m_vecLabels.begin(),m_vecLabels.end(),other.m_vecLabels.begin(),other.m_vecLabels.end()));
}

bool CStar::operator== (const CStar &other) const
{
	if (m_nRoot!=other.m_nRoot)
		return false;

	if (m_vecLabels.size()!=other.m_vecLabels.size())
		return false;

	return (std::equal(m_vecLabels.begin(),m_vecLabels.end(),other.m_vecLabels.begin()));
}

size_t CStar::getElements()
{
	return 1+2*m_vecLeaf.size();
}

size_t CStar::getDiff(const std::vector<int> &vecA,const std::vector<int> &vecB)
{
	size_t _max;
	if (vecA.size()>vecB.size())
		_max=vecA.size();
	else _max=vecB.size();

	vector<int> _vecRS=vecA;

	vector<int>::iterator _iter=set_intersection(_vecRS.begin(),_vecRS.end(),
		vecB.begin(),vecB.end(),_vecRS.begin());

	size_t _s=distance<vector<int>::iterator>(_vecRS.begin(),_iter);

	return _max-_s;
}


size_t CStar::getDist(const CStar &a, const CStar &b)
{
	size_t _nrs=0;
	
	//root label
	if (a.m_nRoot != b.m_nRoot)
		_nrs+=1;

	//edge number
	_nrs+=abs((int)(a.m_vecLeaf.size()-b.m_vecLeaf.size()));	

	_nrs+=CStar::getDiff(a.m_vecLabels,b.m_vecLabels);

	return _nrs;
}

void CStar::clear()
{
	m_nRoot=0;
	m_vecLeaf.clear();
	m_vecLabels.clear();
	m_labelCount.clear();
}


void CStar::normalize()
{
	sort(m_vecLabels.begin(),m_vecLabels.end());
}

void CStar::print(FILE* pf)
{
	fprintf(pf,"Root: %d\nNodes:",m_nRoot);	
	for (unsigned int i=0;m_vecLeaf.size()>i;i++)
		fprintf(pf,"%d,",m_vecLeaf[i]);
	fprintf(pf,"\nLabels:");	
	for (unsigned int i=0;m_vecLabels.size()>i;i++)
		fprintf(pf,"%d,",m_vecLabels[i]);
	fprintf(pf,"\n--------------------\n");
}

bool CStarGraph::operator< (const CStarGraph &other) const
{
	return (m_vecStars.size()<other.m_vecStars.size());
}

int CStarGraph::getSizeDiff(const CStarGraph &a, const CStarGraph &b)
{
	return ( abs((int)(a.m_vecStars.size()-b.m_vecStars.size())) + abs((int)(a.m_nEdges-b.m_nEdges))/2 );
}

void CStarGraph::getSetDiff(std::vector<CStar> &a, std::vector<CStar> &b, std::vector<CStar> &diff)
{// for sorted vectors
	vector<CStar> vecA,vecB;
	if (a.size()>b.size()){
		vecA=a;
		vecB=b;
	}else{
		vecA=b;
		vecB=a;
	}

	set_difference(vecA.begin(), vecA.end(), vecB.begin(), vecB.end(), diff.begin());
}

int CStarGraph::getComm(CStarGraph &a, CStarGraph &b)
{
	vector<CStar> &vecA=a.m_vecStars;
	vector<CStar> &vecB=b.m_vecStars;

	int count=0;
	for (unsigned int i=0,j=0;vecA.size()>i && vecB.size()>j;){
		if (vecA[i] == vecB[j]){
			count++;
			i++;
			j++;
		}
		else if (vecA[i] < vecB[j]){
			i++;
		}
		else
			j++;
	}

	return count;
}

void CStarGraph::getCountBound(CStarGraph &a, CStarGraph &b, int &lower)
{
	int _max=a.m_vecStars.size()>b.m_vecStars.size()?a.m_vecStars.size():b.m_vecStars.size(); //
	int _min=a.m_maxDeg>b.m_maxDeg?a.m_maxDeg:b.m_maxDeg;// update here for max degree
	if (_min<1)	
		_min=2;	
	else _min+=1;	
	
	lower=_max-getComm(a,b);
	float _rs=(float)lower;
	_rs=_rs/_min;
	if (_rs -(int)_rs > 0)
		lower=(int)_rs+1;
	else
		lower=(int)_rs;
}

void CStarGraph::getCountBound(CStarGraph &a, CStarGraph &b, vector<int> &aHash, vector<int> &bHash, int &lower)
{
	int _max=aHash.size()>bHash.size()?aHash.size():bHash.size(); //
	int _min=a.m_maxDeg>b.m_maxDeg?a.m_maxDeg:b.m_maxDeg;// update here for max degree
	if (_min<1)	
		_min=2;	
	else _min+=1;

	int _num=set_intersection(aHash.begin(),aHash.end(),bHash.begin(),bHash.end());
	
	lower=_max-_num;
	float _rs=(float)lower;
	_rs=_rs/_min;
	if (_rs -(int)_rs > 0)
		lower=(int)_rs+1;
	else
		lower=(int)_rs;
}

int CStarGraph::getMappingDist(CStarGraph &a, CStarGraph &b, CMapGraph *map)
{
	vector<CStar>::pointer _vecA,_vecB,_vecBB;
	size_t m=0,n=0; //m >= n

	double _maxDist=a.m_maxDeg>b.m_maxDeg?a.m_maxDeg:b.m_maxDeg;// update here for max degree
	_maxDist=1+2*_maxDist;

	if (a.m_vecStars.size()>=b.m_vecStars.size()){
		_vecA=&(b.m_vecStars[0]);
		_vecB=&(a.m_vecStars[0]);
		m=a.m_vecStars.size();
		n=b.m_vecStars.size();
	}else{
		_vecA=&(a.m_vecStars[0]);
		_vecB=&(b.m_vecStars[0]);
		m=b.m_vecStars.size();
		n=a.m_vecStars.size();
	}

	Matrix<double> _matrix(m,m);
	{
		for (unsigned int i=0;n>i;i++,_vecA++){
			_vecBB=_vecB;
			for (unsigned int j=0;m>j;j++,_vecBB++)
				_matrix(i,j)=_maxDist-CStar::getDist(*_vecA,*_vecBB);
		}
	
		// fill empty vertex value

		vector<double> _vecDiff;
		for (unsigned int i=0;m>i;i++,_vecB++)
			_vecDiff.push_back((*_vecB).getElements());	

		for (unsigned int i=n;m>i;i++)
			for (unsigned int j=0;m>j;j++)
				_matrix(i,j)=_maxDist-_vecDiff[j];
	}

	Hungarian _h;
	double _fRS=m*_maxDist-_h.solve(_matrix);

	//get the mapping function
	if (0x00!=map){
		map->m_vecMap.clear();
		map->m_vecMap.resize(m,-1);

		map->getMapping(_matrix);

		vector<int> _tmpMap(map->m_vecMap.size());
		if (a.m_vecStars.size()<b.m_vecStars.size()){
			copy(map->m_vecMap.begin(),map->m_vecMap.end(),_tmpMap.begin());
			for (unsigned int i=0;_tmpMap.size()>i;i++)
				map->m_vecMap[_tmpMap[i]]=i;
		}
	}

	return (int)_fRS;
}

int CStarGraph::getMappingDist(vector<CStar> &a, vector<CStar> &b,
							   int &maxDist, Matrix<double> &ma, Hungarian &h)
{
	size_t m=0,n=0; //m >= n
	size_t _low=0;

	if (a.size()>b.size()){
		m=a.size();
		n=b.size();
		ma.resize(m,m);
		_low=m;

		for (unsigned int i=0;m>i;i++)
			for (unsigned int j=0;n>j;j++)
				ma(i,j)=maxDist-CStar::getDist(a[i],b[j]);

		vector<double> _vecDiff;
		for (unsigned int i=0;m>i;i++)
			_vecDiff.push_back(a[i].getElements());	

		for (unsigned int i=0;m>i;i++)
			for (unsigned int j=n;m>j;j++)
				ma(i,j)=maxDist-_vecDiff[i];

	}
	else{
		m=b.size();
		n=a.size();
		ma.resize(n,m);
		_low=n;

		for (unsigned int i=0;n>i;i++)
			for (unsigned int j=0;m>j;j++)
				ma(i,j)=maxDist-CStar::getDist(a[i],b[j]);
	}

	h.set(ma);
	double _fRS=_low*maxDist-h.solve();

	return (int)_fRS;
}

int CStarGraph::getMappingDist(vector<CStar> &diff,
							   vector<CStar> &a, vector<CStar> &b,
							   int &maxDist, Matrix<double> &ma,
							   Hungarian &h, vector<int> &pos, CMapGraph *map) 
{
	size_t m=b.size();
	size_t n=a.size()+diff.size();
	const size_t len=n;

	if (n<m){
		ma.resize(m,m);
		for (unsigned int i=a.size();n>i;i++)
			for (unsigned int j=0;m>j;j++)
				ma(i,j)=maxDist-CStar::getDist(diff[i-a.size()],b[j]);

		vector<double> _vecDiff;
		for (unsigned int i=0;m>i;i++)
			_vecDiff.push_back(b[i].getElements());

		for (unsigned int i=n;m>i;i++)
			for (unsigned int j=0;m>j;j++)
				ma(i,j)=maxDist-_vecDiff[j];
	}
	else{
		m=n;
		n=b.size();
		ma.resize(m,m);

		for (unsigned int i=a.size();m>i;i++)
			for (unsigned int j=0;n>j;j++)
				ma(i,j)=maxDist-CStar::getDist(diff[i-a.size()],b[j]);

		vector<double> _vecDiff;
		for (unsigned int i=0;a.size()>i;i++)
			_vecDiff.push_back(a[i].getElements());
		for (unsigned int i=0;diff.size()>i;i++)
			_vecDiff.push_back(diff[i].getElements());

		for (unsigned int i=0;m>i;i++)
			for (unsigned int j=n;m>j;j++)
				ma(i,j)=maxDist-_vecDiff[i];
	}

	double _fRS=m*maxDist-h.incre_solve(ma);

	// realign the vertices to map to original positions
	vector<int> _vecPos;
	for (int i=0;len>i;i++){
		for (int j=0;m>j;j++){
			if (ma(i,j)==0){
				_vecPos.push_back(j);
				ma(i,j)=-1;
			}
		}
	}
	for (int i=0;pos.size()>i;i++)
		ma(pos[i],_vecPos[i])=0;

	//get the mapping function
	if (0x00!=map){
		map->m_vecMap.clear();
		map->m_vecMap.resize(m,-1);

		map->getMapping(ma);
	}

	return (int)_fRS;
}

void CStarGraph::getLowerBound(CStarGraph &a, CStarGraph &b,
							   int &mdist, int &lower, CMapGraph *map)
{
	int _min=a.m_maxDeg>b.m_maxDeg?a.m_maxDeg:b.m_maxDeg;
	if (_min<3)	
		_min=4;	
	else _min+=1;	
	
	mdist=CStarGraph::getMappingDist(a,b,map);
	float _rs=(float)mdist;
	_rs=_rs/_min;
	if (_rs -(int)_rs > 0)
		lower=(int)_rs+1;
	else
		lower=(int)_rs;
}

void CStarGraph::print(FILE* pf)
{
	for (unsigned int i=0;m_vecStars.size()>i;i++){
		m_vecStars[i].print(pf);	
	}
}

void CStarGraph::addStar(int id, int label)
{
	CStar _star(id,label);
	m_vecStars.push_back(_star);
}

void CStarGraph::addEdge(int sv, int ev)
{
	if (0>sv || 0>ev || (int)m_vecStars.size()<=sv || (int)m_vecStars.size()<=ev) 
		return;

	{
		CStar& _star=m_vecStars[sv];
		_star.m_vecLeaf.push_back(ev);
		_star.m_vecLabels.push_back(m_vecStars[ev].m_nRoot);
	}
	{
		CStar& _star=m_vecStars[ev];
		_star.m_vecLeaf.push_back(sv);
		_star.m_vecLabels.push_back(m_vecStars[sv].m_nRoot);
	}
}

void CStarGraph::normalize()
{
	size_t _max=0,_total=0;
	for (unsigned int i=0;m_vecStars.size()>i;i++){
		m_vecStars[i].normalize();

		_max=m_vecStars[i].m_vecLeaf.size();
		_total+=_max;
		if (_max>m_maxDeg)
			m_maxDeg=_max;
	}
	m_nEdges=_total;

	sort(m_vecStars.begin(),m_vecStars.end());

	vector<int> _tmpMap(m_vecStars.size(),0);
	for (unsigned int i=0;m_vecStars.size()>i;i++){
		_tmpMap[m_vecStars[i].m_nRootID]=i;
		m_vecStars[i].m_nRootID=i;
	}
	for (unsigned int i=0;m_vecStars.size()>i;i++)
		for (unsigned int j=0;m_vecStars[i].m_vecLeaf.size()>j;j++)
			m_vecStars[i].m_vecLeaf[j]=_tmpMap[m_vecStars[i].m_vecLeaf[j]];
}

void CStarGraph::clear()
{
	m_maxDeg=0;
	m_nEdges=0;
	m_vecStars.clear();
}

