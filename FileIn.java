// Joseph Brunner
// Project 4

/*
 * FileIn constructs an object that stores information about a given file located
 * within the project.  FileIn is used with reading in files.
 */

import java.io.*;

public class FileIn
{
	// create a BufferedReader to read in the file to
	private BufferedReader in;

	// create a string to store the text in the file
	private String read;

	public FileIn(String loc)
	{
		try
		{
			// read in the file
			in = new BufferedReader(new FileReader(loc));

			// store the text in the file
			read = in.readLine();

			// close the file
			in.close();
		}

		catch(IOException e)
		{
			System.out.println("There was a problem:" + e);
		}
	}

	/*
	 * getString() returns the text inside of the file that this
	 * FileIn object is assigned to.
	 */

	public String getString()
	{
		return read;
	}
}
