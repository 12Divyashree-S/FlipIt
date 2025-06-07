package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;
import java.util.List;


public class MemoryGame extends JFrame {
    private final int SIZE = 4;
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private String[][] values = new String[SIZE][SIZE];
    private JButton firstSelected = null;
    private JButton secondSelected = null;
    private Timer flipBackTimer;
    private int moves = 0;
    private int matchedPairs = 0;

    private JLabel timerLabel;
    private JLabel movesLabel;
    private int secondsElapsed = 0;
    private Timer gameTimer;

    private JPanel gridPanel;

    public MemoryGame() {
        setTitle("Memory Game");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(1, 2));
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setPreferredSize(new Dimension(500, 40));

        movesLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        movesLabel.setForeground(Color.WHITE);
        movesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(movesLabel);

        timerLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(timerLabel);

        add(headerPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(SIZE, SIZE, 5, 5));
        gridPanel.setBackground(Color.BLACK);
        add(gridPanel, BorderLayout.CENTER);

        initializeBoard();
        startGameTimer();

        setVisible(true);
    }

    private void initializeBoard() {
        moves = 0;
        matchedPairs = 0;
        secondsElapsed = 0;
        movesLabel.setText("Moves: 0");
        timerLabel.setText("Time: 0s");

        gridPanel.removeAll();
        gridPanel.revalidate();
        gridPanel.repaint();

        String[] emojis = { "🍎", "🍇", "🍌", "🍓", "🍍", "🍉", "🥝", "🍑" };
        List<String> pairs = new ArrayList<>();
        for (String emoji : emojis) {
            pairs.add(emoji);
            pairs.add(emoji);
        }
        Collections.shuffle(pairs);

        int index = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String value = pairs.get(index++);
                values[row][col] = value;

                JButton button = new JButton();
                button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
                button.setFocusPainted(false);
                button.setBackground(new Color(80, 80, 80));
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                button.setText("");
                button.setEnabled(true);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.addActionListener(new ButtonClickListener(row, col, button));
                buttons[row][col] = button;
                gridPanel.add(button);
            }
        }

        if (gameTimer != null) {
            gameTimer.stop();
        }
        startGameTimer();
    }

    private void startGameTimer() {
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            timerLabel.setText("Time: " + secondsElapsed + "s");
        });
        gameTimer.start();
    }

    private class ButtonClickListener implements ActionListener {
        private int row, col;
        private JButton button;

        public ButtonClickListener(int row, int col, JButton button) {
            this.row = row;
            this.col = col;
            this.button = button;
        }

        public void actionPerformed(ActionEvent e) {
            if (firstSelected != null && secondSelected != null)
                return;

            button.setText(values[row][col]);
            button.setEnabled(false);

            if (firstSelected == null) {
                firstSelected = button;
            } else {
                secondSelected = button;
                moves++;
                movesLabel.setText("Moves: " + moves);

                if (firstSelected.getText().equals(secondSelected.getText())) {
                    matchedPairs++;
                    firstSelected = null;
                    secondSelected = null;

                    if (matchedPairs == (SIZE * SIZE) / 2) {
                        gameTimer.stop();
                        int option = JOptionPane.showConfirmDialog(
                                MemoryGame.this,
                                "🎉 You Win!\n\nMoves: " + moves + "\nTime: " + secondsElapsed + "s\n\nPlay Again?",
                                "Game Over 🎊",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (option == JOptionPane.YES_OPTION) {
                            initializeBoard();
                        } else {
                            System.exit(0);
                        }
                    }
                } else {
                    flipBackTimer = new Timer(1000, evt -> {
                        firstSelected.setText("");
                        firstSelected.setEnabled(true);
                        secondSelected.setText("");
                        secondSelected.setEnabled(true);
                        firstSelected = null;
                        secondSelected = null;
                        flipBackTimer.stop();
                    });
                    flipBackTimer.setRepeats(false);
                    flipBackTimer.start();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemoryGame::new);
    }
}
