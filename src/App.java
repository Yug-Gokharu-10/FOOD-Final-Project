import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.layout.StackPane;

public class App extends Application {

        private GameWorld gameWorld;

        @Override
        public void start(Stage stage) {
        int width = 1920;
        int height = 1200;

        // Create the game world and load the TMX map
        // Adjust the TMX path and collision layer name to match your Tiled project
        gameWorld = new GameWorld(width, height);
        
        stage.setTitle("Escape Hunt West");
        stage.setFullScreen(true);
        stage.initStyle(StageStyle.DECORATED);

        // StackPane stack = new StackPane();
        // stack.getChildren().add(gameWorld);

        BorderPane borderPane = new BorderPane();
        // borderPane.setCenter(stack);

        Scene scene = new Scene(borderPane, 100, 100);

        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(gameWorld);
        borderPane.setCenter(centerPane);
                

        scene.setOnKeyPressed(e -> gameWorld.handleKeyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> gameWorld.handleKeyReleased(e.getCode()));


        stage.setScene(scene);
        stage.show();

        // Animation loop
        AnimationTimer loop = new AnimationTimer() {
            private long previousTime = 0;

            @Override
            public void handle(long now) {
                if (previousTime == 0) {
                previousTime = now;
                return;
                }

                double delta = (now - previousTime) / 1_000_000_000.0;
                previousTime = now;

                gameWorld.update(delta);
            }
        };
        loop.start();
        
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}