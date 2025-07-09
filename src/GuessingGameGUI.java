package src;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

class GuessGame {
    private int attempts;
    private int numberToGuess;
    private int maxRange;

    public GuessGame(int maxRange) {
        this.maxRange = maxRange;
        Random rand = new Random();
        attempts = 0;
        numberToGuess = rand.nextInt(maxRange) + 1;
    }

    public boolean checkGuess(int guess) {
        attempts++;
        return guess == numberToGuess;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getNumberToGuess() {
        return numberToGuess;
    }

    public int getMaxRange() {
        return maxRange;
    }
}

public class GuessingGameGUI extends JFrame {
    private GuessGame game;
    private long startTime;
    private int maxRange = 100;

    private JTextField inputField;
    private JLabel messageLabel;
    private JLabel attemptsLabel;
    private JComboBox<String> difficultyBox;
    private JButton guessButton, resetButton;
    private JLabel avatarLabel;

    public GuessingGameGUI() {
        setTitle("🎯 Guess the Number");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color latte = new Color(255, 240, 230);
        getContentPane().setBackground(latte);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(latte);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("🎲 Let's Play: Guess the Number!");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageLabel = new JLabel("Pick a level and start guessing!");
        messageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        attemptsLabel = new JLabel("Attempts: 0");
        attemptsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultyBox = new JComboBox<>(new String[] { "Easy (1-50)", "Medium (1-100)", "Hard (1-200)" });
        difficultyBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyBox.setMaximumSize(new Dimension(200, 30));
        difficultyBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        inputField.setMaximumSize(new Dimension(200, 30));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        buttonPanel.setBackground(latte);
        buttonPanel.setMaximumSize(new Dimension(300, 40));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        guessButton = new JButton("🎯 Guess");
        resetButton = new JButton("🔄 Restart");

        styleButton(guessButton, new Color(255, 182, 193));
        styleButton(resetButton, new Color(173, 216, 230));

        buttonPanel.add(guessButton);
        buttonPanel.add(resetButton);
        guessButton.setMargin(new Insets(20, 20, 20, 20));
        resetButton.setMargin(new Insets(20, 20, 20, 20));

        avatarLabel = new JLabel(new ImageIcon("resources/happy.png"));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        mainPanel.add(avatarLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(difficultyBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(inputField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(messageLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        mainPanel.add(attemptsLabel);

        add(mainPanel);
        setupGame();

        guessButton.addActionListener(e -> handleGuess());
        resetButton.addActionListener(e -> setupGame());

        setVisible(true);
    }

    private void setupGame() {
        String level = (String) difficultyBox.getSelectedItem();
        if (level.contains("50"))
            maxRange = 50;
        else if (level.contains("100"))
            maxRange = 100;
        else if (level.contains("200"))
            maxRange = 200;

        game = new GuessGame(maxRange);
        startTime = System.currentTimeMillis();

        attemptsLabel.setText("Attempts: 0");
        messageLabel.setText("Enter a number between 1 and " + maxRange);
        inputField.setText("");
        avatarLabel.setIcon(new ImageIcon("resources/happy.png"));
    }

    private void handleGuess() {
        String input = inputField.getText().trim();
        try {
            int guess = Integer.parseInt(input);
            if (guess < 1 || guess > maxRange) {
                messageLabel.setText("❌ Number must be between 1 and " + maxRange);
                avatarLabel.setIcon(new ImageIcon("resources/sad.png"));
                return;
            }

            boolean correct = game.checkGuess(guess);
            attemptsLabel.setText("Attempts: " + game.getAttempts());

            if (correct) {
                long time = (System.currentTimeMillis() - startTime) / 1000;
                messageLabel.setText("🎉 You guessed it in " + game.getAttempts() + " tries and " + time + " sec!");
                avatarLabel.setIcon(new ImageIcon("resources/happy.png"));
            } else if (guess < game.getNumberToGuess()) {
                messageLabel.setText("🔼 Too low! Try again.");
                avatarLabel.setIcon(new ImageIcon("resources/sad.png"));
            } else {
                messageLabel.setText("🔽 Too high! Try again.");
                avatarLabel.setIcon(new ImageIcon("resources/sad.png"));
            }

        } catch (NumberFormatException ex) {
            messageLabel.setText("⚠️ Please enter a valid number!");
            avatarLabel.setIcon(new ImageIcon("resources/sad.png"));
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GuessingGameGUI::new);
    }
}
