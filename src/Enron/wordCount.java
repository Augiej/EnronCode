package Enron;
/**
 * Word count is a data type that would have been used to store a count for a word. This means we could iterate the count for every occurance of the word without hashing.
 * @author Augie
 *
 */
class wordCount {
	protected int frequency;
	protected String myWord;
	
	public wordCount (String a) {
		frequency = 1;
		myWord = a;
	}
	
	public void addFreq() {
		frequency++;
	}
	public void addFreq(int a) {
		frequency += a; 
	}

	public int getFreq() {
		return frequency;
	}
	public String getWord() {
		return myWord;
	}
	
	public String toString() {
		return "" + frequency;
	}
}