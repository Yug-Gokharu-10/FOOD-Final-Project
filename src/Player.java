import javafx.scene.shape.*;
import javafx.geometry.Bounds;
import javafx.scene.paint.*;

public class Player {

    private Rectangle player;
    private double width;
    private double length;
    private Color color;
   

    //movement constants
    static final double MOVEMENT_Y = 32;
    static final double MOVEMENT_X = 32;
    
    public Player(double width, double length, Color color) {
        this.width = width;
        this.length = length;
        this.color = color;
        player = new Rectangle(width, length, color);
        player.setLayoutX(0);
        player.setLayoutY(0);
    }

    public Rectangle getPlayer() {
        return player;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    public Color getColor() {
        return color;
    }

    public void moveLeft() {
        player.setLayoutX(player.getLayoutX() - 2);
    }

    public void moveRight() {
        player.setLayoutX(player.getLayoutX() + 2);
    }

    public void moveDown() {
        player.setLayoutY(player.getLayoutY() + 2);
    }

    public void moveUp() {
        player.setLayoutY(player.getLayoutY() - 2);
    }

      /**
     * Returns the player's bounds from the previous frame for collision detection.
     */

    public Bounds getBoundsAfterMove(double dx, double dy) {
        Rectangle nextPos = new Rectangle(width, length);
        nextPos.setLayoutX(player.getLayoutX() + dx);
        nextPos.setLayoutY(player.getLayoutY() + dy);
        return nextPos.getBoundsInParent();
    }

    // public Bounds getPreviousBounds() {
    //     if (previousBounds == null)
    //     return player.getBoundsInParent();
    //     return previousBounds;
    // }
}

