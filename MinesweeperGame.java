// Joseph Brunner
// Project 4

/*
 * MinesweeperGame creates a frame and a canvas, and then constructs a thread
 * that will run the game.
 */

import java.awt.*;
import javax.swing.*;

public class MinesweeperGame
{
	public static void main(String[] args)
	{
		// resetScores is true when we would like to reset the scores for the game,
		// and false otherwise
		boolean resetScores = true;

		// we begin with the board initialized to the easiest difficulty
		String boardSize = "EASY";

		// declare strings that will hold the file names for text files storing
		// information for the best time for the level and for the difficulty of
		// the most difficult puzzle solved for the level
		String timeData, difficultyData;

		// store the file names into their respective strings
		timeData = "timeDataEasy.txt";
		difficultyData = "3BVDataEasy.txt";

		// create a frame
		JFrame app = new JFrame();

		// instruct the frame to ignore repainting
		app.setIgnoreRepaint(true);

		// instruct the frame to exit when closed
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//create a canvas
		Canvas canvas = new Canvas();

		// add the canvas to the frame
		app.getContentPane().add(canvas);

		// make the frame visible
		app.setVisible(true);

		// set the size of the frame
		app.setSize(441, 503);

		// instruct the canvas to ignore repainting
		canvas.setIgnoreRepaint(true);

		// create a thread using the frame and the canvas as references
		Thread myThread = new Thread(new MinesweeperLoop(app, canvas,
				resetScores, boardSize, timeData, difficultyData));

		// set the priority of the thread
		myThread.setPriority(Thread.MIN_PRIORITY);

		// start the thread
		myThread.start();
	}
}
