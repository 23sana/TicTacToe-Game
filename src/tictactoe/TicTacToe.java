package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TicTacToe extends JFrame implements ActionListener {

    private JButton[] buttons = new JButton[9];
    private boolean playerX = true;
    private boolean vsAI = true;

    private JLabel statusLabel;
    private JLabel scoreLabel;

    private JButton resetButton;
    private JButton modeButton;

    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;

    private final Color darkBG = new Color(30, 30, 30);
    private final Color darkBtn = new Color(50, 50, 50);
    private final Color winColor = new Color(0, 200, 0);

    public TicTacToe() {
        setTitle("Tic Tac Toe - Advanced Java Project");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(darkBG);

        statusLabel = new JLabel("Player X's Turn (VS Computer)", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        scoreLabel = new JLabel("X: 0   O: 0   Draws: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(darkBG);
        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);

        add(topPanel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel(new GridLayout(3, 3));
        gamePanel.setBackground(darkBG);

        Font btnFont = new Font("Arial", Font.BOLD, 50);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(btnFont);
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(darkBtn);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener(this);
            gamePanel.add(buttons[i]);
        }

        add(gamePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setBackground(darkBG);

        resetButton = new JButton("Reset");
        modeButton = new JButton("Switch Mode");

        resetButton.addActionListener(e -> resetGame());
        modeButton.addActionListener(e -> switchMode());

        bottomPanel.add(resetButton);
        bottomPanel.add(modeButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if (!clicked.getText().equals("")) return;

        makeMove(clicked);

        if (checkWin()) {
            handleWin();
            return;
        } else if (isDraw()) {
            handleDraw();
            return;
        }

        playerX = !playerX;

        if (!playerX && vsAI) {
            aiMove();
        }

        updateStatus();
    }

    private void makeMove(JButton btn) {
        btn.setText(playerX ? "X" : "O");
    }

    private void aiMove() {
        Random rand = new Random();
        int move;
        do {
            move = rand.nextInt(9);
        } while (!buttons[move].getText().equals(""));

        buttons[move].setText("O");

        if (checkWin()) {
            handleWin();
        } else if (isDraw()) {
            handleDraw();
        }

        playerX = true;
        updateStatus();
    }

    private boolean checkWin() {
        int[][] wins = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };

        for (int[] w : wins) {
            String a = buttons[w[0]].getText();
            String b = buttons[w[1]].getText();
            String c = buttons[w[2]].getText();

            if (!a.equals("") && a.equals(b) && b.equals(c)) {
                highlightWin(w);
                return true;
            }
        }
        return false;
    }

    private void highlightWin(int[] winLine) {
        for (int i : winLine) {
            buttons[i].setBackground(winColor);
        }
    }

    private void handleWin() {
        if (playerX) xWins++;
        else oWins++;

        JOptionPane.showMessageDialog(this, "Player " + (playerX ? "X" : "O") + " Wins!");
        updateScore();
        disableBoard();
    }

    private void handleDraw() {
        draws++;
        JOptionPane.showMessageDialog(this, "It's a Draw!");
        updateScore();
        disableBoard();
    }

    private void updateScore() {
        scoreLabel.setText("X: " + xWins + "   O: " + oWins + "   Draws: " + draws);
    }

    private void disableBoard() {
        for (JButton b : buttons) {
            b.setEnabled(false);
        }
    }

    private boolean isDraw() {
        for (JButton b : buttons) {
            if (b.getText().equals("")) return false;
        }
        return true;
    }

    private void resetGame() {
        for (JButton b : buttons) {
            b.setText("");
            b.setEnabled(true);
            b.setBackground(darkBtn);
        }
        playerX = true;
        updateStatus();
    }

    private void switchMode() {
        vsAI = !vsAI;
        resetGame();
        statusLabel.setText(vsAI ? "Player X's Turn (VS Computer)" : "Player X's Turn (2 Player Mode)");
    }

    private void updateStatus() {
        if (vsAI && !playerX)
            statusLabel.setText("Computer's Turn");
        else
            statusLabel.setText("Player " + (playerX ? "X" : "O") + "'s Turn");
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
