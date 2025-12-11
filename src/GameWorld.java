import java.util.*;
import javafx.scene.media.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * The GameWorld class manages all game objects, user input,
 * physics updates, and collision detection.
 *
 * This version loads a Tiled TMX map and uses the layer
 * named "Platforms" as solid collision geometry.
 */
public class GameWorld extends Pane {

  boolean cooldown4WExit = false;
  boolean cooldown3WExit = false;
  boolean cooldown2WExit = false;
  boolean cooldownWinExit = false;
  boolean cooldownLoseExit = false;
  
  // private static GameProgressBar gameProgressBar;
  // private static final int totalPuzzles = 8;

  private Player player;
  private Set<KeyCode> keys = new HashSet<>();
  private List<Rectangle> collisionObjects = new ArrayList<>();
  private Tile rDoor = null;
  private Tile lDoor = null;
  private Tile trevorTrigger = null;
  private Tile trevorDoor = null;
  private Tile vijayTrigger = null;
  private Tile vijayDoor = null;
  private Tile doorExit4W = null;
  private Tile vivaanDoorEnter = null;
  private Tile vivaanDoorExit = null;
  private Tile vivaanTrigger = null;
  private Tile anikethDoorEnter = null;
  private Tile anikethDoorExit = null;
  private Tile anikethTrigger = null;
  private Tile doorExit3W = null;
  private Tile raghavDoorEnter = null;
  private Tile raghavDoorExit = null;
  private Tile raghavTrigger = null;
  private Tile ekanshDoorEnter = null;
  private Tile ekanshDoorExit = null;
  private Tile ekanshTrigger = null;
  private Tile doorExit2W = null;
  private Tile abhinavDoorEnter = null;
  private Tile abhinavDoorExit = null;
  private Tile abhinavTrigger = null;
  private Tile snehalDoorEnter = null;
  private Tile snehalDoorExit = null;
  private Tile snehalTrigger = null;
  private Tile losingDoor = null;
  private Tile winningDoor = null;
  private Tile doorExitGame = null;


  // layer name -> tiles
  private Map<String, List<Tile>> tileLayers = new LinkedHashMap<>();

