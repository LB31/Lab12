import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class ScrabbleCheater {
	private ArrayList<String>[] hashTable;
	private HashSet<String> permutations;
	private String initialInput = "";

	// statistic stuff
	private int entriesNumber = 0;
	private int collisionsNumber = 0;
	private int longestChainNumber = 0;
	private int longestChainNumberPosition = 0;

	public ScrabbleCheater(String cheatWord) {
		hashTable = new ArrayList[100003]; // Of course a prime number
		permutations = new HashSet<>();
		initialInput = cheatWord.toLowerCase();
		run();

	}
	
	public void run(){
		findPermutations(initialInput);
		implementDict();
		String[] theResults = findCheatWord(permutations);
		
		System.out.println("Possible words for the input '" + initialInput + "': ");
		for(String printer : theResults){
			if(printer != null)
			System.out.print(printer + " ");
		}
		
	}
	
	

	public void implementDict() {

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("dictionary.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scanner.hasNext()) {
			String word = scanner.next();
//			if (word.length() == 7) {
				long hash = makeHash(word, true);
				addToTable((int) hash, word);
//			}

		}
		scanner.close();

	}

	public long makeHash(String word, boolean modulo) {
		char[] numbWord = word.toLowerCase().toCharArray();
		Arrays.sort(numbWord);
		long all = 0;
		for (int i = 0; i < numbWord.length; i++) {
			// char letter = word.charAt(i);
			double letter = (numbWord[numbWord.length - 1 - i] - 96) * Math.pow(27, i);
			all += letter;
		}
		if (modulo)
			all %= 100003;

		return all;
	}

	public void addToTable(int digit, String word) {
		if (hashTable[digit] == null) {
			hashTable[digit] = new ArrayList<String>();
			entriesNumber++;
		} else {
			collisionsNumber++;
			if (hashTable[digit].size() > longestChainNumber) {
				longestChainNumber = hashTable[digit].size();
				longestChainNumberPosition = digit;

			}
		}
		hashTable[digit].add(word);

	}

	public String[] findCheatWord(HashSet<String> permutations) {
		Iterator hashItr = permutations.iterator();
		String[] back = new String[1000];
		int iterator = 0;
		while (hashItr.hasNext()) {
			String word = (String) hashItr.next();
			long pos = makeHash(word, true);
			long hash = makeHash(word, false);
			try {
				Iterator itr = hashTable[(int) pos].iterator();
				while (itr.hasNext()) {
					String next = (String) itr.next();
					long temp = makeHash(next, false);
					if (temp == hash) {
						back[iterator] = next;
						iterator++;
					}

				}
			} catch (NullPointerException e) {
				// TODO: handle exception
			}

		}

		return back;

	}

	// converts the single letters of a String to an ArrayList
	// removes in each run a single letter, sorts the ArrayList, assigns it
	// again to a String
	// adds the String to a HashSet and calls itself recursively
	public void findPermutations(String input) {
		if (input.length() > 2) {

			String first = input.toLowerCase();
			for (int i = 0; i < first.length(); i++) {
				ArrayList<Character> temp = new ArrayList<>();
				for (int j = 0; j < first.length(); j++) {
					temp.add(first.charAt(j));
				}
				temp.remove(i);
				Collections.sort(temp);
				String str = "";
				for (char cha : temp) {
					str += cha;
				}

				permutations.add(str);
				findPermutations(str);

			}

		} else {

			permutations.add(initialInput);
		}

	}

	// sorts a String in alphabetical order and returns it
	public String sortString(String word) {

		char[] sortWord = (word.toLowerCase().toCharArray());
		Arrays.sort(sortWord);
		String firstWord = new String(sortWord);
		return firstWord;
	}

	
	
	public static void main(String[] args) {
		ScrabbleCheater cheater = new ScrabbleCheater("pikachu");
		
	}

}
