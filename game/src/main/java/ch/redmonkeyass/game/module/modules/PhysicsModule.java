package ch.redmonkeyass.game.module.modules;

import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.UpdatableModul;


public class PhysicsModule extends Module implements UpdatableModul {
  private Body b2Body;
  private Float entityWidthHeight;

  public PhysicsModule(String entityID, Body b2Body, float entityWidthHeight) {
    super(entityID);
    this.b2Body = b2Body;
    this.entityWidthHeight = entityWidthHeight;
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case POSITION:
        return Optional.of(b2Body.getPosition().cpy());
      case COLLISION_FIXTURE:
        return Optional.ofNullable(b2Body.getFixtureList().first());
      case ROTATIONRAD:
        return Optional.of(b2Body.getAngle());
      case ENTITY_WIDTH_HEIGHT:
        return Optional.of(entityWidthHeight);
      case ENTITY_B2DBODY:
        return Optional.of(b2Body);
    }
    return Optional.empty();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {

  }

  @Override
  public void prepareModuleForRemoval() {
    WorldHandler.getB2World().destroyBody(b2Body);
  }
}

