package ch.redmonkeyass.zombieInvasion.module.modules;

import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.util.ImageWrapper;

/**
 * Gives the Entity the ability to render a simple image.
 * <p>
 * <b>Needs:</b><br>
 * DataType.POSITION,<br>
 * DataType.ROTATIONRAD,<br>
 * 
 * @author Matthias
 *
 */
public class SimpleImageRenderModule extends Module implements RenderableModul {
  private final ImageWrapper imageToRenderWrapper;

  public SimpleImageRenderModule(String entityID, ImageWrapper imageToRenderWrapper) {
    super(entityID);
    this.imageToRenderWrapper = imageToRenderWrapper;
  }

  /**
   * get the position data from the entity, transform it to WorldHandler coordinates and render g
   * 
   * @param gc
   * @param sbg
   * @param g
   */
  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
        .ifPresent(position -> {
          WorldHandler.getEntityHandler()
              .getDataFrom(getEntityID(), DataType.ROTATIONRAD, Float.class).ifPresent(rot -> {
            g.pushTransform();
            position.scl(Config.B2PIX); // transform to WorldHandler
            // coordinates
            g.rotate(position.x, position.y, rot * 180 / (float) Math.PI);

            g.drawImage(imageToRenderWrapper.getB2DScaled(),
                position.x - imageToRenderWrapper.getB2DScaled().getWidth() / 2,
                position.y - imageToRenderWrapper.getB2DScaled().getHeight() / 2);
            g.popTransform();
          });
        });
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

}
