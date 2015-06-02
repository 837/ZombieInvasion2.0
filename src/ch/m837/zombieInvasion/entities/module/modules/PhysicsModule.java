package ch.m837.zombieInvasion.entities.module.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

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
        return b2Body.getPosition();
      case COLLISION_FIXTURE:
        return b2Body.getFixtureList().first();
    }
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    b2Body.applyForceToCenter(new Vector2(0.1f, 0.1f), true);

  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    Fixture s = b2Body.getFixtureList().first();
    org.newdawn.slick.geom.Shape ss =
        new Circle(s.getBody().getPosition().x*World.B2PIX, s.getBody().getPosition().y*World.B2PIX, s.getShape().getRadius()*World.B2PIX);
    g.fill(ss);
  }


}

