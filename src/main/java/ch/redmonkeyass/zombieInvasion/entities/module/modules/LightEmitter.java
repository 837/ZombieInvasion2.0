package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;

/**
 * A point-light (shines in all directions) serves as baseclass to all light sources
 * <p>
 * Created by P on 18.06.2015.
 */
public class LightEmitter extends Module implements UpdatableModul, RenderableModul {
  World b2World = ch.redmonkeyass.zombieInvasion.World.getB2World();
  ArrayList<Vector2> line = new ArrayList<>();
  ArrayList<Vector2> intersectionPoints = new ArrayList<>();
  ArrayList<Vector2> vertices = new ArrayList<>();
  Vector2 mPosition;
  Fixture mFixture;
  Body topWall;

  public LightEmitter(String entityID) {
    super(entityID);

    // BodyDef walls = new BodyDef();
    // walls.type = BodyDef.BodyType.KinematicBody;
    // // walls.position.set(new Vector2()); // top
    // walls.position.set(
    // ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X() * 0.5f * Config.PIX2B,
    // 0);
    //
    // FixtureDef f = new FixtureDef();
    // f.isSensor = true;
    // f.density = 1;
    // /*
    // * EdgeShape tltr = new EdgeShape(); tltr.set(new Vector2(),new Vector2(
    // * ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()* Config.PIX2B ,0));
    // */
    // PolygonShape tltr = new PolygonShape();
    // tltr.setAsBox(
    // ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X() * 0.5f * Config.PIX2B,
    // 2);
    // f.shape = tltr;
    //
    // topWall = b2World.createBody(walls);
    // topWall.createFixture(f);
  }

  @Override
  public void prepareModuleForRemoval() {
    super.prepareModuleForRemoval();
    b2World.destroyBody(topWall);
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    return Optional.empty();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    try {
      mPosition = ch.redmonkeyass.zombieInvasion.World.getEntityHandler()
          .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class).get();
      mFixture = ch.redmonkeyass.zombieInvasion.World.getEntityHandler()
          .getDataFrom(getEntityID(), DataType.COLLISION_FIXTURE, Fixture.class).get();
    } catch (NoSuchElementException e) {
      LogManager.getLogger("es").error("couldn't get data: " + e);
    }


