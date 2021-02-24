package Enron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

/**
 * Search engine is a class that can run searches on prefix trees given a root
 * directory and a prefix.
 * 
 * @author Augie
 *
 */
public class SearchEngine {
	private String root;

	public SearchEngine(String setRoot) {
		root = setRoot;
	}

	/*
	 * public String[] instanceSearch(String prefix) {
	 * 
	 * }
	 */
	/**
	 * exclusive search is used to search for the email paths of everything matching
	 * a particular search. For instance amaz would return amaz as well as amazon
	 * and amazing etc
	 * 
	 * @param prefixArr Array of prefixes to search for.
	 * @return
	 */
	public HashSet<String> exclusiveSearch(String[] prefixArr) {
		//create variables that will be our returned destinations, our root path and our fileStack for the iterative navigation through the tree.
		HashSet<String> returnDestinations = new HashSet<String>();
		StringBuilder path = new StringBuilder(root);
		Stack<File> fileStack = new Stack<File>();
		//for loop to go through each prefix we are searching for.
		for (String prefix : prefixArr) {
			//push null to say "we're done" with the search
			fileStack.push(null);
			//get the path to the deepest branch of the prefix example for amazon would be root/a/m/a/z/o/n
			for (int i = 0; i < prefix.length(); i++) {
				path.append("\\" + prefix.charAt(i));
			}
			//get our current file location
			File currentDir = new File(path.toString());
			//if anything exists in our dir
			if (currentDir.exists()) {
				//for each file in our current directory.
				for (File checkFile : currentDir.listFiles()) {
					//if the checked file is a directory add it to the stack
					if (checkFile.isDirectory()) {
						fileStack.push(checkFile);
					} else { //if it is not a directory it contains paths, add those paths to the destination hashset
						returnDestinations = parseDestinations(checkFile, returnDestinations);
					}
				}
				currentDir = fileStack.pop();
				//while our current directory is not null
				while (currentDir != null) {
					//do the same as above
					for (File checkFile : currentDir.listFiles()) {
						if (checkFile.isDirectory()) {
							fileStack.push(checkFile);
						} else {
							returnDestinations = parseDestinations(checkFile, returnDestinations);
						}
					}
					//iterate through the directories
					currentDir = fileStack.pop();
				}
				return returnDestinations;
			}
		}
		return null;
	}

	/**
	 * Autocomplete is a method to suggest words to search for. For instance acc would suggest accounts account, accounting etc.
	 * @param prefix
	 * @return
	 */
	public Stack<String> autoComplete(String prefix) {
		Stack<String> returnStrings = new Stack<String>();
		StringBuilder path = new StringBuilder(root);
		Stack<File> fileStack = new Stack<File>();
		fileStack.push(null);
		for (int i = 0; i < prefix.length(); i++) {
			path.append("\\" + prefix.charAt(i));
		}

		File currentDir = new File(path.toString());
		if (currentDir.exists()) {
			for (File checkFile : currentDir.listFiles()) {
				if (checkFile.isDirectory()) {
					fileStack.push(checkFile);
				}
			}
			currentDir = fileStack.pop();
			while (currentDir != null) {
				for (File checkFile : currentDir.listFiles()) {
					if (checkFile.isDirectory()) {
						fileStack.push(checkFile);
					} else {
						String currentName = checkFile.getParent().replaceAll("[^a-zA-Z0-9]", "");
						currentName = currentName.replaceAll(root.replaceAll("[^a-zA-Z0-9]", ""), "");
						returnStrings.push(currentName);
					}
				}
				currentDir = fileStack.pop();
			}
			Collections.sort(returnStrings);
			return returnStrings;
		}
		return null;
	}
	/**
	 * Parse destinations returns a hashset of destinations from a csv containing them. 
	 * @param dest is the file we are searching for destinations
	 * @param destinationStack is the stack of emails paths that contain the word we are searching for.
	 * @return
	 */
	public static HashSet<String> parseDestinations(File dest, HashSet<String> destinationStack) {
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(dest));
			StringBuilder currentDest = new StringBuilder();
			int value = 0;
			while ((value = csvReader.read()) != -1) {
				// if value is a comma, push the built string to the stack and restart making
				// the string
				if (value == 44) {
					destinationStack.add(currentDest.toString());
					currentDest.setLength(0);
				}
				// if value is a square bracket then ignore it
				else if (!(value == 91 || value == 93 || value == 32)) {
					currentDest.append((char) value);
				}
			}
			return destinationStack;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
