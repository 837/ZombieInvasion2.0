package ch.redmonkeyass.game.module.modules.mouse;

import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

/**
 * MouseTileSelectionModule gives a mouse the ability to select a node.
 * <p>
 * <b>Needs:</b> <br>
 * EventType.MOUSE_MOVED<br>
 * 
 * @author Matthias
 *
 */
public class MouseTileSelectionModule extends Module implements UpdatableModul {
  Node selectedNode = null;
  Node[][] map;

  public MouseTileSelectionModule(String entityID) {
    super(entityID);
    map = WorldHandler.getWorldMap().getMap();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getEventsFrom(getEntityID())
        .ifPresent(events -> events.forEach(event -> {
          switch (event.getEvent()) {
            case MOUSE_MOVED:
              event.getAdditionalInfo(Vector2[].class).ifPresent(positions -> {
                positions[1].add(WorldHandler.getCamera().getPosition())
                    .scl(Config.PIX2B / WorldHandler.getWorldMap().getNodeSizeInMeter());

                selectedNode = map[(int) positions[1].x][(int) positions[1].y];
              });
              break;
          }
        }));
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
