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
    private long beginAt;
    private long surviveMs = 7000;

    private boolean dead = false;

    private long aFreeAt = 0;
    private long bFreeAt = 0;

    private Random rr = new Random();

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

        btnA.setOnAction(e -> tapA());
        btnB.setOnAction(e -> tapB());

        beginAt = System.currentTimeMillis();
        runLoop();
    }

    

    

    private void tapA() {
        if (dead) return;
        long t = System.currentTimeMillis();
        if (t < aFreeAt) return;

        double v = barA.getProgress();
        v += 0.22 + rr.nextDouble() * 0.1;
        if (v > 1) v = 1;
        barA.setProgress(v);

        aFreeAt = t + 900;   
        btnA.setDisable(true);
    }

    private void tapB() {
        if (dead) return;
        long t = System.currentTimeMillis();
        if (t < bFreeAt) return;

        double v = barB.getProgress();
        v += 0.20 + rr.nextDouble() * 0.12;
        if (v > 1) v = 1;
        barB.setProgress(v);

        bFreeAt = t + 900;  
        btnB.setDisable(true);
    }

    private void runLoop() {
        drip = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            long now = System.currentTimeMillis();

            if (now >= aFreeAt) btnA.setDisable(false);
            if (now >= bFreeAt) btnB.setDisable(false);

            double a = barA.getProgress();
            double b = barB.getProgress();

            a -= 0.032 + rr.nextDouble() * 0.005;  
            b -= 0.036 + rr.nextDouble() * 0.006;  

            barA.setProgress(a);
            barB.setProgress(b);

            if (a <= 0 || b <= 0) endGame(false);

            if (now - beginAt >= surviveMs) endGame(true);
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
                gg.setContentText("You kept the robot running! Remember this: R");
            } else {
                gg.setTitle("Shutdown");
                gg.setContentText("The robot powered down.");
            }
            gg.showAndWait();
            App.goToHallway1W();
        });
    }


}