package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.Optional;

/** A point-light (shines in all directions)
 * serves as baseclass to all light sources
 *
 * Created by P on 18.06.2015.
 */
public class LightEmitter extends Module implements UpdatableModul, RenderableModul {
  World b2World = ch.redmonkeyass.zombieInvasion.World.getB2World();
  ArrayList<Vector2> line = new ArrayList<>() ;
  ArrayList<Vector2> intersectionPoints = new ArrayList<>();

  public LightEmitter(String entityID) {
    super(entityID);
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    return Optional.empty();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    intersectionPoints.clear();
    emit();

  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    /*
    for debugging
     */

    g.setColor(Color.red);

    ch.redmonkeyass.zombieInvasion.World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class).ifPresent(mPos -> {
      LogManager.getLogger().debug("nIntersectionPoints: "+intersectionPoints.size());
      final float b2pix = Config.B2PIX;
      for (Vector2 p : intersectionPoints) {
        g.drawLine(mPos.x*b2pix, mPos.y*b2pix, p.x*b2pix, p.y*b2pix);
      }
    });


  }

  private void emit() {
    ch.redmonkeyass.zombieInvasion.World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class).ifPresent(c -> {

      Vector2 p = new Vector2(c.x, c.y - ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y() * Config.PIX2B);

      for (float i = 0; i < 2 * Math.PI; i += 2 * Math.PI / 50) {

        //rotate around the origin of the ray
                /*
                float x_new = (  (x - cx) * cos(?) + (y - cy) * sin(?) ) + cx
                 float y_new = ( -(x - cx) * sin(?) + (y - cy) * cos(?) ) + cy
                 */
        Vector2 line = new Vector2(
            (float) ((p.x - c.x) * Math.cos(i) + (p.y - c.y) * Math.sin(i)) + c.x,
            (float) (-(p.x - c.x) * Math.sin(i) + (p.y - c.y) * Math.cos(i)) + c.y
        );

        LightCallBack b = new LightCallBack();
        b2World.rayCast(b, c, line);

        if (b.hasIntersection) {
          intersectionPoints.add(b.closestIntersectionPoint);
        }
      }
    });
  }

  private class LightCallBack implements RayCastCallback {
    boolean hasIntersection = false;
    Vector2 closestIntersectionPoint;
    Fixture closestFixture;

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 intersectionPoint, Vector2 normal, float fraction) {
            /*
            if fixture == the fixture of this entity then ignore the intersection (return -1)
            else return the length of the ray (v) =>
            the very last callback will contain the closest fixture
             */


      Fixture mFixture =  ch.redmonkeyass.zombieInvasion.World.getEntityHandler().getDataFrom(getEntityID(),DataType.COLLISION_FIXTURE,Fixture.class).get();

      if (mFixture == fixture) {
        return -1.f;
      } else {
        hasIntersection = true;
        closestFixture = fixture;
        closestIntersectionPoint = intersectionPoint;
        return fraction;
      }
    }
  }


}
