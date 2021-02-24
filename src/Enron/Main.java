package Enron;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
public class Main {
	public static int count = 0;
	public static Map<String, Object> wordMap = new HashMap<String, Object>();
	public static Map<String, Object> emailWordMap = new HashMap<String, Object>();
	public static List<String> sortedStrings = new ArrayList<String>();
	public static void main(String[] args) {
		//base directory
		//File dir = new File("A:\\Enron_Data\\maildir");
		//stores maps for the word to email map and a word to count map
		//ArrayList<Map<String, Object>> myLists = new ArrayList<Map<String, Object>>();
		//emailWordMap = iterateFile(dir);
		//System.out.println("done with iterations");
		//This map was going to be used to weigh the different words for autocorrect (favoring ones  that appeared most often) but is no longer useful
		//wordMap = myLists.get(0);
		//this map is the important one because it is the one used to make the tree.
		//sortedStrings = sortStrings(myLists.get(0));
		//make csvs of the maps so we only have to iterate through the entire dataset once
		//writeHashMaptoCsv(wordMap, "A:\\Enron_Data\\myCsvs\\wordMap.csv");
		//writeHashMaptoCsvEmailMap(emailWordMap, "A:\\Enron_Data\\myCsvs\\emailWordMap.csv");
		//System.out.println("Finished writing to CSV");
		//make a prefix tree in file explorer
		//makeWordSearchTree("A:\\Enron_Data\\myCsvs\\emailWordMap.csv", "A:\\Enron_Data\\searchTree");
		//start a front end element passing through the root of the search tree to search for emails.
		
		
		
		
		
		
		GUI myGui = new GUI("A:\\Enron_Data\\searchTree");
	}

	
	public static Map<String, Object> iterateFile(File dir) 
	{	
		Map<String, Object> c1 = new HashMap<String, Object>();
		Map<String, Object> c2 = new HashMap<String, Object>();
		if(dir.isDirectory()) {
			c1 = iterateFile(dir.listFiles()[0]);
			for(int i = 1; i < dir.listFiles().length; i++) {
				c2 = iterateFile(dir.listFiles()[i]);
				c1 = combineEmailMaps(c1, c2);
			}
			return c1;
		}
		else {
			return readFile(dir);
		}
	}
	
