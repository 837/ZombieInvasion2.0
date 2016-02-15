package ch.redmonkeyass.zombieInvasion.module.modules;

import java.util.Optional;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.RenderableModul;

/**
 * Created by P on 22.09.2015.
 * <p>
 * Data: this module provides no data
 * <b>Needs:</b> <br>
 * EventType.AREA_SELECTION,<br>
 * EventType.LEFT_CLICK_SELECTION,<br>
 * DataType.COLLISION_FIXTURE,<br>
 * DataType.POSITION<br>
 */
public class LightSource extends Module implements RenderableModul {

  private final int radius;
  private String _entityID;
  private int occluderTextureID;
  private int occluderFrameBufferID;

  public LightSource(String entityID, int radius) {
    super(entityID);
    this._entityID = entityID;
    this.radius = radius;
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    return null;
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {


  }

  private void drawShadows() {
    /*
    0. check whether any part of the light is actually visible
    1. get all entities within the lights radius (box aabb query)
    2. draw each entity onto an occluder texture
    3. send it through the shadow-pipeline
    4. draw the resulting texture onto a shadows texture (with blend mode activated)
    5. the last light to be rendered shall draw the shadows texture ontop of the world
     */

    WorldHandler.getEntityHandler().getDataFrom(_entityID, DataType.POSITION, Vector2.class).ifPresent(position -> {
      WorldHandler.getCamera().overlapsWithCamera(new Rectangle(position.x, position.y, radius, radius));

      WorldHandler.getB2World().QueryAABB(new DrawShadowsCallback(), position.x, position.y, position.x + radius, position.y + radius);

    });
  }


  private class DrawShadowsCallback implements QueryCallback {

    @Override
    public boolean reportFixture(Fixture fixture) {
      //get entity id from b2body
      String id = (String) fixture.getBody().getUserData();

      //set the rendering context to occluder texture
      ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, occluderFrameBufferID);

      // TODO: render rendermodule, currently no functionality in the engine

      //reset the rendering context to screen
      ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);

      return true; //true to continue until all fixtures are found

    }
  }
}
