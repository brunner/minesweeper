// Joseph Brunner
// Project 4

/*
 * Background represents the "background" of the window and displays all
 * of the information in and regarding the game, such as the actual puzzle,
 * the statistics, and the option buttons.
 */

import java.awt.*;

public class Background
{
	// declare color constants
	private final Color backgroundGrey = new Color(150, 150, 150);
	private final Color lightGrey = new Color(200, 200, 200);
	private final Color offWhite = new Color(220, 220, 220);
	private final Color yellow = new Color(250, 250, 0);
	private final Color blue = new Color(20, 100, 244);
	private final Color green = new Color(62, 160, 85);
	private final Color red = new Color(178, 34, 34);
	private final Color navy = new Color(0, 0, 100);
	private final Color brown = new Color(100, 50, 0);
	private final Color cyan = new Color(0, 200, 250);
	private final Color black = new Color(0, 0, 0);
	private final Color grey = new Color(130, 130, 130);

	// board is a two-dimensional array that is the size of the current puzzle, with corresponding
	// rows and columns, and is filled with numbers that correspond to the number of mines in each
	// adjacent "square"
	private int[][] board;

	// isCovered is a two-dimensional array that is the size of the current puzzle, with corresponding
	// rows and columns, and is filled with booleans such that an element is true when the "square"
	// is concealed and false when the number or mine has been shown
	private boolean[][] isCovered;

	// isCovered is a two-dimensional array that is the size of the current puzzle, with corresponding
	// rows and columns, and is filled with booleans such that an element is true when the "square"
	// has been marked with a flag and false otherwise
	private boolean[][] hasFlag;

	// isCovered is a two-dimensional array that is the size of the current puzzle, with corresponding
	// rows and columns, and is filled with booleans such that an element is true when the "square"
	// is being depressed and false otherwise
	private boolean[][] depressedSquares;

	// isCovered is a two-dimensional array that is the size of the current puzzle, with corresponding
	// rows and columns, and is filled with booleans such that an element is true when the "square"
	// clicked had a mine concealed, resulting in the end of the game, and false otherwise
	private boolean[][] wrongMove;

	// isCovered is a two-dimensional array that is the size of the current puzzle, with corresponding
	// rows and columns, and is filled with booleans such that an element is true when the "square"
	// has been marked with a flag but the "square" is not concealing a mine, and false otherwise
	private boolean[][] wrongFlag;

	// isCovered is a two-dimensional array that is the size of the current puzzle, with corresponding
	// rows and columns, and is filled with booleans such that an element is true when the "square"
	// clicked has also shown each of its adjacent squares, and false otherwise
	private boolean[][] hasShownAdjacentSquares;

	// isCovered is a single-dimensional array that holds booleans such that an element is true when
	// its corresponding option "button" is being depressed, and false otherwise
	private boolean[] isOptionDepressed;

	// gameOver is true when the game has ended
	private boolean gameOver;

	// gameWon is true when the game has been won
	private boolean gameWon;

	// sunGasp is true when the sun's mouth is open
	private boolean sunGasp;

	// rows stores the number of rows in the current puzzle
	private int rows;

	// columns stores the number of columns in the current puzzle
	private int columns;

	// numMines stores the number of mines in the current puzzle
	private int numMines;

	// flagsLeft stores the number of mines that have not yet been marked with flags
	private int flagsLeft;

	// numMinesMarked stores the number of mines that have been marked
	private int numMinesMarked;

	// numMinesUnmarked stores the number of mines that have not been marked yet
	private int numMinesUnmarked;

	// numSquaresCovered stores the number of non-mines that are still concealed
	private int numSquaresCovered;

	// depressedSquareX and depressedSquareY store the indices in the array for the
	// location of the mouse cursor when the mouse was clicked, this makes it so that
	// we can check that the mouse is still over the same square when the mouse button
	// is released
	private int depressedSquareX, depressedSquareY;

	// numClicks represents the difficulty of the current puzzle, and is calculated by
	// taking the least number of clicks to clear the puzzle, it is also called the 3BV
	private int numClicks;

	/*
	 * Background(int hardestPuzzleSolved, ... , String timeData) constructs a new Background object.
	 */
	public Background(int hardestPuzzleSolved, int columns, int rows, int numMines, String timeData)
	{
		// initialize each two-dimensional array to be the size of the current board
		board = new int[rows][columns];
		isCovered = new boolean[rows][columns];
		hasFlag = new boolean[rows][columns];
		depressedSquares = new boolean[rows][columns];
		wrongMove = new boolean[rows][columns];
		wrongFlag = new boolean[rows][columns];
		hasShownAdjacentSquares = new boolean[rows][columns];

		// there are three option buttons, one for each difficulty level
		isOptionDepressed = new boolean[3];

		// initialize instance variables
		this.numMines = numMines;
		this.rows = rows;
		this.columns = columns;
		flagsLeft = numMines;

		// initialize instance variables
		gameOver = false;
		gameWon = false;
		sunGasp = false;

		// initialize intstance variables
		numMinesMarked = 0;
		numMinesUnmarked = 0;
		numSquaresCovered = 0;
		depressedSquareX = 0;
		depressedSquareY = 0;

		// reset the board
		clearBoard();

		// place the mines randomly on the board
		placeMines();

		// calculate the number of mines bordering each "square"
		calculateNumMines();

		// store the difficulty of the current puzzle
		numClicks = calculate3BV(board);
	}

