package ch.redmonkeyass.zombieInvasion.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

/**
 * MovementHelper has helper functions to apply force to entities.
 * 
 * @author Matthias
 *
 */
public class MovementHelper {

  /**
   * MoveToNode moves the entity to the target node.
   * 
   * @param target
   * @param b2Body
   * @param entityWidthHeight
   * @param maxVelocity
   * @param acceleration
   */
  public static void moveToNode(Node target, Body b2Body, float entityWidthHeight,
      float maxVelocity, float acceleration) {
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

    Vector2 steeringForce = desiredHeading.sub(velocity);

    // force to reach and keep desired velocity
    steeringForce.x = desiredHeading.x;
    steeringForce.y = desiredHeading.y;
    steeringForce.scl(maxVelocity);
  //  steeringForce.sub(velocity);
    steeringForce.scl(acceleration);
    steeringForce.scl(1 / Config.B2PIX);

    // apply steering force
    b2Body.applyForceToCenter(steeringForce, true);
  }

  private void arrive(Vector2 target, Vector2 currentPos) {
    float xDistance = target.x - currentPos.x;
    float yDistance = target.y - currentPos.y;
    float distance = (float) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    if (distance > 1) {
      // ship.x += xDistance * easingAmount;
      //  ship.y += yDistance * easingAmount;
    }
  }
}
