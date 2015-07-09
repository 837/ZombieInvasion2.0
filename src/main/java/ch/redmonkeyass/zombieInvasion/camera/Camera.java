package ch.redmonkeyass.zombieInvasion.camera;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;

public class Camera {
  private Vector2 position = new Vector2();

  private final int viewport_size_X;
  private final int viewport_size_Y;

  private float offsetMaxX;
  private float offsetMaxY;
  private float offsetMinX;
  private float offsetMinY;

  public Camera(int viewport_size_X, int viewport_size_Y) {
    this.viewport_size_X = viewport_size_X;
    this.viewport_size_Y = viewport_size_Y;

    offsetMinX = viewport_size_X / 2;
    offsetMinY = viewport_size_Y / 2;
  }

  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEventDispatcher().getEvents().parallelStream().filter(
        event -> event.getReceiverID().equals("GLOBAL") || event.getReceiverID().equals("MOUSE"))
        .forEach(e -> {
          switch (e.getEvent()) {
            case RIGHT_DRAGGED:
              e.getAdditionalInfo(Vector2[].class).ifPresent(positions -> {
                Vector2 oldPos = positions[0].scl(Config.MOUSE_DRAG_SMOOTHNESS);
                Vector2 newPos = positions[1].scl(Config.MOUSE_DRAG_SMOOTHNESS);
                Vector2 movement = newPos.sub(oldPos);
                movement.x = (int) movement.x;
                movement.y = (int) movement.y;
                move(movement);
              });
              break;
          }
        });
  }

  private void setPosition(Vector2 point) {
    position = new Vector2(checkOffset(offsetMinX, offsetMaxX, point.x),
        checkOffset(offsetMinY, offsetMaxY, point.y));
    System.out.println("Camera moved " + position.toString());
  }

  public void move(Vector2 direction) {
    setPosition(position.add(direction));
  }

  public void setMapData(int mapWidth, int mapHeight) {
    offsetMaxX = mapWidth - viewport_size_X / 2;
    offsetMaxY = mapHeight - viewport_size_Y / 2;
    /**
     * muss noch entfernt werden, workaround
     */
    move(new Vector2(0, -1));
  }

  private float checkOffset(float lowerBound, float upperBound, float number) {
    if (number < lowerBound) {
      return lowerBound;
    }
    if (number > upperBound) {
      return upperBound;
    }
    return number;
  }

  public Vector2 getPosition() {
    return new Vector2(position.x - viewport_size_X / 2, position.y - viewport_size_Y / 2);
  }

  public Vector2 getPositionInWorld(Vector2 screenPos) {
    return screenPos.add(getPosition());
  }

  public Vector2 getPositionOnScreen(Vector2 screenPos) {
    return screenPos.sub(getPosition());
  }

  public int getViewport_size_X() {
    return viewport_size_X;
  }

  public int getViewport_size_Y() {
    return viewport_size_Y;
  }
}
