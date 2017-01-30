package ch.redmonkeyass.zombieInvasion.module.modules.zombieAI;

import java.util.List;
import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.Entity;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.util.movement.MovementHelper;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

/**
 * This module allows entities to follow a specific other entity
 * <p>
 * Used path finding algorithm is <b>A*</b>
 * <p>
 * <b>Needs:</b> <br>
 * DataType.POSITION,<br>
 * EventType.CALCULATED_PATH<br>
 * 
 * @author Matthias
 *
 */
public class FollowPlayerAI extends Module implements UpdatableModul {
  Entity randomPlayer = null;

  List<Node> pathToEnd = null;

  public FollowPlayerAI(String entityID, String name) {
    super(entityID);
    randomPlayer = WorldHandler.getEntityHandler().getAllEntities().stream()
        .filter(e -> e.getID().equals(name)).findFirst().orElse(null);
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    if (randomPlayer != null) {
      WorldHandler.getEntityHandler()
          .getDataFrom(randomPlayer.getID(), DataType.POSITION, Vector2.class)
          .ifPresent(targetPos -> {
            WorldHandler.getEntityHandler()
                .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
                .ifPresent(entityPos -> {

              pathToEnd = MovementHelper.ASTAR_CALCULATOR.calculatePath(
                  WorldHandler.getWorldMap().getMapNodePos(entityPos),
                  WorldHandler.getWorldMap().getMapNodePos(targetPos));
            });
          });
    }
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case CALCULATED_PATH:
        return Optional.ofNullable(pathToEnd);
    }
    return Optional.empty();
  }

  @Override
  public void prepareModuleForRemoval() {
    randomPlayer = null;
    pathToEnd = null;
  }
}
