#include <stdio.h>
#include <stdlib.h>


int parms[][5] = {{7, 0, 1, -1, -1}, {27, 2, 9, 10, 11}, {7, 3, 4, -1, -1}, {17, 5, 6, 7, 15},
		{16, 8, 17, -1, -1}, {8, 12, 13, 14, -1}, {14, 16, 25, 26, -1}, {10, 18, 19, 28, -1},
		{13, 20, 21, -1, -1}, {19, 22, 23, 32, -1}, {19, 24, 33, 42, -1}, {16, 27, 36, -1, -1},
		{3, 29, 38, -1, -1}, {12, 30, 31, -1, -1}, {9, 34, 35, -1, -1}, {8, 37, 46, -1, -1}, {12, 39, 48, -1, -1},
		{5, 40, 49, -1, -1}, {14, 41, 50, -1, -1}, {6, 43, 44, 53, -1}, {17, 45, 54, 63, 72},
		{7, 47, 56, -1, -1}, {16, 51, 52, -1, -1}, {16, 55, 64, 65, -1}, {17, 57, 66, -1, -1},
		{18, 58, 59, 67, -1}, {7, 60, 69, 70, -1}, {23, 61, 62, 71, -1}, {7, 68, 77, -1, -1},
		{17, 73, 74, -1, -1}, {3, 75, 76, -1, -1}, {15, 78, 79, 80, -1}, {-1}};

int checkRules(int b[81], int i, int m) {

	for (int j = 0; parms[j][0] != -1; j++) {
		int sum = 0;

		for (int x = 1; x < 5 && parms[j][x] > -1; x++) {
			if (parms[j][x] == i)
				sum = sum + m;
			else	
				if (b[parms[j][x]] == 0) {
					sum = -1;
					break;
				}
				else
					sum = sum + b[parms[j][x]];
		}

		if (sum > -1)
			if (sum != parms[j][0])
				return 0;
	}

	return -1;
}


int checkBoard(int b[81], int i, int m) {
	if (checkRules(b, i, m) == 0)
		return 0;
	
	for (int j=0; j < 81; j++) {
		if (i != j) {
			if ((i / 9 == j / 9)
			|| ((i - j) % 9 == 0 )
			|| ((i / 27 == j / 27) && (i%9/3 == j%9/3))) {
				if (m == b[j])
					return 0;
			}
		}
	}

	return -1;
}


int recursiveCheck(int board[81]) {

	int i;
	int m;
	int newBoard[81];

	for (i = 0; i < 81; ++i) {
		if (board[i] == 0)
			break;
	}
	
	if (i == 81) {
		for (int j=0; j < 81; j++)
			printf("%d ",board[j]);
		exit(0);
	}

	for (m = 1; m < 10; m++) {
		if (checkBoard(board, i, m)) {
			for (int j = 0; j < 81; j++ ) {newBoard[j] = board[j];}
			newBoard[i] = m;
			recursiveCheck(newBoard);
		}
	}
}

int main() {
	int startBoard[81];
	for (int i=0; i < 81; i++) {startBoard[i] = 0;}

	recursiveCheck(startBoard);
}