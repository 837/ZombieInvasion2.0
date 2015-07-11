package ch.redmonkeyass.zombieInvasion.entities.datahandling;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import ch.redmonkeyass.zombieInvasion.entities.module.modules.EntityStatusModule.Entity_Status;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

public enum DataType {
  /*
   * generic
   */
  IS_SELECTED(Boolean.class), EVENTS(ArrayList.class),

  /*
   * physics
   */
  COLLISION_FIXTURE(Fixture.class), POSITION(Vector2.class), ROTATIONRAD(Float.class),

  /*
   * Movement
   */
  MOVE_TO_POS(List.class),

  /*
   * Entity Stati
   */
  ENTITY_STATUS(Entity_Status.class),

  /*
   * MouseThings
   */
  MOUSE_SELECTED_NODE(Node.class);
  private DataType(final Class<?> clazz) {
    // this.clazz = clazz;
  }

  // private Class<?> clazz;
  //
  // public Class<?> getClazz() {
  // return clazz;
  // }
}
