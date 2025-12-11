import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Random;

public class MessyMusicMashGame extends GridPane {

    private String first = "";
    private int count = 0;
    private int fails = 0;
    private boolean end = false;

    private ProgressBar progressBar = new ProgressBar(0);

    private Label bigLetter = new Label();
    private Label scoreThing = new Label("Score: 0");
    private Label oopsThing = new Label("Mistakes: 0 / 3");

    private Timeline looper;
    private Random fluke = new Random();

    
    public MessyMusicMashGame() {

        setAlignment(Pos.CENTER);
        setHgap(15);
        setVgap(15);

        Label title = new Label("Messy Music Mixer");
        title.setFont(new Font(28));

        Label tiny = new Label(
                "Undo the messy tune.\n" +
                "Type the LETTER shown (A-Z).\n" +
                "Clear 10 notes before the bar fills.\n" +
                "You can make 3 mistakes."
        );
        tiny.setFont(new Font(14));

        bigLetter.setFont(new Font(56));
        bigLetter.setTextFill(Color.DARKMAGENTA);

        pickOne();
        progressBar.setPrefWidth(360);


        add(title, 0, 0, 2, 1);
        add(tiny, 0, 1, 2, 1);
        add(bigLetter, 0, 2, 2, 1);
        add(scoreThing, 0, 3);
        add(oopsThing, 1, 3);
        add(progressBar, 0, 4, 2, 1);
        
        startnewround();

        
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyTyped(ev -> {
                    if (end) return;
                    String ch = ev.getCharacter();
                    if (ch == null || ch.length() == 0) return;
                    String cc = ch.toUpperCase();
                    if (!cc.matches("[A-Z]")) return;
                    press(cc);
                });
            }
        });

    }

    private void pickOne() {
        char c = (char) ('A' + fluke.nextInt(26));
        first = String.valueOf(c);
        bigLetter.setText(first);
    }

    private void press(String k) {

        if (end) return;

        if (k.equals(first)) {
            count++;
            scoreThing.setText("Score: " + count);

            double nd = progressBar.getProgress() - 0.10;
            if (nd < 0) nd = 0;
            progressBar.setProgress(nd);

            pickOne();

            if (count >= 10) wrapItUp(true);

        } else {
            fails++;
            oopsThing.setText("Mistakes: " + fails + " / 3");
            if (fails >= 3) wrapItUp(false);
        }
    }

    private void startnewround() {

        looper = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (end) return;

            double now = progressBar.getProgress();
            double nxt = now + 0.025;

            if (nxt >= 1.0) {
                progressBar.setProgress(1.0);
                if (looper != null) looper.stop();
                end = true;
                Platform.runLater(() -> {
                    Alert al = new Alert(Alert.AlertType.INFORMATION);
                    al.setHeaderText(null);
                    al.setTitle("Defeat");
                    al.setContentText("The chaos overwhelmed the tune...");
                    al.showAndWait();
                    App.goToHallway3W();
                });
            } else {
                progressBar.setProgress(nxt);
            }
        }));

        looper.setCycleCount(Timeline.INDEFINITE);
        looper.play();
    }

    private void wrapItUp(boolean nice) {
        if (end) return;
        end = true;
        if (looper != null) looper.stop();

        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(null);
            if (nice) {
                a.setTitle("Victory");
                a.setContentText("You fixed the messy music! REMEMBER THIS LETTER FOR YOUR CODE: Y");
                App.getProgressBar().add();
            } else {
                a.setTitle("Defeat");
                a.setContentText("Too many mistakes. YOu failed.");
            }
            a.showAndWait();
            App.goToHallway3W();
        });
    }

}