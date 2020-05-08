// Joseph Brunner
// Project 4

/*
 * FileOut constructs an object that stores information about a given file located
 * within the project.  FileOut is used with writing to a file.
 */

import java.io.*;

public class FileOut
{
	// create a BufferedReader to write to
	private BufferedWriter out;

	public FileOut(String loc, String newString)
	{
		try
		{
			// initialize the BufferedReader to the correct file name
			out = new BufferedWriter(new FileWriter(loc,false));

			// write out to the file
			out.write(newString);

			// close the file
			out.close();
		}
		catch(IOException e)
		{
			System.out.println("There was a problem:" + e);
		}
	}
}