  /**
   * Initializes the world with a player and a tile map from Tiled.
   */
  public GameWorld(int width, int height, Player player, String mapPath) {
    setPrefSize(width, height);
    setStyle("-fx-background-color: white;");

    // load tiles first so the player is drawn on top
    loadTileMap(mapPath, 3.5);

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
  private void loadTileMap(String levelPath, double scale) {
    tileLayers.clear();
    collisionObjects.clear();
    getChildren().clear();
    
    try {
        // Load tile layers from TMX
        Map<String, List<Tile>> loaded = TiledMapLoader.loadTileMap(levelPath, scale);
        tileLayers.putAll(loaded);

        // Draw layers in order
        for (List<Tile> layerTiles : tileLayers.values()) {
            for (Tile tile : layerTiles) {
                getChildren().add(tile.getTileImage());
            }
        }

        // Walls
        List<Tile> platforms = tileLayers.get("Walls");
        if (platforms != null) {
            for (Tile tile : platforms) {
                collisionObjects.add(tile.getCollisionRectangle());
            }
        }

        rDoor = null;
        lDoor = null;
        trevorTrigger = null;
        trevorDoor = null;
        vijayTrigger = null;
        vijayDoor = null;
        doorExit4W = null;
        vivaanDoorEnter = null;
        vivaanDoorExit = null;
        vivaanTrigger = null;
        anikethDoorEnter = null;
        anikethDoorExit = null;
        anikethTrigger = null;
        doorExit3W = null;
        raghavDoorEnter = null;
        raghavDoorExit = null;
        raghavTrigger = null;
        ekanshDoorEnter = null;
        ekanshDoorExit = null;
        ekanshTrigger = null;
        doorExit2W = null;
        abhinavDoorEnter = null;
        abhinavDoorExit = null;
        abhinavTrigger = null;
        snehalDoorEnter = null;
        snehalDoorExit = null;
        snehalTrigger = null;
        losingDoor = null;
        winningDoor = null;
        doorExitGame = null;



        //4W Hallway Doors
        if (levelPath.equals("map/hallway_4W.tmx")) {
            if (tileLayers.get("Trevor Door Enter") != null) {
                rDoor = tileLayers.get("Trevor Door Enter").get(0);
            }
            if (tileLayers.get("Vijay Door Enter") != null) {
                lDoor = tileLayers.get("Vijay Door Enter").get(0);
            }
            if (tileLayers.get("Exit Door 4W") != null) {
                doorExit4W = tileLayers.get("Exit Door 4W").get(0);
            }
        }

        // Trevor's rooom
        if (levelPath.equals("map/trevor_room_4W.tmx")) {
            if (tileLayers.get("TriggerTrevor") != null) {
                trevorTrigger = tileLayers.get("TriggerTrevor").get(0);
            }
            if (tileLayers.get("Trevor Door Exit") != null) {
                trevorDoor = tileLayers.get("Trevor Door Exit").get(0);
            }
        }

        // Vijay's room
        if (levelPath.equals("map/vijay_room_4W.tmx")) {
            if (tileLayers.get("TriggerVijay") != null) {
                vijayTrigger = tileLayers.get("TriggerVijay").get(0);
            }
            if (tileLayers.get("Vijay Door Exit") != null) {
                vijayDoor = tileLayers.get("Vijay Door Exit").get(0);
            }
        }

        // 3W Hallway Doors
        if (levelPath.equals("map/hallway_3W.tmx")) {
            if (tileLayers.get("Vivaan Door Enter") != null) {
                vivaanDoorEnter = tileLayers.get("Vivaan Door Enter").get(0);
            }
            if (tileLayers.get("Aniketh Door Enter") != null) {
                anikethDoorEnter = tileLayers.get("Aniketh Door Enter").get(0);
            }
            if (tileLayers.get("Exit Door 3W") != null) {
                doorExit3W = tileLayers.get("Exit Door 3W").get(0);
            }
        }

        // Vivaan's room
        if (levelPath.equals("map/vivaan_room_new_3W.tmx")) {
           if (tileLayers.get("Vivaan Trigger") != null) {
                vivaanTrigger = tileLayers.get("Vivaan Trigger").get(0);
            }
            if (tileLayers.get("Vivaan Door Exit") != null) {
                vivaanDoorExit = tileLayers.get("Vivaan Door Exit").get(0);
            }
        }

        // Aniketh's room
        if (levelPath.equals("map/aniketh_room_3W.tmx")) {
            if (tileLayers.get("Aniketh Trigger") != null) {
                anikethTrigger = tileLayers.get("Aniketh Trigger").get(0);
            }
            if (tileLayers.get("Aniketh Door Exit") != null) {
                anikethDoorExit = tileLayers.get("Aniketh Door Exit").get(0);
            }
        }

        // 2W Hallway Doors
        if (levelPath.equals("map/hallway_2W.tmx")) {
            if (tileLayers.get("Raghav Door Enter") != null) {
                raghavDoorEnter = tileLayers.get("Raghav Door Enter").get(0);
            }
            if (tileLayers.get("Ekansh Door Enter") != null) {
                ekanshDoorEnter = tileLayers.get("Ekansh Door Enter").get(0);
            }
            if (tileLayers.get("Exit Door 2W") != null) {
                doorExit2W = tileLayers.get("Exit Door 2W").get(0);
            }
        }

        // Raghav's room
        if (levelPath.equals("map/raghav_room_2W.tmx")) {
           if (tileLayers.get("Raghav Trigger") != null) {
                raghavTrigger = tileLayers.get("Raghav Trigger").get(0);
            }
            if (tileLayers.get("Raghav Door Exit") != null) {
                raghavDoorExit = tileLayers.get("Raghav Door Exit").get(0);
            }
        }

        // Ekansh's room
        if (levelPath.equals("map/ekansh_room_2W.tmx")) {
            if (tileLayers.get("Ekansh Trigger") != null) {
                ekanshTrigger = tileLayers.get("Ekansh Trigger").get(0);
            }
            if (tileLayers.get("Ekansh Door Exit") != null) {
                ekanshDoorExit = tileLayers.get("Ekansh Door Exit").get(0);
            }
        }

        // 1W Hallway Doors
        if (levelPath.equals("map/hallway_1W.tmx")) {
            if (tileLayers.get("Abhinav Door Enter") != null) {
                abhinavDoorEnter = tileLayers.get("Abhinav Door Enter").get(0);
            }
            if (tileLayers.get("Snehal Door Enter") != null) {
                snehalDoorEnter = tileLayers.get("Snehal Door Enter").get(0);
            }
            if (tileLayers.get("Winning Door") != null) {
                winningDoor = tileLayers.get("Winning Door").get(0);
            }
            if (tileLayers.get("Losing Door") != null) {
                losingDoor = tileLayers.get("Losing Door").get(0);
            }
        }

        // Abhinav's room
        if (levelPath.equals("map/abhinav_room_1W.tmx")) {
           if (tileLayers.get("Abhinav Trigger") != null) {
                abhinavTrigger = tileLayers.get("Abhinav Trigger").get(0);
            }
            if (tileLayers.get("Abhinav Door Exit") != null) {
                abhinavDoorExit = tileLayers.get("Abhinav Door Exit").get(0);
            }
        }

        // Snehal's room
        if (levelPath.equals("map/snehal_room_1W.tmx")) {
           if (tileLayers.get("Snehal Trigger") != null) {
                snehalTrigger = tileLayers.get("Snehal Trigger").get(0);
            }
            if (tileLayers.get("Snehal Door Exit") != null) {
                snehalDoorExit = tileLayers.get("Snehal Door Exit").get(0);
            }
        }

          // final game room
        if (levelPath.equals("map/win_screen.tmx")) {
            if (tileLayers.get("Door Exit Game") != null) {
                doorExitGame = tileLayers.get("Door Exit Game").get(0);
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
          loadTileMap("map/trevor_room_4W.tmx", 5.0);
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
          loadTileMap("map/vijay_room_4W.tmx",5.0);
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300); 
          player.getPlayer().setLayoutY(300);
          App.setHallTag("Vijay's Room");
        }

        // For Exit Door 4W and code
        boolean doorExit4WCollision = false;
        if (doorExit4W != null && futureBounds.intersects(doorExit4W.getCollisionRectangle().getBoundsInParent())) {
          doorExit4WCollision = true;
        }

        
        if (doorExit4WCollision && !cooldown4WExit) {
          cooldown4WExit = true;
          Platform.runLater(() -> {
            String code = CodePopupTypeShit.showPopup("AX");
            if (code!= null && code.equalsIgnoreCase("AX")) {
                App.showAlert4W("Correct Code", "You have entered the correct code!");
                loadTileMap("map/hallway_3W.tmx", 5.0);
                getChildren().add(player.getPlayer());
                player.getPlayer().setLayoutX(400);
                player.getPlayer().setLayoutY(400);
                App.setHallTag("3RD WEST");

                keys.clear();
                setOnKeyPressed(e -> keys.add(e.getCode()));
                setOnKeyReleased(e -> keys.remove(e.getCode()));
            } else {
                App.showAlert4W("Wrong Code", "The code you entered is incorrect. Returning to 4TH WEST hallway.");
                App.goToHallway4W();
            }
          });
        }

        if (!doorExit4WCollision) {
            cooldown4WExit = false;
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
          App.goToHallway4W();
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
          App.goToHallway4W();
          App.setHallTag("4TH WEST");
        }

        // 3W HALLWAY
        // For Vivaan Door Enter
        boolean vivaanDoorEnterCollision = false;
        if (vivaanDoorEnter != null && futureBounds.intersects(vivaanDoorEnter.getCollisionRectangle().getBoundsInParent())) {
          vivaanDoorEnterCollision = true;
        }

        if (vivaanDoorEnterCollision) {
          loadTileMap("map/vivaan_room_new_3W.tmx", 5.0);
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300);
          player.getPlayer().setLayoutY(300);
          App.setHallTag("Vivaan's Room");
        }

        // For Aniketh Door Enter
        boolean anikethDoorEnterCollision = false;
        if (anikethDoorEnter != null && futureBounds.intersects(anikethDoorEnter.getCollisionRectangle().getBoundsInParent())) {
          anikethDoorEnterCollision = true;
        }

        if (anikethDoorEnterCollision) {
          loadTileMap("map/aniketh_room_3W.tmx", 5.0);
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300);
          player.getPlayer().setLayoutY(300);
          App.setHallTag("Jacob's Room");
        }

        // For Exit Door 3W
        boolean doorExit3WCollision = false;
        if (doorExit3W != null && futureBounds.intersects(doorExit3W.getCollisionRectangle().getBoundsInParent())) {
          doorExit3WCollision = true;
        }

        if (doorExit3WCollision && !cooldown3WExit) {
          cooldown3WExit = true;
          Platform.runLater(() -> {
            String code = CodePopupTypeShit.showPopup("GY");
            if (code!= null && code.equalsIgnoreCase("GY")) {
                App.showAlert4W("Correct Code", "You have entered the correct code!");
                loadTileMap("map/hallway_2W.tmx", 3.5);
                getChildren().add(player.getPlayer());
                player.getPlayer().setLayoutX(400);
                player.getPlayer().setLayoutY(400);
                App.setHallTag("2ND WEST");

                keys.clear();
                setOnKeyPressed(e -> keys.add(e.getCode()));
                setOnKeyReleased(e -> keys.remove(e.getCode()));
            } else {
                App.showAlert4W("Wrong Code", "The code you entered is incorrect. Returning to 3rd WEST hallway.");
                App.goToHallway3W();
            }
          });
        }

        if (!doorExit3WCollision) {
            cooldown3WExit = false;
        }

        // VIVAAN ROOM
        // For VivaanTrigger
        boolean vivaanTriggerCollision = false;
        if (vivaanTrigger != null && futureBounds.intersects(vivaanTrigger.getCollisionRectangle().getBoundsInParent())) {
          vivaanTriggerCollision = true;
        }

        if (vivaanTriggerCollision) {
          App.changeScreen(new MessyMusicMashGame());
        }

        // Vivaan Door Exit
        boolean vivaanDoorExitCollision = false;
        if (vivaanDoorExit != null && futureBounds.intersects(vivaanDoorExit.getCollisionRectangle().getBoundsInParent())) {
          vivaanDoorExitCollision = true;
        }

        if (vivaanDoorExitCollision) {
          App.goToHallway3W();
          App.setHallTag("3RD WEST");
        }

        // ANIKETH ROOM
        // For AnikethTrigger
        boolean anikethTriggerCollision = false;
        if (anikethTrigger != null && futureBounds.intersects(anikethTrigger.getCollisionRectangle().getBoundsInParent())) {
          anikethTriggerCollision = true;
        }
        if (anikethTriggerCollision) {
          App.changeScreen(new JobHuntGame());
        }

        // Aniketh Door Exit
        boolean anikethDoorExitCollision = false;
        if (anikethDoorExit != null && futureBounds.intersects(anikethDoorExit.getCollisionRectangle().getBoundsInParent())) {
          anikethDoorExitCollision = true;
        }

        if (anikethDoorExitCollision) {
          App.goToHallway3W();
          App.setHallTag("3RD WEST");
        }

        //2W DOORS
        // For Raghav Door Enter
        boolean raghavDoorEnterCollision = false;
        if (raghavDoorEnter != null && futureBounds.intersects(raghavDoorEnter.getCollisionRectangle().getBoundsInParent())) {
          raghavDoorEnterCollision = true;
        }

        boolean cooldown = false;
        if (raghavDoorEnterCollision) {
          loadTileMap("map/raghav_room_2W.tmx", 5.0);
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300);
          player.getPlayer().setLayoutY(450);
          App.setHallTag("Raghav's Room");
          cooldown = true;
        }

