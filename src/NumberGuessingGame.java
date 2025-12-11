import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class NumberGuessingGame extends GridPane {

    
    public NumberGuessingGame() {
        setAlignment(Pos.CENTER);
        setHgap(15);
        setVgap(15);

        Random random = new Random();
        int[] targetNumber = { random.nextInt(120) + 1 };
        int[] attemptCount = { 0 };
        ArrayList<Integer> previousGuesses = new ArrayList<>();

        Label promptLabel = new Label("Guess a number between 1-120:");
        TextField guessInput = new TextField();
        Button guessButton = new Button("Guess");
        Button newGameButton = new Button("New Game");
        Label feedbackLabel = new Label("You have 6 tries!");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(promptLabel, guessInput, guessButton, feedbackLabel, newGameButton);

        add(layout, 0, 0);



        guessButton.setOnAction(e -> {
            String inputText = guessInput.getText();
            int guess;
            try {
                guess = Integer.parseInt(inputText);
            } catch (Exception ex) {
                feedbackLabel.setText("Please enter a valid number!");
                guessInput.clear();
                return;
            }

            attemptCount[0]++;
            previousGuesses.add(guess);

            if (guess == targetNumber[0]) {
                feedbackLabel.setText("Correct! The number was " + targetNumber[0] + " in " + attemptCount[0] + " tries! Remember this letter: Y");
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished( ev -> {
                    App.goToHallway2W();
                });
                pause.play();
            } else if (attemptCount[0] >= 6) {
                feedbackLabel.setText("Out of tries! The number was " + targetNumber[0]);
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished( ev -> {
                    App.goToHallway2W();
                });
                pause.play();

            } else if (guess < targetNumber[0]) {
                feedbackLabel.setText("Too low! Tries: " + attemptCount[0] + "/6");
            } else {
                feedbackLabel.setText("Too high! Tries: " + attemptCount[0] + "/6");
            }

            guessInput.clear();
        });

        newGameButton.setOnAction(e -> {
            targetNumber[0] = random.nextInt(100) + 1;
            attemptCount[0] = 0;
            previousGuesses.clear();
            feedbackLabel.setText("New game! You have 6 tries.");
            guessInput.clear();
        });

    }


    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}