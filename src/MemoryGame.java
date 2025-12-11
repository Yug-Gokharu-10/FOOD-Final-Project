import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class MemoryGame extends GridPane {

    private ArrayList<Integer> sequence = new ArrayList<>();
    private int playerIndex = 0;
    private Random random = new Random();
    private ArrayList<String> colors = new ArrayList<>();

    public MemoryGame() {
        Button redBtn = createColorButton(Color.RED, 0);
        Button blueBtn = createColorButton(Color.BLUE, 1);
        Button greenBtn = createColorButton(Color.GREEN, 2);
        Button yellowBtn = createColorButton(Color.YELLOW, 3);
        colors.add("Red");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Yellow");
        this.setAlignment(Pos.CENTER);
        this.setHgap(15);
        this.setVgap(15);

        this.add(redBtn,   0, 0);
        this.add(blueBtn,  1, 0);
        this.add(greenBtn, 0, 1);
        this.add(yellowBtn,1, 1);

        
        App.changeScreen(this);

        Platform.runLater(() -> startNewRound());
    }

    
    private Button createColorButton(Color color, int colorIndex) {
        Button btn = new Button();
        btn.setMinSize(150, 150);
        btn.setStyle("-fx-background-color: " + toRgbCode(color) + ";");

        btn.setOnAction(e -> handlePlayerInput(colorIndex));
        return btn;
    }

    private String toRgbCode(Color c) {
        return String.format("rgb(%d,%d,%d)",
                (int)(c.getRed() * 250),
                (int)(c.getGreen() * 250),
                (int)(c.getBlue() * 250));
    }


    private void startNewRound() {
        sequence.add(random.nextInt(4));
        playerIndex = 0;

        int roundNum = sequence.size();
        String nextColor = colors.get(sequence.get(roundNum - 1));

        showAlert("Round " + roundNum, "New color: " + nextColor);

        if (roundNum == 16) {
            showAlert("Game Over", "Congratulations! You beat the Memory Game! REMEMBER THIS LETTER FOR YOUR CODE: X");
            App.changeScreen(new GameWorld(1920, 1200, App.getPlayer(), "map/hallway_4W.tmx"));
        }   
    }

   

    private void handlePlayerInput(int input) {
        if (input == sequence.get(playerIndex)) {
            playerIndex++;

            // If player has correctly entered the whole sequence
            if (playerIndex == sequence.size()) {
                showAlert("Correct!", "Round complete!");
                if (sequence.size() == 10) {
                    showAlert("Game Over", "Congratulations! You beat the Memory Game! REMEMBER THIS LETTER FOR YOUR CODE: X");
                    // App.changeScreen(new GameWorld(1920, 1200, App.getPlayer(), "map/hallway_4W.tmx"));
                    App.goToHallway4W();
                } else {
                    startNewRound();
                }
            }
        } else {
            showAlert("Game Over", "Wrong button! Returning to hallway...");
            sequence.clear();
            App.goToHallway4W();
        }
    }



    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
}