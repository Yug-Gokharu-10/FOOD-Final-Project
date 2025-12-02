import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile {
  private ImageView tileImage;
  private Rectangle collisionRectangle;

  public Tile(ImageView tileImage) {
    this.tileImage = tileImage;
    collisionRectangle = new Rectangle(
              tileImage.getLayoutX(),
              tileImage.getLayoutY(),
              tileImage.getFitWidth(),
              tileImage.getFitHeight());
    collisionRectangle.setFill(Color.TRANSPARENT); // collision only
  }

  public ImageView getTileImage(){
    return tileImage;
  }

  public Rectangle getCollisionRectangle(){
    return collisionRectangle;
  }
}