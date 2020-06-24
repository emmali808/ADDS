#include "Database.h"

#include <string>
#include <limits.h>
#include <math.h>   

#include "Permutation.h"

using namespace NUS_EMMA_EDITDISTANCE;

bool gl_flag=false;

bool myComp(CEntry const& a, CEntry const& b) 
{
	return (a.m_nSize<b.m_nSize);
}

template<class InputIterator1, class InputIterator2, class OutputIterator>
void set_difference(InputIterator1 first1, InputIterator1 last1,
					InputIterator2 first2, InputIterator2 last2,
					OutputIterator result, vector<int> &index)
{
	vector<int> _index1,_index2;
	InputIterator1 first = first1;
	while (first1!=last1 && first2!=last2){
		if (*first1<*first2){
			_index2.push_back((int)(first1-first));
			*result++ = *first1++;
		}
		else if (*first2<*first1)
			first2++;
		else{
			_index1.push_back((int)(first1-first));
			first1++;
			first2++;
		}
	}

	copy(first1,last1,result);

	while (first1!=last1){
		_index2.push_back((int)(first1-first));
		first1++;
	}

	for(int i=0;_index1.size()>i;i++)
		index.push_back(_index1[i]);
	for(int i=0;_index2.size()>i;i++)
		index.push_back(_index2[i]);
}



CGraphDB::CGraphDB(void)
{
	m_laNum=0;
	m_topk=100;
	m_depth=1000;
	m_nDeg = 0;
}

void CGraphDB::parseData(ifstream &fs)
{
	if (fs.fail()){
		fprintf(stderr,"Input file error while parsing data file!\n");
		exit(-1);
	}

	string _strLine,_strTmp;
	CStarGraph _starGraph;
	size_t _pos=0;
	int _none=0,_ntwo=0;
	bool _bFirstGraph=true;
	map<int,int>::iterator _it;

	while(!fs.eof()) {
		getline(fs,_strLine);

		if (_strLine.empty() || '#'==_strLine.at(0))
			continue;

		if ('t' == _strLine.at(0)) {	
			if (!_bFirstGraph) {
				_starGraph.normalize();
				m_nDeg = m_nDeg > _starGraph.m_maxDeg ? m_nDeg : _starGraph.m_maxDeg;
				m_vecStars.push_back(_starGraph);
			}else
				_bFirstGraph=false;

			_starGraph.clear();
		}else if ('v' == _strLine.at(0)){
			_pos=_strLine.find(" ");	
			if (string::npos == _pos){
				fprintf(stderr,"Locating vertex information failed!\n");
				exit(-1);
			}
			_strTmp=_strLine.substr(_pos+1,_strLine.size()-_pos);	
			sscanf(_strTmp.c_str(),"%d %d",&_none,&_ntwo);

			//the vertex label should be greater than 0
			if (_none<0 || _ntwo<=0) {
				fprintf(stderr,"Error: Vertex information error: %s!\n",_strLine.c_str());
				exit(-1);
			}
			_starGraph.addStar(_none,_ntwo);

			if (_ntwo>m_laNum)
				m_laNum=_ntwo;

		}else if ('e' == _strLine.at(0)) {
			_pos=_strLine.find(" ");	
			if (string::npos == _pos){
				fprintf(stderr,"Locating edge infomartion failed!\n");
				exit(-1);
			}
			_strTmp=_strLine.substr(_pos+1,_strLine.size()-_pos);	
			sscanf(_strTmp.c_str(),"%d %d",&_none,&_ntwo);

			//edge index should be >=0
			if (_none<0 || _ntwo<0) {
				fprintf(stderr,"Error: Edge index error: %d,%d!\n",_none,_ntwo);
				exit(-1);
			}
			_starGraph.addEdge(_none,_ntwo);
		}
	}
	_starGraph.normalize();
	m_nDeg = m_nDeg > _starGraph.m_maxDeg ? m_nDeg : _starGraph.m_maxDeg;
	m_vecStars.push_back(_starGraph);

	m_nDeg = m_nDeg > 3 ? m_nDeg + 1 : 4;

	fprintf(stdout, "Finish parsing the original data file!\n");
}

