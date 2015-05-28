package ch.m837.zombieInvasion.entities.modul.moduls;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.modul.Modul;
import ch.m837.zombieInvasion.entities.modul.UpdatableModul;
import ch.zombieInvasion.util.Vector2D;

public class PhysicModul extends Modul implements UpdatableModul {
  Box2d box2d = new Box2d();

  public PhysicModul(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getData(DataType dataType) {
    switch (dataType) {
      case POSITION:
        return box2d.pos;
      case COLLISION_SHAPE:
        return box2d.shape;
    }
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    // TODO Auto-generated method stub

  }


}


class Box2d {
  Object shape = null;
  Vector2D pos = new Vector2D(100, 100);

}
