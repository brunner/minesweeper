# Minesweeper

## Description
This project mimics the game Minesweeper using an Active Rendering technique.
All graphics are self-drawn, which I felt gave me greater control and increased
freedom in designing my frame and window. The left side of the screen contains
option buttons that allow the user to cycle through the difficulty modes Easy,
Medium, and Hard, and also displays statistics to the user including statistics
for the current game as well as high and low scores. This project will keep
track of the quickest time that the user was able to complete a puzzle for each
difficulty mode, and will also keep track of the most difficult puzzle solved
for each difficulty level, measured using the common Minesweeper 3BV scoring
method. The scores will automatically refresh with each new load of the game,
but feel free to change the boolean variable resetScores to false in the file
MinesweeperGame.java so that the high and low scores will not reset with each
new load. As for the actual game play, it resembles the classic Minesweeper
approach completely. Enjoy!

## Instructions
Run MinesweeperGame.java. A frame will open up displaying both the game and the
statistics for the game. The user can select a different difficulty mode by
clicking on the buttons labeled "Easy", "Medium", and "Hard", and reset to a new
game at any time by clicking any of these buttons or the sun. When playing the
game, the user can left click to open a square, and right click to place a flag
marking a mine. If the user left clicks an opened square, the game will respond
depending on different scenarios. If the opened square has all adjacent mines
marked correctly with flags, any unopened squares adjacent to the clicked square
are revealed as well. Clicking on an opened square that does not have all of
its mines correctly marked with flags causes all unopened squares adjacent to
the clicked square to become depressed, giving the user an idea about how many
squares remain to be marked with flags. If the clicked square has mines
incorrectly marked, the game ends. Also, if the user clicks and opens a square
that holds a mine, the game ends. The game is won if the user opens all squares
that do not contain mines.