void CGraphDB::buildIndex()
{
	map<CStar,CList> _graphList;
	map<CStar,CList>::iterator _it;
	for (int i=0;m_vecStars.size()>i;i++){
		CStarGraph &_vecStar = m_vecStars[i];
		for (int j=0;_vecStar.m_vecStars.size()>j;j++){
			CStar &_theStar = _vecStar.m_vecStars[j];
			_it = _graphList.find(_theStar);
			if (_it == _graphList.end()){
				CList _theList;
				_theList.m_vecGraph.push_back(CEntry(i,1,_vecStar.m_vecStars.size()));
				_graphList.insert(pair<CStar,CList>(_theStar,_theList));
				continue;
			}

			if (_it->second.m_vecGraph.back().m_nId == i)
				_it->second.m_vecGraph.back().m_nInf++;
			else
				_it->second.m_vecGraph.push_back(CEntry(i,1,_vecStar.m_vecStars.size()));
		}
	}

	m_starList.resize(m_laNum+1);
	int _sid=0;
	for (_it=_graphList.begin();_it!=_graphList.end();_it++,_sid++){
		m_vecAllStar.push_back(_it->first); // store orignal star structures

		stable_sort(_it->second.m_vecGraph.begin(),_it->second.m_vecGraph.end(),myComp);
		int _sizeTmp=_it->second.m_vecGraph[0].m_nSize;
		_it->second.m_vecSize.push_back(_sizeTmp);
		_it->second.m_vecIndex.push_back(0);
		int j=0;
		for (;_it->second.m_vecGraph.size()>j;j++){
			int _size=_it->second.m_vecGraph[j].m_nSize;
			if (_size!=_sizeTmp){
				_it->second.m_vecSize.push_back(_size);
				_it->second.m_vecIndex.push_back(j);
				_sizeTmp=_size;
			}
		}
		_it->second.m_vecIndex.push_back(j);
		m_graphList.push_back(_it->second); // store the graph inverted lists

		const vector<int> &_starLabel=_it->first.m_vecLabels;
		int _size=_starLabel.size();
		m_starSizeList.push_back(CEntry(_sid,_size));
		for (int i=0;_size>i;i++){
			if (0 >= m_starList[_starLabel[i]].m_vecGraph.size()){
				CList _theList;
				m_starList[_starLabel[i]].m_vecGraph.push_back(CEntry(_sid,1,_size));
				continue;
			}

			if (m_starList[_starLabel[i]].m_vecGraph.back().m_nId == _sid)
				m_starList[_starLabel[i]].m_vecGraph.back().m_nInf++;
			else
				m_starList[_starLabel[i]].m_vecGraph.push_back(CEntry(_sid,1,_size));
		}
	}

	fprintf(stdout, "Finish building the upper-level index!\n");

	for (int i=1;m_starList.size()>i;i++){
		if (m_starList[i].m_vecGraph.size()<=0)
			continue;

		// sort and group the star inverted lists
		sort(m_starList[i].m_vecGraph.rbegin(),m_starList[i].m_vecGraph.rend());
		stable_sort(m_starList[i].m_vecGraph.begin(),m_starList[i].m_vecGraph.end(),myComp);

		// record the pointer to each size group
		int _sizeTmp=m_starList[i].m_vecGraph[0].m_nSize;
		m_starList[i].m_vecSize.push_back(_sizeTmp);
		m_starList[i].m_vecIndex.push_back(0);
		int j=0;
		int _size;
		for (;m_starList[i].m_vecGraph.size()>j;j++){
			_size=m_starList[i].m_vecGraph[j].m_nSize;
			if (_size!=_sizeTmp){
				m_starList[i].m_vecSize.push_back(_size);
				m_starList[i].m_vecIndex.push_back(j);
				_sizeTmp=_size;
			}
		}
		m_starList[i].m_vecIndex.push_back(j);
	}
	sort(m_starSizeList.begin(),m_starSizeList.end());   // sort the list of stars with size

	fprintf(stdout, "Finish building the lower-level index!\n");

	getHash(); // get graph star representation hash codes
}

void CGraphDB::getHash()
{
	vector<CStar>::iterator _it;
	for (int i=0; m_vecStars.size()>i; i++){
		vector<CStar> &_graph=m_vecStars[i].m_vecStars;
		vector<int> _graphHash(_graph.size());
		for (int j=0; _graph.size()>j; j++){
			_it=lower_bound(m_vecAllStar.begin(), m_vecAllStar.end(), _graph[j]);
			_graphHash[j]=int(_it-m_vecAllStar.begin());
		}
		m_vecStarsHash.push_back(_graphHash);
	}
}

void CGraphDB::print(FILE *pf)
{
	fprintf(pf,"Database information...\n");
	fprintf(pf, "Max Degree: %d\n", m_nDeg);

	for (int i = 0; i < m_vecAllStar.size(); i++){
		m_vecAllStar[i].print(pf);
	}

	for (int i = 0; i < m_vecStarsHash.size(); i++){
		fprintf(pf, "\nGraph [%d]...\n", i);
		for (int j = 0; j < m_vecStarsHash[i].size(); j++)
			fprintf(pf,"%d ", m_vecStarsHash[i][j]);
	}
	
	for (int i=0; i < m_vecStars.size(); i++){
		fprintf(pf,"\nGraph [%d]...\n",i);
		fprintf(pf, "Degree: %d Edges: %d\n", m_vecStars[i].m_maxDeg, m_vecStars[i].m_nEdges);	
		m_vecStars[i].print(pf);
	}

	fprintf(pf, "\nUpper-level Index...\n");	
	for (int i = 0; i < m_graphList.size(); i++){
		fprintf(pf, "\nList Number: %d\nSize List: \n", i);	
		CList &_list = m_graphList[i];
		vector<int> &_vecSize = _list.m_vecSize;
		for (int j = 0; j < _vecSize.size(); j++){
			fprintf(pf, "%d ", _vecSize[j]);	
		}
		fprintf(pf, "\nSize Index:\n");	
		vector<int> &_vecindex = _list.m_vecIndex;
		for (int j = 0; j < _vecindex.size(); j++){
			fprintf(pf, "%d ", _vecindex[j]);	
		}
		fprintf(pf, "\nList content: \n");
		_list.print(pf);
	}

	fprintf(pf, "\nLower-level Index...\n");	
	for (int i = 0; i < m_starList.size(); i++){
		fprintf(pf, "\nList Number: %d\nSize List: \n", i);	
		CList &_list = m_starList[i];
		vector<int> &_vecSize = _list.m_vecSize;
		for (int j = 0; j < _vecSize.size(); j++){
			fprintf(pf, "%d ", _vecSize[j]);	
		}
		fprintf(pf, "\nSize Index:\n");	
		vector<int> &_vecindex = _list.m_vecIndex;
		for (int j = 0; j < _vecindex.size(); j++){
			fprintf(pf, "%d ", _vecindex[j]);	
		}
		fprintf(pf, "\nList content: \n");	
		_list.print(pf);
	}

	fprintf(pf, "\nLower-level Size List...\n");
	for (int i = 0; i < m_starSizeList.size(); i++){
		fprintf(pf, "%d,%d ", m_starSizeList[i].m_nId, m_starSizeList[i].m_nInf);
	}
	fprintf(pf, "\n");

}

