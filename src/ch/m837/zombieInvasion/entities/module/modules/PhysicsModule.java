package ch.m837.zombieInvasion.entities.module.modules;

import com.badlogic.gdx.physics.box2d.Body;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.module.Module;
import ch.m837.zombieInvasion.entities.module.UpdatableModul;


public class PhysicsModule extends Module implements UpdatableModul {
  private Body b2Body;

  public PhysicsModule(String entityID,Body b2Body) {
    super(entityID);
    this.b2Body = b2Body;
  }

  @Override
  public Object getData(DataType dataType) {
    switch (dataType) {
      case POSITION:
        return b2Body.getPosition();
      case COLLISION_SHAPE:
        // TODO why would you need this?
    }
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    // TODO Auto-generated method stub

  }


}

