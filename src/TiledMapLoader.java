import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Loads a Tiled TMX map (with external TSX tilesets) and returns
 * a map from layer name to a list of ImageView tiles.
 *
 * Supports tile layers with CSV encoding and spacing / margin.
 * Only tile layers are handled.
 */
public final class TiledMapLoader {

  private static final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
  private static final int FLIPPED_VERTICALLY_FLAG = 0x40000000;
  private static final int FLIPPED_DIAGONALLY_FLAG = 0x20000000;

  private static class Tileset {
    final int firstGid;
    final Image image;
    final int tileWidth;
    final int tileHeight;
    final int sheetW;
    final int sheetH;
    final int spacing;
    final int margin;

    Tileset(int firstGid,
        Image image,
        int tileWidth,
        int tileHeight,
        int sheetW,
        int sheetH,
        int spacing,
        int margin) {
      this.firstGid = firstGid;
      this.image = image;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.sheetW = sheetW;
      this.sheetH = sheetH;
      this.spacing = spacing;
      this.margin = margin;
    }
  }

  private TiledMapLoader() {
    // utility class
  }

  /**
   * Loads a TMX file and returns a map from layer name to a list of ImageView
   * tiles.
   *
   * Tiles are positioned and scaled so you can add them directly to a Pane.
   *
   * @param tmxPath path to the TMX file relative to the working directory
   * @param scale   global scale factor for the map tiles
   */
  public static Map<String, List<Tile>> loadTileMap(String tmxPath, double scale) throws Exception {
    File tmxFile = new File(tmxPath);
    if (!tmxFile.exists()) {
      throw new IllegalArgumentException("TMX file not found: " + tmxFile.getAbsolutePath());
    }

    String mapDir = tmxFile.getParent();
    if (mapDir == null) {
      mapDir = ".";
    }

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(tmxFile);
    Element root = doc.getDocumentElement();

    int mapTileWidth = Integer.parseInt(root.getAttribute("tilewidth"));
    int mapTileHeight = Integer.parseInt(root.getAttribute("tileheight"));

    // Load tilesets from TSX
    Map<Integer, Tileset> tilesetsByFirstGid = new LinkedHashMap<>();
    NodeList tilesetNodes = root.getElementsByTagName("tileset");
    for (int i = 0; i < tilesetNodes.getLength(); i++) {
      Element tsElem = (Element) tilesetNodes.item(i);
      int firstGid = Integer.parseInt(tsElem.getAttribute("firstgid"));
      String source = tsElem.getAttribute("source");

      File tsxFile = new File(mapDir, source);
      Document tsxDoc = db.parse(tsxFile);
      Element tsRoot = tsxDoc.getDocumentElement();

      int tileWidth = Integer.parseInt(tsRoot.getAttribute("tilewidth"));
      int tileHeight = Integer.parseInt(tsRoot.getAttribute("tileheight"));
      int spacing = getIntAttributeOrDefault(tsRoot, "spacing", 0);
      int margin = getIntAttributeOrDefault(tsRoot, "margin", 0);

      Element imageElem = (Element) tsRoot.getElementsByTagName("image").item(0);
      String imageSource = imageElem.getAttribute("source");
      File imageFile = new File(tsxFile.getParentFile(), imageSource);

      // Important for pixel art: smoothing off on the base sheet
      Image image = new Image(imageFile.toURI().toString(), 0, 0, false, false);

      int sheetW = (int) ((image.getWidth() - 2 * margin + spacing) / (tileWidth + spacing));
      int sheetH = (int) ((image.getHeight() - 2 * margin + spacing) / (tileHeight + spacing));

      Tileset tileset = new Tileset(firstGid, image, tileWidth, tileHeight,
          sheetW, sheetH, spacing, margin);
      tilesetsByFirstGid.put(firstGid, tileset);
    }

    List<Integer> sortedFirstGids = new ArrayList<>(tilesetsByFirstGid.keySet());
    Collections.sort(sortedFirstGids);

    Map<String, List<Tile>> layersMap = new LinkedHashMap<>();

    // Tile layers only
    NodeList layerNodes = root.getElementsByTagName("layer");
    for (int i = 0; i < layerNodes.getLength(); i++) {
      Element layerElem = (Element) layerNodes.item(i);
      String layerName = layerElem.getAttribute("name");
      int width = Integer.parseInt(layerElem.getAttribute("width"));
      int height = Integer.parseInt(layerElem.getAttribute("height"));

      Element dataElem = (Element) layerElem.getElementsByTagName("data").item(0);
      String encoding = dataElem.getAttribute("encoding");
      if (encoding != null && !encoding.isEmpty()
          && !"csv".equalsIgnoreCase(encoding)) {
        throw new IllegalStateException(
            "Only CSV encoded layer data is supported for now. Found: " + encoding);
      }

      String[] rowStrings = dataElem.getTextContent().trim().split("\\s*\\n\\s*");
      List<Tile> tilesForLayer = new ArrayList<>();

      for (int row = 0; row < height; row++) {
        String[] colStrings = rowStrings[row].trim().split(",");
        for (int col = 0; col < width; col++) {
          String token = colStrings[col].trim();
          if (token.isEmpty()) {
            continue;
          }

          long rawGid = Long.parseLong(token);
          if (rawGid == 0) {
            continue;
          }

          boolean flippedH = (rawGid & FLIPPED_HORIZONTALLY_FLAG) != 0;
          boolean flippedV = (rawGid & FLIPPED_VERTICALLY_FLAG) != 0;
          boolean flippedD = (rawGid & FLIPPED_DIAGONALLY_FLAG) != 0;

          int gid = (int) (rawGid & 0x0FFFFFFF);

          Tileset tileset = findTilesetForGid(tilesetsByFirstGid, sortedFirstGids, gid);
          if (tileset == null) {
            continue;
          }

          int localId = gid - tileset.firstGid;
          System.out.println(tileset.firstGid);

          int tsx = tileset.margin
              + (localId % tileset.sheetW) * (tileset.tileWidth + tileset.spacing);
          int tsy = tileset.margin
              + (localId / tileset.sheetW) * (tileset.tileHeight + tileset.spacing);

          WritableImage tileImage = new WritableImage(
              tileset.image.getPixelReader(),
              tsx,
              tsy,
              tileset.tileWidth,
              tileset.tileHeight);

          // Match Python logic
          // First scale tileset tile to map tile size
          double tileScaleX = (double) mapTileWidth / tileset.tileWidth;
          double tileScaleY = (double) mapTileHeight / tileset.tileHeight;

          double baseWidth = tileset.tileWidth * tileScaleX;
          double baseHeight = tileset.tileHeight * tileScaleY;

          // Then apply global scale like Actor.scale
          double finalWidth = baseWidth * scale;
          double finalHeight = baseHeight * scale;

          double x = mapTileWidth * col * scale;
          double y = mapTileHeight * row * scale;

          ImageView iv = new ImageView(tileImage);
          iv.setFitWidth(finalWidth);
          iv.setFitHeight(finalHeight);
          iv.setPreserveRatio(false);
          iv.setSmooth(false); // nearest neighbor for pixel art
          iv.setLayoutX(x);
          iv.setLayoutY(y);

          if (flippedH) {
            iv.setScaleX(-1);
            // iv.setLayoutX(x + finalWidth);
          }
          if (flippedV) {
            iv.setScaleY(-1);
            // iv.setLayoutY(y + finalHeight);
          }
          if (flippedD && !flippedH) {
            iv.setRotate(-90);
            iv.setScaleX(-1);
            // iv.setLayoutX(x + finalWidth);
          }
          if (flippedD && flippedH) {
            iv.setRotate(90);
            iv.setScaleX(-1);
            // iv.setLayoutX(x + finalWidth);
          }

          Tile tile = new Tile(iv);
          tilesForLayer.add(tile);
        }
      }

      layersMap.put(layerName, tilesForLayer);
    }

    return layersMap;
  }

  private static Tileset findTilesetForGid(Map<Integer, Tileset> tilesetsByFirstGid,
      List<Integer> sortedFirstGids,
      int gid) {
    Tileset result = null;
    System.out.println(gid);
    for (int first : sortedFirstGids) {
      if (first <= gid) {
        result = tilesetsByFirstGid.get(first);
      } else {
        break;
      }
    }
    return result;
  }

  private static int getIntAttributeOrDefault(Element elem, String attr, int defaultValue) {
    if (elem.hasAttribute(attr) && !elem.getAttribute(attr).isEmpty()) {
      return Integer.parseInt(elem.getAttribute(attr));
    }
    return defaultValue;
  }
}