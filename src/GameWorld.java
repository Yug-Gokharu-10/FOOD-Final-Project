import java.util.*;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * The GameWorld class manages all game objects, user input,
 * physics updates, and collision detection.
 *
 * This version loads a Tiled TMX map and uses the layer
 * named "Platforms" as solid collision geometry.
 */
public class GameWorld extends Pane {

  
  private static double TILE_SCALE = 3.5; //3.5

  private Player player;
  private Set<KeyCode> keys = new HashSet<>();
  private List<Rectangle> collisionObjects = new ArrayList<>();
  private Tile rDoor = null;
  private Tile lDoor = null;
  private Tile trevorTrigger = null;
  private Tile trevorDoor = null;
  private Tile vijayTrigger = null;
  private Tile vijayDoor = null;

  // layer name -> tiles
  private Map<String, List<Tile>> tileLayers = new LinkedHashMap<>();

  /**
   * Initializes the world with a player and a tile map from Tiled.
   */
  public GameWorld(int width, int height, Player player) {
    setPrefSize(width, height);
    setStyle("-fx-background-color: white;");

    // load tiles first so the player is drawn on top
    loadTileMap("map/hallway_4W.tmx");

    // Create player near the top so gravity acts immediately
    this.player = player; 
    if (!getChildren().contains(player.getPlayer())) {
      getChildren().add(player.getPlayer());
    }
    player.getPlayer().setLayoutX(300);
    player.getPlayer().setLayoutY(300);


  }

  /**
   * Loads the TMX map, adds all tile layers for drawing,
   * and builds collision rectangles from the "Platforms" layer.
   */
  private void loadTileMap(String levelPath) {
    tileLayers.clear();
    collisionObjects.clear();
    getChildren().clear();
    
    
    try {
        // Load tile layers from TMX
        Map<String, List<Tile>> loaded = TiledMapLoader.loadTileMap(levelPath, TILE_SCALE);
        tileLayers.putAll(loaded);

        // Draw layers in order
        for (List<Tile> layerTiles : tileLayers.values()) {
            for (Tile tile : layerTiles) {
                getChildren().add(tile.getTileImage());
            }
        }

        // Collision layers
        List<Tile> platforms = tileLayers.get("Walls");
        if (platforms != null) {
            for (Tile tile : platforms) {
                collisionObjects.add(tile.getCollisionRectangle());
            }
        }
        
        //4W Doors
        if (tileLayers.get("Right Door") != null) {
          rDoor = tileLayers.get("Right Door").get(0);
        }

        if (tileLayers.get("Left Door") != null) {
          lDoor = tileLayers.get("Left Door").get(0);
        }

        // Trevor's Room
        if (tileLayers.get("TriggerTrevor") != null) {
            trevorTrigger = tileLayers.get("TriggerTrevor").get(0);
        }

        if (tileLayers.get("DoorTrevor") != null) {
            trevorDoor = tileLayers.get("DoorTrevor").get(0);
        }

        // Vijay's Room
        if (tileLayers.get("TriggerVijay") != null) {
            vijayTrigger = tileLayers.get("TriggerVijay").get(0);
        }

        if (tileLayers.get("DoorVijay") != null) {
            vijayDoor = tileLayers.get("DoorVijay").get(0);
        }
        


        this.layout();

        Bounds mapBounds = this.getBoundsInLocal();
        double mapWidth = mapBounds.getWidth();
        double mapHeight = mapBounds.getHeight();

        setPrefSize(mapWidth, mapHeight);
        setMinSize(mapWidth, mapHeight);
        setMaxSize(mapWidth, mapHeight);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

  /**
   * Optional accessor if you ever want to inspect tiles by layer.
   */
  public Map<String, List<Tile>> getTileLayers() {
    return tileLayers;
  }

  /** Records when a key is pressed so movement continues while held down. */
  public void handleKeyPressed(KeyCode code) {
    keys.add(code);
  }

  /** Removes a key when it is released. */
  public void handleKeyReleased(KeyCode code) {
    keys.remove(code);
  }

  /**
   * Updates the game state once per frame.
   * Called by the main loop in PlatformerDemo.
   */
    public void update(double deltaTime) {
      animation(); 
    }
    public void animation() {
        double dx = 0;
        double dy = 0;

        if (keys.contains(KeyCode.UP)) dy -= 2;
        if (keys.contains(KeyCode.DOWN)) dy += 2;
        if (keys.contains(KeyCode.LEFT)) dx -= 2;
        if (keys.contains(KeyCode.RIGHT)) dx += 2;


        Bounds futureBounds = player.getBoundsAfterMove(dx, dy);

        // For walls
        boolean collision = false;
        for (Rectangle wall : collisionObjects) {
            if (futureBounds.intersects(wall.getBoundsInParent())) {
                collision = true;
                break;
            }
        }
        if (!collision) {
            player.getPlayer().setLayoutX(player.getPlayer().getLayoutX() + dx);
            player.getPlayer().setLayoutY(player.getPlayer().getLayoutY() + dy);
        }
        
        // 4W HALLWAY
        //For 4W Doors
        boolean doorLCollision = false;
        if (lDoor != null && futureBounds.intersects(lDoor.getCollisionRectangle().getBoundsInParent())) {
            doorLCollision = true;
        }
        if (doorLCollision) {
          loadTileMap("map/trevor_room_4W.tmx");
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300);
          player.getPlayer().setLayoutY(300);
          App.setHallTag("Trevor's Room");
        }
        boolean doorRCollision = false;
        if (rDoor != null && futureBounds.intersects(rDoor.getCollisionRectangle().getBoundsInParent())) {
            doorRCollision = true;
        }
        if (doorRCollision) {
          loadTileMap("map/vijay_room_4W.tmx");
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300); 
          player.getPlayer().setLayoutY(300);
          App.setHallTag("Vijay's Room");
        }

        // TREVOR ROOM
        // For TrevorTrigger
        boolean trevorTriggerCollision = false;
        if (trevorTrigger != null && futureBounds.intersects(trevorTrigger.getCollisionRectangle().getBoundsInParent())) {
          trevorTriggerCollision = true;
        }

        if (trevorTriggerCollision) {
          App.changeScreen(new MemoryGame());
        }

        // For Trevor's Room door
        boolean trevorDoorCollision = false;
        if (trevorDoor != null && futureBounds.intersects(trevorDoor.getCollisionRectangle().getBoundsInParent())) {
          trevorDoorCollision = true;
        }

        if (trevorDoorCollision) {
          App.goToHallway();
          App.setHallTag("4TH WEST");
        }
        
        // VIJAY ROOM
        // For VijayTrigger
        boolean vijayTriggerCollision = false;
        if (vijayTrigger != null && futureBounds.intersects(vijayTrigger.getCollisionRectangle().getBoundsInParent())) {
          vijayTriggerCollision = true;
        }

        if (vijayTriggerCollision) {
          App.changeScreen(new VijayQuizGame());
        }

        // For Vijay room door
        boolean vijayDoorCollision = false;
        if (vijayDoor != null && futureBounds.intersects(vijayDoor.getCollisionRectangle().getBoundsInParent())) {
          vijayDoorCollision = true;
        }

        if (vijayDoorCollision) {
          App.goToHallway();
          App.setHallTag("4TH WEST");
        }
        
    }
}