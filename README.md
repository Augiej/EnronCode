# EnronCode
Code to search through the enron email db on a local disk.

EmailWindow.java is a window that displays the paginated emails
1. GUI.java is the search window that will accept search terms
2. Main.java is the main that currently starts just the GUI, but had the "server" code to manipulate the data
3. SearchEngine.java is the class that searches through the prefix tree for paths to emails that contain said prefixes. It also provides a suggest function that suggests valid search terms.
4. wordCount.java is a depricated class that was used to keep track of the count of all appearances of a word. This was going to be used to weight suggested words.
