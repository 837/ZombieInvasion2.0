package ch.zombieInvasion.Camera;

import ch.zombieInvasion.util.Vector2D;

public class Camera {
  private Vector2D position = new Vector2D();

  private final int viewport_size_X;
  private final int viewport_size_Y;

  private double offsetMaxX;
  private double offsetMaxY;
  private double offsetMinX;
  private double offsetMinY;

  public Camera(int gameWidth, int gameHeight) {
    viewport_size_X = gameWidth;
    viewport_size_Y = gameHeight;

    offsetMinX = viewport_size_X / 2;
    offsetMinY = viewport_size_Y / 2;
  }

  public void setPosition(Vector2D point) {
    position = new Vector2D(checkOffset(offsetMinX, offsetMaxX, point.x), checkOffset(offsetMinY, offsetMaxY, point.y));
  }

  public void move(Vector2D direction) {
    setPosition(position.add(direction));
  }

  public void setMapData(int mapWidth, int mapHeight) {
    offsetMaxX = mapWidth - viewport_size_X / 2;
    offsetMaxY = mapHeight - viewport_size_Y / 2;
    /**
     * muss noch entfernt werden, workaround
     */
    move(new Vector2D(0, -1));
  }

  private double checkOffset(double lowerBound, double upperBound, double number) {
    if (number < lowerBound) return lowerBound;
    if (number > upperBound) return upperBound;
    return number;
  }

  public Vector2D getPosition() {
    return new Vector2D(position.x - viewport_size_X / 2, position.y - viewport_size_Y / 2);
  }

  public double getWorldPosX(double x) {
    return (x + getPosition().x);
  }

  public double getWorldPosY(double y) {
    return (y + getPosition().y);
  }

  public Vector2D getPositionInWorld(Vector2D screenPos) {
    return screenPos.add(getPosition());
  }

  
  public double getScreenPosX(double x) {
    return (x - getPosition().x);
  }

  public double getScreenPosY(double y) {
    return (y - getPosition().y);
  }

  public Vector2D getPositionOnScreen(Vector2D screenPos) {
    return screenPos.sub(getPosition());
  }
  
  
  public double getCamPosX() {
    return getPosition().x;
  }

  public double getCamPosY() {
    return getPosition().y;
  }

  public int getViewport_size_X() {
    return viewport_size_X;
  }

  public int getViewport_size_Y() {
    return viewport_size_Y;
  }

}
