import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import java.util.Random;

public class JobHuntGame extends GridPane {
    
    public JobHuntGame() {
        this.setAlignment(Pos.CENTER);
        this.setHgap(15);
        this.setVgap(15);

        Random rand = new Random();
        int[] dayCount = {0};
        boolean[] gotJob = {false};

        Label lblTitle = new Label("Job Hunt Game");
        Label lblInstructions = new Label(
            "Welcome! You have 4 days to get a job.\n" +
            "Choose wisely:\n" +
            "- Aggressive: 40% chance, counts as 2 days\n" +
            "- Conservative: 20% chance, counts as 1 day\n" +
            "Balance your choices to win!"
        );
        Label lblFeedback = new Label("Days left: 4");

        Button btnAggressive = new Button("Apply Aggressively");
        Button btnConservative = new Button("Apply Conservatively");

        HBox buttons = new HBox(10, btnAggressive, btnConservative);
        buttons.setAlignment(Pos.CENTER);

        // VBox layout = new VBox(15, lblTitle, lblInstructions, lblFeedback, buttons);
        // layout.setAlignment(Pos.CENTER);

        add(lblTitle, 0, 0, 2, 1);
        add(lblInstructions, 0, 1, 2, 1);
        add(lblFeedback, 0, 2, 2, 1);
        add(buttons, 0, 3, 2, 1);


        btnAggressive.setOnAction(e -> {
            if (gotJob[0]) return;

            dayCount[0] += 2;
            int chance = rand.nextInt(100);
            if (chance < 40) {
                gotJob[0] = true;
                showResult("You Win!", "Congrats! You got a job on day " + dayCount[0] + "!");
            } else if (dayCount[0] >= 4) {
                showResult("Game Over", "You failed to get a job in 4 days.");
            } else {
                lblFeedback.setText("No job today. Days left: " + (4 - dayCount[0]));
            }
        });

        btnConservative.setOnAction(e -> {
            if (gotJob[0]) return;

            dayCount[0] += 1;
            int chance = rand.nextInt(100);
            if (chance < 20) {
                gotJob[0] = true;
                showResult("You Win!", "Congrats! You got a job on day " + dayCount[0] + "!");
            } else if (dayCount[0] >= 4) {
                showResult("Game Over", "You failed to get a job in 4 days.");
            } else {
                lblFeedback.setText("No job today. Days left: " + (4 - dayCount[0]));
            }
        });
    }

    public void start(Stage stage) {
        Random rand = new Random();
        int[] dayCount = {0};
        boolean[] gotJob = {false};

        Label lblTitle = new Label("Job Hunt Game");
        Label lblInstructions = new Label(
            "Welcome! You have 4 days to get a job.\n" +
            "Choose wisely:\n" +
            "- Aggressive: 40% chance, counts as 2 days\n" +
            "- Conservative: 20% chance, counts as 1 day\n" +
            "Balance your choices to win!"
        );
        Label lblFeedback = new Label("Days left: 4");

        Button btnAggressive = new Button("Apply Aggressively");
        Button btnConservative = new Button("Apply Conservatively");

        HBox buttons = new HBox(10, btnAggressive, btnConservative);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, lblTitle, lblInstructions, lblFeedback, buttons);
        layout.setAlignment(Pos.CENTER);

        btnAggressive.setOnAction(e -> {
            if (gotJob[0]) return;

            dayCount[0] += 2;
            int chance = rand.nextInt(100);
            if (chance < 40) {
                gotJob[0] = true;
                showResult("You Win!", "Congrats!REMEMBER THIS LETTER FOR YOUR CODE: G");
            } else if (dayCount[0] >= 4) {
                showResult("Game Over", "You failed to get a job in 4 days.");
            } else {
                lblFeedback.setText("No job today. Days left: " + (4 - dayCount[0]));
            }
        });

        btnConservative.setOnAction(e -> {
            if (gotJob[0]) return;

            dayCount[0] += 1;
            int chance = rand.nextInt(100);
            if (chance < 20) {
                gotJob[0] = true;
                showResult("You Win!", "Congrats! REMEMBER THIS LETTER FOR YOUR CODE: G");
            } else if (dayCount[0] >= 4) {
                showResult("Game Over", "You failed to get a job in 4 days.");
            } else {
                lblFeedback.setText("No job today. Days left: " + (4 - dayCount[0]));
            }
        });
    }

    private void showResult(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        App.goToHallway3W();
    }

}