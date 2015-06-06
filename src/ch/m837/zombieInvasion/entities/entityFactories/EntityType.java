package ch.m837.zombieInvasion.entities.entityFactories;

public enum EntityType {
  ZERO(0, 0), PLAYER_TEST(1, 1);

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