        if (cooldown) {
          cooldown = false;
          return; 
        }

        // For Ekansh Door Enter
        boolean ekanshDoorEnterCollision = false;
        if (ekanshDoorEnter != null && futureBounds.intersects(ekanshDoorEnter.getCollisionRectangle().getBoundsInParent())) {
          ekanshDoorEnterCollision = true;
        }

        if (ekanshDoorEnterCollision) {
          loadTileMap("map/ekansh_room_2W.tmx", 5.0);
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300);
          player.getPlayer().setLayoutY(300);
          App.setHallTag("Ekansh's Room");
        }

        // For Exit Door 2W
        boolean doorExit2WCollision = false;
        if (doorExit2W != null && futureBounds.intersects(doorExit2W.getCollisionRectangle().getBoundsInParent())) {
          doorExit2WCollision = true;
        } 
        if (doorExit2WCollision && !cooldown2WExit) {
          cooldown2WExit = true;
          Platform.runLater(() -> {
            String code = CodePopupTypeShit.showPopup("FY");
            if (code!= null && code.equalsIgnoreCase("FY")) {
                App.showAlert4W("Correct Code", "You have entered the correct code!");
                loadTileMap("map/hallway_1W.tmx", 3.5);
                getChildren().add(player.getPlayer());
                player.getPlayer().setLayoutX(300);
                player.getPlayer().setLayoutY(300);
                App.setHallTag("1ST WEST");

                keys.clear();
                setOnKeyPressed(e -> keys.add(e.getCode()));
                setOnKeyReleased(e -> keys.remove(e.getCode()));
            } else {
                App.showAlert4W("Wrong Code", "The code you entered is incorrect. Returning to 2ND WEST hallway.");
                App.goToHallway2W();
            }
          });
        }

        if (!doorExit2WCollision) {
            cooldown2WExit = false;
        }


        //RAGHAV ROOM
        // For Raghav trigger
        boolean raghavTriggerCollision = false;
        if (raghavTrigger != null && futureBounds.intersects(raghavTrigger.getCollisionRectangle().getBoundsInParent())) {
          raghavTriggerCollision = true;
        }
        if (raghavTriggerCollision) {
          App.changeScreen(new TicTacToeGame());
        }

        
        //For Raghav Door Exit
        boolean raghavDoorExitCollision = false;
        if (raghavDoorExit != null && futureBounds.intersects(raghavDoorExit.getCollisionRectangle().getBoundsInParent())) {
          raghavDoorExitCollision = true;
        }
        if (raghavDoorExitCollision) {
          App.goToHallway2W();
          App.setHallTag("2ND WEST");
        }


        // EKANSH ROOM
        // For Ekansh trigger
        boolean ekanshTriggerCollision = false;
        if (ekanshTrigger != null && futureBounds.intersects(ekanshTrigger.getCollisionRectangle().getBoundsInParent())) {
          ekanshTriggerCollision = true;
        }
        if (ekanshTriggerCollision) {
          App.changeScreen(new NumberGuessingGame());
          System.out.println("WIP");
        }

        // For Ekansh Door Exit
        boolean ekanshDoorExitCollision = false;
        if (ekanshDoorExit != null && futureBounds.intersects(ekanshDoorExit.getCollisionRectangle().getBoundsInParent())) {
          ekanshDoorExitCollision = true;
        }
        if (ekanshDoorExitCollision) {
          App.goToHallway2W();
          App.setHallTag("2ND WEST");
        }

        // 1W HALLWAY
        // For Abhinav Door Enter
        boolean abhinavDoorEnterCollision = false;
        if (abhinavDoorEnter != null && futureBounds.intersects(abhinavDoorEnter.getCollisionRectangle().getBoundsInParent())) {
          abhinavDoorEnterCollision = true;
        }

        if (abhinavDoorEnterCollision) {
          loadTileMap("map/abhinav_room_1W.tmx", 5.0);
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(450);
          player.getPlayer().setLayoutY(450);
          App.setHallTag("Abhinav and Tanush's Room");
        }

        // For Snehal Door Enter
        boolean snehalDoorEnterCollision = false;
        if (snehalDoorEnter != null && futureBounds.intersects(snehalDoorEnter.getCollisionRectangle().getBoundsInParent())) {
          snehalDoorEnterCollision = true;
        }

        if (snehalDoorEnterCollision) {
          loadTileMap("map/snehal_room_1W.tmx", 5.0);
          getChildren().add(player.getPlayer());
          player.getPlayer().setLayoutX(300);
          player.getPlayer().setLayoutY(300);
          App.setHallTag("Snehal's Room");
        }

        // For winning door
        boolean doorWin = false;
        if (winningDoor != null && futureBounds.intersects(winningDoor.getCollisionRectangle().getBoundsInParent())) {
          doorWin = true;
        }

        if (doorWin && !cooldownWinExit) {
          cooldownWinExit = true;
          Platform.runLater(() -> {
            String code = CodePopupTypeShit.showPopup("RR");
            if (code!= null && code.equalsIgnoreCase("RR")) {
                App.showAlert4W("Correct Code", "You have entered the correct code, and you chose the right door! YOU HAVE ESCAPED HUNT WEST!.");
                loadTileMap("map/win_screen.tmx", 5.0);
                getChildren().add(player.getPlayer());
                player.getPlayer().setLayoutX(400);
                player.getPlayer().setLayoutY(400);
                App.setHallTag("OUTSIDE WORLD");

                keys.clear();
                setOnKeyPressed(e -> keys.add(e.getCode()));
                setOnKeyReleased(e -> keys.remove(e.getCode()));
            } else {
                App.showAlert4W("Wrong Code", "The code you entered is incorrect. Returning to 1ST WEST hallway.");
                App.goToHallway1W();
            }
          });
        }

        if (!doorWin) {
            cooldownWinExit = false;
        }

        // For losing door
        boolean doorLose = false;
        if (losingDoor != null && futureBounds.intersects(losingDoor.getCollisionRectangle().getBoundsInParent())) {
          doorLose = true;
        }
        if (doorLose && !cooldownLoseExit) {
          cooldownLoseExit = true;
          Platform.runLater(() -> {
            String code = CodePopupTypeShit.showPopup("RR");
            if (code!= null && code.equalsIgnoreCase("RR")) {
                App.showAlert4W("Correct Code", "You have entered the correct code, but you chose the wrong door. Returning to 4TH WEST hallway.");
                loadTileMap("map/hallway_4W.tmx", 3.5);
                getChildren().add(player.getPlayer());
                player.getPlayer().setLayoutX(400);
                player.getPlayer().setLayoutY(400);
                App.setHallTag("4TH WEST");

                keys.clear();
                setOnKeyPressed(e -> keys.add(e.getCode()));
                setOnKeyReleased(e -> keys.remove(e.getCode()));
            } else {
                App.showAlert4W("Wrong Code", "The code you entered is incorrect. Returning to 1ST WEST hallway.");
                App.goToHallway1W();
            }
          });
        }

        if (!doorLose) {
            cooldownLoseExit = false;
        }


        // ABHINAV ROOM
        // For Abhinav trigger
        boolean abhinavTriggerCollision = false;
        if (abhinavTrigger != null && futureBounds.intersects(abhinavTrigger.getCollisionRectangle().getBoundsInParent())) {
          abhinavTriggerCollision = true;
        }
        if (abhinavTriggerCollision) {
          App.changeScreen(new RobotGame());
          System.out.println("WIP");
        }

        // For Abhinav Door Exit
        boolean abhinavDoorExitCollision = false;
        if (abhinavDoorExit != null && futureBounds.intersects(abhinavDoorExit.getCollisionRectangle().getBoundsInParent())) {
          abhinavDoorExitCollision = true;
        }
        if (abhinavDoorExitCollision) {
          App.goToHallway1W();
          App.setHallTag("1ST WEST");
        }

        // SNEHAL ROOM
        // For Snehal trigger
        boolean snehalTriggerCollision = false;
        if (snehalTrigger != null && futureBounds.intersects(snehalTrigger.getCollisionRectangle().getBoundsInParent())) {
          snehalTriggerCollision = true;
        }
        if (snehalTriggerCollision) {
          App.changeScreen(new SurveyGame());
          System.out.println("WIP");
        }

        // For Snehal Door Exit
        boolean snehalDoorExitCollision = false;
        if (snehalDoorExit != null && futureBounds.intersects(snehalDoorExit.getCollisionRectangle().getBoundsInParent())) {
          snehalDoorExitCollision = true;
        }
        if (snehalDoorExitCollision) {
          App.goToHallway1W();
          App.setHallTag("1ST WEST");
        }

        //OUTSIDE WORLD
        // For Door Exit Game
        boolean doorExitGameCollision = false;
        if (doorExitGame != null && futureBounds.intersects(doorExitGame.getCollisionRectangle().getBoundsInParent())) {
          doorExitGameCollision = true;
        }
        if (doorExitGameCollision) {
          System.exit(0);
        }



      
        
    }
}