	public static Map<String, Object> readFile(File dir) {
		Map<String, Object> myEmailMap = new HashMap<String, Object>();
		try {
			Scanner myScanner = new Scanner(dir);
			while(myScanner.hasNextLine()) {
				Scanner s2 = new Scanner(myScanner.nextLine());
				
				while(s2.hasNext()) {
					String s = s2.next();
					s = s.replaceAll("[^a-zA-Z0-9]", "");
					s = s.toLowerCase();
					if(!s.isEmpty()) {
						if(!myEmailMap.containsKey(s)) {
							myEmailMap.put(s, new ArrayList<String>());
							((List<String>) myEmailMap.get(s)).add(dir.getPath());
						}
						else {
							if(!((List<String>) myEmailMap.get(s)).contains(dir.getPath())) {
								((List<String>) myEmailMap.get(s)).add(dir.getPath());
							}
						}
					}
				}
			}
			return myEmailMap;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	

	
	
	public static void writeHashMaptoCsvEmailMap(Map<String, Object> a, String destination) 
	{
		String eol = System.getProperty("line.separator");
		try (Writer writer = new FileWriter(destination)) {
		  for (String key : a.keySet()) {
		    writer.append(key)
		          .append(',')
		          .append(a.get(key).toString())
		          .append(eol);
		  }
		} catch (IOException ex) {
		  ex.printStackTrace(System.err);
		}
	}
	
	public static Map<String, Object> combineEmailMaps(Map<String, Object> map1, Map<String, Object> map2)
	{
		if(map1 != null && map2 != null) {
			for (String key : map1.keySet()) {
				if (map2.containsKey(key)) {
					for (String path : (List<String>) map1.get(key)) {
						if (!((List<String>) map2.get(key)).contains(path)) {
							((List<String>) map2.get(key)).add(path);
						}
					}
				} else {
					map2.put(key, map1.get(key));
				}
			}
			return map2;
		}
		else {
			return map1;
		}
	}
	
	public static void makeWordSearchTree(String csvLocation, String rootPath) 
	{
		try {
			File myCSV = new File(csvLocation);
			FileReader fr = new FileReader(myCSV);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while((line = br.readLine())!= null) {
				String[] splitLine = line.split(",", 2);
				String word = splitLine[0];
				StringBuilder path = new StringBuilder (rootPath);
				String myPath = "";
				for(int i = 0; i < word.length(); i++) {
					path = path.append("\\"+ word.charAt(i));
					myPath = path.toString();
					File root = new File(myPath);
					if(!root.exists()) {
						Files.createDirectory(Paths.get(myPath));
					}
				}
				path.append("\\" + "emails.csv");
				makeLocationCSV(splitLine[1], path.toString());
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void makeLocationCSV(String locations, String destination) 
	{
		String eol = System.getProperty("line.separator");
		try (Writer writer = new FileWriter(destination)) {
		    writer.append(locations)
		          .append(eol);
		} catch (IOException ex) {
		  ex.printStackTrace(System.err);
		}
	}
	
	//Depricated code for handling of a Hashmap that counted the instances of words.
//	public static  ArrayList<Map<String, Object>> combineCollections(ArrayList<Map<String, Object>> c1,  ArrayList<Map<String, Object>> c2) 
//	{
//		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
//		if(c1 != null && c2!= null) {
//			//maps.add(combineCountMaps((Map<String, Object>)(c1.get(0)), (Map<String, Object>)(c2.get(0))));
//			maps.add(combineEmailMaps((Map<String, Object>)(c1.get(1)), (Map<String, Object>)(c2.get(1))));
//		}
//		else {
//			return c1;
//		}
//		return maps;
//		
//	}
	
//	public static void writeHashMaptoCsv(Map<String, Object> a, String destination) 
//	{
//		String eol = System.getProperty("line.separator");
//
//		try (Writer writer = new FileWriter(destination)) {
//		  for (String key : sortedStrings) {
//			
//			writer.append(key)
//		          .append(',')
//		          .append(((wordCount)a.get(key)).toString())
//		          .append(eol);
//		  }
//		} catch (IOException ex) {
//		  ex.printStackTrace(System.err);
//		}
//	}
	
//	public static Map<String, Object> readFile(File dir) {
//		//List<String> emailWords = new ArrayList<String>();
//		Map<String, Object> myEmailMap = new HashMap<String, Object>();
//		Map<String, Object> myWordMap = new HashMap<String, Object>();
//		try {
//			Scanner myScanner = new Scanner(dir);
//			while(myScanner.hasNextLine()) {
//				Scanner s2 = new Scanner(myScanner.nextLine());
//				
//				while(s2.hasNext()) {
//					String s = s2.next();
//					s = s.replaceAll("[^a-zA-Z0-9]", "");
//					s = s.toLowerCase();
//					if(!s.isEmpty()) {
//						//emailWords.add(s);
//						if(!myEmailMap.containsKey(s)) {
//							myEmailMap.put(s, new ArrayList<String>());
//							((List<String>) myEmailMap.get(s)).add(dir.getPath());
//						}
//						else {
//							if(!((List<String>) myEmailMap.get(s)).contains(dir.getPath())) {
//								((List<String>) myEmailMap.get(s)).add(dir.getPath());
//							}
//						}
//					}
//				}
//			}
//			
//			//myWordMap = countWord(emailWords, myWordMap);
//			ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
//			maps.add(myWordMap);
//			maps.add(myEmailMap);
//			System.out.println(myEmailMap);
//			return maps;
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//	}
//	public static List<String> sortStrings(Map<String, Object> a) {
//		ArrayList<String> myList = new ArrayList<String>(a.keySet());
//		Collections.sort(myList);
//		return myList;
//	}
//	public static ArrayList<Map<String, Object>> iterateFile(File dir) 
//	{
////		ArrayList<Map<String, Object>> c1 = new ArrayList<Map<String, Object>>();
////		ArrayList<Map<String, Object>> c2 = new ArrayList<Map<String, Object>>();
//		Map<String, Object> c1 = new HashMap<String, Object>();
//		Map<String, Object> c2 = new HashMap<String, Object>();
//		if(dir.isDirectory()) {
//			c1 = iterateFile(dir.listFiles()[0]);
//			for(int i = 1; i < dir.listFiles().length - 1; i+=2) {
//				c2 = iterateFile(dir.listFiles()[i]);
//				c1 = combineCollections(c1, c2);
//			}
//			return c1;
//		}
//		else {
//			return readFile(dir);
//		}
//	}
	
//	public static  ArrayList<Map<String, Object>> combineCollections(ArrayList<Map<String, Object>> c1,  ArrayList<Map<String, Object>> c2) 
//	{
//		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
//		if(c1 != null && c2!= null) {
//			//maps.add(combineCountMaps((Map<String, Object>)(c1.get(0)), (Map<String, Object>)(c2.get(0))));
//			maps.add(combineEmailMaps((Map<String, Object>)(c1.get(1)), (Map<String, Object>)(c2.get(1))));
//		}
//		else {
//			return c1;
//		}
//		return maps;
//		
//	}
//	public static Map<String, Object> combineCountMaps(Map<String, Object> map1, Map<String, Object> map2)
//	{
//		for(String key : map1.keySet()) {
//			if(map2.containsKey(key)) 
//			{
//				((wordCount) map2.get(key)).addFreq(((wordCount) map1.get(key)).getFreq());
//			}
//			else {
//				map2.put(key, map1.get(key));
//			}
//		}
//		return map2;
//	}
//	public static Map<String, Object> countWord(List<String> myList, Map<String, Object> myWordMap) 
//	{
//		
//		for(String word : myList) {
//			if(!myWordMap.containsKey(word)) {
//				myWordMap.put(word, new wordCount(word));
//			}
//			else {
//				((wordCount) myWordMap.get(word)).addFreq();
//			}
//		}
//		return myWordMap;
//	}
}
