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

    private String wobble = "";
    private int tidyCount = 0;
    private int faceplants = 0;
    private boolean kaput = false;

    private ProgressBar doomBar = new ProgressBar(0);

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
                "Un-tangle the messy tune.\n" +
                "Type the LETTER shown (A-Z).\n" +
                "Clear 10 notes before the bar fills.\n" +
                "3 mistakes and it's over."
        );
        tiny.setFont(new Font(14));

        bigLetter.setFont(new Font(56));
        bigLetter.setTextFill(Color.DARKMAGENTA);

        pickOne();
        doomBar.setPrefWidth(360);


        add(title, 0, 0, 2, 1);
        add(tiny, 0, 1, 2, 1);
        add(bigLetter, 0, 2, 2, 1);
        add(scoreThing, 0, 3);
        add(oopsThing, 1, 3);
        add(doomBar, 0, 4, 2, 1);
        
        startTheDoom();

        
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyTyped(ev -> {
                    if (kaput) return;
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
        wobble = String.valueOf(c);
        bigLetter.setText(wobble);
    }

    private void press(String k) {

        if (kaput) return;

        if (k.equals(wobble)) {
            tidyCount++;
            scoreThing.setText("Score: " + tidyCount);

            double nd = doomBar.getProgress() - 0.10;
            if (nd < 0) nd = 0;
            doomBar.setProgress(nd);

            pickOne();

            if (tidyCount >= 10) wrapItUp(true);

        } else {
            faceplants++;
            oopsThing.setText("Mistakes: " + faceplants + " / 3");
            if (faceplants >= 3) wrapItUp(false);
        }
    }

    private void startTheDoom() {

        looper = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (kaput) return;

            double now = doomBar.getProgress();
            double nxt = now + 0.025;

            if (nxt >= 1.0) {
                doomBar.setProgress(1.0);
                if (looper != null) looper.stop();
                kaput = true;
                Platform.runLater(() -> {
                    Alert al = new Alert(Alert.AlertType.INFORMATION);
                    al.setHeaderText(null);
                    al.setTitle("Defeat");
                    al.setContentText("The chaos overwhelmed the tune...");
                    al.showAndWait();
                    App.goToHallway3W();
                });
            } else {
                doomBar.setProgress(nxt);
            }
        }));

        looper.setCycleCount(Timeline.INDEFINITE);
        looper.play();
    }

    private void wrapItUp(boolean nice) {
        if (kaput) return;
        kaput = true;
        if (looper != null) looper.stop();

        Platform.runLater(() -> {
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText(null);
            if (nice) {
                al.setTitle("Victory");
                al.setContentText("You fixed the messy music! REMEMBER THIS LETTER FOR YOUR CODE: Y");
            } else {
                al.setTitle("Defeat");
                al.setContentText("Too many mistakes — the tune stays messy.");
            }
            al.showAndWait();
            App.goToHallway3W();
        });
    }

}