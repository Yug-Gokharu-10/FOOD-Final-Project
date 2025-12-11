import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.geometry.Pos;
import java.util.*;

public class TicTacToeGame extends GridPane {
    boolean gameover = false;
    int turn = 0;
    ArrayList<Integer> taken = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0));
    ArrayList<Button> buttons = new ArrayList<>();

    public TicTacToeGame() {
        setAlignment(Pos.CENTER);
        setHgap(15);
        setVgap(15);

        for (int i = 0; i < 9; i++) {
            Button b = new Button(" ");
            b.setMinSize(100,100);
            int index = i;

            b.setOnAction(e -> {
                if (gameover || taken.get(index) != 0 || turn % 2 == 0) return;

                b.setText("X");
                taken.set(index, 1);

                if (checkWin()) {
                    endGame();
                    return;
                } 
                
                turn++;
                botMove();
            });

            buttons.add(index, b);
            add(b, i%3, i/3);
        }
        botMove();
    }

    private void botMove() {
        if (gameover) return;

        showAlert("BOT's TURN","Bot is thinking...");

        List<Integer> empty = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            if (taken.get(i) == 0) empty.add(i);

        if (empty.isEmpty()) { 
            showAlert("Game Over","It's a draw!"); 
            App.goToHallway2W();
            return;
        }

        int move = empty.get(new Random().nextInt(empty.size()));

        buttons.get(move).setText("O");
        taken.set(move, 2);

        if (checkWin()) {
            endGame();
            //turn++; 
            return;
        }
        turn++;
            
    }


    private boolean checkWin() {
        int[][] wins = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };

        for (int[] w : wins)
            if (taken.get(w[0]) != 0 &&
                taken.get(w[0]).equals(taken.get(w[1])) &&
                taken.get(w[1]).equals(taken.get(w[2])))
                return true;

        return false;
    }

    private void endGame() {
        gameover = true;
        int winner = getWinner();
        if (winner == 1) showAlert("Game Over","You Won 🎉! REMEMBER THIS LETTER FOR THE EXIT CODE: F"); App.goToHallway2W();
        if (winner == 2) showAlert("Game Over","Bot Won 🤖"); App.goToHallway2W();
        
    }

    private int getWinner() {
        int[][] wins = {
            {0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}
        };
        for (int[] w: wins)
            if (taken.get(w[0]) != 0 &&
                taken.get(w[0]).equals(taken.get(w[1])) &&
                taken.get(w[1]).equals(taken.get(w[2])))
                return taken.get(w[0]);
        return 0;
    }

    private void showAlert(String t,String m){
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION,m);
            a.setTitle(t);
            a.setHeaderText(null);
            a.showAndWait();
        });
        
    }


}