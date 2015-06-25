package ch.redmonkeyass.zombieInvasion.entities.entityfactories;

public enum EntityType {
  ZERO(0, 0), MOUSE(0, 0), ADOLF(1, 1), HANS(1, 1), GERHART(1, 1), ZOMBIE(1, 1);

  private float width, height;

  /**
   * 
   * @return width in meter
   */
  public float getWidth() {
    return width;
  }

  /**
   * 
   * @return height in meter
   */
  public float getHeight() {
    return height;
  }

  /**
   * 
   * @param x = width in meter
   * @param y = height in meter
   */
  private EntityType(float width, float height) {
    this.width = width;
    this.height = height;
  }
}
