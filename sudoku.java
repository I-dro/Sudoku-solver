/**
 * A class to represent a Sudoku puzzle and solve it using a recursive backtracking algorithm.
 */
public class sudoku {

    /**
     * Method to print the Sudoku board.
     *
     * @param board The 2D array representing the Sudoku puzzle.
     */
    public static void printBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            System.out.print("| ");
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
                // Add separator after every 3 numbers in a row
                if ((j + 1) % 3 == 0) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            // Add horizontal line separator after every 3 rows
            if ((i + 1) % 3 == 0) {
                System.out.println("-------------------------");
            }
        }
    }

    /**
     * Method to find the first empty cell (value 0) and return its coordinates.
     *
     * @param board The 2D array representing the Sudoku puzzle.
     * @return An array containing the row and column indices of the first empty cell, or null if no empty cell is found.
     */
    public static int[] pickEmpty(int[][] board) {
        int[] coordinates = new int[2];
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (board[i][j] == 0) {
                    coordinates[0] = i;
                    coordinates[1] = j;
                    return coordinates;
                }
            }
        }
        return null; // Return null if no empty cell is found
    }

    /**
     * Method to solve the Sudoku puzzle using a recursive backtracking algorithm.
     *
     * @param board The 2D array representing the Sudoku puzzle.
     * @return true if a solution is found, false otherwise.
     */
    public static boolean solve(int[][] board) {
        int[] coordinates = pickEmpty(board);
        if (coordinates == null) {
            return true; // Puzzle solved, return true to indicate success
        } else {
            int row = coordinates[0];
            int col = coordinates[1];

            for (int num = 1; num <= 9; num++) {
                if (numValid(board, num, coordinates)) {
                    board[row][col] = num;

                    if (solve(board)) {
                        return true; // If the rest of the puzzle is solvable, return true
                    } else {
                        board[row][col] = 0; // Backtrack and reset the current cell to 0
                    }
                }
            }
            return false; // If no valid number can be placed, trigger backtracking
        }
    }


    /**
     * Method to check if a number is valid to place in the cell.
     *
     * @param board The 2D array representing the Sudoku puzzle.
     * @param num   The number to be placed.
     * @param pos   An array containing the row and column indices of the cell to check.
     * @return true if the number can be placed in the cell, false otherwise.
     */
    public static boolean numValid(int[][] board, int num, int[] pos) {
        // Check row
        for (int i = 0; i < 9; ++i) {
            if (board[pos[0]][i] == num && pos[1] != i) {
                return false;
            }
        }
        // Check column
        for (int i = 0; i < 9; ++i) {
            if (board[i][pos[1]] == num && pos[0] != i) {
                return false;
            }
        }
        // Check box
        int boxX = pos[1] / 3;
        int boxY = pos[0] / 3;
        for (int i = boxY * 3; i < boxY * 3 + 3; i++) {
            for (int j = boxX * 3; j < boxX * 3 + 3; j++) {
                if (board[i][j] == num && (pos[0] != i || pos[1] != j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Main method (entry point of the program)
    public static void main(String[] args) {
        // Sample Sudoku board
        int[][] board = {
                {4, 2, 9, 8, 1, 3, 5, 6, 7},
                {5, 1, 6, 4, 7, 2, 9, 3, 0},
                {7, 8, 3, 6, 5, 9, 2, 4, 1},
                {6, 7, 2, 1, 3, 4, 8, 5, 9},
                {3, 9, 5, 0, 8, 6, 1, 7, 4},
                {8, 0, 1, 7, 9, 5, 6, 2, 3},
                {1, 5, 8, 3, 6, 7, 4, 9, 2},
                {9, 3, 4, 5, 2, 8, 7, 0, 6},
                {2, 6, 7, 9, 4, 1, 3, 8, 5}
        };

        // Print the Sudoku board
        printBoard(board);
        if (solve(board)) {
            System.out.println("The Sudoku puzzle is solved!\n");
            
            printBoard(board);
        } else {
            System.out.println("There are no solutions for the Sudoku puzzle.");
        }
    }
}
