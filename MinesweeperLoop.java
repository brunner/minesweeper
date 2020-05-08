// Joseph Brunner
// Project 4

/*
 * MinesweeperLoop constructs a threaded object that continuously runs and
 * updates itself, to display the most current image to the user.  It responds
 * to user input through the mouse using MouseListeners and MouseMotionListeners.
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class MinesweeperLoop implements Runnable, MouseMotionListener, MouseListener
{
	// declare a constant to represent the delay, in milliseconds, between timer events
	private final int DELAY = 1000;

	// declare a graphics object
	private Graphics g;

	// declare a frame
	private JFrame app;

	// declare a canvas
	private Canvas canvas;

	// declare a background
	private Background background;

	// declare input file objects for both time data and difficulty data
	private FileIn timeFileIn, difficultyFileIn;

	// declare output file objects for both time data and difficulty data
	private FileOut timeFileOut, difficultyFileOut;

	// declare a new timer
	private Timer timer;

	// declare storage values for the location of the mouse cursor
	private int mouseXPos, mouseYPos;

	// declare a storage value for the number of seconds elapsed in the game
	private int seconds;

	// declare storage values to keep track of the least amount of seconds
	// that it took to complete the game and the difficulty of the hardest
	// puzzle solved, both of which will be read in from their respective
	// text files
	private int lowSeconds, highest3BV;

	// mouseOnCanvas is true when the cursor is on the canvas and false otherwise
	private boolean mouseOnCanvas = true;

	// timerStarted is true when the timer has been started and false otherwise
	private boolean timerStarted = false;

	// squareDepressed is true when the user clicks on a square that has not had
	// all of its mine flags placed, and the squares adjacent to it that have not
	// been revealed are then depressed
	private boolean squareDepressed = false;

	// metaDown is true when the user right clicks and false otherwise
	private boolean metaDown = false;

	// declare storage values for the file names to the files that will store the
	// best time and the most difficult puzzle solved by the user
	private String timeData, difficultyData;

	// timerListener is an ActionListener which counts the number of seconds the user
	// has been working on the puzzle
	private ActionListener timerListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent evt)
	    {
			// if the game is not yet won
			if(background.shouldTimerStop() == false)
			{
				// increment the number of seconds
				seconds++;

				// if the number of seconds has reached 999
				if(seconds == 999)
				{
					// stop the timer
					timer.stop();
				}
			}

			// if the game is won
			else
			{
				// stop the timer
				timer.stop();
			}
	    }
	};


	/*
	 * MinesweeperLoop(JFrame myApp, ... , String difficultyData) constructs a new
	 * MinesweeperLoop object.
	 */
	public MinesweeperLoop(JFrame myApp, Canvas myCanvas, boolean resetScores,
						   String boardSize, String myTimeData, String myDifficultyData)
	{
		// assign values to instance variables
		app = myApp;
		canvas = myCanvas;
		timeData = myTimeData;
		difficultyData = myDifficultyData;

		// add MouseListener and MouseMotionListener to the canvas
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);

		// if we would like to reset the scores in the game before playing
		if(resetScores == true)
		{
			// for the files containing the easiest level's data, reset the
			// information to read 999 seconds and 0 level difficulty
			timeFileOut = new FileOut("timeDataEasy.txt", "" + 999);
			difficultyFileOut = new FileOut("3BVDataEasy.txt", "" + 0);

			// for the files containing the second easiest level's data, reset
			// the information to read 999 seconds and 0 level difficulty
			timeFileOut = new FileOut("timeDataMedium.txt", "" + 999);
			difficultyFileOut = new FileOut("3BVDataMedium.txt", "" + 0);

			// for the files containing the hardest level's data, reset the
			// information to read 999 seconds and 0 level difficulty
			timeFileOut = new FileOut("timeDataHard.txt", "" + 999);
			difficultyFileOut = new FileOut("3BVDataHard.txt", "" + 0);
		}

		// create a new file in for the current difficulty level
		timeFileIn = new FileIn(myTimeData);
		difficultyFileIn = new FileIn(myDifficultyData);

		// set the number of seconds to zero
		seconds = 0;

		// set lowSeconds and highest3BV to the integer versions of
		// the strings stored in the files
		lowSeconds = Integer.parseInt(timeFileIn.getString());
		highest3BV = Integer.parseInt(difficultyFileIn.getString());

		// if the board is in the easiest difficulty level
		if(boardSize.equals("EASY"))
		{
			// create a new background with the proper number of rows and columns, and the proper time information
			background = new Background(Integer.parseInt(difficultyFileIn.getString()), 8, 8, 10, timeData);
		}

		// if the board is in the second easiest difficulty level
		else if(boardSize.equals("MEDIUM"))
		{
			// create a new background with the proper number of rows and columns, and the proper time information
			background = new Background(Integer.parseInt(difficultyFileIn.getString()), 16, 16, 40, timeData);
		}

		// if the board is in the hardest difficulty level
		else
		{
			// create a new background with the proper number of rows and columns, and the proper time information
			background = new Background(Integer.parseInt(difficultyFileIn.getString()), 30, 16, 99, timeData);
		}

		// create a new timer object to count the number of seconds elapsed
		timer = new Timer(DELAY, timerListener);
	}

	/*
	 * run() runs the threaded object.
	 */
	public void run()
	{
		// create the "back buffer"
		canvas.createBufferStrategy(2);

		// get the buffer from the canvas
		BufferStrategy buffer = canvas.getBufferStrategy();

		while(true)
		{
			// run the game
			updateGame(buffer);
		}
	}

	/*
	 * updateGame(BufferStrategy buffer) is located inside an infinite loop and will continue
	 * to update and print out the most current infomation to the user.
	 */
	public void updateGame(BufferStrategy buffer)
	{
		// get the graphics component from the buffer
		g = (Graphics2D) buffer.getDrawGraphics();

		// smooth out the graphics component
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw in the background of the window, a plain white
		g.setColor(Color.white);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// draw the background object, given the graphics component and several instance variables
		// containing information about the state of the game
		background.drawBackground(g, seconds, timeFileIn.getString(), difficultyFileIn.getString());

		// dispose of the graphics component
		g.dispose();

		// display the buffer
		buffer.show();
	}

	/*
	 * mouseDragged(MouseEvent me) updates the x and y position of the mouse cursor when
	 * the cursor is being dragged.
	 */
	public void mouseDragged(MouseEvent me)
	{
		// update mouse positions
		mouseXPos = me.getX();
		mouseYPos = me.getY();
	}

	/*
	 * mouseMoved(MouseEvent me) updates the x and y position of the mouse cursor when the
	 * cursor is being moved, but not dragged
	 */
	public void mouseMoved(MouseEvent me)
	{
		// update mouse positions
		mouseXPos = me.getX();
		mouseYPos = me.getY();
	}

	/*
	 * mouseClicked(MouseEvent me) is not implemented.
	 */
	public void mouseClicked(MouseEvent me) {}

	/*
	 * mouseEntered(MouseEvent me) is activated when the mouse is moved onto the canvas.
	 */
	public void mouseEntered(MouseEvent me)
	{
		// if the mouse cursor was not on the canvas
		if(mouseOnCanvas == false)
		{
			// the mouse cursor is now on the canvas
			mouseOnCanvas = true;
		}
	}

	/*
	 * mouseExited(MouseEvent me) is activated when the mouse is moved off of the canvas.
	 */
	public void mouseExited(MouseEvent me)
	{
		// if the mouse cursor was on the canvas
		if(mouseOnCanvas == true)
		{
			// the mouse cursor is now off the canvas
			mouseOnCanvas = false;
		}
	}

	/*
	 * mousePressed(MouseEvent me) handles events when the mouse cursor is clicked and held
	 * down.
	 */
	public void mousePressed(MouseEvent me)
	{
		// if the user right clicks, the mouse is on the canvas, and the cursor is not directly above one of the lines dividing
		// the puzzle into a grid
		if(me.isMetaDown() == true && mouseOnCanvas == true && mouseXPos > 200 && (mouseXPos - 200)%30 != 0 && mouseYPos%30 != 0)
		{
			// store that the user has right clicked
			metaDown = true;
		}

		// if the user is not right clicking, the mouse is on the canvas, and the cursor is not directly above one of the lines
		// dividing the puzzle into a grid
		else if(me.isMetaDown() == false && mouseOnCanvas == true && mouseXPos > 200 && (mouseXPos - 200)%30 != 0 && mouseYPos%30 != 0)
		{
			// if the square has not already been shown, depress the square, but if it has, depress its adjacent squares
			background.showSquare((mouseXPos - 200)/30, mouseYPos/30, false);

			// store that the user is depressing a square
			squareDepressed = true;
		}

		// if the user is not right clicking, the mouse is on the canvas, and the cursor is above the first game mode option that
		// allows the user to switch to the easiest level difficulty
		else if(me.isMetaDown() == false && mouseOnCanvas == true && mouseXPos > 140 && mouseXPos < 190 && mouseYPos > 5 && mouseYPos < 25)
		{
			// depress this first option box
			background.depressOption(1);
		}

		// if the user is not right clicking, the mouse is on the canvas, and the cursor is above the second game mode option that
		// allows the user to switch to the second easiest level difficulty
		else if(me.isMetaDown() == false && mouseOnCanvas == true && mouseXPos > 140 && mouseXPos < 190 && mouseYPos > 30 && mouseYPos < 50)
		{
			// depress this second option box
			background.depressOption(2);
		}

		// if the user is not right clicking, the mouse is on the canvas, and the cursor is above the third game mode option that
		// allows the user to switch to the hardest level difficulty
		else if(me.isMetaDown() == false && mouseOnCanvas == true && mouseXPos > 140 && mouseXPos < 190 && mouseYPos > 55 && mouseYPos < 75)
		{
			// depress this third option box
			background.depressOption(3);
		}

		// make the sun open his mouth
		background.depressSun();
	}

	/*
	 * mouseReleased(MouseEvent me) handles events when the mouse cursor is released from being held down.
	 */
	public void mouseReleased(MouseEvent me)
	{
		// if the user has right clicked, the mouse is on the canvas, and the cursor is not directly above one of the
		// lines dividing the puzzle into a grid
		if(metaDown == true && mouseOnCanvas == true && mouseXPos > 200 && (mouseXPos - 200)%30 != 0 && mouseYPos%30 != 0)
		{
			// store that the user has released his right click
			metaDown = false;

			// add a flag to the location where the user first right clicked
			background.addOrRemoveFlag((mouseXPos - 200)/30, mouseYPos/30);
		}

		// if the user was not right clicking, the user has depressed a square, the mouse is on the canvas, and cursor
		// is not directly above one of the lines dividing the puzzle into a grid
		if(metaDown == false && squareDepressed == true && mouseOnCanvas == true && mouseXPos > 200 && (mouseXPos - 200)%30 != 0 && mouseYPos%30 != 0)
		{
			// if the square has not already been shown, show the square, but if it has, and all of its mines have been
			// marked correctly, show all adjacent squares, and if the mines have been incorrectly marked, respond accordingly
			background.showSquare((mouseXPos - 200)/30, mouseYPos/30, true);

			// if the timer has not yet been started, and the user shows a square properly
			if(timerStarted == false && background.showSquare((mouseXPos - 200)/30, mouseYPos/30, true) == true)
			{
				// start the timer
				timer.start();

				// store that the timer has been started
				timerStarted = true;
			}
		}

		// if the user was not right clicking, the user has depressed a square, the mouse is on the canvas, and cursor
		// is above the first option allowing the user to select the easiest difficulty
		else if(metaDown == false && mouseOnCanvas == true && mouseXPos > 140 && mouseXPos < 190 && mouseYPos > 5 && mouseYPos < 25)
		{
			// if the first option has been depressed
			if(background.isOptionDepressed(1) == true)
			{
				// resize the frame
				app.setSize(441, 503);

				// reset the file names of the storage files to their appropriate file names
				timeData = "timeDataEasy.txt";
				difficultyData = "3BVDataEasy.txt";

				// create new file input and output objects
				timeFileIn = new FileIn(timeData);
				difficultyFileIn = new FileIn(difficultyData);

				// create a new background with the easiest board size layout
				background = new Background(Integer.parseInt(difficultyFileIn.getString()), 8, 8, 10, timeData);

				// stop the timer
				timer.stop();

				// store that the timer is not started
				timerStarted = false;

				// reset the number of seconds to zero
				seconds = 0;

				// read in from the file what the current low time score and high difficulty score are
				lowSeconds = Integer.parseInt(timeFileIn.getString());
				highest3BV = Integer.parseInt(difficultyFileIn.getString());
			}
		}

		// if the user was not right clicking, the user has depressed a square, the mouse is on the canvas, and cursor
		// is above the second option allowing the user to select the second easiest difficulty
		else if(metaDown == false && mouseOnCanvas == true && mouseXPos > 140 && mouseXPos < 190 && mouseYPos > 30 && mouseYPos < 50)
		{
			// if the second option has been depressed
			if(background.isOptionDepressed(2) == true)
			{
				// resize the frame
				app.setSize(681, 503);

				// reset the file names of the storage files to their appropriate file names
				timeData = "timeDataMedium.txt";
				difficultyData = "3BVDataMedium.txt";

				// create new file input and output objects
				timeFileIn = new FileIn(timeData);
				difficultyFileIn = new FileIn(difficultyData);

				// create a new background with the easiest board size layout
				background = new Background(Integer.parseInt(difficultyFileIn.getString()), 16, 16, 40, timeData);

				// stop the timer
				timer.stop();

				// store that the timer is not started
				timerStarted = false;

				// reset the number of seconds to zero
				seconds = 0;

				// read in from the file what the current low time score and high difficulty score are
				lowSeconds = Integer.parseInt(timeFileIn.getString());
				highest3BV = Integer.parseInt(difficultyFileIn.getString());
			}
		}

		// if the user was not right clicking, the user has depressed a square, the mouse is on the canvas, and cursor
		// is above the third option allowing the user to select the hardest difficulty
		else if(me.isMetaDown() == false && mouseOnCanvas == true && mouseXPos > 140 && mouseXPos < 190 && mouseYPos > 55 && mouseYPos < 75)
		{
			// if the third option has been depressed
			if(background.isOptionDepressed(3) == true)
			{
				// resize the frame
				app.setSize(1101, 503);

				// reset the file names of the storage files to their appropriate file names
				timeData = "timeDataHard.txt";
				difficultyData = "3BVDataHard.txt";

				// create new file input and output objects
				timeFileIn = new FileIn(timeData);
				difficultyFileIn = new FileIn(difficultyData);

				// create a new background with the easiest board size layout
				background = new Background(Integer.parseInt(difficultyFileIn.getString()), 30, 16, 99, timeData);

				// stop the timer
				timer.stop();

				// store that the timer is not started
				timerStarted = false;

				// reset the number of seconds to zero
				seconds = 0;

				// read in from the file what the current low time score and high difficulty score are
				lowSeconds = Integer.parseInt(timeFileIn.getString());
				highest3BV = Integer.parseInt(difficultyFileIn.getString());
			}
		}

		// if the mouse is on the canvas and the mouse is above the sun
		else if(mouseOnCanvas == true && (Math.pow(mouseXPos - 100, 2) + Math.pow(mouseYPos - 40, 2) <= 625))
		{
			// reset the background with the current game settings
			background = new Background(Integer.parseInt(difficultyFileIn.getString()), background.getMyColumns(),
														 background.getMyRows(), background.getNumMines(), timeData);

			// stop the timer
			timer.stop();

			// store that the timer is not started
			timerStarted = false;

			// reset the number of seconds to zero
			seconds = 0;
		}

		// if the game has been won
		if(background.isGameWon() == true)
		{
			// if the number of seconds in the current game is lower than the low score
			// recorded in the file
			if(seconds < Integer.parseInt(timeFileIn.getString()))
			{
				// store that the low number of seconds is the number of seconds that
				// it took to complete this game
				lowSeconds = seconds;
			}

			// if the difficulty of the puzzle that was solved is greater than the highest
			// difficulty recorded in the file
			if(background.get3BV() > Integer.parseInt(difficultyFileIn.getString()))
			{
				// store that the highest difficulty is the difficulty of the puzzle
				// just completed
				highest3BV = background.get3BV();
			}

			// write to the files with eiter the new information or the same information,
			// depending on whether new high or low scores were set
			timeFileOut = new FileOut(timeData, "" + lowSeconds);
			difficultyFileOut = new FileOut(difficultyData, "" + highest3BV);

			// create new input files with this new information
			timeFileIn = new FileIn(timeData);
			difficultyFileIn = new FileIn(difficultyData);
		}

		// reset all squares so that none are depressed
		background.clearDepressedSquares();

		// store that no squares are depressed
		squareDepressed = false;
	}
}
