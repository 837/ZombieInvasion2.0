package ch.m837.zombieInvasion.entities.module.modules;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.module.Module;
import ch.m837.zombieInvasion.entities.module.RenderableModul;
import ch.m837.zombieInvasion.entities.module.UpdatableModul;


public class PhysicsModule extends Module implements UpdatableModul, RenderableModul {
  private Body b2Body;

  public PhysicsModule(String entityID, Body b2Body) {
    super(entityID);
    this.b2Body = b2Body;
  }

  @Override
  public Object getData(DataType dataType) {
    switch (dataType) {
      case POSITION:
        return b2Body.getPosition().cpy();
      case COLLISION_FIXTURE:
        return b2Body.getFixtureList().first();
    }
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getDataFrom(getEntityID(), DataType.MOVE_TO_POS, Vector2.class)
        .ifPresent(moveToPos -> {
         
          b2Body.setLinearVelocity((b2Body.getPosition().x - moveToPos.x) * 0.3f, (b2Body.getPosition().y - moveToPos.y)* 0.3f);
        });
   

  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    // Fixture f = b2Body.getFixtureList().first();
    // PolygonShape p = (PolygonShape) f.getShape();
    // Rectangle ss = new Rectangle(
    // f.getBody().getPosition().x * Config.B2PIX,
    // f.getBody().getPosition().y * Config.B2PIX,
    // f.,
    // 0
    // );
    //
    // g.fill(ss);
  }


}

