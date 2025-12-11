import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.Random;

public class RobotGame extends GridPane {

    private ProgressBar barA = new ProgressBar(1.0);
    private ProgressBar barB = new ProgressBar(1.0);

    private Button btnA = new Button("Boost A");
    private Button btnB = new Button("Boost B");

    private Timeline drip;
    private long begin;
    private long timetosurvive = 7000;

    private boolean dead = false;

    private long a = 0;
    private long b = 0;

    private Random randomdecrease = new Random();

    public RobotGame() {
        setAlignment(Pos.CENTER);
        setHgap(15);
        setVgap(15);

        Label title = new Label("ROBO TAPS");
        title.setFont(Font.font(28));

        barA.setPrefWidth(250);
        barB.setPrefWidth(250);

        btnA.setFont(Font.font(16));
        btnB.setFont(Font.font(16));

        VBox root = new VBox(20, title, barA, barB, btnA, btnB);
        root.setAlignment(Pos.CENTER);

        this.getChildren().add(root);

        btnA.setOnAction(e -> clickA());
        btnB.setOnAction(e -> clickB());

        begin = System.currentTimeMillis();
        runLoop();
    }

    

    

    private void clickA() {
        if (dead) return;
        long t = System.currentTimeMillis();
        if (t < a) return;

        double v = barA.getProgress();
        v += 0.22 + randomdecrease.nextDouble() * 0.1;
        if (v > 1) v = 1;
        barA.setProgress(v);

        a = t + 900;   
        btnA.setDisable(true);
    }

    private void clickB() {
        if (dead) return;
        long t = System.currentTimeMillis();
        if (t < b) return;

        double v = barB.getProgress();
        v += 0.20 + randomdecrease.nextDouble() * 0.12;
        if (v > 1) v = 1;
        barB.setProgress(v);

        b = t + 900;  
        btnB.setDisable(true);
    }

    private void runLoop() {
        drip = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            long now = System.currentTimeMillis();

            if (now >= a) btnA.setDisable(false);
            if (now >= b) btnB.setDisable(false);

            double a = barA.getProgress();
            double b = barB.getProgress();

            a -= 0.032 + randomdecrease.nextDouble() * 0.005;  
            b -= 0.036 + randomdecrease.nextDouble() * 0.006;  

            barA.setProgress(a);
            barB.setProgress(b);

            if (a <= 0 || b <= 0) endGame(false);

            if (now - begin >= timetosurvive) endGame(true);
        }));

        drip.setCycleCount(Timeline.INDEFINITE);
        drip.play();
    }

    private void endGame(boolean win) {
        if (dead) return;
        dead = true;
        if (drip != null) drip.stop();

        Platform.runLater(() -> {
            Alert gg = new Alert(Alert.AlertType.INFORMATION);
            gg.setHeaderText(null);
            if (win) {
                gg.setTitle("Stable");
                gg.setContentText("You win! Remember this: R");
                App.getProgressBar().add();
            } else {
                gg.setTitle("Shutdown");
                gg.setContentText("The robot powered down. YOU LOSE BUM!");
            }
            gg.showAndWait();
            App.goToHallway1W();
        });
    }


}