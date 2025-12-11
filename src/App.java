import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;



public class App extends Application {
        private static BorderPane borderPane = new BorderPane();
        private GameWorld gameWorld;
        private static Text hallTag = new Text("4TH WEST");

        public static String getHallTag() {
            return hallTag.getText();
        }

        public static void setHallTag(String hallTagName) {
            hallTag.setText(hallTagName);
        }

        private static Player player;

        @Override
        public void start(Stage stage) {
        int width = 1920;
        int height = 1200;
        player = new Player(40, 40, Color.DARKCYAN);
        

        // Create the game world and load the TMX map
        // Adjust the TMX path and collision layer name to match your Tiled project
        
        GameWorld gameWorld = new GameWorld(width, height, player, "map/hallway_4W.tmx");
        
        
        stage.setTitle("Escape Hunt West");
        stage.setFullScreen(true);
        stage.initStyle(StageStyle.DECORATED);


        Scene scene = new Scene(borderPane, 100, 100);


        borderPane.setCenter(gameWorld);

        hallTag.setX(575);
        hallTag.setY(900);
        hallTag.setFont(Font.font("Bungee Inline", FontWeight.BOLD, 80));
        hallTag.setFill(Color.GOLD);
        

        borderPane.getChildren().add(hallTag);
        BorderPane.setAlignment(hallTag, Pos.BOTTOM_CENTER);


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

                if (borderPane.getCenter() instanceof GameWorld world) {
                    world.update(delta);
                }
            }
        };
        loop.start();
        
    }

    public static void main(String[] args) {
        Application.launch(args);

    }

    public static void changeScreen(Node node){
        borderPane.setCenter(node);
    }

    public static Player getPlayer() {
        return player;
    }

    public static void goToHallway4W() {
        GameWorld newWorld = new GameWorld(1920, 1200, player, "map/hallway_4W.tmx");
        borderPane.setCenter(newWorld);
        //setHallTag("4TH WEST");
        Scene scene = borderPane.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> newWorld.handleKeyPressed(e.getCode()));
            scene.setOnKeyReleased(e -> newWorld.handleKeyReleased(e.getCode()));
        }
    }

    public static void goToHallway3W() {
        GameWorld newWorld = new GameWorld(1920, 1200, player, "map/hallway_3W.tmx");
        borderPane.setCenter(newWorld);
        setHallTag("3RD WEST");
        Scene scene = borderPane.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> newWorld.handleKeyPressed(e.getCode()));
            scene.setOnKeyReleased(e -> newWorld.handleKeyReleased(e.getCode()));
        }

    }

    public static void goToHallway2W() {
        GameWorld newWorld = new GameWorld(1920, 1200, player, "map/hallway_2W.tmx");
        borderPane.setCenter(newWorld);
        setHallTag("2ND WEST");
        Scene scene = borderPane.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> newWorld.handleKeyPressed(e.getCode()));
            scene.setOnKeyReleased(e -> newWorld.handleKeyReleased(e.getCode()));
        }
    }

    public static void goToHallway1W() {
        GameWorld newWorld = new GameWorld(1920, 1200, player, "map/hallway_1W.tmx");
        borderPane.setCenter(newWorld);
        setHallTag("1ST WEST");
        Scene scene = borderPane.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> newWorld.handleKeyPressed(e.getCode()));
            scene.setOnKeyReleased(e -> newWorld.handleKeyReleased(e.getCode()));
        }

    }

    public static void showAlert4W(String t,String m){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //alert.setTitle(title);
            alert.setHeaderText(null);
            //alert.setContentText(msg);
            alert.showAndWait();
        }); 
    }


}