package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;

public class SimpleImageRenderModule extends Module implements RenderableModul {
  private final Image imageToRender;

  public SimpleImageRenderModule(String entityID, Image imageToRender) {
    super(entityID);
    this.imageToRender = imageToRender;
  }

  /**
   * get the position data from the entity, transform it to world coordinates and render g
   * 
   * @param gc
   * @param sbg
   * @param g
   */
  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
        .ifPresent(position -> {
          position.scl(Config.B2PIX); // transform to world
                                      // coordinates
          g.drawImage(imageToRender, position.x - imageToRender.getWidth() / 2,
              position.y - imageToRender.getHeight() / 2);
        });
  }

  @Override
  public Object getData(DataType dataType) {
    // TODO Auto-generated method stub
    return null;
  }

}
