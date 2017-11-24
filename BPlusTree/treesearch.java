/*
 *  author: Karan Chauhan
 */

import com.karan.ads.bplustree.Key;
import com.karan.ads.bplustree.Node;
import com.karan.ads.bplustree.BPlusTree;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * The Class treesearch.
 */
public class treesearch {

	/** The Constant OPERATION_INSERT. */
	private static final String OPERATION_INSERT = "Insert";

	/** The Constant OPERATION_SEARCH. */
	private static final String OPERATION_SEARCH = "Search";

	/** The Constant RESULT_NOT_FOUND. */
	private static final String RESULT_NOT_FOUND = "Null";

	/** The Constant OUTPUT_FILENAME. */
	private static final String OUTPUT_FILENAME = "output_file";

	/** The Constant OUTPUT_FILEEXTENSION. */
	private static final String OUTPUT_FILEEXTENSION = ".txt";

	// Sample inputs: Insert(0.02,Value98) Search(3.55) Search(-3.91,30.96)

	/**
	 * The main method.
	 *
	 * @param args
	 *            the name of the input file
	 */
	public static void main(String args[]) {

		// Read name of input file from command line argument
		String fileName = args[0];
		File inputFile = new File(fileName);
		try {
			Scanner sc = new Scanner(inputFile);
			// For creating output file
			BufferedWriter bw = openNewFile();

			BPlusTree tree = new BPlusTree();
			tree.initialize(Integer.parseInt(sc.nextLine()));

			while (sc.hasNextLine()) {
				String newLine = sc.nextLine();
				// splitting input file line based on regex
				String[] input = newLine.split("\\(|,|\\)");

				switch (input[0]) {
				// for inserting element into B Plus Tree
				case OPERATION_INSERT: {
					tree.insert(Double.parseDouble(input[1]), input[2]);
					break;
				}
				case OPERATION_SEARCH: {
					// for finding all key value pairs between two keys
					if (input.length == 2) {
						List<String> res = tree.search(Double.parseDouble(input[1]));
						writeSearchByKey(res, bw);
					} 
					// for finding all values for a key
					else {
						List<Key> res = tree.search(Double.parseDouble(input[1]), Double.parseDouble(input[2]));
						writeSearchByKeys(res, bw);
					}
					break;
				}
				}

			}
			// closing scanner and buffered writer
			sc.close();
			bw.close();
		} catch (FileNotFoundException e) {
			// LOGGER.severe("File is not found");
			System.out.println("Error: File not found with name: " + fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: Failed to create new file");
			e.printStackTrace();
		} catch (NumberFormatException e) {	
			System.out.println("Enter valid degree");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open new file to which output has to be written to.
	 *
	 * @return the buffered writer
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static BufferedWriter openNewFile() throws IOException {
		// Creating a new file to write output to (output_file.txt)
		File file = new File(OUTPUT_FILENAME + OUTPUT_FILEEXTENSION);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		return bw;
	}

	/**
	 * Write search by key result to the output file.
	 *
	 * @param res
	 *            the list of values to be written
	 * @param bw
	 *            the BufferedWriter object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void writeSearchByKey(List<String> res, BufferedWriter bw) throws IOException {
		String newLine = "";
		if (null == res) {
			// if no values are found for key
			bw.write(RESULT_NOT_FOUND);
		} else {
			// if values are found, write to file in given format
			Iterator<String> valueIterator = res.iterator();
			while (valueIterator.hasNext()) {
				newLine = newLine + valueIterator.next() + ", ";
			}
			bw.write(newLine.substring(0, newLine.length() - 2));
		}
		bw.newLine();

	}

	/**
	 * Write search by keys result to the output file.
	 *
	 * @param res
	 *            the list of key value pairs to be written
	 * @param bw
	 *            the BufferedWriter object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void writeSearchByKeys(List<Key> res, BufferedWriter bw) throws IOException {
		String newLine = "";
		if (res.isEmpty()) {
			// if no values are found between given keys
			bw.write(RESULT_NOT_FOUND);
		} else {
			// if pairs are found, write to file in given format
			Iterator<Key> keyIterator = res.iterator();
			Iterator<String> valueIterator;
			Key key;
			while (keyIterator.hasNext()) {
				key = keyIterator.next();
				valueIterator = key.getValues().iterator();
				while (valueIterator.hasNext()) {
					newLine = newLine + "(" + key.getKey() + "," + valueIterator.next() + "), ";
				}
			}
			bw.write(newLine.substring(0, newLine.length() - 2));
		}
		bw.newLine();

	}

}
