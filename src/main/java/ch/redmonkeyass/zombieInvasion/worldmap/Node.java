package ch.redmonkeyass.zombieInvasion.worldmap;

import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.worldmap.WorldMap.FieldType;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.GridCell;

public class Node extends GridCell {
  private final Body b2dBody;
  private final FieldType type;
  private final int tileSize;

  public Node(int x, int y, Body b2dBody, FieldType type, int tileSize) {
    super(x, y, type.isWalkable());
    this.b2dBody = b2dBody;
    this.type = type;
    this.tileSize = tileSize;
  }

  public Body getBody() {
    return b2dBody;
  }

  public FieldType getType() {
    return type;
  }

  public int getTileSize() {
    return tileSize;
  }

  public boolean isSame(Node other) {
    return (other.x == this.x && other.y == this.y);
  }
}