void CGraphDB::save(const char *fileName)
{
	FILE *pFile;
 
	if(!(pFile = fopen(fileName, "wb"))){
		fprintf(stderr, "Error open index binary file!\n");
		exit(-1);
	}

	// Write out star information
	int _dataNum = m_vecAllStar.size();
	fwrite(&_dataNum, sizeof(int), 1, pFile);
	int _leafNum;
	for (int i = 0; i < _dataNum; i++){
		CStar &_star=m_vecAllStar[i];
		fwrite(&_star.m_nRootID, sizeof(int), 1, pFile);
		fwrite(&_star.m_nRoot, sizeof(int), 1, pFile);
		_leafNum = _star.m_vecLeaf.size();
		fwrite(&_leafNum, sizeof(int), 1, pFile);
		if (_leafNum > 0){
			fwrite(&_star.m_vecLeaf[0], sizeof(int), _leafNum, pFile);
			fwrite(&_star.m_vecLabels[0], sizeof(int), _leafNum, pFile);
		}
	}

	// Write out graph information
	_dataNum = m_vecStarsHash.size();
	fwrite(&_dataNum, sizeof(int), 1, pFile);

	// Write out inverted index information
	_dataNum = m_graphList.size();
	fwrite(&_dataNum, sizeof(int), 1, pFile);
	for (int i = 0; i < _dataNum; i++){
		vector<CEntry> &_entry = m_graphList[i].m_vecGraph;
		_leafNum = _entry.size();
		fwrite(&_leafNum, sizeof(int), 1, pFile);
		for(int j = 0; j < _leafNum; j++){
			fwrite(&_entry[j].m_nId, sizeof(size_t), 1, pFile);
			fwrite(&_entry[j].m_nInf, sizeof(size_t), 1, pFile);
			fwrite(&_entry[j].m_nSize, sizeof(size_t), 1, pFile);
		}
	}

	_dataNum = m_starList.size();
	fwrite(&_dataNum, sizeof(int), 1, pFile);
	for (int i = 0; i < _dataNum; i++){
		vector<CEntry> &_entry = m_starList[i].m_vecGraph;
		_leafNum = _entry.size();
		fwrite(&_leafNum, sizeof(int), 1, pFile);
		for(int j = 0; j < _leafNum; j++){
			fwrite(&_entry[j].m_nId, sizeof(size_t), 1, pFile);
			fwrite(&_entry[j].m_nInf, sizeof(size_t), 1, pFile);
			fwrite(&_entry[j].m_nSize, sizeof(size_t), 1, pFile);
		}
	}

	_dataNum = m_starSizeList.size();
	fwrite(&_dataNum, sizeof(int), 1, pFile);
	for (int i = 0; i < _dataNum; i++){
		fwrite(&m_starSizeList[i].m_nId, sizeof(size_t), 1, pFile);
		fwrite(&m_starSizeList[i].m_nInf, sizeof(size_t), 1, pFile);
	}

	fclose(pFile);
}

