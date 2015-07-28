package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import com.badlogic.gdx.math.Vector2;

public class Door {
  enum DOORSTATE {
    Closed, Open
  }
  enum DOORDIR {
    North, East, West, South;

    public static DOORDIR getOpposite(DOORDIR d) {
      switch (d) {
        case East:
          return West;
        case North:
          return South;
        case South:
          return North;
        case West:
          return East;
      }
      return d;
    }
  }

  /**
   * Door width in tiles
   */
  private int doorWidth;
  private DOORDIR doordir;

  public DOORDIR getDoordir() {
    return doordir;
  }

  private DOORSTATE doorstate = DOORSTATE.Open;


  public void setDoorstate(DOORSTATE doorstate) {
    this.doorstate = doorstate;
  }

  public DOORSTATE getDoorstate() {
    return doorstate;
  }

  private Vector2 startPosDoorTiles;
  private Vector2 endPosDoorTiles;

  private Room parent;

  public Room getParent() {
    return parent;
  }

  public void setParent(Room parent) {
    this.parent = parent;
  }

  public Door(Vector2 startPos, Vector2 endPos, DOORDIR doordir) {
    startPosDoorTiles = startPos.cpy();
    endPosDoorTiles = endPos.cpy();
    this.doordir = doordir;
    doorWidth = (int) startPosDoorTiles.dst(endPosDoorTiles);
  }

  public int getDoorWidth() {
    return doorWidth;
  }

  public Vector2 getEndPosDoorTiles() {
    return endPosDoorTiles;
  }

  public Vector2 getStartPosDoorTiles() {
    return startPosDoorTiles;
  }
}
