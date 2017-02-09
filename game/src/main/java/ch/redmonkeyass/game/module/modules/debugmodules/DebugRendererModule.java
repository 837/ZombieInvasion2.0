package ch.redmonkeyass.game.module.modules.debugmodules;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.RenderableModule;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import com.badlogic.gdx.math.Vector2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.Optional;

/**
 * DebugRendererModule is exactly what it sounds like. Renders all the debug stuff. put in it
 * whatever you want to draw.
 * 
 * @author Matthias
 *
 */
public class DebugRendererModule extends Module implements RenderableModule {

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
          g.setColor(Color.white);
          g.drawString(getEntityID(), position.x - 18, position.y - 18);
          g.drawString(position.toString(), position.x, position.y);

        });

    // XXX DEBUG PATHFINDING
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.CALCULATED_PATH, List.class)
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
