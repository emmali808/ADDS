#ifndef _HUNGARIAN_H_
#define _HUNGARIAN_H_

#include "Matrix.h"

#define INF 100000000        // just infinity

using namespace std;

class Hungarian
{
public:
	Hungarian(void);
	Hungarian(Matrix<double> &);
	~Hungarian(void);

public:
	void set(Matrix<double> &);
	double solve(void);
	double solve(Matrix<double> &);
	double incre_solve(Matrix<double> &); // given new parts

private:
	void init_labels(void);
	void init_labels(int, int);
	void update_labels(void);
	void add_to_tree(int, int);
	void augment(void);

private:
	void initAll(void);
	void freeAll(void);

private:
	Matrix<double> cost; // cost matrix
	int rows, columns;   // the row and column number before resize
	int n, max_match;    // n left nodes and n right nodes
	double *lx, *ly;     // labels of X and Y parts
	int *xy;             // xy[x] - vertex that is matched with x,
	int *yx;             // yx[y] - vertex that is matched with y
	bool *S, *T;         // sets S and T in algorithm
	double *slack;       // as in the algorithm description
	int *slackx;         // slackx[y] such a vertex, that
						 // l(slackx[y]) + l(y) - w(slackx[y],y) = slack[y]
	int *prev;           // array for memorizing alternating paths
};

#endif
