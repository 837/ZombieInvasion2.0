package ch.redmonkeyass.zombieInvasion.worldmap;

import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.worldmap.WorldMap.FieldType;

public class Node {
  private final Body body;
  private final FieldType type;

  private final float width, height;
  private final float x, y;

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
}
