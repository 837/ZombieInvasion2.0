package ch.redmonkeyass.zombieInvasion.camera;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class Camera {
  private final int viewport_size_X;
  private final int viewport_size_Y;
  /**
   * update before use
   */
  Rectangle screenRect;
  private Vector2 position;
  private float offsetMaxX = 0;
  private float offsetMaxY = 0;
  private float offsetMinX;
  private float offsetMinY;

  /**
   * After creating a camera object, you need to call <b> setMapData(mapWidth, mapHeight);</b>
   * 
   * @param viewport_size_X
   * @param viewport_size_Y
   */
  public Camera(int viewport_size_X, int viewport_size_Y) {
    this.viewport_size_X = viewport_size_X;
    this.viewport_size_Y = viewport_size_Y;

    offsetMinX = viewport_size_X / 2;
    offsetMinY = viewport_size_Y / 2;
    position = Vector2.Zero.add(viewport_size_X / 2, viewport_size_Y / 2);
    screenRect = new Rectangle(getPosition().x, getPosition().y, viewport_size_X, viewport_size_Y);
  }

  /**
   * @return top left corner of the camera window
   */
  public Vector2 getPosition() {
    return new Vector2(position.x - viewport_size_X / 2, position.y - viewport_size_Y / 2);
  }

  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEventDispatcher().getEvents().parallelStream().filter(
        event -> event.getReceiverID().equals("CAMERA") || event.getReceiverID().equals("MOUSE"))
        .forEach(e -> {
          switch (e.getEvent()) {
            case RIGHT_DRAGGED:
              e.getAdditionalInfo(Vector2[].class).ifPresent(positions -> {
                Vector2 oldPos = positions[0].scl(Config.MOUSE_DRAG_SMOOTHNESS);
                Vector2 newPos = positions[1].scl(Config.MOUSE_DRAG_SMOOTHNESS);
                Vector2 direction = newPos.sub(oldPos);
                direction.x = (float) Math.floor(direction.x);
                direction.y = (float) Math.floor(direction.y);

                move(direction);
              });
              break;
            case LEFT_ARROW_PRESSED:
              move(new Vector2(-25, 0));
              break;
            case RIGHT_ARROW_PRESSED:
              move(new Vector2(25, 0));
              break;
            case UP_ARROW_PRESSED:
              move(new Vector2(0, -25));
              break;
            case DOWN_ARROW_PRESSED:
              move(new Vector2(0, 25));
              break;
            case LEFT_ARROW_RELEASED:
              WorldHandler.getEventDispatcher()
                  .removePersistentEvent(
                      WorldHandler.getEventDispatcher().getEvents().parallelStream()
                          .filter(event -> (event.getReceiverID().equals("CAMERA")
                              && event.getEvent() == EventType.LEFT_ARROW_PRESSED))
                          .findAny().get());
              WorldHandler.getEventDispatcher().removePersistentEvent(e);
              break;
            case RIGHT_ARROW_RELEASED:
              WorldHandler.getEventDispatcher()
                  .removePersistentEvent(
                      WorldHandler.getEventDispatcher().getEvents().parallelStream()
                          .filter(event -> (event.getReceiverID().equals("CAMERA")
                              && event.getEvent() == EventType.RIGHT_ARROW_PRESSED))
                          .findAny().get());
              WorldHandler.getEventDispatcher().removePersistentEvent(e);
              break;
            case UP_ARROW_RELEASED:
              WorldHandler.getEventDispatcher()
                  .removePersistentEvent(WorldHandler.getEventDispatcher().getEvents()
                      .parallelStream().filter(event -> (event.getReceiverID().equals("CAMERA")
                          && event.getEvent() == EventType.UP_ARROW_PRESSED))
                      .findAny().get());
              WorldHandler.getEventDispatcher().removePersistentEvent(e);
              break;
            case DOWN_ARROW_RELEASED:
              WorldHandler.getEventDispatcher()
                  .removePersistentEvent(
                      WorldHandler.getEventDispatcher().getEvents().parallelStream()
                          .filter(event -> (event.getReceiverID().equals("CAMERA")
                              && event.getEvent() == EventType.DOWN_ARROW_PRESSED))
                          .findAny().get());
              WorldHandler.getEventDispatcher().removePersistentEvent(e);
              break;
          }
        });
  }

  public void move(Vector2 direction) {
    setPositionAndKeepWithinMapBoundaries(position.add(direction));
  }

  private void setPositionAndKeepWithinMapBoundaries(Vector2 point) {
    position = new Vector2(keepWithinBoundaries(offsetMinX, offsetMaxX, point.x),
        keepWithinBoundaries(offsetMinY, offsetMaxY, point.y));
  }

  private float keepWithinBoundaries(float lowerBound, float upperBound, float number) {
    if (number < lowerBound) {
      return lowerBound;
    }
    if (number > upperBound) {
      return upperBound;
    }
    return number;
  }

  public void setMapData(float f, float g) {
    offsetMaxX = f * Config.B2PIX - viewport_size_X / 2;
    offsetMaxY = g * Config.B2PIX - viewport_size_Y / 2;
    move(WorldHandler.getWorldMap().getWorldMapLoader().getStartRoomPos().cpy().scl(Config.B2PIX)
        .sub(viewport_size_X / 2, viewport_size_Y / 2));
  }

  /**
   * pix to pix
   * 
   * @param pos to be converted to screen coordinates
   * @return a new vector towards position on screen in pix
   */
  public Vector2 getPositionOnScreen(Vector2 pos) {
    return pos.cpy().sub(position);
  }

  /**
   * box to pix
   * 
   * @param pos to be converted to screen coordinates
   * @return a new vector towards position on screen in pix
   */
  public Vector2 bgetPositionOnScreen(Vector2 pos) {
    return pos.cpy().scl(Config.B2PIX).sub(position);
  }

  /**
   *
   * @param pos in box coordinates
   * @return true if pos is on the screen window
   */
  public boolean bIsOnCamera(Vector2 pos) {
    updateScreenRectangle();
    return isOnCamera(pos.cpy().scl(Config.B2PIX));
  }

  private void updateScreenRectangle() {
    screenRect.setPosition(getPosition());
  }

  public boolean isOnCamera(Vector2 pos) {
    updateScreenRectangle();
    return screenRect.contains(pos.cpy());
  }

  /**
   * @param r in screen coordinats/pixels
   * @return true if the rectangle overlaps with the screen
   */
  public boolean overlapsWithCamera(Rectangle r) {
    return r.overlaps(screenRect);
  }

  public int getViewport_size_X() {
    return viewport_size_X;
  }

  public int getViewport_size_Y() {
    return viewport_size_Y;
  }
}
