#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int rules[81][5];
char related[81][81];
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
		int rule[5];
		for (token = strtok(line, ","); j < 5; token = strtok(NULL, ","))
		{
			if (token == NULL)
				rule[j++] = -1;
			else
				rule[j++] = atof(token);
		}
		i++;
		for (j = 1; j < 5; j++)
		{
			if (rule[j] != -1)
			{
				for (int k = 0; k < 5; k++)
				{
					rules[rule[j]][k] = rule[k];
				}
			}
		}
	}
	fclose(fp);
}

int checkRules(int b[81], int i, int m)
{
	if (rules[i][3] == -1)
	{
		if (rules[i][2] != i)
		{
			return -1;
		}
		else
		{
			if (rules[i][0] == b[rules[i][1]] + m)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}

	if (rules[i][4] == -1)
	{
		if (rules[i][3] != i)
		{
			return -1;
		}
		else
		{
			if (rules[i][0] == b[rules[i][1]] + b[rules[i][2]] + m)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}

	if (rules[i][4] != i)
	{
		return -1;
	}
	else
	{
		if (rules[i][0] == b[rules[i][1]] + b[rules[i][2]] + b[rules[i][3]] + m)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
}

int checkBoard(int b[81], int i, int m)
{
	if (checkRules(b, i, m) == 0)
		return 0;

	for (int j = 0; j < i; j++)
	{
		if (i != j)
		{
			if (related[i][j] == '\0')
			{
				if ((i / 9 == j / 9) || ((i - j) % 9 == 0) || ((i / 27 == j / 27) && (i % 9 / 3 == j % 9 / 3)))
				{
					related[i][j] = 'Y';
				}
				else
				{
					related[i][j] = 'N';
				}
			}
			if (related[i][j] == 'Y')
			{

				if (b[j] == m)
				{
					return 0;
				}
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
		double e = ((double)t) / CLOCKS_PER_SEC;
		printf("\nelapsed time=%f\n", e);
		exit(0);
	}

	for (m = 1; m < 10; m++)
	{
		if (checkBoard(board, i, m))
		{
			for (int j = 0; j < 81; j++)
			{
				newBoard[j] = board[j];
			}
			newBoard[i] = m;

			recursiveCheck(newBoard);
			int z = 1;
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