    intersectionPoints.clear();
    vertices.clear();
    vertices.ensureCapacity(b2World.getBodyCount() * 5);
    emitToAllFixtures();

  }

  /*
   * for debugging
   */
  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {

    // add the bottom left/right to the intersectionpoints
    intersectionPoints
        .add(new Vector2(ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X(),
            ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y()));
    intersectionPoints
        .add(new Vector2(0, ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y()));


    debugDrawVisibilityLines(g, Color.red);
  }

  /**
   * requires intersectionpoints to be sorted by angle from the body's perspective!!! colors
   * non-visible areas black
   *
   * @param g jwgl Graphics context
   */
  private void debugDrawNotVisibleArea(Graphics g) {

    sortByAngleFromBodysPerspecive();

    Polygon p = new Polygon();
    intersectionPoints.forEach(ip -> p.addPoint(ip.x * Config.B2PIX, ip.y * Config.B2PIX));

    // GradientFill filler = new GradientFill(mPosition.x * b2pix, mPosition.y * b2pix, Color.white,
    // p.getCenterX(), p.getCenterY(), Color.transparent);
    // g.fill(p,filler);

    ch.redmonkeyass.zombieInvasion.World.getEntityHandler()
        .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class).ifPresent(position -> {
          g.setDrawMode(Graphics.MODE_ALPHA_MAP);
          g.clearAlphaMap();
          g.setColor(Color.white);
          g.fill(p);
          g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
        });
  }

  /**
   * draws the lines as conmputed by the emitToall function, useful for debugging
   *
   * @param g jwgl Graphics context
   */
  private void debugDrawVisibilityLines(Graphics g, Color color) {
    Color previousColor = g.getColor();
    g.setColor(color);
    for (Vector2 p : intersectionPoints) {
      g.drawLine(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX, p.x * Config.B2PIX,
          p.y * Config.B2PIX);
    }
    g.setColor(previousColor);
  }

  private void sortByAngleFromBodysPerspecive() {
    // sort by angle from the emitting body
    // uses this formula to account for 360 degree view: (x > 0 ? x : (2*PI + x))
    intersectionPoints.sort((v0, v1) -> Double.compare(
        Math.atan2(mFixture.getBody().getLocalPoint(v1.cpy()).y,
            mFixture.getBody().getLocalPoint(v1.cpy()).x),
        Math.atan2(mFixture.getBody().getLocalPoint(v0.cpy()).y,
            mFixture.getBody().getLocalPoint(v0.cpy()).x)));
  }

  private void emit() {
    ch.redmonkeyass.zombieInvasion.World.getEntityHandler()
        .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class).ifPresent(c -> {

          Vector2 p = new Vector2(c.x,
              c.y - ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y()
                  * Config.PIX2B);

          for (float i = 0; i < 2 * Math.PI; i += 2 * Math.PI / 50) {

            // rotate around the origin of the ray
            /*
             * float x_new = ( (x - cx) * cos(?) + (y - cy) * sin(?) ) + cx float y_new = ( -(x -
             * cx) * sin(?) + (y - cy) * cos(?) ) + cy
             */
            Vector2 line =
                new Vector2((float) ((p.x - c.x) * Math.cos(i) + (p.y - c.y) * Math.sin(i)) + c.x,
                    (float) (-(p.x - c.x) * Math.sin(i) + (p.y - c.y) * Math.cos(i)) + c.y);

            LightCallBack b = new LightCallBack();
            b2World.rayCast(b, c, line);

            if (b.hasIntersection) {
              intersectionPoints.add(b.closestIntersectionPoint);
            }
          }
        });
  }

  /**
   * //TODO make more efficient by using AABB query first
   */
  private void emitToAllFixtures() {
    Array<Body> bodiesFromWorld = new Array<>();

    b2World.getBodies(bodiesFromWorld);
    bodiesFromWorld.removeValue(mFixture.getBody(), true);
    

    ArrayList<Body> bodies = new ArrayList<Body>(Arrays.asList(bodiesFromWorld.toArray()));
    
    
    bodies.stream().filter(e -> mPosition.cpy().sub(e.getPosition()).len() < 10).forEach(b -> {
      Vector2 bWorldPos = new Vector2();

      b.getFixtureList().forEach(f -> {
        if (f.getBody() != mFixture.getBody()) {
          switch (f.getType()) {
            case Circle:
              break;
            case Edge:
              /*
               * EdgeShape e = (EdgeShape)f.getShape(); Vector2 tmp1 = new Vector2();
               * e.getVertex1(tmp1); vertices.add(tmp1.cpy()); vertices.add(createPointAt(0.001f,
               * tmp1)); vertices.add(createPointAt(-0.001f,tmp1)); e.getVertex2(tmp1);
               * vertices.add(tmp1.cpy()); vertices.add(createPointAt(0.001f,tmp1));
               * vertices.add(createPointAt(-0.001f,tmp1));
               */
              break;
            case Polygon:
              PolygonShape p = (PolygonShape) f.getShape();
              Vector2 vertice = new Vector2();
              Vector2 worldpoint = new Vector2();
              for (int i = 0; i < p.getVertexCount(); i++) {
                p.getVertex(i, vertice);
                worldpoint = b.getWorldPoint(vertice).cpy();
                vertices.add(worldpoint.cpy());
                // TODO adjust precision maybe?
                float maxDistVisibleOnScreen =
                    ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()
                        * Config.PIX2B;
                vertices.add(createPointAt(0.000001f, worldpoint).cpy().sub(mPosition)
                    .scl(maxDistVisibleOnScreen).add(mPosition));
                vertices.add(createPointAt(-0.000001f, worldpoint).cpy().sub(mPosition)
                    .scl(maxDistVisibleOnScreen).add(mPosition));
              }
              break;
            case Chain:
              break;
          }
        }
      });
    });

    vertices.forEach(v -> {
      LightCallBack b = new LightCallBack();
      b2World.rayCast(b, mPosition, v);
      if (b.hasIntersection) {
        intersectionPoints.add(b.closestIntersectionPoint.cpy());
      } else {
        intersectionPoints.add(v);
      }
    });


  }

  private Vector2 createPointAt(float offsetinRadians, Vector2 point) {
    final float o = offsetinRadians;
    final Vector2 center = mPosition;
    final Vector2 p = point;

    Vector2 line = new Vector2(
        (float) ((p.x - center.x) * Math.cos(o) + (p.y - center.y) * Math.sin(o)) + center.x,
        (float) (-(p.x - center.x) * Math.sin(o) + (p.y - center.y) * Math.cos(o)) + center.y);

    return line;
  }

  private class LightCallBack implements RayCastCallback {
    boolean hasIntersection = false;
    Vector2 closestIntersectionPoint;
    Fixture closestFixture;

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 intersectionPoint, Vector2 normal,
        float fraction) {
      /*
       * if fixture == the fixture of this entity then ignore the intersection (return -1) else
       * return the length of the ray (v) => the very last callback will contain the closest fixture
       */
      hasIntersection = true;
      closestFixture = fixture;
      closestIntersectionPoint = intersectionPoint;
      return fraction;
    }
  }


}