void CGraphDB::load(const char *fileName)
{
	FILE *pFile;
 
	if(!(pFile = fopen(fileName, "rb"))){
		fprintf(stderr, "Error load the index!\n");
		exit(-1);
	}

	fprintf(stderr, "Load the index...\n");

	// Load star information
	int _dataNum, _leafNum;
	fread(&_dataNum, sizeof(int), 1, pFile);
	CStar _star;
	for (int i = 0; i < _dataNum; i++){
		fread(&_star.m_nRootID, sizeof(int), 1, pFile);
		fread(&_star.m_nRoot, sizeof(int), 1, pFile);
		fread(&_leafNum, sizeof(int), 1, pFile);
		if (_leafNum > 0){
			_star.m_vecLeaf.resize(_leafNum);
			_star.m_vecLabels.resize(_leafNum);
			fread(&_star.m_vecLeaf[0], sizeof(int), _leafNum, pFile);
			fread(&_star.m_vecLabels[0], sizeof(int), _leafNum, pFile);
		}

		m_vecAllStar.push_back(_star);
	}

	fprintf(stderr, "Finish loading 10%\n");

	// Load graph information
	fread(&_dataNum, sizeof(int), 1, pFile);
	m_vecStarsHash.resize(_dataNum);
	m_vecStars.resize(_dataNum);

	// Load inverted index information
	fread(&_dataNum, sizeof(int), 1, pFile);
	m_graphList.resize(_dataNum);
	size_t _graphID;
	size_t _size, _sizeTmp;
	size_t _max;
	for (int i = 0; i < _dataNum; i++){
		fread(&_leafNum, sizeof(int), 1, pFile);
		m_graphList[i].m_vecGraph.resize(_leafNum);
		
		_sizeTmp = 0;
		_max = m_vecAllStar[i].m_vecLeaf.size();
		for(int j = 0; j < _leafNum; j++){
			fread(&_graphID, sizeof(size_t), 1, pFile);
			m_graphList[i].m_vecGraph[j].m_nId = _graphID;
			fread(&m_graphList[i].m_vecGraph[j].m_nInf, sizeof(size_t), 1, pFile);
			fread(&_size, sizeof(size_t), 1, pFile);
			m_graphList[i].m_vecGraph[j].m_nSize = _size;

			if (j == 0 || _size != _sizeTmp) {
				_sizeTmp = _size;
				m_graphList[i].m_vecSize.push_back(_size);
				m_graphList[i].m_vecIndex.push_back(j);
			}

			for (int k = 0; k < m_graphList[i].m_vecGraph[j].m_nInf; k++) {
				m_vecStarsHash[_graphID].push_back(i);
				m_vecStars[_graphID].m_vecStars.push_back(m_vecAllStar[i]);
			}

			m_vecStars[_graphID].m_nEdges += _max;
			if (_max > m_vecStars[_graphID].m_maxDeg)
				m_vecStars[_graphID].m_maxDeg = _max;
		}

		if (_leafNum > 0)
			m_graphList[i].m_vecIndex.push_back(_leafNum);

		if (m_nDeg < _max)
			m_nDeg = _max;
	}

	m_nDeg = m_nDeg > 3 ? m_nDeg + 1 : 4;

	fprintf(stderr, "Finish loading 50%\n");

	fread(&_dataNum, sizeof(int), 1, pFile);
	m_starList.resize(_dataNum);
	m_laNum = _dataNum - 1;
	for (int i = 0; i < _dataNum; i++){
		fread(&_leafNum, sizeof(int), 1, pFile);
		m_starList[i].m_vecGraph.resize(_leafNum);

		_sizeTmp = 0;
		for(int j = 0; j < _leafNum; j++){
			fread(&m_starList[i].m_vecGraph[j].m_nId, sizeof(size_t), 1, pFile);
			fread(&m_starList[i].m_vecGraph[j].m_nInf, sizeof(size_t), 1, pFile);
			fread(&_size, sizeof(size_t), 1, pFile);
			m_starList[i].m_vecGraph[j].m_nSize = _size;

			if (j == 0 || _size != _sizeTmp) {
				_sizeTmp = _size;
				m_starList[i].m_vecSize.push_back(_size);
				m_starList[i].m_vecIndex.push_back(j);
			}
		}

		if (_leafNum > 0)
			m_starList[i].m_vecIndex.push_back(_leafNum);
	}

	fread(&_dataNum, sizeof(int), 1, pFile);
	m_starSizeList.resize(_dataNum);
	for (int i = 0; i < _dataNum; i++){
		fread(&m_starSizeList[i].m_nId, sizeof(size_t), 1, pFile);
		fread(&m_starSizeList[i].m_nInf, sizeof(size_t), 1, pFile);
	}


	fclose(pFile);

	fprintf(stderr, "Load the index success!\n");
}

void CGraphDB::estByStar(CQuery &query,size_t index, int &mdist, int &lower, int &upper, int &refine)
{
	if (m_vecStars.size()<=index){
		fprintf(stderr,"Error - index out of bound: %d > %d\n",index, m_vecStars.size());

		exit(-1);
	}
	
	CStarGraph &_starGraph=query.m_starGraph;
	CMapGraph _map;

	CStarGraph::getLowerBound(_starGraph,m_vecStars[index],mdist,lower, &_map);
		
	//get upper bound
	CMatrixGraph _matrixA=CMatrixGraph::getMatrix(m_vecStars[index],_map);
	CMatrixGraph _matrixB=query.m_matrix.refMatrix(_map.m_vecMap.size());
	upper=CMatrixGraph::diffElements(_matrixA,_matrixB);

	//refine the upper bound
	refine=CMatrixGraph::refineDiff(_matrixA,_matrixB,_map,upper);
}

void CGraphDB::estByStar(CQuery &query,size_t index, int &mdist, int &lower, int &upper)
{
	if (m_vecStars.size()<=index){
		fprintf(stderr,"Error - index out of bound: %d > %d\n",index, m_vecStars.size());

		exit(-1);
	}
	
	CStarGraph &_starGraph=query.m_starGraph;
	CMapGraph _map;

	CStarGraph::getLowerBound(_starGraph,m_vecStars[index],mdist,lower, &_map);
		
	//get upper bound
	upper=CMatrixGraph::getDiff(m_vecStars[index],query.m_matrix,_map);
}


