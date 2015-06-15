package ch.redmonkeyass.zombieinvasion.entities.entityfactories;

public enum EntityType {
    ZERO(0, 0), PLAYER_TEST(1, 1), MOUSE(0, 0), BOX(3,3);

  private float height;
  private float width;


  public float getWidth() {
    return width;
  }


  public float getHeight() {
    return height;
  }

  /**.
   * 
   * @param width = width in meter
   * @param height = height in meter
   */
  EntityType(float width, float height) {
    this.width = width;
    this.height = height;
  }
}
