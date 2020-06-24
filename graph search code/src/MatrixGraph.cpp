#include "MatrixGraph.h"

#include "assert.h"

using namespace NUS_EMMA_EDITDISTANCE;

void CMatrixGraph::print(FILE* pf)
{
	unsigned int i=0,j=0;
	fprintf(pf,"Adjacency Matrix Information ---\nLabels:");	
	for (i=0;m_vecLabels.size()>i;i++)
		fprintf(pf,"%d,",m_vecLabels[i]);	
	fprintf(pf,"\nAdj Matrix ---\n");	

	for (i=0;m_vecLabels.size()>i;i++){
		for (j=0;m_vecLabels.size()>j;j++)
			fprintf(pf,"%d,",m_vvecMatrix[i][j]);	
		fprintf(pf,"\n");
	}
	fprintf(pf,"\n");
}

CMatrixGraph CMatrixGraph::getMatrix(const CStarGraph &graph,const CMapGraph &map)
{
	CMatrixGraph _matrix;
	size_t _cdt=map.m_vecMap.size();

	if (graph.m_vecStars.size()>_cdt){
		fprintf(stderr,"Error - the map cardinality exceeds graph's vertices!\n");
		exit(-1);
	}

	vector<int> _vec;
	_vec.resize(_cdt,0);

	_matrix.m_vvecMatrix.resize(_cdt,_vec);
	_matrix.m_vecLabels.resize(_cdt,0);
	unsigned int i=0,j=0,k=0;


	for (i=0;graph.m_vecStars.size()>i;i++) 
		_matrix.m_vecLabels[map.getMap(i)]=graph.m_vecStars[i].m_nRoot;

	for (i=0;graph.m_vecStars.size()>i;i++) {
		const vector<int> &_vec=graph.m_vecStars[i].m_vecLeaf;
		for (j=0;_vec.size()>j;j++) {
			_matrix.m_vvecMatrix[map.getMap(i)][map.getMap(_vec[j])]=1;	
		}
	}

	return _matrix;
}

class CMatrixGraph CMatrixGraph::refMatrix(size_t cdt) const
{
	class CMatrixGraph _matrix=*this;
	int _diff=cdt-m_vecLabels.size();

	if (_diff>0){
		_matrix.m_vecLabels.insert(_matrix.m_vecLabels.end(),_diff,0);	

		for (unsigned i=0;_matrix.m_vvecMatrix.size()>i;i++)
			_matrix.m_vvecMatrix[i].insert(_matrix.m_vvecMatrix[i].end(),_diff,0);

		vector<int> _vec;
		_vec.resize(cdt,0);
		_matrix.m_vvecMatrix.insert(_matrix.m_vvecMatrix.end(),_diff,_vec);
	}else if (_diff<0){
		fprintf(stderr,"Error - The matrix's cardinality is greater than the refined size!\n");
		exit(-1);
	}
	return _matrix;
}

// note that a is the data graph, and b is the query graph
int CMatrixGraph::getDiff(const CStarGraph &a,const CMatrixGraph &b,const CMapGraph &map)
{
	//refined the graphs with identical cardinality

	CMatrixGraph _matrixA=CMatrixGraph::getMatrix(a,map);	
	CMatrixGraph _matrixB=b.refMatrix(map.m_vecMap.size());

	return CMatrixGraph::diffElements(_matrixA,_matrixB);
}

// get refined upper bound
int CMatrixGraph::getDiff(const CStarGraph &a,const CMatrixGraph &b,const CMapGraph &map,int relax)
{
	//refined the graphs with identical cardinality

	CMatrixGraph _matrixA=CMatrixGraph::getMatrix(a,map);	
	CMatrixGraph _matrixB=b.refMatrix(map.m_vecMap.size());

	int _ub = CMatrixGraph::diffElements(_matrixA,_matrixB);
	if ( _ub > relax )
		return refineDiff(_matrixA,_matrixB,map,_ub);
	return _ub;
}

void CMatrixGraph::exchange(CMatrixGraph &a,int row, int column)
{
	int _tmp;	
	unsigned int i=0;	
	size_t _row=a.m_vecLabels.size();

	for (i=0;i<_row;i++){
		if (i!= row && i!=column){
			//row exchange
			_tmp=a.m_vvecMatrix[row][i];	
			a.m_vvecMatrix[row][i]=a.m_vvecMatrix[column][i];
			a.m_vvecMatrix[column][i]=_tmp;

			//column exchange
			_tmp=a.m_vvecMatrix[i][row];	
			a.m_vvecMatrix[i][row]=a.m_vvecMatrix[i][column];
			a.m_vvecMatrix[i][column]=_tmp;
		}
	}

	_tmp=a.m_vvecMatrix[row][row];
	a.m_vvecMatrix[row][row]=a.m_vvecMatrix[column][column];
	a.m_vvecMatrix[column][column]=_tmp;
}

int CMatrixGraph::refineDiff(const CMatrixGraph &a,const CMatrixGraph &b,const CMapGraph &map,int est)
{

	int _localmin=est;
	int _dist=0,_min=0;
	size_t _num=map.m_vecMap.size();
	CMatrixGraph _matrixA=a,_matrixB=b;

	do {
		_min=_localmin;	
		_localmin=0x7FFF;
		for (unsigned int i=0;i<_num;i++)
			for (unsigned int j=i+1;j<_num;j++){
				exchange(_matrixB,i,j);		
				_dist=CMatrixGraph::diffElements(_matrixA,_matrixB);
				if (_dist<_localmin) _localmin=_dist;
			}
	}while (_localmin<_min);

	return _min;
}

// the two matrix must have the identical vertex number.
int CMatrixGraph::diffElements(const CMatrixGraph &a,const CMatrixGraph &b)
{
	int _rs=0;
	size_t _cdt=a.m_vecLabels.size();
	if (_cdt!=b.m_vecLabels.size()){
		fprintf(stderr,"Error - Two comparing matrices possess different elements!\n");
		exit(-1);
	}

	unsigned int i=0,j=0;

	for (i=0;_cdt>i;i++){
		if (a.m_vecLabels[i]!=b.m_vecLabels[i])
			_rs++;
	}

	unsigned int _tmp=0;
	for (i=0;_cdt>i;i++){
		for (j=0;_cdt>j;j++)
		{
			_tmp+=abs(a.m_vvecMatrix[i][j]-b.m_vvecMatrix[i][j]);	
		}
	}

	//_tmp should be divided by 2 if the graph is undirected
	_rs+=_tmp/2;
	return _rs;
}