int CGraphDB::distExact(CQuery &query, size_t index)
{
	if (m_vecStars.size()<=index){
		fprintf(stderr,"Error - index out of bound: %d > %d\n",index, m_vecStars.size());

		exit(-1);
	}
	
	//get the maximal vertex number
	size_t _max=m_vecStars[index].m_vecStars.size();
	int _rs=0x7FFF,_tmp;

	if(query.m_starGraph.m_vecStars.size()>_max)
		_max=query.m_starGraph.m_vecStars.size();

	CPermutation _perm;
	CMapGraph _map,_rsMap;

//	if (_max>10)
//		return -1;

	_perm.setBase(_max);

	while (_perm.getNext(_map.m_vecMap)){
		_tmp=CMatrixGraph::getDiff(m_vecStars[index],query.m_matrix,_map);	
		if (_tmp<_rs){
			_rs=_tmp;
			_rsMap=_map;
		}
		if (_rs==0)
			break;
	}
	return _rs;
}

void CGraphDB::merge(vector<CEntry> &oldvec,vector<int> &points,vector<CEntry> &newvec)
{
	if (oldvec.size()<=0 || points.size()<=0)
		return;

	int *_oldpos = new int[points.size()];
	for (int i=0;points.size()>i;i++)
		_oldpos[i]=points[i];

	int _min=0;
	int _pos=0;
	bool _end;
	while(1){
		_min=0;
		_end=true;
		for (int i=0;points.size()-1>i;i++){
			if (points[i] == _oldpos[i+1])
				continue;
			_end=false;
			if (_min<oldvec[points[i]].m_nInf){
				_min=oldvec[points[i]].m_nInf;
				_pos=i;
			}
		}
		if (_end)
			break;
		newvec.push_back(oldvec[points[_pos]]);
		points[_pos]++;
	}

	delete [] _oldpos;
}

// get star score sorted lists; temply no bound to star size
void CGraphDB::getScoreLists(vector<vector<CEntry> > &scoreListLow,
							 vector<vector<CEntry> > &scoreListHigh,CStar &qstar)
{
	int _label=0;
	int _count=0;
	int _qSize=qstar.m_vecLabels.size();

	vector<CEntry> _vecListLow;
	vector<CEntry> _vecListHigh;
	vector<int>::iterator _itLow;

	for (int i=0;_qSize>i;i++){
		if (qstar.m_vecLabels[i]!=_label){
			_label=qstar.m_vecLabels[i];
			if (_label > (int)m_laNum)
				break;
			if (i!=0){
				qstar.m_labelCount.push_back(_count);
				_count=0; // error at first, now correct
			}

			vector<int> &_vecSize=m_starList[_label].m_vecSize;
			vector<int> &_vecIndex=m_starList[_label].m_vecIndex;

			_itLow=upper_bound(_vecSize.begin(),_vecSize.end(),_qSize);
			_itLow=_vecIndex.begin()+(_itLow-_vecSize.begin());
			vector<int> _vecPosLow(_vecIndex.begin(),_itLow+1);
			vector<int> _vecPosHigh(_itLow,_vecIndex.end());
			_vecListLow.clear();
			_vecListHigh.clear();
			merge(m_starList[_label].m_vecGraph,_vecPosLow,_vecListLow);
			merge(m_starList[_label].m_vecGraph,_vecPosHigh,_vecListHigh);
			scoreListLow.push_back(_vecListLow);
			scoreListHigh.push_back(_vecListHigh);
		}

		_count++;
	}
	qstar.m_labelCount.push_back(_count); // record star label counts

	vector<CEntry>::iterator _itLow1=upper_bound(m_starSizeList.begin(),m_starSizeList.end(),CEntry(0,_qSize));
	vector<CEntry> _vecSizeLow(vector<CEntry>::reverse_iterator(_itLow1),vector<CEntry>::reverse_iterator(m_starSizeList.begin()));
	vector<CEntry> _vecSizeHigh(_itLow1,m_starSizeList.end());
	scoreListLow.push_back(_vecSizeLow);
	scoreListHigh.push_back(_vecSizeHigh);
}

