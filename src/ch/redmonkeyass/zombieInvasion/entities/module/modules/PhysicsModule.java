package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.util.MathUtil;


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
    Vector2 target =
        World.getEntityHandler().getDataFrom(getEntityID(), DataType.MOVE_TO_POS, Vector2.class)
            .orElse(b2Body.getPosition());
    arrive(target);
  }

  void arrive(Vector2 target) {
    Vector2 desired = target.sub(b2Body.getPosition());

    // The distance is the magnitude of the vector pointing from location to target.
    float d = desired.len();
    desired.nor();
    // If we are closer than 100 pixels...
    if (d < 10 && d > 2) {
      // ...set the magnitude according to how close we are.
      float m = MathUtil.map(d, 0, 30, 0, 10);
      desired.scl(m);
    } else if (d <= 2) {
      desired.scl(0);
    } else {
      // Otherwise, proceed at maximum speed.
      desired.scl(10);
    }

    // The usual steering = desired - velocity
    Vector2 steer = desired.sub(b2Body.getLinearVelocity());
    steer.limit(10);
    b2Body.applyForceToCenter(steer, true);
  }

}

