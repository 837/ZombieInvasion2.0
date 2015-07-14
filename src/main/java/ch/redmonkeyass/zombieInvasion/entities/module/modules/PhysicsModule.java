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

  public PhysicsModule(String entityID, Body b2Body) {
    super(entityID);
    this.b2Body = b2Body;
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
            actualPos = WorldHandler.getWorldMap()
                .getMap()[(int) (b2Body.getPosition().x)][(int) (b2Body.getPosition().y)];
            goalPos = pathToGoal.get(0);


            if (actualPos == goalPos) {
              pathToGoal.remove(0);
            } else {
              moveToTile(new Vector2(
                  pathToGoal.get(0).getX() + (WorldHandler.getWorldMap().getNODE_SIZE_BOX2D() / 2),
                  pathToGoal.get(0).getY() + (WorldHandler.getWorldMap().getNODE_SIZE_BOX2D() / 2)));
            }
          } else {
            b2Body.applyForceToCenter(
                b2Body.getLinearVelocity().cpy().scl(-1).scl(b2Body.getMass()), true);
          }
        });

  }

  void moveToTile(Vector2 target) {
    Vector2 desiredHeading = target.cpy().sub(b2Body.getPosition());

    // input from sprite
    Vector2 velocity = b2Body.getLinearVelocity();
    float maxVelocity = 10;
    float acceleration = 10;

    Vector2 steeringForce = desiredHeading.sub(velocity);

    // force to reach and keep desired velocity
    steeringForce.x = desiredHeading.x;
    steeringForce.y = desiredHeading.y;
    steeringForce.scl(maxVelocity);
    steeringForce.sub(velocity.x, velocity.y);
    steeringForce.scl(acceleration);
    steeringForce.scl(1 / Config.B2PIX);

    // apply steering force
    b2Body.applyForceToCenter(steeringForce, true);

  }

  void arrive(Vector2 target) {
    Vector2 desired = target.cpy().sub(b2Body.getPosition());

    float d = desired.len();
    int maxSpeed = 10;
    int maxForce = 10;

    desired.nor();
    // if (d < 0.2 && d > 0.1) {
    // float m = MathUtil.map(d, 0, 10, 0, maxSpeed);
    // desired.scl(m);
    // } else if (d <= 0.05) {
    // desired.scl(0);
    // } else {
    desired.scl(maxSpeed);
    // }

    Vector2 steer = desired.sub(b2Body.getLinearVelocity());
    // steer.limit(maxForce);
    b2Body.applyForceToCenter(steer.scl(b2Body.getMass()), true);
  }

  @Override
  public void prepareModuleForRemoval() {
    WorldHandler.getB2World().destroyBody(b2Body);
  }
}

