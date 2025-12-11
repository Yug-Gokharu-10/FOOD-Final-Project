import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class GameProgressBar extends GridPane {

    private int totalPuzzles;
    private int puzzlesCompleted;
    private ProgressBar progressBar;

    public GameProgressBar(int totalPuzzles) {
        this.totalPuzzles = totalPuzzles;
        this.puzzlesCompleted = 0;
        this.progressBar = new ProgressBar();

        progressBar.setPrefWidth(300);
        progressBar.setLayoutX(20);
        progressBar.setLayoutY(20);
        getChildren().add(progressBar);
    }

    public void updateProgress() {
        puzzlesCompleted++;
        double progress = (double) puzzlesCompleted / totalPuzzles;
        //System.out.println("Progress: " + (progress * 100) + "%");
        Platform.runLater(() -> progressBar.setProgress(progress));
    }

    public int getPuzzlesCompleted() {
        return puzzlesCompleted;
    }

    public int getTotalPuzzles() {
        return totalPuzzles;
    }
    
}
