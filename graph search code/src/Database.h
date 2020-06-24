#ifndef _DATABASE_H_
#define _DATABASE_H_

#include <fstream>
#include <vector>
#include <queue>
#include <map>

#include "Query.h"
#include "Star.h"
#include "List.h"

using namespace std;

namespace NUS_EMMA_EDITDISTANCE{

	class CGraphDB	
	{
	private:
		size_t m_laNum;			// the number of labels

	public:
		CGraphDB(void);

		int m_topk;				// k values default by 100 on stars
		int m_depth;			// h values default by 1000 on CA
		int m_accessNum;		// the number of Hungarian compuations
		int m_nDeg;

	public:
		vector<CStarGraph> m_vecStars;
		vector< vector<int> > m_vecStarsHash;  // hash each graph star representation into a number array
		vector<CStar> m_vecAllStar;            // all stars sorted increasingly

		// index structures
		vector<CList> m_graphList;	           // graph inverted lists (map to m_vecAllStar)
		vector<CList> m_starList;              // star inverted lists (now index with label number)
		vector<CEntry> m_starSizeList;         // star lists with increasing size

		// candidate set
		vector<char> m_graphCandidate;

	private:
		void merge(vector<CEntry> &oldvec,vector<int> &points,
					vector<CEntry> &newvec);	// merge star inverted lists

	public:
		void parseData(ifstream &fs);		
		void print(FILE* pf);
		void save(const char *fileName);
		void load(const char *fileName);		// Load function has problem now; cannot be used; further needs to be updated

	//build the two-level index structures
	public:
		void buildIndex();

		void getHash();
	
	//distance estimation functions
	public:
		//get the edit distance lower bound betweeen graph[index] and query by mapping distance
		void estByStar(CQuery &query,size_t index, int &mdist,int &lower,int &upper,int &refine);
		void estByStar(CQuery &query,size_t index, int &mdist,int &lower,int &upper);

		//get the exact edit distance
		int distExact(CQuery  &query, size_t index); 

	//online construct score lists
	public:
		void findSimilarStars(std::vector<list_pair> &simiStars,CStar &qstar);
		void getScoreLists(std::vector<std::vector<CEntry> > &scoreListLow,
							std::vector<std::vector<CEntry> > &scoreListHigh,
							CStar &qstar);
		void getScoreLists(std::vector<std::vector<list_entry> > &scoreListLow,
							std::vector<std::vector<list_entry> > &scoreListHigh,
							std::vector<int> &countLow,std::vector<int> &countHigh,
							CQuery &query,int relax);

	//searching methods
	public:
		// KNN for stars on TA
		void getCandidate(priority_queue<list_pair> &kStar,
						std::vector<std::vector<CEntry> > &scoreList,
						CStar &qstar,bool flag);
		// range query on CA
		int getCandidate(std::vector<std::vector<list_entry> > &scoreList,
							std::vector<int> &candidate,CQuery &query,int relax,bool low);

	// query function, return the number of candidate graphs
	public:
		int rangeQuery(CQuery &query,int relax, bool altype);
	};
}

#endif

