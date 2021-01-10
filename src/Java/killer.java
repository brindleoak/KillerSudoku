
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.*;
import java.sql.Timestamp;

/**
 * 
 * A simple Java Program to demonstrate how to use map and filter method Java 8.
 * In this program, we'll convert a list of String into a list of Integer and
 * then filter all even numbers.
 */
public class Killer {

	private static final int _0 = 0;
	private static final String COMMA_DELIMITER = ",";
	public static List<List<Integer>> parms = new ArrayList<>();

	public static void main(String[] args) {
		
		System.out.println(new Timestamp(System.currentTimeMillis()));
		try (BufferedReader br = new BufferedReader(new FileReader("/home/sandy/Documents/puzzle.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				List<String> values = Arrays.asList(line.split(COMMA_DELIMITER));
				List<Integer> intvals = values.stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
				parms.add(intvals);
			}
		} catch (Exception ex) {
		}
		;
		System.out.println("parms: " + parms);

		int[] newBoard = new int[81];
		recursiveCheck(newBoard);

	}

	public static List<List<Integer>> getSelectedParms(int position) {

		List<List<Integer>> selectedParms = parms.stream().filter(parm -> parm.get(parm.size() - 1) == position)
				.collect(Collectors.toList());
		return selectedParms;
	}

	public static boolean sameRow(int i, int j) {
		return (i / 9) >> 0 == (j / 9) >> 0;
	}

	public static boolean sameCol(int k, int l) {
		return (k - l) % 9 == 0;
	}

	public static boolean sameBlock(int m, int n) {
		return ((m / 27) >> 0 == (n / 27) >> 0 && ((m % 9) / 3) >> 0 == ((n % 9) / 3) >> 0);
	}

	public static int parmTotal(List<Integer> parm, int[] aRow) {
		List<Integer> subParm = parm.subList(1, parm.size());
		int result = subParm.stream().reduce(0, (subtotal, element) -> subtotal + aRow[element]);
		return result;

	}

	public static boolean checkRow(int[] aRow) {

		int pos = IntStream.range(0, aRow.length).filter(i -> aRow[i] == _0).findFirst().orElse(-1);

		List<List<Integer>> selectedParms = getSelectedParms(pos - 1);

		if (selectedParms.size() > 0) {

			int total = parmTotal(selectedParms.get(0), aRow);
			if (total != selectedParms.get(0).get(0)) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public static boolean recursiveCheck(int[] inBoard) {

		int pos = IntStream.range(0, inBoard.length).filter(i -> inBoard[i] == _0).findFirst().orElse(-1);
		if (pos == -1) {
			System.out.println("results:" + Arrays.toString(inBoard));
			System.out.println(new Timestamp(System.currentTimeMillis()));
			// return true;
			System.exit(1);
		}

		Set<Integer> excludedNumbers = new HashSet<Integer>();

		for (int x = 0; x < pos; x++) {
			if (sameRow(pos, x) || sameCol(pos, x) || sameBlock(pos, x)) {
				excludedNumbers.add(inBoard[x]);
			}
		}

		for (int y = 1; y < 10; y++) {
			if (!excludedNumbers.contains(y)) {
				int[] newBoard = inBoard.clone();
				newBoard[pos] = y;
				if (checkRow(newBoard)) {
					recursiveCheck(newBoard);
				}
			}
		}
		return true;
	}
}
