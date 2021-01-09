#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int rules[50][5];
clock_t t;

int readRules()
{
	int i = 0;
	char line[128];
	char *token = NULL;
	FILE *fp = fopen("puzzle.csv", "r");
	if (fp == NULL)
	{
		printf("File puzzle.csv not found");
		exit(0);
	}

	while (fgets(line, sizeof(line), fp) != NULL && i < 50)
	{
		int j = 0;
		for (token = strtok(line, ","); j < 5; token = strtok(NULL, ","))
		{
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

int checkRules(int b[81], int i)
{
	int j;
	for (j = 0; rules[j][1] != i && rules[j][2] != i && rules[j][3] != i && rules[j][4] != i; j++)
	{
	}

	if (b[rules[j][1]] == 0 || b[rules[j][2]] == 0 || b[rules[j][3]] == 0 || b[rules[j][4]] == 0)
		return -1;

	if (rules[j][3] == -1)
		if (rules[j][0] == b[rules[j][1]] + b[rules[j][2]])
			return -1;
		else
			return 0;

	if (rules[j][4] == -1)
		if (rules[j][0] == b[rules[j][1]] + b[rules[j][2]] + b[rules[j][3]])
			return -1;
		else
			return 0;

	if (rules[j][0] == b[rules[j][1]] + b[rules[j][2]] + b[rules[j][3]] + b[rules[j][4]])
		return -1;
	else
		return 0;
}

int checkBoard(int b[81], int i)
{
	if (checkRules(b, i) == 0)
		return 0;

	for (int j = 0; j < 81; j++)
	{
		if (i != j)
		{
			if ((i / 9 == j / 9) || ((i - j) % 9 == 0) || ((i / 27 == j / 27) && (i % 9 / 3 == j % 9 / 3)))
			{
				if (b[i] == b[j])
					return 0;
			}
		}
	}

	return -1;
}

int recursiveCheck(int board[81])
{
	int i;
	int m;
	int newBoard[81];

	for (i = 0; i < 81; ++i)
	{
		if (board[i] == 0)
			break;
	}

	if (i == 81)
	{
		for (int j = 0; j < 81; j++)
			printf("%d ", board[j]);

		t = clock() - t;
		double e = ((double)t)/CLOCKS_PER_SEC;
		printf("\nelapsed time=%f\n", e);
		exit(0);
	}

	for (m = 1; m < 10; m++)
	{
		for (int j = 0; j < 81; j++)
		{
			newBoard[j] = board[j];
		}
		newBoard[i] = m;
		if (checkBoard(newBoard, i))
		{
			recursiveCheck(newBoard);
		}
	}
}

int main()
{
	t = clock();
	readRules();

	int startBoard[81];
	for (int i = 0; i < 81; i++)
	{
		startBoard[i] = 0;
	}

	recursiveCheck(startBoard);
}