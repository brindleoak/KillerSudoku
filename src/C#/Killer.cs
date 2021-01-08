using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
class Program
{
   static void Main(string[] args)
    {
        //Convert a CSV file into a list of integer arrays
        List<int[]> parms = File.ReadLines("puzzle.csv")
                                .Select(s => s.ToString().Split(","))
                                .Select(s => Array.ConvertAll(s, int.Parse)).ToList()
                                .OrderBy(o => o.Last()).ToList();

        //Check the rules for the cell against the given board. A rule is an array where the first item
        //is the required total and the remaining items are the cell positions that must be summed"
        bool checkAllRules(int[] board, List<int[]> filteredParms, int m)
        {
            foreach (int[] p in filteredParms)
            {
                int[] cells = p.Skip(1).Select(i => board[i]).ToArray();
                cells[cells.Length - 1] = m;
                if (!cells.Contains(0))
                    if (p[0] != cells.Sum())
                        return false;
            }
            return true;
        }

        //Check the board is valid - the value m at position i is allowed
        bool checkBoardValid(int[] board, int i, int m)
        {
            bool sameRow(int i, int j) { return i / 9 == j / 9; }
            bool sameCol(int i, int j) { return (i - j) % 9 == 0; }
            bool sameBlock(int i, int j) { return ((i / 27 == j / 27) && (i % 9 / 3 == j % 9 / 3)); }

            foreach (int j in Enumerable.Range(0, 80))
            {
                if (i != j && board[j] != 0)
                    if (sameRow(i, j) || sameCol(i, j) || sameBlock(i, j))
                        if (board[j] == m)
                            return false;
            }

            return true;
        }

        //Finds the first non-zero cell then try the values 1, 2, 3...
        //If the attempt satisfies the rules the recurse to solve this new position"
        void recursiveCheck(int[] board)
        {
            int i = Array.IndexOf(board, 0);
            if (i == -1)
            {
                Console.Write("[{0}]", string.Join(", ", board));
                Environment.Exit(0);
            }

            foreach (int m in Enumerable.Range(1, 9))
            {
                if (checkBoardValid(board, i, m))
                    if (checkAllRules(board, (parms.Where(p => p.Last() == i)).ToList<int[]>(), m))
                    {
                        int[] newBoard = (int[])board.Clone();
                        newBoard[i] = m;
                        recursiveCheck(newBoard);
                    }
            }
        }

        int[] board = new int[80];
        recursiveCheck(board);
    }
}