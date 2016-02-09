package ch.redmonkeyass.zombieInvasion.entities.module.modules.zombieAI;

import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.Entity;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

public class FollowPlayerAI extends Module implements UpdatableModul {
  Entity randomPlayer = null;
  Node selectedNode = null;

  public FollowPlayerAI(String entityID) {
    super(entityID);
    randomPlayer = WorldHandler.getEntityHandler().getAllEntities().stream()
        .filter(e -> e.getID().equals("HANS")).findFirst().orElse(null);
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    // Asking the randomPlayer Entity for its position
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
        .ifPresent(entityPos -> {
          entityPos.add(WorldHandler.getCamera().getPosition())
              .scl(Config.PIX2B / WorldHandler.getWorldMap().getNodeSizeInMeter());
          Node[][] map = WorldHandler.getWorldMap().getMap();
          selectedNode = map[(int) entityPos.x][(int) entityPos.y];
        });

  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case MOUSE_SELECTED_NODE:
        return Optional.ofNullable(selectedNode);
    }
    return Optional.empty();
  }

}
