package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.TiledMap;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.Door.DOORDIR;

public class MapPartDescription {

  /**
   * width in tiles
   */
  private int width = 0;
  /**
   * height in tiles
   */
  private int height = 0;
  private final TiledMap roomMap;

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  private final String name;

  public String getName() {
    return name;
  }

  public TiledMap getRoomMap() {
    return roomMap;
  }

  private ArrayList<Door> doors = new ArrayList<>();

  public ArrayList<Door> getDoorDescriptions() {
    return new ArrayList<>(doors);
  }

  public MapPartDescription(TiledMap map, String name) {
    this.roomMap = map;
    this.name = name;
    width = roomMap.getWidth();
    height = roomMap.getHeight();
    createDoors(doors);
  }

  public void createDoors(ArrayList<Door> doors) {
    // NORTH
    for (int x = 0; x < width; x++) {
      Vector2 startPos = new Vector2(x, 0);
      while (roomMap.getTileId(x, 0, MapPart.WALL_LAYER) == 0) {
        x++;
        if (x == width)
          break;
      }
      Vector2 endPos = new Vector2(x - 1, 0);
      if (startPos.x < endPos.x) {
        doors.add(new Door(startPos, endPos, DOORDIR.North));
      }
    }
    // EAST
    for (int y = 0; y < height; y++) {
      Vector2 startPos = new Vector2(width - 1, y);
      while (roomMap.getTileId(width - 1, y, MapPart.WALL_LAYER) == 0) {
        y++;
        if (y == height)
          break;
      }
      Vector2 endPos = new Vector2(width - 1, y - 1);
      if (startPos.y < endPos.y) {
        doors.add(new Door(startPos, endPos, DOORDIR.East));
      }
    }
    // SOUTH
    for (int x = 0; x < width; x++) {
      Vector2 startPos = new Vector2(x, height - 1);
      while (roomMap.getTileId(x, height - 1, MapPart.WALL_LAYER) == 0) {
        x++;
        if (x == width)
          break;
      }
      Vector2 endPos = new Vector2(x - 1, height - 1);
      if (startPos.x < endPos.x) {
        doors.add(new Door(startPos, endPos, DOORDIR.South));
      }
    }
    // WEST
    for (int y = 0; y < height; y++) {
      Vector2 startPos = new Vector2(0, y);
      while (roomMap.getTileId(0, y, MapPart.WALL_LAYER) == 0) {
        y++;
        if (y == height)
          break;
      }
      Vector2 endPos = new Vector2(0, y - 1);
      if (startPos.y < endPos.y) {
        doors.add(new Door(startPos, endPos, DOORDIR.West));
      }
    }
  }
}


