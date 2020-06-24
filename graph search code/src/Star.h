#ifndef _STAR_H_
#define _STAR_H_

#include <vector>
#include <fstream>

#include "Matrix.h"
#include "Hungarian.h"
#include "MapGraph.h"

#define VERTEXNUM 128

using namespace std;

namespace NUS_EMMA_EDITDISTANCE{
	class CStar 
	{
	public:
		int m_nRootID;  //root ID
		int m_nRoot;	//root label
		vector<int> m_vecLeaf;		//leaves, from others
		vector<int> m_vecLabels;    //labels

		public:
		vector<int> m_labelCount;  // label counts

		CStar();
		CStar(int id, int root){m_nRootID=id;m_nRoot=root;};
		~CStar();

	public:
		bool operator< (const CStar &other) const;
		bool operator== (const CStar &other) const;

		void normalize();
	
	private:
		//multiset intersection
		static size_t getDiff(const vector<int> &vecA,const vector<int> &vecB);

	public:
		static size_t getDist(const CStar &a, const CStar &b);

		void clear();
		void print(FILE* pf);
		size_t getElements();
	};

	class CStarGraph
	{
	public:
		CStarGraph(){m_maxDeg=0;m_nEdges=0;};
		size_t m_maxDeg;
		size_t m_nEdges;
		vector<CStar> m_vecStars;

	public:
		bool operator< (const CStarGraph &other) const;
		void print(FILE* pf);
		//sort the labels in each star
		void normalize();		
		//clear maxDeg and star vector
		void clear();

		void addStar(int id, int label);
		void addEdge(int sv, int ev);

	public:
		static int getSizeDiff(const CStarGraph &a, const CStarGraph &b);

		static void getSetDiff(vector<CStar> &a, vector<CStar> &b,
								vector<CStar> &diff);

		static int getComm(CStarGraph &a, CStarGraph &b);

		static void getCountBound(CStarGraph &a, CStarGraph &b, int &lower);

		static void getCountBound(CStarGraph &a, CStarGraph &b, vector<int> &aHash, vector<int> &bHash, int &lower);

		static int getMappingDist(vector<CStar> &a, vector<CStar> &b, int &maxDist,
									Matrix<double> &ma, Hungarian &h); // Incremental Hungarian Part

		static int getMappingDist(vector<CStar> &diff, vector<CStar> &a,
									vector<CStar> &b, int &maxDist,
									Matrix<double> &ma, Hungarian &h,
									vector<int> &pos, CMapGraph *map=0x00); // Incremental Hungarian Full

		static int getMappingDist(CStarGraph &a, CStarGraph &b, CMapGraph *map=0x00);

		static void getLowerBound(CStarGraph &a, CStarGraph &b,
									int &mdist, int &lower, CMapGraph *map=0x00);
	};
};

#endif
