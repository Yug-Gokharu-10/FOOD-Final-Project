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

public class VijayQuizGame extends GridPane {

    ArrayList<String> Questionbank = new ArrayList<>();
    ArrayList<String> Correct = new ArrayList<>();
    ArrayList<String> UserAnswer = new ArrayList<>();

    Label questionLabel = new Label();
        

    int turn = 0;

    
    public VijayQuizGame() {
        setAlignment(Pos.CENTER);
        setHgap(15);
        setVgap(15);

        add(questionLabel, 0, 2, 2, 1);

        Questionbank.add("What is Vijay Adkin known for saying?\nA) Hello Juniors\nB) Badababam\nC) Answer Choice\nD) Joshua Chilikuri");
        Correct.add("A");

        Questionbank.add("Which hall does Vijay Adkin live on ?\nA) 3\nB) 4\nC) 2\nD) 1");
        Correct.add("B");

        Questionbank.add("Who does Vijay Adkin extort for physics answers?\nA) Yug Ghokaru\nB) Sai Mudium\nC) Raghav Arun\nD) All of the Above");
        Correct.add("D");

        Questionbank.add("What shoes does Vijay Adkin wear most of the time?\nA) Nike Sneakers\nB) Addidas Slides\nC) Hoka Running Shoes\nD) Socks");
        Correct.add("B");
        Questionbank.add("What club is Vijay Adkin co-president of?\nA) DECA\nB) TSA\nC) FBLA\nD) Hosa");
        Correct.add("C");
        Questionbank.add("What event does Vijay Adkin do in FBLA?\nA) Securities and Investments\nB) Business Law\nC) Accounting\nD) None of the above");
        Correct.add("A");
        Questionbank.add("Who is Vijay Adkin's roommate?\nA) Joshua Chilikuri\nB) Yug Ghokaru\nC) Preston Cosby\nD) Joshua Taylor");
        Correct.add("D");
        Questionbank.add("Who is Vijay Adkin's neighbor?\nA) Preston Cosby \nB) Yug Ghokaru\nC) Jacob Phillip\nD) Finley Mclean");
        Correct.add("B");
        Questionbank.add("What forum does Vijay Adkin run?\nA) Economics Forum \nB) Engineering Design\nC) App Development\nD) None of the above");
        Correct.add("A");
        Questionbank.add("What hall was Vijay Adkin on last year?\nA) 4 \nB) 3\nC) 2\nD) 1");
        Correct.add("D");

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
            //showAlert("Question " + (turn + 1), Questionbank.get(turn));
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
            showAlert("Game over","Congrats you beat the Game. Remember this LETTER FOR YOUR CODE: A");
            // GameWorld.updateProgressBar();
            App.goToHallway4W();
        }
        else {
            showAlert(
                "Game Over", "You Lost! Going back to hallway...");
        }
        App.goToHallway4W();
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