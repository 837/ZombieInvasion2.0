package ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse;

import java.util.Optional;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

public class MouseTileSelectionModule extends Module implements UpdatableModul, RenderableModul {
  Node selectedNode = null;

  public MouseTileSelectionModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    if (selectedNode != null) {
      g.setColor(Color.white);
      g.drawRect(
          selectedNode.getX() * (Config.B2PIX * WorldHandler.getWorldMap().getNODE_SIZE_BOX2D()),
          selectedNode.getY() * (Config.B2PIX * WorldHandler.getWorldMap().getNODE_SIZE_BOX2D()),
          selectedNode.getTileSize(), selectedNode.getTileSize());
      g.drawString("SelectedNodeType: " + selectedNode.getType(),
          10 + WorldHandler.getCamera().getPosition().x,
          30 + WorldHandler.getCamera().getPosition().y);
      g.drawString("SelectedNodePos: [" + selectedNode.getX() + ", " + selectedNode.getY() + "]",
          10 + WorldHandler.getCamera().getPosition().x,
          45 + WorldHandler.getCamera().getPosition().y);
    }
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getEventsFrom(getEntityID())
        .ifPresent(events -> events.forEach(event -> {
          switch (event.getEvent()) {
            case MOUSE_MOVED:
              event.getAdditionalInfo(Vector2[].class).ifPresent(positions -> {
                positions[1].add(WorldHandler.getCamera().getPosition())
                    .scl(Config.PIX2B / WorldHandler.getWorldMap().getNODE_SIZE_BOX2D());
                Node[][] map = WorldHandler.getWorldMap().getMap();
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
