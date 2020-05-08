# Minesweeper

Name: Joseph Brunner

Email: jbrunner@u.rochester.edu

Date: December 12, 2010

Project Number: 4

Included Files:

README.txt: This file.

MinesweeperGame.java: MinesweeperGame creates a frame and a canvas, and then 
  constructs a thread that will run the game.

MinesweeperLoop.java: MinesweeperLoop constructs a threaded object that 
  continuously runs and updates itself, to display the most current image
  to the user.  It responds to user input through the mouse using 
  MouseListeners and MouseMotionListeners.

Background.java: Background represents the "background" of the window and 
  displays all of the information in and regarding the game, such as the 
  actual puzzle, the statistics, and the option buttons.

FileIn.java: FileIn constructs an object that stores information about a given 
  file located within the project.  FileIn is used with reading in files.

FileOut.java: FileOut constructs an object that stores information about a given
  file located within the project.  FileOut is used with writing to a file.

timeDataEasy.txt, timeDataMedium.txt, timeDataHard.txt: Files that hold low scores
  for each difficulty mode and are written to and read from in the program.
  
3BVDataEasy.txt, 3BVDataMedium.txt, 3BVDataHard.txt: Files that hold high scores
  for each difficulty mode and are written to and read from in the program.

Project Write-up:

(1) Project Discription:
This project mimics the game Minesweeper using an Active Rendering technique.
All graphics are self-drawn, which I felt gave me greater control and increased
freedom in designing my frame and window.  The left side of the screen contains
option buttons that allow the user to cycle through the difficulty modes Easy,
Medium, and Hard, and also displays statistics to the user including statistics
for the current game as well as high and low scores.  This project will keep 
track of the quickest time that the user was able to complete a puzzle for each
difficulty mode, and will also keep track of the most difficult puzzle solved 
for each difficulty level, measured using the common Minesweeper 3BV scoring
method.  The scores will automatically refresh with each new load of the game,
but feel free to change the boolean variable resetScores to false in the file
MinesweeperGame.java so that the high and low scores will not reset with each
new load.  As for the actual gameplay, it resembles the classic Minesweeper
approach completely.  Enjoy!

(2) Instructions:
Run MinesweeperGame.java.  A frame will open up displaying both the game and the
statistics for the game.  The user can select a different difficulty mode by
clicking on the buttons labeled "Easy", "Medium", and "Hard", and reset to a new
game at any time by clicking any of these buttons or the sun.  When playing the
game, the user can left click to open a square, and right click to place a flag
marking a mine.  If the user left clicks an opened square, the game will respond
depending on different scenarios.  If the opened square has all adjacent mines
marked correctly with flags, any unopened squares adjacent to the clicked square
are revealed as well.  Clicking on an opened square that does not have all of
its mines correctly marked with flags causes all unopened squares adjacent to 
the clicked square to become depressed, giving the user an idea about how many
squares remain to be marked with flags.  If the clicked square has mines 
incorrectly marked, the game ends.  Also, if the user clicks and opens a square
that holds a mine, the game ends.  The game is won if the user opens all squares
that do not contain mines.

(3) Other:
All requirements have been fully implemented.
