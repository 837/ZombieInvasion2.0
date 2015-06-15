package ch.redmonkeyass.zombieinvasion.entities.module.modules;

import ch.redmonkeyass.zombieinvasion.World;
import ch.redmonkeyass.zombieinvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieinvasion.entities.module.UpdatableModul;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieinvasion.entities.module.Module;


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
          moveToPos.scl(0.05f);
          b2Body.applyForceToCenter(moveToPos, true);
        });
  }
}

