package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;


public class PhysicsModule extends Module implements UpdatableModul {
  private Body b2Body;

  public PhysicsModule(String entityID, Body b2Body) {
    super(entityID);
    this.b2Body = b2Body;
  }

  @Override
  public Object getData(DataType dataType) {
    switch (dataType) {
      case POSITION:
        return b2Body.getPosition().cpy();
      case COLLISION_FIXTURE:
        return b2Body.getFixtureList().first();
    }
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getDataFrom(getEntityID(), DataType.MOVE_TO_POS, Vector2.class)
        .ifPresent(moveToPos -> {
          moveToPos.sub(b2Body.getPosition());
          moveToPos.nor();
          moveToPos.scl(16.5f);
          b2Body.applyForceToCenter(moveToPos, true);
        });
  }
  
  
}

