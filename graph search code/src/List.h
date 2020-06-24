#ifndef _LIST_H_
#define _LIST_H_

#include <vector>

#include "Star.h"

namespace NUS_EMMA_EDITDISTANCE{

	struct list_pair{
		int m_sid;
		int m_dist;

		list_pair(int sid,int dist){m_sid=sid;m_dist=dist;};

		bool operator< (const list_pair &other) const
		{
			return (m_dist<other.m_dist);
		};
	};

	struct list_entry{
		int m_gid;
		int m_count;
		int m_diff;

		list_entry(int gid,int count,int diff){m_gid=gid;m_count=count;m_diff=diff;};
	};

	struct queue_entry{
		int m_gid;
		int m_md;
		int m_lb;
		int m_ub;

		queue_entry(int gid,int md,int lower,int upper){m_gid=gid;m_md=md;m_lb=lower;m_ub=upper;};

		bool operator< (const queue_entry &other) const
		{
			return (m_ub<other.m_ub);
		};
	};

	class CEntry 
	{
	public:
		size_t m_nId;	 // star id or graph id
		size_t m_nInf;   // star count, score, or size
		size_t m_nSize;  // size information

		CEntry(){};
		CEntry(size_t id, size_t info){ m_nId=id; m_nInf=info;};
		CEntry(size_t id, size_t info, size_t size){ m_nId=id; m_nInf=info;m_nSize=size;};

		bool operator< (const CEntry &other) const;
	};

	class CList
	{
	public:
		CList(){m_index=0;};
		std::vector<CEntry> m_vecGraph;

	public:
		std::vector<int> m_vecSize;
		std::vector<int> m_vecIndex; // note that the end of index must be added for merging star inverted lists

	private:
		int m_index;

	public:
		bool hasMore();
		CEntry getNext();

	public:
		void normalize(); // sort the values for the list		
		void clear();     // clear inverted list
		void print(FILE* pf);
	};

};

#endif