// with size filtering
void CGraphDB::getScoreLists(vector<vector<list_entry> > &scoreListLow,
							vector<vector<list_entry> > &scoreListHigh,
							vector<int> &countLow,vector<int> &countHigh,CQuery &query,int relax)
{// use 1+2l to bound graph with lower size; for CA
	vector<CStar> &_qStar=query.m_starGraph.m_vecStars;
	vector<int> &_qIndex=query.m_index;
	vector<int> &_qMax=query.m_qMax;
	vector<char> _flag(m_vecStars.size(),'u');
	vector<list_entry> _graphListLow;
	vector<list_entry> _graphListHigh;

	vector<int>::iterator _itLow;
	for (int i=0;_qIndex.size()-1>i;i++){
		_graphListLow.clear();
		_graphListHigh.clear();

		vector<list_pair> &_vecSimiStar=query.m_topk[i];
		vector<list_pair>::reverse_iterator rit;
		for(rit=_vecSimiStar.rbegin();rit<_vecSimiStar.rend();rit++){
			int _sid=(*rit).m_sid;
			int _dist=(*rit).m_dist;

			vector<int> &_vecSize=m_graphList[_sid].m_vecSize;
			vector<int> &_vecIndex=m_graphList[_sid].m_vecIndex;
			_itLow=upper_bound(_vecSize.begin(),_vecSize.end(),_qStar.size());
			_itLow=_vecIndex.begin()+(_itLow-_vecSize.begin());

			vector<CEntry> &_oldList=m_graphList[_sid].m_vecGraph;
			if (_dist<=_qMax[i]){
				for (int j=0;*_itLow>j;j++){
					int _gid=_oldList[j].m_nId;
					if (_flag[_gid] == 'u'){

						// add size filtering here
						int _sizeDiff=CStarGraph::getSizeDiff(m_vecStars[_gid],query.m_starGraph);
						if (_sizeDiff <= relax){
							countLow.push_back(_gid);
							_graphListLow.push_back(list_entry(_gid,_oldList[j].m_nInf,_vecSimiStar.rend()-rit-1));
							_flag[_gid]='t';
						}
						else{ // filter out
							m_graphCandidate[_gid]='f';
							_flag[_gid]='f';
						}
						continue;
					}
					if (_flag[_gid] == 't')
						_graphListLow.push_back(list_entry(_gid,_oldList[j].m_nInf,_vecSimiStar.rend()-rit-1));
				}
			}

			for (int j=*_itLow;_oldList.size()>j;j++){
				int _gid=_oldList[j].m_nId;
				if (_flag[_gid] == 'u'){

					// add size filtering here
					int _sizeDiff=CStarGraph::getSizeDiff(m_vecStars[_gid],query.m_starGraph);
					if (_sizeDiff <= relax){
						countHigh.push_back(_gid);
						_graphListHigh.push_back(list_entry(_gid,_oldList[j].m_nInf,_vecSimiStar.rend()-rit-1));
						_flag[_gid]='t';
					}
					else{ // filter out
						m_graphCandidate[_gid]='f';
						_flag[_gid]='f';
					}
					continue;
				}
				if (_flag[_gid] == 't')
					_graphListHigh.push_back(list_entry(_gid,_oldList[j].m_nInf,_vecSimiStar.rend()-rit-1));
			}
		}
		scoreListLow.push_back(_graphListLow);
		scoreListHigh.push_back(_graphListHigh);
	}
}

// top-k star search
void CGraphDB::getCandidate(priority_queue<list_pair> &kStar,vector<vector<CEntry> > &scoreList,CStar &qstar,bool flag)
{// use TA with round robin
	const int _candidateCount=scoreList[scoreList.size()-1].size();
	const int _qsize=scoreList.size();

	vector<bool> _visit(m_vecAllStar.size(),false);
	vector<int> _value(_qsize,0);

	int _count=0;
	int _pos=0;
	bool _flag=false;
	bool _end=false;
	while(1){
		_end=false;
		int i=0;
		for (;scoreList.size()>i;i++){
			if (_pos >= scoreList[i].size())
				continue;
			_end=true;

			int _sid=scoreList[i][_pos].m_nId;
			if (_visit[_sid])
				continue;

			_visit[_sid]=true;
			_count++;

			if (_value[i]!=scoreList[i][_pos].m_nInf){
				_flag=true;// new value is detected; recompute threshold
				_value[i]=scoreList[i][_pos].m_nInf;
			}

			CStar &_gstar=m_vecAllStar[_sid];
			size_t _dist=CStar::getDist(_gstar,qstar);

			if (kStar.size()<m_topk){
				kStar.push(list_pair(_sid,_dist));
				continue;
			}

			if (_dist<kStar.top().m_dist){
				kStar.pop();
				kStar.push(list_pair(_sid,_dist));
			}

			if (_count == _candidateCount){
				_end=false;
				break;
			}

			if (_pos>0 && _flag){
				int _threshold=0;
				for (int j=0;_qsize-1>j;j++)
					_threshold+=min(_value[j],qstar.m_labelCount[j]);
				if (flag)
					_threshold-=2*qstar.m_vecLabels.size()-_value[_qsize-1]; // for low lists
				else
					_threshold=2*_value[_qsize-1]-_threshold-qstar.m_vecLabels.size(); // for high lists
				if (_threshold>=kStar.top().m_dist){
					_end=false;
					break;
				}

				_flag = false;
			}
		}
		if (!_end) // all candidate stars are visited or threshold halt
			break;
		_pos++;
	}

//#define DEBUG
#ifdef DEBUG
		if (_count == _candidateCount)
			fprintf(stdout,"All lists have been accessed!\n");
		while (!kStar.empty()){
			fprintf(stdout,"%d,%d ", kStar.top().m_sid,kStar.top().m_dist);
			kStar.pop();
		}
		fprintf(stdout,"\n");
#endif
}

