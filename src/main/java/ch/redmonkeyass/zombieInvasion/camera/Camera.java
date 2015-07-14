package ch.redmonkeyass.zombieInvasion.camera;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;

public class Camera {
  private final int viewport_size_X;
  private final int viewport_size_Y;

  private Vector2 position;
  /**
   * update before use
   */
  Rectangle screenRect;
  private float offsetMaxX;
  private float offsetMaxY;
  private float offsetMinX;
  private float offsetMinY;

  public Camera(int viewport_size_X, int viewport_size_Y, int mapWidth, int mapHeight) {
    this.viewport_size_X = viewport_size_X;
    this.viewport_size_Y = viewport_size_Y;

    offsetMinX = viewport_size_X / 2;
    offsetMinY = viewport_size_Y / 2;
    setMapData(mapWidth, mapHeight);
    position = Vector2.Zero.add(viewport_size_X / 2, viewport_size_Y / 2);
    screenRect = new Rectangle(getPosition().x, getPosition().y, viewport_size_X, viewport_size_Y);
  }

  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEventDispatcher().getEvents().parallelStream().filter(
        event -> event.getReceiverID().equals("GLOBAL") || event.getReceiverID().equals("MOUSE"))
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
          }
        });
  }

  public void move(Vector2 direction) {
    setPositionAndKeepWithinMapBoundaries(position.add(direction));
  }

  private void setMapData(int mapWidth, int mapHeight) {
    offsetMaxX = mapWidth * (Config.B2PIX * WorldHandler.getWorldMap().getNODE_SIZE_BOX2D())
        - viewport_size_X / 2;
    offsetMaxY = mapHeight * (Config.B2PIX * WorldHandler.getWorldMap().getNODE_SIZE_BOX2D())
        - viewport_size_Y / 2;
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

  /**
   *
   * @return top left corner of the camera window
   */
  public Vector2 getPosition() {
    return new Vector2(position.x - viewport_size_X / 2, position.y - viewport_size_Y / 2);
  }

  private void setPositionAndKeepWithinMapBoundaries(Vector2 point) {
    position = new Vector2(keepWithinBoundaries(offsetMinX, offsetMaxX, point.x),
        keepWithinBoundaries(offsetMinY, offsetMaxY, point.y));
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
  public boolean bOnCamera(Vector2 pos) {
    updateScreenRectangle();
    return screenRect.contains(pos.cpy().scl(Config.B2PIX));
  }



  private void updateScreenRectangle() {
    screenRect.setPosition(getPosition());
  }

  public int getViewport_size_X() {
    return viewport_size_X;
  }

  public int getViewport_size_Y() {
    return viewport_size_Y;
  }
}
