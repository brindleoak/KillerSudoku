#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int rules[50][5];

int readRules() {
	int i = 0;
    char line[128];
	char* token = NULL;
  	FILE* fp = fopen("puzzle.csv","r");
  	if (fp == NULL) {
    	printf("File puzzle.csv not found");
		exit(0);
	}
		
	while (fgets( line, sizeof(line), fp) != NULL && i < 50) {
    	int j = 0;
    	for (token = strtok(line, ","); j < 5; token = strtok(NULL, ",")) {
        	if (token == NULL)
				rules[i][j++] = -1;
			else	   
				rules[i][j++] = atof(token);
      	}
      	i++;
    }
	rules[i][0] = -1;
    fclose(fp);
}

int checkRules(int b[81], int i, int m) {

	for (int j = 0; rules[j][0] != -1; j++) {
		int sum = 0;

		for (int x = 1; x < 5 && rules[j][x] > -1; x++) {
			if (rules[j][x] == i)
				sum = sum + m;
			else	
				if (b[rules[j][x]] == 0) {
					return 0;
				}
				else
					sum = sum + b[rules[j][x]];
		}

		if (sum != rules[j][0])
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

	readRules();

	int startBoard[81];
	for (int i=0; i < 81; i++) {startBoard[i] = 0;}

	recursiveCheck(startBoard);
}