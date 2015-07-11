package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.List;
import java.util.Optional;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.xguzm.pathfinding.grid.GridCell;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.util.ImageWrapper;

public class SimpleImageRenderModule extends Module implements RenderableModul {
  private final ImageWrapper imageToRenderWrapper;

  public SimpleImageRenderModule(String entityID, ImageWrapper imageToRenderWrapper) {
    super(entityID);
    this.imageToRenderWrapper = imageToRenderWrapper;
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
          World.getEntityHandler().getDataFrom(getEntityID(), DataType.ROTATIONRAD, Float.class)
              .ifPresent(rot -> {
            g.pushTransform();
            position.scl(Config.B2PIX); // transform to world
            // coordinates
            g.rotate(position.x, position.y, rot * 180 / (float) Math.PI);

            g.drawImage(imageToRenderWrapper.getB2DScaled(),
                position.x - imageToRenderWrapper.getB2DScaled().getWidth() / 2,
                position.y - imageToRenderWrapper.getB2DScaled().getHeight() / 2);
            g.popTransform();
          });
        });

    World.getEntityHandler().getDataFrom(getEntityID(), DataType.MOVE_TO_POS, List.class)
        .ifPresent(path -> {
          g.setColor(Color.yellow);
          ((List<GridCell>) path).forEach(c -> g.drawRect(c.getX()*Config.B2PIX, c.getY()*Config.B2PIX, 32, 32));
        });
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

}
