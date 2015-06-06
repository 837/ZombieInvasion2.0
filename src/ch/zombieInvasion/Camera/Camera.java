package ch.zombieInvasion.Camera;

import com.badlogic.gdx.math.Vector2;

public class Camera {
  private Vector2 position = new Vector2();

  private final int viewport_size_X;
  private final int viewport_size_Y;

  private float offsetMaxX;
  private float offsetMaxY;
  private float offsetMinX;
  private float offsetMinY;

  public Camera(int gameWidth, int gameHeight) {
    viewport_size_X = gameWidth;
    viewport_size_Y = gameHeight;

    offsetMinX = viewport_size_X / 2;
    offsetMinY = viewport_size_Y / 2;
  }

  private void setPosition(Vector2 point) {
    position = new Vector2(checkOffset(offsetMinX, offsetMaxX, point.x),
        checkOffset(offsetMinY, offsetMaxY, point.y));
    System.out.println("Moved " + position.toString());
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

  public float getWorldPosX(float x) {
    return (x + getPosition().x);
  }

  public float getWorldPosY(float y) {
    return (y + getPosition().y);
  }

  public Vector2 getPositionInWorld(Vector2 screenPos) {
    return screenPos.add(getPosition());
  }

  public float getScreenPosX(float x) {
    return (x - getPosition().x);
  }

  public float getScreenPosY(float y) {
    return (y - getPosition().y);
  }

  public Vector2 getPositionOnScreen(Vector2 screenPos) {
    return screenPos.sub(getPosition());
  }

  public float getCamPosX() {
    return getPosition().x;
  }

  public float getCamPosY() {
    return getPosition().y;
  }

  public int getViewport_size_X() {
    return viewport_size_X;
  }

  public int getViewport_size_Y() {
    return viewport_size_Y;
  }

}
