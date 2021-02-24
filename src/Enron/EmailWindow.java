package Enron;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
/**
 * Email window is a class that creates a pop up window that will paginate through emails, only storing 10 in memory at one time
 * @author Augie
 *
 */
public class EmailWindow {
	int currentEmailIndex = 0;
	public EmailWindow(Object[] emailPaths) {
		JFrame frame = new JFrame("Enron Search Engine");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1400, 1000);
		JTextArea emails = new JTextArea(10000, 500);
		JScrollPane scrollEmails = new JScrollPane(emails);
		DefaultCaret caret = (DefaultCaret) emails.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		scrollEmails.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		writeNextPage(emails, emailPaths);
		JButton nextPage = new JButton(new AbstractAction("Next Page") {
			public void actionPerformed(ActionEvent e) {
				writeNextPage(emails, emailPaths);
				System.out.println(currentEmailIndex);
			}
		});
		JButton previousPage = new JButton(new AbstractAction("Previous Page") {
			public void actionPerformed(ActionEvent e) {
				writePreviousPage(emails, emailPaths);
				System.out.println(currentEmailIndex);
			}
		});
		JPanel panel = new JPanel();
		panel.add(nextPage);
		panel.add(previousPage);
		frame.getContentPane().add(BorderLayout.CENTER, scrollEmails);
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		frame.setVisible(true);
	}
	/**
	 * reads an email to the scrollable file
	 * @param ta
	 * @param email
	 */
	public void emailReader(JTextArea ta, File email) {
		try {
			BufferedReader emailReader = new BufferedReader(new FileReader(email));
			String line = "";
			while ((line = emailReader.readLine()) != null) {
				ta.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * reads the next 10 email paths from email path array
	 * @param ta this is the window that displays the emails
	 * @param emails this is the array of emails
	 */
	public void writeNextPage(JTextArea ta, Object[] emails) {
		int range = currentEmailIndex + 10;
		ta.setText("");
		while(currentEmailIndex < range) {
			if(currentEmailIndex < 0) {
				currentEmailIndex = 0;
			}
			if(currentEmailIndex < emails.length) {
				File curEmail = new File((String)emails[currentEmailIndex]);
				if(curEmail.exists()) {
					emailReader(ta, curEmail);
				}
				else {
					System.out.println("no path found" + curEmail.getAbsolutePath());
				}
			}
			else {
				currentEmailIndex = emails.length - 1;
				return;
			}
			currentEmailIndex++;
		}
	}
	/**
	 * Reads the previous 10 email paths from the email path array.
	 * @param ta window this is the window that displays the emails
	 * @param emails is the email path array.
	 */
	public void writePreviousPage(JTextArea ta, Object[] emails) {
		int range = currentEmailIndex - 10;
		ta.setText("");
		while(currentEmailIndex > range) {
			if(currentEmailIndex > emails.length) {
				currentEmailIndex = emails.length - 1;
			}
			if(currentEmailIndex > 0) {
				File curEmail = new File((String)emails[currentEmailIndex]);
				if(curEmail.exists()) {
					emailReader(ta, curEmail);
				}
				else {
					System.out.println("no path found" + curEmail.getAbsolutePath());
				}
			}
			else {
				currentEmailIndex = 0;
				return;
			}
			currentEmailIndex--;
		}
	}
}
