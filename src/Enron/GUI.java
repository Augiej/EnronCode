package Enron;
import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;


/**
 * GUI is a class that provides the front end search tools for the enron email set. It takes in the root folder of the prefix tree that we are iterating through
 * @author Augie
 *
 */
public class GUI implements ActionListener{
	private static String root = "";
	/**
	 * The constructor creates a frame with 2 buttons search and suggest. Search searches for standalone cases of the searched word. It
	 * @param setRoot
	 */
	public GUI(String setRoot) 
	{
		//set the root dir and make the search engine
		root = setRoot;
		//Search engine object for searching through the prefix tree.
		SearchEngine notGoogle = new SearchEngine (root);
		//Creating the Frame
        JFrame frame = new JFrame("Enron Search Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        JTextField tf = new JTextField(40); //accepts up to 40 characters
        JTextArea ta = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(ta); 
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); 
        JLabel label = new JLabel("Enter Text");
        Writer output = new StringWriter();
        
        //Create the buttons for search and suggest.
        JButton search = new JButton(new AbstractAction("Search") {
        	
			public void actionPerformed(ActionEvent e) {
				String[] ins = parseInput(tf.getText());
				HashSet<String> searchWords = notGoogle.exclusiveSearch(ins);
				EmailWindow newWindow = new EmailWindow(searchWords.toArray());
			}
        });
        
        JButton suggest = new JButton(new AbstractAction("Suggest") {
        	
			public void actionPerformed(ActionEvent e) {
				String[] ins = parseInput(tf.getText());
				for(String cur : ins) {
					Stack<String> temp = notGoogle.autoComplete(cur);
					if(temp!= null) {
						while(!temp.isEmpty()) {
							ta.append(temp.pop() + "\n");
						}
					}
				}
			}
        });
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(search);
        panel.add(suggest);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        frame.setVisible(true);

	}

	//parses the string input to handle spaces so we can search for multiple words i.e. account taxable and it would return all instances of account and taxable
	public static String[] parseInput(String in)
	{
		String[] wordsIn = in.split(" ", 3);
		return wordsIn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
