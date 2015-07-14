package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.List;
import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;


public class PhysicsModule extends Module implements UpdatableModul {
  private Body b2Body;
  private float entityWidthHeight;

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
    }
    return Optional.empty();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.MOVE_TO_POS, List.class)
        .ifPresent(path -> {
          List<Node> pathToGoal = (List<Node>) path;
          Node actualPos, goalPos;

          if (!pathToGoal.isEmpty()) {
            Vector2 position =
                b2Body.getPosition().scl(1f / WorldHandler.getWorldMap().getNodeSizeInMeter());

            actualPos = WorldHandler.getWorldMap().getMap()[(int) (position.x)][(int) (position.y)];
            goalPos = pathToGoal.get(0);

            if (actualPos == goalPos) {
              pathToGoal.remove(0);
            } else {
              moveToNode(goalPos);
            }
          } else {
            b2Body.applyForceToCenter(
                b2Body.getLinearVelocity().cpy().scl(-1).scl(b2Body.getMass()), true);
          }
        });
  }

  private void moveToNode(Node target) {
    float positionOffSet = 0;
    if (entityWidthHeight < WorldHandler.getWorldMap().getNodeSizeInMeter()) {
      positionOffSet = entityWidthHeight / 2;
    } else if (entityWidthHeight > WorldHandler.getWorldMap().getNodeSizeInMeter()) {
      positionOffSet = WorldHandler.getWorldMap().getNodeSizeInMeter();
    }


    Vector2 desiredHeading = new Vector2(
        (target.getX() + (WorldHandler.getWorldMap().getNodeSizeInMeter() / 2) - positionOffSet)
            * WorldHandler.getWorldMap().getNodeSizeInMeter(),
        (target.getY() + (WorldHandler.getWorldMap().getNodeSizeInMeter() / 2) - positionOffSet)
            * WorldHandler.getWorldMap().getNodeSizeInMeter()).sub(b2Body.getPosition());

    // input from sprite
    Vector2 velocity = b2Body.getLinearVelocity();
    float maxVelocity = 10;
    float acceleration = 10;

    Vector2 steeringForce = desiredHeading.sub(velocity);

    // force to reach and keep desired velocity
    steeringForce.x = desiredHeading.x;
    steeringForce.y = desiredHeading.y;
    steeringForce.scl(maxVelocity);
    steeringForce.sub(velocity);
    steeringForce.scl(acceleration);
    steeringForce.scl(1 / Config.B2PIX);

    // apply steering force
    b2Body.applyForceToCenter(steeringForce, true);
  }

  @Override
  public void prepareModuleForRemoval() {
    WorldHandler.getB2World().destroyBody(b2Body);
  }
}

