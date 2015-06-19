package ch.redmonkeyass.zombieInvasion.entities.datahandling;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

public enum DataType {
  /*
   * generic
   */
  IS_SELECTED(Boolean.class), EVENTS(ArrayList.class),

  /*
   * physics
   */
  COLLISION_FIXTURE(Fixture.class), POSITION(Vector2.class),

  /*
   * Movement
   */
  MOVE_TO_POS(Vector2.class);

  private DataType(final Class<?> clazz) {
    // this.clazz = clazz;
  }

  // private Class<?> clazz;
  //
  // public Class<?> getClazz() {
  // return clazz;
  // }
}
