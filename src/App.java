import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;

public class App extends Application {

    @Override 
    public void start(Stage stage) {
        stage.setTitle("Escape Hunt West");
        stage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}