import java.util.*;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The GameWorld class manages all game objects, user input,
 * physics updates, and collision detection.
 *
 * This version loads a Tiled TMX map and uses the layer
 * named "Platforms" as solid collision geometry.
 */
public class GameWorld extends Pane {

  private static final String TMX_PATH = "map/hallway_4W.tmx";
  private static final double TILE_SCALE = 3.0;

  private Player player;
  private Set<KeyCode> keys = new HashSet<>();
  private List<Rectangle> collisionObjects = new ArrayList<>();

  // layer name -> tiles
  private Map<String, List<Tile>> tileLayers = new LinkedHashMap<>();

  /**
   * Initializes the world with a player and a tile map from Tiled.
   */
  public GameWorld(int width, int height) {
    setPrefSize(width, height);
    setStyle("-fx-background-color: white;");

    // load tiles first so the player is drawn on top
    loadTileMap();

    // Create player near the top so gravity acts immediately
    player = new Player(50, 50, Color.DARKCYAN);
    getChildren().add(player.getPlayer());
    player.getPlayer().setLayoutX(300); // adjust based on your map
    player.getPlayer().setLayoutY(300);

  }

  /**
   * Loads the TMX map, adds all tile layers for drawing,
   * and builds collision rectangles from the "Platforms" layer.
   */
  private void loadTileMap() {
    try {
        // Load tile layers from TMX
        Map<String, List<Tile>> loaded = TiledMapLoader.loadTileMap(TMX_PATH, TILE_SCALE);
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
    // handleInput();
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
    }

    // public void animation() {
    //     double x = 0;
    //     double y = 0;

    //     if (keys.contains(KeyCode.UP)) {
    //         // y -= Player.MOVEMENT_Y;
    //         player.moveUp();
    //     }
    //     if (keys.contains(KeyCode.DOWN)) {
    //         // y += Player.MOVEMENT_Y;
    //         player.moveDown();
    //     }
    //     if (keys.contains(KeyCode.LEFT)) {
    //         // x -= Player.MOVEMENT_X;
    //         player.moveLeft();
    //     }
    //     if (keys.contains(KeyCode.RIGHT)) {
    //         // x += Player.MOVEMENT_X;
    //         player.moveRight();
    //     }

    //     player.getPlayer().setLayoutX(player.getPlayer().getLayoutX() + x);
    //     player.getPlayer().setLayoutY(player.getPlayer().getLayoutY() + y);
    // }
  
}