// CA-based range query with round robin
int CGraphDB::getCandidate(vector<vector<list_entry> > &scoreList,
						   vector<int> &candidate,CQuery &query,
						   int relax,bool low)
{
	vector<int> &_qIndex=query.m_index;
	vector<int> &_qMax=query.m_qMax;
	vector<vector<list_pair> > &_qtopk=query.m_topk;
	int _countGraph=0; // count of final candidates
	int _qsize=scoreList.size();
	int _gnum=m_vecStars.size();
	vector< map<int,int> > _sidSet(_gnum); // store the intermediate components
	vector< map<int,int> > _sdistSet(_gnum); // star seen without duplicates

	vector<int> _tmp(_qsize, -1);
	vector< vector<int> > _seen(_gnum, _tmp);
	vector<bool> _seenID(_gnum, false);
	vector<bool> _neverseen(_gnum, false);
	vector<int> _curScoreLow(_gnum, 0);
	vector<int> _valueUp(_gnum);
	vector<int> _value(_qsize);

	for (int i=0;_qsize>i;i++){
		if (scoreList[i].size()>0)
			_value[i]=_qtopk[i][scoreList[i].front().m_diff].m_dist;
		else if (low)
			_value[i]=min<int>(_qtopk[i].front().m_dist,_qMax[i]);
		else
			_value[i]=_qtopk[i].front().m_dist;
	}
	for (int i=0;_gnum>i;i++)
		_valueUp[i]=2*query.m_maxDeg[i]-1;

	int _count=0;
	int _pos=0;
	int _threshold=0;
	::gl_flag=false; // for treshold halt conditions
	bool _flag=false; // for threshold calculation
	bool _end=false;
	while (1){
		_end=false;
		int i=0;
		for (;_qsize>i;i++){	
			if (_pos >= scoreList[i].size())
				continue;

			_end=true;
			int _gid=scoreList[i][_pos].m_gid;
			if (m_graphCandidate[_gid] == 'f' || m_graphCandidate[_gid] == 's' || m_graphCandidate[_gid] == 't')
				continue;

			_flag=false;
			list_pair &_tmp=_qtopk[i][scoreList[i][_pos].m_diff];
			if (_value[i]!=_tmp.m_dist){
				_value[i]=_tmp.m_dist;
				_flag=true;
			}

			_seenID[_gid]=true;
			if (_seen[_gid][i] == -1){
				_curScoreLow[_gid]+=_value[i];
				_neverseen[_gid]=true;
			}
			_seen[_gid][i]=_value[i];
			_sidSet[_gid].insert(pair<int,int>(_tmp.m_sid,scoreList[i][_pos].m_count));
			_sdistSet[_gid].insert(pair<int,int>(_tmp.m_sid,_value[i]));

			// check the threshold value
			if (_flag){
				_threshold=0;
				for (int j=0;_qsize>j;j++)
					_threshold+=(_value[j])*(_qIndex[j+1]-_qIndex[j]);
				int _relax;
				if (query.m_nDeg == 0)
					_relax=relax*m_nDeg;
				else _relax=relax*query.m_nDeg;
				if (_threshold>_relax){ // the threshold bound is too loose, no use before all graphs are seen
					::gl_flag=true;
					_end=false;
					break;
				}
			}
		}
		_pos++;
		if (_pos%m_depth == 0 || !_end){
			for (int j=0;candidate.size()>j;j++){
				if (_count == candidate.size()){
					_end=false;
					break;
				}

				int _tmpID=candidate[j];
				if (_seenID[_tmpID]){
					_seenID[_tmpID]=false;
					m_graphCandidate[_tmpID]='f';
					_count++;

					// use score lower and upper bounds
					int _sl=_curScoreLow[_tmpID];
					int _su=0;
					int _seenNum=0;
					for (size_t k=0;_qsize>k;k++){
						if (_seen[_tmpID][k] == -1)
							_sl += _value[k]*(_qIndex[k+1]-_qIndex[k]);
						else
							_seenNum++;
					}

					map<int,int>::iterator _mapIt;
					for (_mapIt=_sdistSet[_tmpID].begin();_mapIt!=_sdistSet[_tmpID].end();_mapIt++)
						_su+=_mapIt->second;

					CStarGraph &_gStar=m_vecStars[_tmpID];
					size_t _gsize=_gStar.m_vecStars.size();
					if(low)
						_su += _valueUp[_tmpID]*(query.m_starGraph.m_vecStars.size()-_sdistSet[_tmpID].size());
					else{
						int _min=min<int>(_seenNum,_sdistSet[_tmpID].size());
						_min=min<int>(_min,query.m_starGraph.m_vecStars.size());
						_su += _valueUp[_tmpID]*(_gsize-_min);
					}
					
					int _relax=query.m_maxDeg[_tmpID]*relax;
					if (_sl > _relax)
						continue;
					if (_su < _relax){
						m_accessNum++;
						int _md,_lb,_ub;
						CMapGraph _map;
						CStarGraph::getLowerBound(query.m_starGraph,m_vecStars[_tmpID],_md,_lb,&_map);
						_ub=CMatrixGraph::getDiff(m_vecStars[_tmpID],query.m_matrix,_map);
						if (_ub <= relax)
							m_graphCandidate[_tmpID]='t';
						else{
							_countGraph++;
							m_graphCandidate[_tmpID]='s';
						}
						continue;
					}

					// use mapping distance with incremental Hungarian
					vector<int> _aHash;
					map<int,int>::iterator it=_sidSet[_tmpID].begin();
					for (;it!=_sidSet[_tmpID].end();it++)
						for (int k=0;k<it->second;k++)
							_aHash.push_back(it->first);

					if (_aHash.size() == _gsize){
						m_accessNum++;
						int _md,_lb;
						CMapGraph _map;
						CStarGraph::getLowerBound(query.m_starGraph,_gStar,_md,_lb,&_map);
						if (_lb <= relax){
							//get upper bound
							int _ub=CMatrixGraph::getDiff(m_vecStars[_tmpID],query.m_matrix,_map);
							if (_ub <= relax)
								m_graphCandidate[_tmpID]='t';
							else{
								_countGraph++;
								m_graphCandidate[_tmpID]='s';
							}
						}
						continue;
					}

					Hungarian _h;
					Matrix<double> _m;
					vector<CStar> _a;
					sort(_aHash.begin(),_aHash.end());
					for (int k=0; _aHash.size()>k; k++)
						_a.push_back(m_vecAllStar[_aHash[k]]);
					int _md=CStarGraph::getMappingDist(_a,query.m_starGraph.m_vecStars,_valueUp[_tmpID],_m,_h);
					if (_md <= _relax){
						m_accessNum++;

						vector<int> _diffHash(_gsize-_a.size());
						vector<int> _mapPos;
						set_difference(m_vecStarsHash[_tmpID].begin(), m_vecStarsHash[_tmpID].end(), _aHash.begin(), _aHash.end(), _diffHash.begin(),_mapPos);
						vector<CStar> _diff;
						for (int k=0; _diffHash.size()>k; k++)
							_diff.push_back(m_vecAllStar[_diffHash[k]]);

						CMapGraph _map; // wrong map here for positions of vertices having been changed
						_md=CStarGraph::getMappingDist(_diff,_a,query.m_starGraph.m_vecStars,_valueUp[_tmpID],_m,_h,_mapPos,&_map);
						if (_md <= _relax){
							//get upper bound
							int _ub=CMatrixGraph::getDiff(m_vecStars[_tmpID],query.m_matrix,_map);

							if (_ub <= relax)
								m_graphCandidate[_tmpID]='t';
							else{
								_countGraph++;
								m_graphCandidate[_tmpID]='s';
							}
						}
					}
				}
			}
		}
		if (::gl_flag){
			for (int j=0;candidate.size()>j;j++)
				if (!_neverseen[candidate[j]])
					m_graphCandidate[candidate[j]]='f';
			break;
		}
		if (!_end)
			break;
	}

	return _countGraph;
}

