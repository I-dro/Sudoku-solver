import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuGUI extends JFrame implements ActionListener {

    private JPanel[][] sectionPanels; // Array of 3x3 section panels
    private JTextField[][] cells;
    private JButton solveButton;
    private JButton clearButton;
    private JButton resetButton;

      // Colors for section backgrounds
      private final Color[] sectionColors = {
        new Color(255, 218, 185), // Light coral
        new Color(173, 216, 230), // Light blue
        new Color(240, 128, 128), // Light coral
        new Color(135, 206, 235), // Light blue
        new Color(255, 160, 122), // Light coral
        new Color(176, 224, 230), // Light blue
        new Color(255, 182, 193), // Light coral
        new Color(173, 216, 230), // Light blue
        new Color(255, 160, 122)  // Light coral
};

    public SudokuGUI() {
        // Set up the JFrame
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);

        // Create 9 section panels
        sectionPanels = new JPanel[3][3];
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sectionPanels[i][j] = new JPanel(new GridLayout(3, 3));
                boardPanel.add(sectionPanels[i][j]);
                
            }
        }

        // Create a 9x9 grid of text fields to represent the Sudoku board
        cells = new JTextField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField(1);
                cells[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setBackground(new Color(230, 230, 250)); // Light yellow color
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add cell borders
                cells[i][j].addKeyListener(new DigitKeyListener()); // Add KeyListener to filter input
                cells[i][j].addActionListener(this); // Add ActionListener to handle user input

                // Determine the section panel and add the cell to the corresponding section panel
                int sectionRow = i / 3;
                int sectionCol = j / 3;
                sectionPanels[sectionRow][sectionCol].add(cells[i][j]);

                // Create a final variable to store the value of i and j
                final int row = i;
                final int col = j;

                // Add focus listener to highlight the row and column when the cell is being edited
                cells[i][j].addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        highlightRowAndColumn(cells[row][col]);
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        resetHighlight();
                    }
                });
        sectionPanels[sectionRow][sectionCol].add(cells[i][j]);
            }
        }

        // Create the Solve, Clear, and Reset buttons
        solveButton = new JButton("Solve");
        solveButton.addActionListener(this);
        solveButton.setBackground(new Color(51, 153, 102)); // Green color
        solveButton.setForeground(Color.WHITE); // White text color

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        clearButton.setBackground(new Color(204, 102, 102)); // Red color
        clearButton.setForeground(Color.WHITE); // White text color

        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        resetButton.setBackground(new Color(102, 102, 204)); // Blue color
        resetButton.setForeground(Color.WHITE); // White text color

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(resetButton);

        // Add components to the JFrame
        add(boardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            solve();
        } else if (e.getSource() == clearButton) {
            clearBoard();
        } else if (e.getSource() == resetButton) {
            resetBoard();
        }
    }

    private void solve() {
        int[][] board = readBoardFromTextFields();

        if (board != null && sudoku.solve(board)) {
            updateTextFields(board);
        } else {
            JOptionPane.showMessageDialog(this, "No solution found for the Sudoku puzzle.");
        }
    }

    private int[][] readBoardFromTextFields() {
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText();
                if (!text.isEmpty()) {
                    try {
                        int value = Integer.parseInt(text);
                        if (value >= 1 && value <= 9) {
                            board[i][j] = value;
                        } else {
                            // Invalid input, show an error message
                            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers from 1 to 9.");
                            return null;
                        }
                    } catch (NumberFormatException ex) {
                        // Invalid input, show an error message
                        JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers from 1 to 9.");
                        return null;
                    }
                }
            }
        }
        return board;
    }

    private void updateTextFields(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText("");
            }
        }
    }

    private void resetBoard() {
        clearBoard();
    }

    // Method to highlight the row and column of the selected cell
    private void highlightRowAndColumn(JTextField selectedCell) {
        int selectedRow = -1;
        int selectedColumn = -1;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j] == selectedCell) {
                    selectedRow = i;
                    selectedColumn = j;
                    break;
                }
            }
        }

        if (selectedRow >= 0 && selectedColumn >= 0) {
            // Highlight row and column of the selected cell
            for (int k = 0; k < 9; k++) {
                cells[selectedRow][k].setBackground(new Color(255, 218, 185)); // Highlight row with light coral color
                cells[k][selectedColumn].setBackground(new Color(255, 218, 185)); // Highlight column with light coral color
            }
        }
    }


    // Method to reset the cell background color after focus is lost
    private void resetHighlight() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setBackground(new Color(230, 230, 250)); // Light yellow color
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuGUI gui = new SudokuGUI();
            gui.setVisible(true);
        });
    }
}

// Custom KeyListener to restrict input to a single digit
class DigitKeyListener extends KeyAdapter {
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c)) {
            e.consume(); // Ignore non-digit characters
        }
    }
}
