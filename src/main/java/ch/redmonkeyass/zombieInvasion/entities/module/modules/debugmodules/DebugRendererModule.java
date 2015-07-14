package ch.redmonkeyass.zombieInvasion.entities.module.modules.debugmodules;

import java.util.List;
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
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

public class DebugRendererModule extends Module implements RenderableModul {

  public DebugRendererModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
        .ifPresent(position -> {
          position.scl(Config.B2PIX); // transform to world
                                      // coordinates
          g.setColor(Color.black);
          g.drawString(getEntityID(), position.x - 18, position.y - 18);
          g.drawString(position.toString(), position.x, position.y);
        });

    // XXX DEBUG PATHFINDING
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.MOVE_TO_POS, List.class)
        .ifPresent(path -> {
          g.setColor(Color.red);
          ((List<Node>) path).forEach(c -> g.drawRect(
              c.getX() * (Config.B2PIX * WorldHandler.getWorldMap().getNodeSizeInMeter()),
              c.getY() * (Config.B2PIX * WorldHandler.getWorldMap().getNodeSizeInMeter()),
              c.getTileSize(), c.getTileSize()));
        });

  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

}