void CGraphDB::findSimilarStars(vector<list_pair> &simiStars,CStar &qstar)
{
	vector<vector<CEntry> > _scoreListLow;  // score lists with size<=q
	vector<vector<CEntry> > _scoreListHigh; // score lists with size>q
	getScoreLists(_scoreListLow,_scoreListHigh,qstar);

	priority_queue<list_pair> _kStar;
	getCandidate(_kStar,_scoreListLow,qstar,true);
	getCandidate(_kStar,_scoreListHigh,qstar,false);

	while (!_kStar.empty()){
		simiStars.push_back(_kStar.top());
		_kStar.pop();
	}
}

// for graph search
int CGraphDB::rangeQuery(CQuery &query,int relax, bool altype)
{
	m_accessNum=0;
	int _candidateNum=0;
	m_graphCandidate.assign(m_vecStars.size(),'c');

	vector<int> _countLow, _countHigh;
	bool _flagLow=false,_flagHigh=false;
	vector<vector<list_entry> > _scoreListLow, _scoreListHigh;
	getScoreLists(_scoreListLow,_scoreListHigh,_countLow,_countHigh,query,relax);
	_candidateNum+=getCandidate(_scoreListLow,_countLow,query,relax,true);
	_flagLow=::gl_flag;
	_candidateNum+=getCandidate(_scoreListHigh,_countHigh,query,relax,false);
	_flagHigh=::gl_flag;

	if (!_flagLow || !_flagHigh){
		for (int i=0;m_graphCandidate.size()>i;i++){
			if (m_graphCandidate[i] == 'c'){
				if (_flagLow && m_vecStars[i].m_vecStars.size()<=query.m_starGraph.m_vecStars.size())
					continue;
				if (_flagHigh && m_vecStars[i].m_vecStars.size()>query.m_starGraph.m_vecStars.size())
					continue;

				int _sizeDiff=CStarGraph::getSizeDiff(m_vecStars[i],query.m_starGraph);
				if (_sizeDiff > relax)
					continue;

				m_accessNum++;
				int _md,_lb;
				CMapGraph _map;
				CStarGraph::getLowerBound(query.m_starGraph,m_vecStars[i],_md,_lb,&_map);
				if (_lb <= relax){
					//get upper bound
					int _ub=CMatrixGraph::getDiff(m_vecStars[i],query.m_matrix,_map);
					if (_ub <= relax)
						continue;

					_candidateNum++;
					continue;
				}
			}
		}
	}
	
	return _candidateNum;
}

