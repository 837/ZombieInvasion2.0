package ch.m837.zombieInvasion.entities.dataHandling;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

public enum DataType {
  /*
   * generic
   */
  IS_SELECTED(boolean.class),

  /*
   * physics
   */
  COLLISION_FIXTURE(Fixture.class), POSITION(Vector2.class),

  /*
   * Movement
   */
  MOVE_TO_POS(Vector2.class);

  private <T> DataType(Class<T> clazz) {
   // this.clazz = clazz;
  }
}
