package ch.m837.zombieInvasion.entities.module.modules;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.module.Module;
import ch.m837.zombieInvasion.entities.module.RenderableModul;

public class SimpleImageRenderModule extends Module implements RenderableModul {
  private final Image imageToRender;

  public SimpleImageRenderModule(String entityID, Image imageToRender) {
    super(entityID);
    this.imageToRender = imageToRender;
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    Object position = World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION);
    if (position instanceof Vector2) {
      Vector2 vec2Position = (Vector2) position;
      g.drawImage(imageToRender, vec2Position.x, vec2Position.y);
    }
  }

  @Override
  public Object getData(DataType dataType) {
    // TODO Auto-generated method stub
    return null;
  }

}
