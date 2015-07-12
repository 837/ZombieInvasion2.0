package ch.redmonkeyass.zombieInvasion.worldmap;

import com.badlogic.gdx.physics.box2d.Body;
import com.sun.istack.internal.NotNull;

import ch.redmonkeyass.zombieInvasion.worldmap.WorldMap.FieldType;

public class Node implements Comparable {
  private final Body body;
  private final FieldType type;

  private final float width, height;
  private final float x, y;


  /** The path cost for this node */
  private float cost;
  /** The parent of this node, how we reached it in the search */
  private Node parent;
  /** The heuristic cost of this node */
  private float heuristic;
  /** The search depth of this node */
  private int depth;

  public Node(float width, float height, float x, float y, Body body, FieldType type) {
    this.body = body;
    this.type = type;
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }

  public Body getBody() {
    return body;
  }

  public FieldType getType() {
    return type;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public boolean isSame(Node other) {
    return (other.x == this.x && other.y == this.y);
  }

  /**
   * Set the parent of this node
   * 
   * @param parent The parent node which lead us to this node
   * @return The depth we have no reached in searching
   */
  public int setParent(Node parent) {
    depth = parent.depth + 1;
    this.parent = parent;

    return depth;
  }

  @Override
  public int compareTo(@NotNull Object other) {
    Node o = (Node) other;

    float f = heuristic + cost;
    float of = o.heuristic + o.cost;

    if (f < of) {
      return -1;
    } else if (f > of) {
      return 1;
    } else {
      return 0;
    }
  }
}