	/*
	 * clearBoard() resets instance variables pertaining to the "board" itself.
	 */
	private void clearBoard()
	{
		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// there are no mines surrounding this square
				board[j][i] = 0;

				// the square is concealed
				isCovered[j][i] = true;

				// the square does not have a flag marking it
				hasFlag[j][i] = false;

				// no wrong move has been made at this square
				wrongMove[j][i] = false;

				// no wrong flag has been placed at this square
				wrongFlag[j][i] = false;
			}
		}

		// for each option button
		for(int i = 0; i < 3; i++)
		{
			// the button is not depressed
			isOptionDepressed[i] = false;
		}

		// no squares are depressed
		clearDepressedSquares();

		// no adjacent squares have been shown
		clearAdjacentSquares();
	}

	/*
	 * clearDepressedSquares() resets the arrays and variables containing information
	 * about which items in the window are "depressed" or not.
	 */
	public void clearDepressedSquares()
	{
		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// the "square" is no longer depressed
				depressedSquares[j][i] = false;
			}
		}

		// for each option button
		for(int i = 0; i < 3; i++)
		{
			// the button is no longer depressed
			isOptionDepressed[i] = false;
		}

		// the sun's mouth is no longer open
		sunGasp = false;
	}

	/*
	 * clearAdjacentSquares() resets the array containing information about which squares
	 * have adjacent squares "depressed".
	 */
	public void clearAdjacentSquares()
	{
		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// the "square" does not have any adjacent squares that are depressed
				hasShownAdjacentSquares[j][i] = false;
			}
		}
	}

	// placeMines() randomly places a given number of mines on the grid.
	private void placeMines()
	{
		// the number of mines placed, initialized to zero
		int numMinesPlaced = 0;

		// the x and y indices of the potential location of the mine
		int x, y;

		// while we have not placed all of the mines
		while(numMinesPlaced < numMines)
		{
			// randomly choose an x index that corresponds to the
			// number of columns
			x = (int)(Math.random()*columns);

			// randomly choose a y index that corresponds to the
			// number of rows
			y = (int)(Math.random()*rows);

			// if that location does not already have a mine placed
			if(board[y][x] != -1)
			{
				// place a mine at that location
				board[y][x] = -1;

				// we have placed a mine, so update the number of mines
				// placed
				numMinesPlaced++;
			}
		}
	}

	// calculateNumMines() finds the number of adjacent squares to each
	// non-mine square that are occupied by mines.
	private void calculateNumMines()
	{
		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// if the "square" does not have a mine
				if(board[j][i] != -1)
				{
					// if there is a mine located to the upper left of the square
					if(j > 0 && i > 0 && board[j - 1][i - 1] == -1)
					{
						// increment the number of mines adjacent to this square
						board[j][i]++;
					}

					// if there is a mine above the square
					if(j > 0 && board[j - 1][i] == -1)
					{
						// increment the number of mines adjacent to this square
						board[j][i]++;
					}

					// if there is a mine located to the upper right of the square
					if(j > 0 && i < columns - 1 && board[j - 1][i + 1] == -1)
					{
						// increment the number of mines adjacent to this square
						board[j][i]++;
					}

					// if there is a mine located to the left of the square
					if(i > 0 && board[j][i - 1] == -1)
						// increment the number of mines adjacent to this square
						board[j][i]++;

					// if there is a mine located to the right of the square
					if(i < columns - 1 && board[j][i + 1] == -1)
					{
						// increment the number of mines adjacent to this square
						board[j][i]++;
					}

					// if there is a mine located to the lower right of the square
					if(j < rows - 1 && i > 0 && board[j + 1][i - 1] == -1)
					{
						board[j][i]++;
					}

					// if there is a mine located below the square
					if(j < rows - 1 && board[j + 1][i] == -1)
					{
						// increment the number of mines adjacent to this square
						board[j][i]++;
					}

					// if there is a mine located to the lower right of the square
					if(j < rows - 1 && i < columns - 1 && board[j + 1][i + 1] == -1)
					{
						// increment the number of mines adjacent to this square
						board[j][i]++;
					}
				}
			}
		}
	}

	/*
	 * calculateBoard(int[][] thisBoard) finds the 3BV value of the current puzzle.  3BV
	 * is a common measure of board difficulty and stands for Bechtel's Board Benchmark Value.
	 * It is the sum of all "openings in the board" (where the user can click a space that has
	 * no adjacent mines and opens up an area, and all other numbered squares that are not a
	 * part of an opening.  Therefore, the 3BV is also the least number of clicks it would take
	 * to solve the puzzle, assuming the player marks no mines.
	 */
	private int calculate3BV(int[][] thisBoard)
	{
		// we initialize the number of clicks to zero
		int myNumClicks = 0;

		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// if the board has no adjacent mines and is still covered
				if(thisBoard[j][i] == 0 && isCovered[j][i] == true)
				{
					// show the "opening" by opening up all adjacent squares
					showAdjacentSquares(i, j);

					// increment the number of clicks
					myNumClicks++;
				}
			}
		}

		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// if the square does not have a mine and is still covered,
				// which would mean that it is not part of any "opening"
				if(thisBoard[j][i] != -1 && isCovered[j][i] == true)
				{
					// open this square
					isCovered[j][i] = false;

					// increment the number of clicks
					myNumClicks++;
				}
			}
		}

		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// conceal this square again
				isCovered[j][i] = true;
			}
		}

		// clear any adjacent squares that are stored
		clearAdjacentSquares();

		// return the number of clicks, which will be equivalent to the 3BV of
		// the board
		return myNumClicks;
	}

	/*
	 * addOrRemoveFlag(int xIndex, int yIndex) either adds a flag to mark a mine or
	 * removes a flag that was already placed to mark a mine.
	 */
	public void addOrRemoveFlag(int xIndex, int yIndex)
	{
		// if the game is not over
		if(gameOver == false)
		{
			// if the square is covered, does not already have a flag, and the user still has
			// flags remaining to be placed
			if(isCovered[yIndex][xIndex] == true && hasFlag[yIndex][xIndex] == false && flagsLeft > 0)
			{
				// place a flag at this location
				hasFlag[yIndex][xIndex] = true;

				// decrement the number of flags the user has left
				flagsLeft--;
			}

			// if the square is covered and already has a flag
			else if(isCovered[yIndex][xIndex] == true && hasFlag[yIndex][xIndex] == true)
			{
				// remove the flag from this location
				hasFlag[yIndex][xIndex] = false;

				// increment the number of flags the user has left
				flagsLeft++;
			}

			// clear any depressed squares stored in memory
			clearDepressedSquares();
		}
	}

	/*
	 * depressSun() makes the sun open his mouth whenever the mouse is clicked.
	 */
	public void depressSun()
	{
		// this variable is true when the sun's mouth is open
		sunGasp = true;
	}

	/*
	 * showSquare(int xIndex, ... , boolean depressSquare) "shows" the square being
	 * clicked on and any potential implications of clicking that square, including
	 * additional squares being revealed, or the game ending for several different
	 * reasons.  The variable depressSquare is false when the user is clicking down
	 * on a square for the first time, and then true after the user releases the
	 * mouse button and squares have been depressed already. showSquare(int xIndex, ... ,
	 * boolean depressSquare) returns true when a square is revealed properly.
	 */
	public boolean showSquare(int xIndex, int yIndex, boolean depressSquare)
	{
		// if the game is not over and not won
		if(gameOver == false && gameWon == false)
		{
			// if the user is clicking down on a square
			if(depressSquare == false)
			{
				// if the square is concealed and does not have a flag
				if(isCovered[yIndex][xIndex] == true && hasFlag[yIndex][xIndex] == false)
				{
					// depress that square
					depressedSquares[yIndex][xIndex] = true;
				}

				// if the square is not concealed
				else if(isCovered[yIndex][xIndex] == false)
				{
					// depress the squares surrounding the square clicked on
					depressSurroundingSquares(xIndex, yIndex);
				}

				// store the indices of the square that has been clicked on for future
				// reference
				depressedSquareX = xIndex;
				depressedSquareY = yIndex;

				// we have not shown squares "properly"
				return false;
			}

			// if the use is releasing the mouse button
			else
			{
				// if the cursor is still hovering over the same square
				if(depressedSquareX == xIndex && depressedSquareY == yIndex)
				{
					// if the square is still covered, does not have a flag, and is not hiding a mine
					if(isCovered[yIndex][xIndex] == true && hasFlag[yIndex][xIndex] == false && board[yIndex][xIndex] != -1)
					{
						// show the square
						isCovered[yIndex][xIndex] = false;

						// clear any depressed squares stored in memory
						clearDepressedSquares();

						// if the board does not have any adjacent mines, show its "opening"
						if(board[yIndex][xIndex] == 0)
						{
							// display the squares adjacent to any squares that have no adjacent
							// mines
							showAdjacentSquares(xIndex, yIndex);
						}
					}

					// if the square is still covered, does not have a flag, but does have a mine
					else if(isCovered[yIndex][xIndex] == true && hasFlag[yIndex][xIndex] == false && board[yIndex][xIndex] == -1)
					{
						// show the square
						isCovered[yIndex][xIndex] = false;

						// clear any depressed squares stored in memory
						clearDepressedSquares();

						// store that a wrong move has been made
						wrongMove[yIndex][xIndex] = true;

						// store that the game is now over
						gameOver = true;

						// for each column
						for(int i = 0; i < columns; i++)
						{
							// for each row in that column
							for(int j = 0; j < rows; j++)
							{
								// if the board has a mine at that location that is not marked by a flag
								if(board[j][i] == -1 && hasFlag[j][i] == false)
								{
									// show the location of that mine
									isCovered[j][i] = false;
								}
							}
						}
					}

					// if the square is covered and has a flag
					else if(isCovered[yIndex][xIndex] == true && hasFlag[yIndex][xIndex] == true)
					{
						// clear any depressed squares stored in memory
						clearDepressedSquares();
					}

					// if the square is not covered
					else if(isCovered[yIndex][xIndex] == false)
					{
						// check for any adjacent squares that could be shown, and if
						// they could be, then show these squares
						checkAndShowAdjacentSquares(xIndex, yIndex, true);

						// clear any depressed squares stored in memory
						clearDepressedSquares();
					}

					// check to see if the game has been won
					checkGameIfWon();

					// we have shown squares "properly"
					return true;
				}
			}
		}

		// we have not shown squares "properly"
		return false;
	}

	/*
	 * depressSurroundingSquares(int xIndex, int yIndex) depresses the squares around a given location.
	 */
	private void depressSurroundingSquares(int xIndex, int yIndex)
	{
		// if the square to the upper left of the square that has been clicked is still covered and
		// does not have a flag
		if(xIndex > 0 && yIndex > 0 && isCovered[yIndex - 1][xIndex - 1] == true && hasFlag[yIndex - 1][xIndex - 1] == false)
			depressedSquares[yIndex - 1][xIndex - 1] = true;

		// if the square above the square that has been clicked is still covered and does not have a flag
		if(yIndex > 0 && isCovered[yIndex - 1][xIndex] == true && hasFlag[yIndex - 1][xIndex] == false)
		{
			// depress this square
			depressedSquares[yIndex - 1][xIndex] = true;
		}

		// if the square to the upper right of the square that has been clicked is still covered and
		// does not have a flag
		if(xIndex < columns - 1 && yIndex > 0 && isCovered[yIndex - 1][xIndex + 1] == true && hasFlag[yIndex - 1][xIndex + 1] == false)
		{
			// depress this square
			depressedSquares[yIndex - 1][xIndex + 1] = true;
		}


		// if the square to the left of the square that has been clicked is still covered and does not
		// have a flag
		if(xIndex > 0 && isCovered[yIndex][xIndex - 1] == true && hasFlag[yIndex][xIndex - 1] == false)
		{
			// depress this square
			depressedSquares[yIndex][xIndex - 1] = true;
		}

		// if the square to the right of the square that has been clicked is still covered and does not
		// have a flag
		if(xIndex < columns - 1 && isCovered[yIndex][xIndex + 1] == true && hasFlag[yIndex][xIndex + 1] == false)
		{
			// depress this square
			depressedSquares[yIndex][xIndex + 1] = true;
		}

		// if the square to the lower left of the square that has been clicked is still covered and
		// does not have a flag
		if(xIndex > 0 && yIndex < rows - 1 && isCovered[yIndex + 1][xIndex - 1] == true && hasFlag[yIndex + 1][xIndex - 1] == false)
		{
			// depress this square
			depressedSquares[yIndex + 1][xIndex - 1] = true;
		}

		// if the square below the square that has been clicked is still covered and does not have a flag
		if(yIndex < rows - 1 && isCovered[yIndex + 1][xIndex] == true && hasFlag[yIndex + 1][xIndex] == false)
		{
			// depress this square
			depressedSquares[yIndex + 1][xIndex] = true;
		}

		// if the square to the lower right of the square that has been clicked is still covered and
		// does not have a flag
		if(xIndex < columns - 1 && yIndex < rows - 1 && isCovered[yIndex + 1][xIndex + 1] == true && hasFlag[yIndex + 1][xIndex + 1] == false)
		{
			// depress this square
			depressedSquares[yIndex + 1][xIndex + 1] = true;
		}
	}

	/*
	 * showAdjacentSquares(int xIndex, int yIndex) shows any adjacent squares to
	 * squares that have been clicked on, and also opens up any openings on the
	 * board by recursively calling this method.
	 */
	private void showAdjacentSquares(int xIndex, int yIndex)
	{
		// if the square clicked on has a square to its upper left
		if(yIndex > 0 && xIndex > 0)
		{
			// show this square
			isCovered[yIndex - 1][xIndex - 1] = false;
		}

		// if the square clicked on has a square above it
		if(yIndex > 0)
		{
			// show this square
			isCovered[yIndex - 1][xIndex] = false;
		}

		// if the square clicked on has a square to its upper right
		if(yIndex > 0 && xIndex < columns - 1)
		{
			// show this square
			isCovered[yIndex - 1][xIndex + 1] = false;
		}

		// if the square clicked on has a square to its left
		if(xIndex > 0)
		{
			// show this square
			isCovered[yIndex][xIndex - 1] = false;
		}

		// if the square clicked on has a square to its right
		if(xIndex < columns - 1)
		{
			// show this square
			isCovered[yIndex][xIndex + 1] = false;
		}

		// if the square clicked on has a square to its lower left
		if(yIndex < rows - 1 && xIndex > 0)
		{
			// show this square
			isCovered[yIndex + 1][xIndex - 1] = false;
		}

		// if the square clicked on has a square below it
		if(yIndex < rows - 1)
		{
			// show this square
			isCovered[yIndex + 1][xIndex] = false;
		}

		// if the square clicked on has a square to its lower right
		if(yIndex < rows - 1 && xIndex < columns - 1)
		{
			// show this square
			isCovered[yIndex + 1][xIndex + 1] = false;
		}

		// store that we have shown any adjacent squares to the clicked square
		hasShownAdjacentSquares[yIndex][xIndex] = true;

		// if the square to the upper left of the square that was clicked has no adjacent mines either
		// and it has not yet shown its adjacent squares
		if(yIndex > 0 && xIndex > 0 && board[yIndex - 1][xIndex - 1] == 0 && hasShownAdjacentSquares[yIndex - 1][xIndex - 1] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex - 1, yIndex - 1);
		}

		// if the square above the square that was clicked has no adjacent mines either and it has not
		// yet shown its adjacent squares
		if(yIndex > 0 && board[yIndex - 1][xIndex] == 0 && hasShownAdjacentSquares[yIndex - 1][xIndex] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex, yIndex - 1);
		}

		// if the square to the upper right of the square that was clicked has no adjacent mines either
		// and it has not yet shown its adjacent squares
		if(yIndex > 0 && xIndex < columns - 1 && board[yIndex - 1][xIndex + 1] == 0 && hasShownAdjacentSquares[yIndex - 1][xIndex + 1] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex + 1, yIndex - 1);
		}

		// if the square to the left of the square that was clicked has no adjacent mines either
		// and it has not yet shown its adjacent squares
		if(xIndex > 0 && board[yIndex][xIndex - 1] == 0 && hasShownAdjacentSquares[yIndex][xIndex - 1] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex - 1, yIndex);
		}

		// if the square to the right of the square that was clicked has no adjacent mines either
		// and it has not yet shown its adjacent squares
		if(xIndex < columns - 1 && board[yIndex][xIndex + 1] == 0 && hasShownAdjacentSquares[yIndex][xIndex + 1] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex + 1, yIndex);
		}

		// if the square to the lower left of the square that was clicked has no adjacent mines either
		// and it has not yet shown its adjacent squares
		if(yIndex < rows - 1 && xIndex > 0 && board[yIndex + 1][xIndex - 1] == 0 && hasShownAdjacentSquares[yIndex + 1][xIndex - 1] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex - 1, yIndex + 1);
		}

		// if the square below the square that was clicked has no adjacent mines either and it has not
		// yet shown its adjacent squares
		if(yIndex < rows - 1 && board[yIndex + 1][xIndex] == 0 && hasShownAdjacentSquares[yIndex + 1][xIndex] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex, yIndex + 1);
		}

		// if the square to the lower right of the square that was clicked has no adjacent mines either
		// and it has not yet shown its adjacent squares
		if(yIndex < rows - 1 && xIndex < columns - 1 && board[yIndex + 1][xIndex + 1] == 0 && hasShownAdjacentSquares[yIndex + 1][xIndex + 1] == false)
		{
			// show any adjacent squares to this square
			showAdjacentSquares(xIndex + 1, yIndex + 1);
		}
	}

	/*
	 * checkAndShowAdjacentSquares(int xIndex, ... , boolean depressSquare) checks to see if
	 * any clicked squares that have already been opened and marked with flags have the flags
	 * correctly placed.  If they do, then any other covered squares that are adjacent to the
	 * clicked square are then revealed.
	 */
	private void checkAndShowAdjacentSquares(int xIndex, int yIndex, boolean depressSquare)
	{
		// initialize both the number of flags correctly placed and incorrectly placed to zero
		int numCorrectFlags = 0, numIncorrectFlags = 0;

		// if the square to the upper left of the clicked square has a flag
		if(yIndex > 0 && xIndex > 0 && hasFlag[yIndex - 1][xIndex - 1] == true)
		{
			// if the square has a mine
			if(board[yIndex - 1][xIndex - 1] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the square above the clicked square has a flag
		if(yIndex > 0 && hasFlag[yIndex - 1][xIndex] == true)
		{
			// if the square has a mine
			if(board[yIndex - 1][xIndex] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the square to the upper right of the clicked square has a flag
		if(yIndex > 0 && xIndex < columns - 1 && hasFlag[yIndex - 1][xIndex + 1] == true)
		{
			// if the square has a mine
			if(board[yIndex - 1][xIndex + 1] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the square to the left of the clicked square has a flag
		if(xIndex > 0 && hasFlag[yIndex][xIndex - 1] == true)
		{
			// if the square has a mine
			if(board[yIndex][xIndex - 1] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the square to the right of the clicked square has a flag
		if(xIndex < columns - 1 && hasFlag[yIndex][xIndex + 1] == true)
		{
			// if the square has a mine
			if(board[yIndex][xIndex + 1] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the square to the lower left of the clicked square has a flag
		if(yIndex < rows - 1 && xIndex > 0 && hasFlag[yIndex + 1][xIndex - 1] == true)
		{
			// if the square has a mine
			if(board[yIndex + 1][xIndex - 1] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the square below the clicked square has a flag
		if(yIndex < rows - 1 && hasFlag[yIndex + 1][xIndex] == true)
		{
			// if the square has a mine
			if(board[yIndex + 1][xIndex] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the square to the lower right of the clicked square has a flag
		if(yIndex < rows - 1 && xIndex < columns - 1 && hasFlag[yIndex + 1][xIndex + 1] == true)
		{
			// if the square has a mine
			if(board[yIndex + 1][xIndex + 1] == -1)
			{
				// the flag was placed correctly
				numCorrectFlags++;
			}

			// if the square does not have a mine
			else
			{
				// the flag was placed incorrectly
				numIncorrectFlags++;
			}
		}

		// if the number of flags correctly placed is the same as the number of mines adjacent to
		// the square, there were no incorrectly placed flags, and the user is releasing the mouse
		if(numCorrectFlags == board[yIndex][xIndex] && numIncorrectFlags == 0 && depressSquare == true)
		{
			// if the square to the upper left of the clicked square is not a mine
			if(yIndex > 0 && xIndex > 0 && isCovered[yIndex - 1][xIndex - 1] == true && board[yIndex - 1][xIndex - 1] != -1)
			{
				// show this square
				isCovered[yIndex - 1][xIndex - 1] = false;

				// if this square has no adjacent mines
				if(board[yIndex - 1][xIndex - 1] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex - 1, yIndex - 1);
				}
			}

			// if the square above the clicked square is not a mine
			if(yIndex > 0 && isCovered[yIndex - 1][xIndex] == true && board[yIndex - 1][xIndex] != -1)
			{
				// show this square
				isCovered[yIndex - 1][xIndex] = false;

				// if this square has no adjacent mines
				if(board[yIndex - 1][xIndex] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex, yIndex - 1);
				}
			}

			// if the square to the upper right of the clicked square is not a mine
			if(yIndex > 0 && xIndex < columns - 1 && isCovered[yIndex - 1][xIndex + 1] == true && board[yIndex - 1][xIndex + 1] != -1)
			{
				// show this square
				isCovered[yIndex - 1][xIndex + 1] = false;

				// if this square has no adjacent mines
				if(board[yIndex - 1][xIndex + 1] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex + 1, yIndex - 1);
				}
			}

			// if the square to the left of the clicked square is not a mine
			if(xIndex > 0 && isCovered[yIndex][xIndex - 1] == true && board[yIndex][xIndex - 1] != -1)
			{
				// show this square
				isCovered[yIndex][xIndex - 1] = false;

				// if this square has no adjacent mines
				if(board[yIndex][xIndex - 1] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex - 1, yIndex);
				}
			}

			// if the square to the right of the clicked square is not a mine
			if(xIndex < columns - 1 && isCovered[yIndex][xIndex + 1] == true && board[yIndex][xIndex + 1] != -1)
			{
				// show this square
				isCovered[yIndex][xIndex + 1] = false;

				// if this square has no adjacent mines
				if(board[yIndex][xIndex + 1] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex + 1, yIndex);
				}
			}

			// if the square to the lower left of the clicked square is not a mine
			if(yIndex < rows - 1 && xIndex > 0 && isCovered[yIndex + 1][xIndex - 1] == true && board[yIndex + 1][xIndex - 1] != -1)
			{
				// show this square
				isCovered[yIndex + 1][xIndex - 1] = false;

				// if this square has no adjacent mines
				if(board[yIndex + 1][xIndex - 1] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex - 1, yIndex + 1);
				}
			}

			// if the square below the clicked square is not a mine
			if(yIndex < rows - 1 && isCovered[yIndex + 1][xIndex] == true && board[yIndex + 1][xIndex] != -1)
			{
				// show this square
				isCovered[yIndex + 1][xIndex] = false;

				// if this square has no adjacent mines
				if(board[yIndex + 1][xIndex] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex, yIndex + 1);
				}
			}

			// if the square to the lower right of the clicked square is not a mine
			if(yIndex < rows - 1 && xIndex < columns - 1 && isCovered[yIndex + 1][xIndex + 1] == true && board[yIndex + 1][xIndex + 1] != -1)
			{
				// show this square
				isCovered[yIndex + 1][xIndex + 1] = false;

				// if this square has no adjacent mines
				if(board[yIndex + 1][xIndex + 1] == 0)
				{
					// show its adjacent squares
					showAdjacentSquares(xIndex + 1, yIndex + 1);
				}
			}
		}

		// if the number of correctly placed flags is less than the number of mines adjacent to
		// the clicked square, no flags were incorrectly placed, and the user is releasing the mouse
		else if(numCorrectFlags < board[yIndex][xIndex] && numIncorrectFlags == 0 && depressSquare == true)
		{
			// if the square has already been shown
			if(isCovered[yIndex][xIndex] == false)
			{
				// if the square to the upper left of the clicked square is covered
				if(yIndex > 0 && xIndex > 0 && isCovered[yIndex - 1][xIndex - 1] == true)
				{
					// depress this square
					depressedSquares[yIndex - 1][xIndex - 1] = true;
				}

				// if the square above the clicked square is covered
				if(yIndex > 0 && isCovered[yIndex - 1][xIndex] == true)
				{
					// depress this square
					depressedSquares[yIndex - 1][xIndex] = true;
				}

				// if the square to the upper right of the clicked square is covered
				if(yIndex > 0 && xIndex < columns - 1 && isCovered[yIndex - 1][xIndex + 1] == true)
				{
					// depress this square
					depressedSquares[yIndex - 1][xIndex + 1] = true;
				}

				// if the square to the left of the clicked square is covered
				if(xIndex > 0 && isCovered[yIndex][xIndex - 1] == true)
				{
					// depress this square
					depressedSquares[yIndex][xIndex - 1] = true;
				}

				// if the square to the right of the clicked square is covered
				if(xIndex < columns - 1 && isCovered[yIndex][xIndex + 1] == true)
				{
					// depress this square
					depressedSquares[yIndex][xIndex + 1] = true;
				}

				// if the square to the lower left of the clicked square is covered
				if(yIndex < rows - 1 && xIndex > 0 && isCovered[yIndex + 1][xIndex - 1] == true)
				{
					// depress this square
					depressedSquares[yIndex + 1][xIndex - 1] = true;
				}

				// if the square below the clicked square is covered
				if(yIndex < rows - 1 && isCovered[yIndex + 1][xIndex] == true)
				{
					// depress this square
					depressedSquares[yIndex + 1][xIndex] = true;
				}

				// if the square to the lower right of the clicked square is covered
				if(yIndex < rows - 1 && xIndex < columns - 1 && isCovered[yIndex + 1][xIndex + 1] == true)
				{
					// depress this square
					depressedSquares[yIndex + 1][xIndex + 1] = true;
				}
			}

			// otherwise
			else
			{
				// depress the clicked square
				depressedSquares[yIndex][xIndex] = true;
			}
		}

		// if the user has placed any flags incorrectly
		else if(numIncorrectFlags > 0)
		{
			// if the square to the upper left of the clicked square has a flag
			if(yIndex > 0 && xIndex > 0 && hasFlag[yIndex - 1][xIndex - 1] == true)
			{
				// if the square does not have a mine
				if(board[yIndex - 1][xIndex - 1] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex - 1][xIndex - 1] = true;

					// show this square
					isCovered[yIndex - 1][xIndex - 1] = false;
				}
			}

			// if the square above the clicked square has a flag
			if(yIndex > 0 && hasFlag[yIndex - 1][xIndex] == true)
			{
				// if the square does not have a mine
				if(board[yIndex - 1][xIndex] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex - 1][xIndex] = true;

					// show this square
					isCovered[yIndex - 1][xIndex] = false;
				}
			}

			// if the square to the upper right of the clicked square has a flag
			if(yIndex > 0 && xIndex < columns - 1 && hasFlag[yIndex - 1][xIndex + 1] == true)
			{
				// if the square does not have a mine
				if(board[yIndex - 1][xIndex + 1] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex - 1][xIndex + 1] = true;

					// show this square
					isCovered[yIndex - 1][xIndex + 1] = false;
				}
			}

			// if the square to the left of the clicked square has a flag
			if(xIndex > 0 && hasFlag[yIndex][xIndex - 1] == true)
			{
				// if the square does not have a mine
				if(board[yIndex][xIndex - 1] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex][xIndex - 1] = true;

					// show this square
					isCovered[yIndex][xIndex - 1] = false;
				}
			}

			// if the square to the right of the clicked square has a flag
			if(xIndex < columns - 1 && hasFlag[yIndex][xIndex + 1] == true)
			{
				// if the square does not have a mine
				if(board[yIndex][xIndex + 1] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex][xIndex + 1] = true;

					// show this square
					isCovered[yIndex][xIndex + 1] = false;
				}
			}

			// if the square to the lower left of the clicked square has a flag
			if(yIndex < rows - 1 && xIndex > 0 && hasFlag[yIndex + 1][xIndex - 1] == true)
			{
				// if the square does not have a mine
				if(board[yIndex + 1][xIndex - 1] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex + 1][xIndex - 1] = true;

					// show this square
					isCovered[yIndex + 1][xIndex - 1] = false;
				}
			}

			// if the square below the clicked square has a flag
			if(yIndex < rows - 1 && hasFlag[yIndex + 1][xIndex] == true)
			{
				// if the square does not have a mine
				if(board[yIndex + 1][xIndex] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex + 1][xIndex] = true;

					// show this square
					isCovered[yIndex + 1][xIndex] = false;
				}
			}

			// if the square to the lower right of the clicked square has a flag
			if(yIndex < rows - 1 && xIndex < columns - 1 && hasFlag[yIndex + 1][xIndex + 1] == true)
			{
				// if the square does not have a mine
				if(board[yIndex + 1][xIndex + 1] != -1)
				{
					// a flag was placed incorrectly at this square
					wrongFlag[yIndex + 1][xIndex + 1] = true;

					// show this square
					isCovered[yIndex + 1][xIndex + 1] = false;
				}
			}

			// the game is over
			gameOver = true;

			// for each column
			for(int i = 0; i < columns; i++)
			{
				// for each row in that column
				for(int j = 0; j < rows; j++)
				{
					// if the square at this location has a mine but no flag
					if(board[j][i] == -1 && hasFlag[j][i] == false)
					{
						// show the mine at this location
						isCovered[j][i] = false;
					}
				}
			}
		}
	}

	/*
	 * checkGameIfWon() checks to see if the game has been won.
	 */
	private void checkGameIfWon()
	{
		// initialize all counters to zero
		numMinesMarked = 0;
		numMinesUnmarked = 0;
		numSquaresCovered = 0;

		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// if the square has a mine, is still covered, and does not have a flag
				if(board[j][i] == -1 && isCovered[j][i] == true && hasFlag[j][i] == false)
				{
					// increment the number of mines that have not been marked
					numMinesUnmarked++;
				}

				// if the square has a mine, is still covered, and has a flag
				else if(board[j][i] == -1 && isCovered[j][i] == true && hasFlag[j][i] == true)
				{
					// increment the number of mines that have been marked
					numMinesMarked++;
				}

				// if the square does not have a mine, and is still covered
				else if(board[j][i] != -1 && isCovered[j][i] == true)
				{
					// increment the number of squares that are still covered
					numSquaresCovered++;
				}
			}
		}

		// if the number of mines that have been marked and the number of mines that
		// have not been marked add up to equal the total number of mines placed, and
		// no other squares are covered
		if(numMinesUnmarked + numMinesMarked == numMines && numSquaresCovered == 0)
		{
			// for each column
			for(int i = 0; i < columns; i++)
			{
				// for each row in that column
				for(int j = 0; j < rows; j++)
				{
					// if the square has a mine and is covered
					if(board[j][i] == -1 && isCovered[j][i] == true)
					{
						// set it so that the square also has a flag
						hasFlag[j][i] = true;
					}
				}
			}

			// there are no flags left
			flagsLeft = 0;

			// the game has been won
			gameWon = true;
		}
	}

	/*
	 * shouldTimerStop() returns whether or not the timer should stop.
	 */
	public boolean shouldTimerStop()
	{
		// if the game is over or the game is won, return true
		return gameOver || gameWon;
	}

	/*
	 * isGameWon() returns whether or not the game has been won
	 */
	public boolean isGameWon()
	{
		return gameWon;
	}

	/*
	 * printNumber(Graphics g, ... , int height) prints out a number on the
	 * screen in a nice alarm-clock-esque format.
	 */
	public void printNumber(Graphics g, int number, int height)
	{
		// declare three integers to hold the three digits
		int digit1, digit2, digit3;

		// if the number is greater than 99, or has three digits
		if(number > 99)
		{
			// the last digit will be the number modulus 10
			digit3 = number%10;

			// the middle digit will be the number divided by ten and then modulus 10
			digit2 = (number/10)%10;

			// the first digit will be the number divided by 100
			digit1 = number/100;
		}

		// if the number falls between 10 and 99, or has two digits
		else if(number > 9)
		{
			// the last digit will be the number modulus 10
			digit3 = number%10;

			// the middle digit will be the number divided by 10
			digit2 = number/10;

			// the first digit will not have a value
			digit1 = 0;
		}

		// if the number falls between 0 and 9, or has one digit
		else
		{
			// the last digit will be the number
			digit3 = number;

			// the first and second digits will not have a value
			digit2 = 0;
			digit1 = 0;
		}

		// set the text color to red
		g.setColor(red);

		// if the number is greater than 99, or has three digits
		if(number > 99)
		{
			// if the first digit is a number that has the upper left vertical line in their clock number representation
			if(digit1 == 4 || digit1 == 5 || digit1 == 6 || digit1 == 8 || digit1 == 9)
			{
				// fill in this line in the number
				g.fillRect(95, 15 + height, 3, 15);
			}

			// if the first digit is a number that has the upper horizontal line in their clock number representation
			if(digit1 == 2 || digit1 == 3 || digit1 == 5 || digit1 == 6 || digit1 == 7 || digit1 == 8 || digit1 == 9)
			{
				// fill in this line in the number
				g.fillRect(99, 15 + height, 12, 3);
			}

			// if the first digit is a number that has the upper right vertical line in their clock number representation
			if(digit1 == 1 || digit1 == 2 || digit1 == 3 || digit1 == 4 || digit1 == 7 || digit1 == 8 || digit1 == 9)
			{
				// fill in this line in the number
				g.fillRect(112, 15 + height, 3, 15);
			}

			// if the first digit is a number that has the lower right vertical line in their clock number representation
			if(digit1 == 1 || digit1 == 3 || digit1 == 4 || digit1 == 5 || digit1 == 6 || digit1 == 7 || digit1 == 8 || digit1 == 9)
			{
				// fill in this line in the number
				g.fillRect(112, 31 + height, 3, 15);
			}

			// if the first digit is a number that has the lower horizontal line in their clock number representation
			if(digit1 == 2 || digit1 == 3 || digit1 == 5 || digit1 == 6 || digit1 == 8 || digit1 == 9)
			{
				// fill in this line in the number
				g.fillRect(99, 43 + height, 12, 3);
			}

			// if the first digit is a number that has the lower left vertical line in their clock number representation
			if(digit1 == 2 || digit1 == 6 || digit1 == 8)
			{
				// fill in this line in the number
				g.fillRect(95, 31 + height, 3, 15);
			}

			// if the first digit is a number that has the lower horizontal line in their clock number representation
			if(digit1 == 2 || digit1 == 3 || digit1 == 4 || digit1 == 5 || digit1 == 6 || digit1 == 8 || digit1 == 9)
			{
				// fill in this line in the number
				g.fillRect(99, 28 + height, 12, 3);
			}
		}

		// if the number is greater than 9, or has at least two digits
		if(number > 9)
		{
			// if the first digit is a number that has the upper left vertical line in their clock number representation
			if(digit2 == 0 || digit2 == 4 || digit2 == 5 || digit2 == 6 || digit2 == 8 || digit2 == 9)
			{
				// fill in this line in the number
				g.fillRect(120, 15 + height, 3, 15);
			}

			// if the first digit is a number that has the upper horizontal line in their clock number representation
			if(digit2 == 0 || digit2 == 2 || digit2 == 3 || digit2 == 5 || digit2 == 6 || digit2 == 7 || digit2 == 8 || digit2 == 9)
			{
				// fill in this line in the number
				g.fillRect(124, 15 + height, 12, 3);
			}

			// if the first digit is a number that has the upper right vertical line in their clock number representation
			if(digit2 == 0 || digit2 == 1 || digit2 == 2 || digit2 == 3 || digit2 == 4 || digit2 == 7 || digit2 == 8 || digit2 == 9)
			{
				// fill in this line in the number
				g.fillRect(137, 15 + height, 3, 15);
			}

			// if the first digit is a number that has the lower right vertical line in their clock number representation
			if(digit2 == 0 || digit2 == 1 || digit2 == 3 || digit2 == 4 || digit2 == 5 || digit2 == 6 || digit2 == 7 || digit2 == 8 || digit2 == 9)
			{
				// fill in this line in the number
				g.fillRect(137, 31 + height, 3, 15);
			}

			// if the first digit is a number that has the lower horizontal line in their clock number representation
			if(digit2 == 0 || digit2 == 2 || digit2 == 3 || digit2 == 5 || digit2 == 6 || digit2 == 8 || digit2 == 9)
			{
				// fill in this line in the number
				g.fillRect(124, 43 + height, 12, 3);
			}

			// if the first digit is a number that has the lower left vertical line in their clock number representation
			if(digit2 == 0 || digit2 == 2 || digit2 == 6 || digit2 == 8)
			{
				// fill in this line in the number
				g.fillRect(120, 31 + height, 3, 15);
			}

			// if the first digit is a number that has the lower horizontal line in their clock number representation
			if(digit2 == 2 || digit2 == 3 || digit2 == 4 || digit2 == 5 || digit2 == 6 || digit2 == 8 || digit2 == 9)
			{
				// fill in this line in the number
				g.fillRect(124, 28 + height, 12, 3);
			}
		}

		// if the first digit is a number that has the upper left vertical line in their clock number representation
		if(digit3 == 0 || digit3 == 4 || digit3 == 5 || digit3 == 6 || digit3 == 8 || digit3 == 9)
		{
			// fill in this line in the number
			g.fillRect(145, 15 + height, 3, 15);
		}

		// if the first digit is a number that has the upper horizontal line in their clock number representation
		if(digit3 == 0 || digit3 == 2 || digit3 == 3 || digit3 == 5 || digit3 == 6 || digit3 == 7 || digit3 == 8 || digit3 == 9)
		{
			// fill in this line in the number
			g.fillRect(149, 15 + height, 12, 3);
		}

		// if the first digit is a number that has the upper right vertical line in their clock number representation
		if(digit3 == 0 || digit3 == 1 || digit3 == 2 || digit3 == 3 || digit3 == 4 || digit3 == 7 || digit3 == 8 || digit3 == 9)
		{
			// fill in this line in the number
			g.fillRect(162, 15 + height, 3, 15);
		}

		// if the first digit is a number that has the lower right vertical line in their clock number representation
		if(digit3 == 0 || digit3 == 1 || digit3 == 3 || digit3 == 4 || digit3 == 5 || digit3 == 6 || digit3 == 7 || digit3 == 8 || digit3 == 9)
		{
			// fill in this line in the number
			g.fillRect(162, 31 + height, 3, 15);
		}

		// if the first digit is a number that has the lower horizontal line in their clock number representation
		if(digit3 == 0 || digit3 == 2 || digit3 == 3 || digit3 == 5 || digit3 == 6 || digit3 == 8 || digit3 == 9)
		{
			// fill in this line in the number
			g.fillRect(149, 43 + height, 12, 3);
		}

		// if the first digit is a number that has the lower left vertical line in their clock number representation
		if(digit3 == 0 || digit3 == 2 || digit3 == 6 || digit3 == 8)
		{
			// fill in this line in the number
			g.fillRect(145, 31 + height, 3, 15);
		}

		// if the first digit is a number that has the lower horizontal line in their clock number representation
		if(digit3 == 2 || digit3 == 3 || digit3 == 4 || digit3 == 5 || digit3 == 6 || digit3 == 8 || digit3 == 9)
		{
			// fill in this line in the number
			g.fillRect(149, 28 + height, 12, 3);
		}
	}

	/*
	 * drawSun(Graphics g) draws the sun in the upper left corner of the window.
	 */
	public void drawSun(Graphics g)
	{
		// set the color to yellow
		g.setColor(yellow);

		// fill in the circle that is the sun
		g.fillOval(75, 15, 50, 50);

		// set the color to black
		g.setColor(black);

		// draw the outline of the circle
		g.drawOval(75, 15, 50, 50);

		// draw the sun's nose using two lines
		g.drawLine(100, 35, 96, 45);
		g.drawLine(96, 45, 100, 45);

		// if the sun's mouth is open
		if(sunGasp == true)
		{
			// draw the opened mouth
			g.drawOval(96, 51, 8, 8);
		}

		// if the game is over
		else if(gameOver == false)
		{
			// draw the sun's frown
			g.drawArc(90, 45, 20, 10, 220, 100);
		}

		// otherwise
		else
		{
			// draw the sun's half smile
			g.drawArc(90, 53, 20, 10, 40, 100);
		}

		// if the game is not over
		if(gameOver == false)
		{
			// draw the sun's eyes
			g.fillOval(88, 24, 10, 10);
			g.fillOval(102, 24, 10, 10);

			// if the game has been won
			if(gameWon == true)
			{
				// draw the sun's sunglasses
				g.fillRect(79, 23, 42, 3);
				g.fillRect(95, 27, 10, 3);
				g.fillRoundRect(82, 23, 15, 13, 3, 3);
				g.fillRoundRect(103, 23, 15, 13, 3, 3);
			}
		}

		// if the game is over
		else
		{
			// draw Xs for the sun's eyes
			g.drawLine(89, 25, 97, 33);
			g.drawLine(97, 25, 89, 33);
			g.drawLine(103, 25, 111, 33);
			g.drawLine(111, 25, 103, 33);
		}
	}

	/*
	 * drawOptions draws the three option buttons that allow the user to
	 * switch between difficulty modes.
	 */
	public void drawOptions(Graphics g)
	{
		// if the first option is depressed
		if(isOptionDepressed[0] == true)
		{
			// set the color to a lighter shade of the normal button color
			g.setColor(offWhite);
		}

		// if the option is not depressed
		else
		{
			// set the color to the normal color for these buttons
			g.setColor(lightGrey);
		}

		// draw the box for the button
		g.fillRect(140, 5, 50, 20);

		// if the second option is depressed
		if(isOptionDepressed[1] == true)
		{
			// set the color to a lighter shade of the normal button color
			g.setColor(offWhite);
		}

		// if the option is not depressed
		else
		{
			// set the color to the normal color for these buttons
			g.setColor(lightGrey);
		}

		// draw the box for the button
		g.fillRect(140, 30, 50, 20);

		// if the third option is depressed
		if(isOptionDepressed[2] == true)
		{
			// set the color to a lighter shade of the normal button color
			g.setColor(offWhite);
		}

		// if the option is not depressed
		else
		{
			// set the color to the normal color for these buttons
			g.setColor(lightGrey);
		}

		// draw the box for the button
		g.fillRect(140, 55, 50, 20);

		// set the color to black
		g.setColor(black);

		// draw the outlines for each option box
		g.drawRect(140, 5, 50, 20);
		g.drawRect(140, 30, 50, 20);
		g.drawRect(140, 55, 50, 20);

		// set the font
		g.setFont(new Font("Arial", Font.BOLD, 10));

		// draw in the labels for each button
		g.drawString("EASY", 153, 20);
		g.drawString("MEDIUM", 146, 45);
		g.drawString("HARD", 153, 70);
	}

	/*
	 * depressOption(int optionNumber) depresses the given option.
	 */
	public void depressOption(int optionNumber)
	{
		// the option is now depressed
		isOptionDepressed[optionNumber - 1] = true;
	}

	/*
	 * isOptionDepressed(int optionNumber) returns whether or not a given
	 * option is depressed.
	 */
	public boolean isOptionDepressed(int optionNumber)
	{
		// returns whether or not the option is depressed
		return isOptionDepressed[optionNumber - 1];
	}

	/*
	 * undepressOption(int optionNumber) undepresses the given option.
	 */
	public void undepressOption(int optionNumber)
	{
		// the option is now not depressed
		isOptionDepressed[optionNumber - 1] = false;
	}

	/*
	 * drawSquares(Graphics g) draws the squares that make up the puzzle.
	 */
	public void drawSquares(Graphics g)
	{
		// set the font
		g.setFont(new Font("Arial", Font.BOLD, 26));

		// for each column
		for(int i = 0; i < columns; i++)
		{
			// for each row in that column
			for(int j = 0; j < rows; j++)
			{
				// set the color to a light grey
				g.setColor(backgroundGrey);

				// fill in the outline of the square
				g.fillRect(i*30 + 200, j*30, 31, 31);

				// set the color to a very light grey
				g.setColor(offWhite);

				// fill in the square
				g.fillRect(i*30 + 201, j*30 + 1, 29, 29);

				// if the square is not depressed
				if(depressedSquares[j][i] == false)
				{
					// set the color to yet another grey
					g.setColor(lightGrey);

					// fill in the square
					g.fillRect(i*30 + 200, j*30, 30, 30);

					// set the color to a different grey
					g.setColor(backgroundGrey);

					// draw the outline for the square
					g.drawRect(i*30 + 200, j*30, 30, 30);

					// draw the interior rectangle of the square
					g.drawRect(i*30 + 205, j*30 + 5, 20, 20);

					// set the color to white
					g.setColor(Color.white);

					// draw in the white shading in the upper left portion of the square
					g.drawLine(i*30 + 201, j*30 + 1, i*30 + 229, j*30 + 1);
					g.drawLine(i*30 + 201, j*30 + 2, i*30 + 229, j*30 + 2);
					g.drawLine(i*30 + 201, j*30 + 3, i*30 + 201, j*30 + 29);
					g.drawLine(i*30 + 202, j*30 + 3, i*30 + 201, j*30 + 29);

					// set the color to a darker grey
					g.setColor(grey);

					// draw in the grey shading in the lower right portion of the square
					g.drawLine(i*30 + 228, j*30 + 3, i*30 + 228, j*30 + 29);
					g.drawLine(i*30 + 229, j*30 + 3, i*30 + 229, j*30 + 29);
					g.drawLine(i*30 + 203, j*30 + 28, i*30 + 227, j*30 + 28);
					g.drawLine(i*30 + 203, j*30 + 29, i*30 + 227, j*30 + 29);
				}

				// if the square is not covered
				if(isCovered[j][i] == false)
				{
					// set the color to another grey
					g.setColor(backgroundGrey);

					// fill in the outline of the square
					g.fillRect(i*30 + 200, j*30, 30, 30);

					// set the color to a very light grey
					g.setColor(offWhite);

					// fill in the square
					g.fillRect(i*30 + 201, j*30 + 1, 29, 29);

					// if a wrong flag was placed at the square
					if(wrongFlag[j][i] == true)
					{
						// set the color to black
						g.setColor(black);

						// create arrays to hold x and y points for the pointy object that is part of the mine
						int[] xPoints2 = {i*30 + 215, i*30 + 217, i*30 + 226, i*30 + 217, i*30 + 215, i*30 + 213, i*30 + 204, i*30 + 213};
						int[] yPoints2 = {j*30 + 4, j*30 + 13, j*30 + 15, j*30 + 17, j*30 + 26, j*30 + 17, j*30 + 15, j*30 + 13};

						// fill in this polygon
						g.fillPolygon(xPoints2, yPoints2, 8);

						// fill in the oval that completes the "mine"
						g.fillOval(i*30 + 207, j*30 + 7, 16, 16);

						// set the color to red
						g.setColor(red);

						// draw lines to create a red X over the mine
						g.drawLine(i*30 + 200, j*30, i*30 + 230, j*30 + 30);
						g.drawLine(i*30 + 200, j*30 + 1, i*30 + 229, j*30 + 30);
						g.drawLine(i*30 + 201, j*30, i*30 + 230, j*30 + 29);
						g.drawLine(i*30 + 230, j*30, i*30 + 200, j*30 + 30);
						g.drawLine(i*30 + 229, j*30, i*30 + 200, j*30 + 29);
						g.drawLine(i*30 + 230, j*30 + 1, i*30 + 201, j*30 + 30);
					}

					// if a flag was not placed incorrectly
					else
					{
						// if the square has a mine
						if(board[j][i] == -1)
						{
							// if a wrong move was made, where the player clicks on the square hiding the mine
							if(wrongMove[j][i] == true)
							{
								// set the color to red
								g.setColor(red);

								// fill in the background of the square with red
								g.fillRect(i*30 + 201, j*30 + 1, 29, 29);
							}

							// set the color to black
							g.setColor(black);

							// create arrays to hold x and y points for the pointy object that is part of the mine
							int[] xPoints3 = {i*30 + 215, i*30 + 217, i*30 + 226, i*30 + 217, i*30 + 215, i*30 + 213, i*30 + 204, i*30 + 213};
							int[] yPoints3 = {j*30 + 4, j*30 + 13, j*30 + 15, j*30 + 17, j*30 + 26, j*30 + 17, j*30 + 15, j*30 + 13};

							// fill in this polygon
							g.fillPolygon(xPoints3, yPoints3, 8);

							// fill in the oval that completes the "mine"
							g.fillOval(i*30 + 207, j*30 + 7, 16, 16);
						}

						// if the square has one adjacent mine
						else if(board[j][i] == 1)
						{
							// set the color to blue
							g.setColor(blue);

							// draw the number 1
							g.drawString("" + 1, i*30 + 209, j*30 + 25);
						}

						// if the square has two adjacent mines
						else if(board[j][i] == 2)
						{
							// set the color to green
							g.setColor(green);

							// draw the number 2
							g.drawString("" + 2, i*30 + 209, j*30 + 25);
						}

						// if the square has three adjacent mines
						else if(board[j][i] == 3)
						{
							// set the color to red
							g.setColor(red);

							// draw the number 3
							g.drawString("" + 3, i*30 + 209, j*30 + 25);
						}

						// if the square has fourth adjacent mines
						else if(board[j][i] == 4)
						{
							// set the color to navy blue
							g.setColor(navy);

							// draw the number 4
							g.drawString("" + 4, i*30 + 209, j*30 + 25);
						}

						// if the square has five adjacent mines
						else if(board[j][i] == 5)
						{
							// set the color to brown
							g.setColor(brown);

							// draw the number 5
							g.drawString("" + 5, i*30 + 209, j*30 + 25);
						}

						// if the square has six adjacent mines
						else if(board[j][i] == 6)
						{
							// set the color to light blue
							g.setColor(cyan);

							// draw the number 6
							g.drawString("" + 6, i*30 + 209, j*30 + 25);
						}

						// if the square has seven adjacent mines
						else if(board[j][i] == 7)
						{
							// set the color to black
							g.setColor(black);

							// draw the number 7
							g.drawString("" + 7, i*30 + 209, j*30 + 25);
						}

						// if the square has eight adjacent mines
						else if(board[j][i] == 8)
						{
							// set the color to grey
							g.setColor(grey);

							// draw the number 8
							g.drawString("" + 8, i*30 + 209, j*30 + 25);
						}
					}
				}

				// if the square has a flag
				else if(hasFlag[j][i] == true)
				{
					// set the color to black
					g.setColor(black);

					// draw the base and "pole" for the flag
					g.drawLine(i*30 + 207, j*30 + 22, i*30 + 223, j*30 + 22);
					g.drawLine(i*30 + 210, j*30 + 21, i*30 + 220, j*30 + 21);
					g.drawLine(i*30 + 213, j*30 + 20, i*30 + 217, j*30 + 20);
					g.drawLine(i*30 + 215, j*30 + 8, i*30 + 215, j*30 + 20);

					// set the color to red
					g.setColor(red);

					// create arrays to hold x and y points for the flag part of the flag
					int[] xPoints2 = {i*30 + 208, i*30 + 215, i*30 + 215};
					int[] yPoints2 = {j*30 + 12, j*30 + 8, j*30 + 16};

					// fill in this polygon
					g.fillPolygon(xPoints2, yPoints2, 3);

					// set the color to black
					g.setColor(black);

					// draw the outline of the flag
					g.drawPolygon(xPoints2, yPoints2, 3);
				}
			}
		}
	}

	/*
	 * drawBackground(Graphics g, ... , String mostDifficultPuzzle) draws all the information in the puzzle,
	 * using various methods defined above.
	 */
	public void drawBackground(Graphics g, int seconds, String lowScore, String mostDifficultPuzzle)
	{
		// draw the sun
		drawSun(g);

		// draw the option buttons
		drawOptions(g);

		// set the font
		g.setFont(new Font("Arial", Font.BOLD, 12));

		// set the color to black
		g.setColor(black);

		// draw the labels for the statistics to be printed out
		g.drawString("MINES LEFT", 5, 90);
		g.drawString("TIME", 5, 163);
		g.drawString("FASTEST TIME", 5, 236);
		g.drawString("PUZZLE DIFFICULTY", 5, 309);
		g.drawString("HARDEST PUZZLE SOLVED", 5, 382);

		// print out each number in "clock-like" format
		printNumber(g, flagsLeft, 90);
		printNumber(g, seconds, 163);
		printNumber(g, Integer.parseInt(lowScore), 236);
		printNumber(g, numClicks, 309);
		printNumber(g, Integer.parseInt(mostDifficultPuzzle), 382);

		// draw the squares in the puzzle
		drawSquares(g);
	}

	/*
	 * get3BV() returns the private variable numClicks.
	 */
	public int get3BV()
	{
		return numClicks;
	}

	/*
	 * getMyColumns() returns the number of columns in the current puzzle.
	 */
	public int getMyColumns()
	{
		return columns;
	}

	/*
	 * getMyRows() returns the number of rows in the current puzzle.
	 */
	public int getMyRows()
	{
		return rows;
	}

	/*
	 * getNumMines() returns the number of mines in the current puzzle.
	 */
	public int getNumMines()
	{
		return numMines;
	}
}
