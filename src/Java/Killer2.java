
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.*;
import java.sql.Timestamp;

public class Killer2 {

	private static final int _0 = 0;
	private static final String COMMA_DELIMITER = ",";
	public static HashMap<Integer,ArrayList<Integer>> parms = new HashMap<Integer, ArrayList<Integer>>();

	public static void main(String[] args) {
		
		System.out.println(new Timestamp(System.currentTimeMillis()));
		try (BufferedReader br = new BufferedReader(new FileReader("puzzle.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				List<String> values = Arrays.asList(line.split(COMMA_DELIMITER));
				List<Integer> intvals = values.stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
				parms.put(intvals.get(intvals.size() - 1),(ArrayList<Integer>) intvals);
			}
		} catch (Exception ex) {
		}
		;
		System.out.println("parms: " + parms);

		int[] newBoard = new int[81];
		recursiveCheck(newBoard);

	}

	public static List<Integer> getSelectedParm(int position) {

		List<Integer> selectedParm = parms.get(position);
		return selectedParm;
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

	public static int parmTotal(List<Integer> parm, int[] aRow, int value) {
		List<Integer> subParm = parm.subList(1, parm.size()-1);
		int result = subParm.stream().reduce(value, (subtotal, element) -> subtotal + aRow[element]);
		return result;

	}

	public static boolean checkRow(int[] aRow, int position, int value) {

		
		List<Integer> selectedParm = getSelectedParm(position);

		if (selectedParm != null) {

			int total = parmTotal(selectedParm, aRow, value);
			if (total != selectedParm.get(0)) {
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
				if (checkRow(inBoard,pos, y)) {
					int[] newBoard = inBoard.clone();
					newBoard[pos] = y;
					recursiveCheck(newBoard);
				}
			}
		}
		return true;
	}
}
