package ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse;

import java.util.Optional;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;
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
      g.drawRect(selectedNode.getX() * Config.B2PIX, selectedNode.getY() * Config.B2PIX,
          selectedNode.getWidth(), selectedNode.getHeight());
    }
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID())
        .ifPresent(events -> events.forEach(event -> {
          switch (event.getEvent()) {
            case MOUSE_MOVED:
              event.getAdditionalInfo(Vector2[].class).ifPresent(positions -> {
                positions[1].add(World.getCamera().getPosition()).scl(Config.PIX2B);
                Node[][] map = World.getWorldMap().getMap();
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
