package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.List;
import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.util.movement.MovementHelper;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

public class MovementModule extends Module implements UpdatableModul {
  private float maxVelocity;
  private float acceleration;

  public MovementModule(String entityID, float maxVelocity, float acceleration) {
    super(entityID);
    this.maxVelocity = maxVelocity;
    this.acceleration = acceleration;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.ENTITY_B2DBODY, Body.class)
        .ifPresent(b2Body -> {
          WorldHandler.getEntityHandler()
              .getDataFrom(getEntityID(), DataType.ENTITY_WIDTH_HEIGHT, Float.class)
              .ifPresent(entityWidthHeight -> {
            WorldHandler.getEntityHandler()
                .getDataFrom(getEntityID(), DataType.CALCULATED_PATH, List.class)
                .ifPresent(path -> {
              List<Node> pathToGoal = path;
              Node actualPos, nextPos;

              if (!pathToGoal.isEmpty()) {
                actualPos = WorldHandler.getWorldMap().getMapNodePos(b2Body.getPosition());
                nextPos = pathToGoal.get(0);

                if (actualPos == nextPos) {
                  pathToGoal.remove(0);
                } else {
                  MovementHelper.moveToNode(nextPos, b2Body, entityWidthHeight, maxVelocity,
                      acceleration);
                }
              } else {
                b2Body.applyForceToCenter(
                    b2Body.getLinearVelocity().cpy().scl(-1).scl(b2Body.getMass()), true);
              }
            });
          });

          //limit max velocity
          Vector2 vel = b2Body.getLinearVelocity();
          if(vel.len() > maxVelocity){
            b2Body.setLinearVelocity(vel.nor().scl(maxVelocity));
          }
        });
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

}
