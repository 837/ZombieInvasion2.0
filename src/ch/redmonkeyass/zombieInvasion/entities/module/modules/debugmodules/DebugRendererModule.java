package ch.redmonkeyass.zombieInvasion.entities.module.modules.debugmodules;

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

public class DebugRendererModule extends Module implements RenderableModul {

  public DebugRendererModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
        .ifPresent(position -> {
          position.scl(Config.B2PIX); // transform to world
                                      // coordinates
          g.setColor(Color.black);
          g.drawString(getEntityID(), position.x - 18, position.y - 18);
          g.drawString(position.toString(), position.x, position.y);
        });
  }

  @Override
  public Object getData(DataType dataType) {
    // TODO Auto-generated method stub
    return null;
  }

}
