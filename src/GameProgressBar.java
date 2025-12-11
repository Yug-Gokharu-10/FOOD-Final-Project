import javafx.scene.layout.GridPane;
import javafx.scene.control.ProgressBar;

public class GameProgressBar extends GridPane {

    private ProgressBar bar;
    private int totalPuzzles;
    private int puzzlesCompleted = 0;

    public GameProgressBar(int totalPuzzles) {
        this.totalPuzzles = totalPuzzles;

        bar = new ProgressBar(0);
        bar.setPrefWidth(2400);

        this.add(bar, 0, 0);   
    }

    public void add() {
        puzzlesCompleted++;
        update();
    }

    private void update() {
        double fraction = (double) puzzlesCompleted / totalPuzzles;
        bar.setProgress(fraction);
    }

    public int getPuzzlesCompleted() { return puzzlesCompleted; }
    public int getTotalPuzzles() { return totalPuzzles; }
}
