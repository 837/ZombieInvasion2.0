package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.Config;
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
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case POSITION:
        return Optional.ofNullable(b2Body.getPosition().cpy());
      case COLLISION_FIXTURE:
        return Optional.ofNullable(b2Body.getFixtureList().first());
    }
    return Optional.empty();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getDataFrom(getEntityID(), DataType.MOVE_TO_POS, Vector2.class)
        .ifPresent(pos -> arrive(pos.scl(Config.PIX2B)));
  }

  void arrive(Vector2 target) {
    Vector2 desired = target.sub(b2Body.getPosition().cpy());

    float d = desired.len();
    int maxSpeed = 3;
    int maxForce = 3;

    System.out.println(d);
    desired.nor();
    if (d < 3.5 && d > 0.3) {
      float m = MathUtil.map(d, 0, 10, 0, maxSpeed);
      desired.scl(m);
    } else if (d <= 0.3) {
      desired.scl(0);
    } else {
      desired.scl(maxSpeed);
    }

    System.out.println(b2Body.getLinearVelocity());
    System.out.println(desired);
    Vector2 steer = desired.sub(b2Body.getLinearVelocity().cpy());
    System.out.println(steer);
    steer.limit(maxForce);
    b2Body.applyForceToCenter(steer, true);
  }

}

