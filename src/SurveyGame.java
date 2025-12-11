import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import java.util.*;

public class SurveyGame extends GridPane {

    ArrayList<String> Questionbank = new ArrayList<>();
    ArrayList<String> Correct = new ArrayList<>();
    ArrayList<String> UserAnswer = new ArrayList<>();

    Label questionLabel = new Label();
        

    int turn = 0;

    
    public SurveyGame() {
        setAlignment(Pos.CENTER);
        setHgap(15);
        setVgap(15);

        add(questionLabel, 0, 2, 2, 1);

        Questionbank.add("What is the theme of the room with the Simon Says Color Game?\nA) Computing\nB) Memory\nC) Sports\nD) Joshua Chilikuri");
        Correct.add("A");

        Questionbank.add("What is the Theme of the room with the Vijay Adkin Quiz ?\nA)Computing 3\nB)Finance 4\nC)Music 2\nD) Locked In");
        Correct.add("B");

        Questionbank.add("What is the theme of the job hunt game?\nA) Julian Steck\nB) Entrepreneurship\nC) Success\nD) Joblessness");
        Correct.add("D");

        Questionbank.add("What is the theme of the music unraveling game?\nA) music\nB) instruments\nC) Gym\nD) None of the above");
        Correct.add("A");

        Questionbank.add("What is the theme of the game with Tic-tac-toe?\nA) food\nB) TSA\nC) FBLA\nD) Raghav");
        Correct.add("A");

        Questionbank.add("What is the theme of the Number Guessing room?\nA) Studying\nB)Physics\nC) Jobfulness\nD) None of the above");
        Correct.add("A");

        Questionbank.add("What is the theme of the reactor stabilization game?\nA) NBA Youngboy\nB) Yug Ghokaru\nC) Robotics\nD) Joshua Taylor");
        Correct.add("c");

        Button redBtn = createColorButton(Color.RED, 0, "A");
        redBtn.setOnAction(e -> handleAnswer("A"));

        Button blueBtn = createColorButton(Color.BLUE, 1, "B");
        blueBtn.setOnAction(e -> handleAnswer("B"));

        Button greenBtn = createColorButton(Color.GREEN, 2, "C");
        greenBtn.setOnAction(e -> handleAnswer("C"));

        Button yellowBtn = createColorButton(Color.YELLOW, 3, "D");
        yellowBtn.setOnAction(e -> handleAnswer("D"));


        add(redBtn, 0, 0);
        add(blueBtn, 1, 0);
        add(greenBtn, 0, 1);
        add(yellowBtn, 1, 1);

        StartnewRound();
        
    }

    private void handleAnswer(String answer) {
        UserAnswer.add(answer);

        if (answer.equals(Correct.get(turn))) {
            showAlert("Correct!", "Nice job!");
        } else {
            showAlert("Incorrect","Try Again Next Quiz");
        }

        turn++;
        StartnewRound();
    }

    private void StartnewRound() {
        
        if (turn < Questionbank.size()) {
            questionLabel.setText("Question " + (turn + 1) + ":\n" + Questionbank.get(turn));
        } else {
            endGame();
        }
    }

    private void endGame() {
        int score = 0;
        for (int i = 0; i < UserAnswer.size(); i++) {
            if (UserAnswer.get(i).equals(Correct.get(i))) {
                score++;
            }
        }

        showAlert("Game Over",
                "You scored " + score + " out of " + Questionbank.size());

        // Reset game
        if (score >= 0.9*Questionbank.size()){
            showAlert("Game over","Congrats you beat the Game. Remember this LETTER FOR YOUR CODE: R");
            App.goToHallway1W();
        }
        else {
            showAlert(
                "Game Over", "You Lost! Going back to hallway...");
        }
        App.goToHallway1W();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private Button createColorButton(Color color, int colorIndex, String message) {
        Button btn = new Button(message);
        btn.setMinSize(150, 150);
        btn.setStyle("-fx-background-color: " + toRgbCode(color) + ";");
        return btn;
    }

    private String toRgbCode(Color c) {
        return String.format("rgb(%d,%d,%d)",
        (int)(c.getRed() * 250),
        (int)(c.getGreen() * 250),
        (int)(c.getBlue() * 250));
    }
}