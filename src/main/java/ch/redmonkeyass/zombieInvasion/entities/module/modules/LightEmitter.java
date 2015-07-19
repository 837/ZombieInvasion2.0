package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;

/**
 * A point-light (shines in all directions) serves as baseclass to all light sources <p> Created by
 * P on 18.06.2015.
 */
public class LightEmitter extends Module implements UpdatableModul, RenderableModul {
  World b2World = WorldHandler.getB2World();
  ArrayList<Vector2> line = new ArrayList<>();
  ArrayList<Vector2> visibilityPolygon = new ArrayList<>();
  ArrayList<Vector2> vertices = new ArrayList<>();
  Vector2 mPosition;
  Vector2 lastValidPos;
  Fixture mFixture;

  //limits range of the light
  private float lightCircleRadiusPix = 300;
  private float lightCircleRadiusM = lightCircleRadiusPix * Config.PIX2B;

  public LightEmitter(String entityID) {
    super(entityID);
  }

  @Override
  public void prepareModuleForRemoval() {
    super.prepareModuleForRemoval();
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    return Optional.empty();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    try {
      mPosition = WorldHandler.getEntityHandler()
          .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class).get();
      mFixture = WorldHandler.getEntityHandler()
          .getDataFrom(getEntityID(), DataType.COLLISION_FIXTURE, Fixture.class).get();
    } catch (NoSuchElementException e) {
      LogManager.getLogger("es").error("couldn't get data: " + e);
    }
    //visibility polygon has changed or hasn't been created yet, do calculation
    visibilityPolygon.clear();
    vertices.clear();
    vertices.ensureCapacity(b2World.getBodyCount() * 5);
    calculateVisibilityPolygon();
    sortVisibilityPolygonByAngleFromBodysPerspective();

    lastValidPos = mPosition.cpy();
  }

  /**
   * //TODO make more efficient by using AABB query first
   */
  private void calculateVisibilityPolygon() {
    Array<Body> bodies = new Array<>();
    b2World.getBodies(bodies);

    final float c = lightCircleRadiusM;

    BodiesInRangeCB cb = new BodiesInRangeCB();
    b2World.QueryAABB(cb,
        mPosition.x - c,
        mPosition.y - c,
        mPosition.x + c,
        mPosition.y + c
    );


    cb.bodiesInRange.remove(mFixture.getBody());
    bodies.removeValue(mFixture.getBody(), true);
    // TODO adjust precision maybe?
    final float maxDistVisibleOnScreen = lightCircleRadiusPix * Config.PIX2B;

    cb.bodiesInRange.forEach(b -> {
      Vector2 bWorldPos = new Vector2();
      b.getFixtureList().forEach(f -> {

        if (f.getBody() != mFixture.getBody()) {
          switch (f.getType()) {
            case Circle:
              break;
            case Edge:

              EdgeShape e = (EdgeShape) f.getShape();
              Vector2 tmp1 = new Vector2();
              e.getVertex1(tmp1);
              Vector2 worldpoint1 = b.getWorldPoint(tmp1).cpy();
              vertices.add(worldpoint1.cpy());
              vertices.add(createVectorRotatedAroundCenter(0.000001f, worldpoint1).cpy().sub(mPosition).scl(maxDistVisibleOnScreen)
                  .add(mPosition));
              vertices.add(createVectorRotatedAroundCenter(-0.000001f, worldpoint1).cpy().sub(mPosition)
                  .scl(maxDistVisibleOnScreen).add(mPosition));
              e.getVertex2(tmp1);
              worldpoint1 = b.getWorldPoint(tmp1).cpy();
              vertices.add(worldpoint1.cpy());
              vertices.add(createVectorRotatedAroundCenter(0.000001f, worldpoint1).cpy().sub(mPosition).scl(maxDistVisibleOnScreen)
                  .add(mPosition));
              vertices.add(createVectorRotatedAroundCenter(-0.000001f, worldpoint1).cpy().sub(mPosition)
                  .scl(maxDistVisibleOnScreen).add(mPosition));

              break;
            case Polygon:
              PolygonShape p = (PolygonShape) f.getShape();
              Vector2 vertice = new Vector2();
              Vector2 worldpoint = new Vector2();
              for (int i = 0; i < p.getVertexCount(); i++) {
                p.getVertex(i, vertice);
                worldpoint = b.getWorldPoint(vertice).cpy();
                vertices.add(worldpoint.cpy());
                vertices.add(createVectorRotatedAroundCenter(0.000001f, worldpoint).cpy().sub(mPosition).scl(maxDistVisibleOnScreen)
                    .add(mPosition));
                vertices.add(createVectorRotatedAroundCenter(-0.000001f, worldpoint).cpy().sub(mPosition)
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
        visibilityPolygon.add(b.closestIntersectionPoint.cpy());
      } else {
        //visibilityPolygon.add(v);
      }
    });
  }

  private void sortVisibilityPolygonByAngleFromBodysPerspective() {
    //sort by angle from the emitting body
    // uses this formula to account for 360 degree view: (x > 0 ? x : (2*PI + x))
    visibilityPolygon.sort((v0, v1) -> Double.compare(
        Math.atan2(v1.y - mPosition.y,
            v1.x - mPosition.x),
        Math.atan2(v0.y - mPosition.y,
            v0.x - mPosition.x)));
  }

  private Vector2 createVectorRotatedAroundCenter(float offsetinRadians, Vector2 point) {
    final float o = offsetinRadians;
    final Vector2 center = mPosition;
    final Vector2 p = point;

    return new Vector2(
        (float) ((p.x - center.x) * Math.cos(o) + (p.y - center.y) * Math.sin(o)) + center.x,
        (float) (-(p.x - center.x) * Math.sin(o) + (p.y - center.y) * Math.cos(o)) + center.y);
  }

  /*
 * for debugging
 */
  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    Color colorBefore = g.getColor();
    /*add the bottom left/right to the intersectionpoints
    visibilityPolygon
        .add(new Vector2(ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_X(),
                ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y()));
    visibilityPolygon
        .add(new Vector2(0, ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y()));
  */
    //debugDrawNotVisibleArea(g);
    debugDrawVisibilityLines(g, Color.blue);


//    prepareVisibilityPolygonForLights(g);
//    testLight(g);

    g.setColor(colorBefore);
    g.setDrawMode(Graphics.MODE_NORMAL);
  }

  /**
   * requires visibilityPolygon to be sorted by angle from the body's perspective!!! colors
   * non-visible areas black
   *
   * @param g jwgl Graphics context
   */
  private void prepareVisibilityPolygonForLights(Graphics g) {
    g.pushTransform();

    Vector2 pos = mPosition.cpy().scl(Config.B2PIX);
    g.setDrawMode(Graphics.MODE_ALPHA_MAP);
    g.setColor(new Color(0, 0, 0, 0));

    //ensure length from center of light to point is at max getLightCircleRadiusM
    for (int i = 0; i < visibilityPolygon.size(); i++) {
      float distFromCenter2 = visibilityPolygon.get(i).cpy().sub(mPosition).len2();
      if (distFromCenter2 > lightCircleRadiusM * lightCircleRadiusM) {
        visibilityPolygon.get(i).sub(mPosition).nor().scl((int) lightCircleRadiusM).add(mPosition);
      }
    }

    for (int i = 0; i < visibilityPolygon.size() - 1; i++) {
      Polygon p = new Polygon();
      p.addPoint(visibilityPolygon.get(i).x * Config.B2PIX, visibilityPolygon.get(i).y * Config.B2PIX);
      p.addPoint(visibilityPolygon.get(i + 1).x * Config.B2PIX, visibilityPolygon.get(i + 1).y * Config.B2PIX);
      p.addPoint(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX);
      g.fill(p);
    }
    Polygon p = new Polygon();
    p.addPoint(visibilityPolygon.get(0).x * Config.B2PIX, visibilityPolygon.get(0).y * Config.B2PIX);
    p.addPoint(visibilityPolygon.get(visibilityPolygon.size() - 1).x * Config.B2PIX, visibilityPolygon.get(visibilityPolygon.size() - 1).y * Config.B2PIX);
    p.addPoint(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX);
    g.fill(p);

    g.setDrawMode(Graphics.MODE_NORMAL);
    g.popTransform();
  }

  private void testLight(Graphics g) {
    WorldHandler.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
        .ifPresent(position -> {

          Vector2 posOnScreen = position.scl(Config.B2PIX);
          final float lightX = posOnScreen.x;
          final float lightY = posOnScreen.y;

          /*
          to make a light bright use a low alpha value as inner color and a high aplpha end value
           */
          float[] innerColor = {0, 0, 0, .2f};
          float[] outerColor = {1, 1, 1, 1f};

          //multiply alpha with values of other lights
          g.setDrawMode(Graphics.MODE_ADD);
          //lock color channels, only alpha shall be multiplied
          GL11.glColorMask(false, false, false, true);
          //draw a circle (made up of 64 triangles) with gradient from inner to outer color
          radialGradientCircle(lightX, lightY, innerColor, outerColor, 64, lightCircleRadiusPix);
          GL11.glColorMask(true, true, true, true);

          g.setDrawMode(Graphics.MODE_NORMAL);

          /*
          after ALL alpha circles have been drawn
          the screen has to be filled black like with the following options enabled:
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
           */
        });
  }

  private void radialGradientCircle(float posX, float posY, float[] innerColor, float[] outerColor, int slices, float radius) {
    float incr = (float) (2 * Math.PI / slices);

    GL11.glBegin(GL11.GL_TRIANGLE_FAN);
    {
      GL11.glColor4f(innerColor[0], innerColor[1], innerColor[2], innerColor[3]);
      //start position - hub
      GL11.glVertex2f(posX, posY);

      GL11.glColor4f(outerColor[0], outerColor[1], outerColor[2], outerColor[3]);
      for (int i = 0; i < slices; i++) {
        float angle = incr * i;

        float x = (float) Math.cos(angle) * radius;
        float y = (float) Math.sin(angle) * radius;

        //vertices in counter clock wise order
        GL11.glVertex2f(x + posX, y + posY);
      }
      //closing vertice
      GL11.glVertex2f(radius + posX, posY);
    }
    GL11.glEnd();
  }

  /**
   * draws the lines as computed by the calculateVisibilityPolygon function, useful for debugging
   *
   * @param g jwgl Graphics context
   */
  private void debugDrawVisibilityLines(Graphics g, Color color) {
    Color previousColor = g.getColor();
    g.setColor(color);
    for (Vector2 p : visibilityPolygon) {
      g.drawLine(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX, p.x * Config.B2PIX, p.y * Config.B2PIX);
    }
    g.setColor(previousColor);
  }

  private void debugDrawNotVisibleArea(Graphics g) {

    g.pushTransform();
    final float range = 128;

    g.setColor(Color.red);

    for (int i = 0; i < visibilityPolygon.size() - 1; i++) {
      Polygon p = new Polygon();
      p.addPoint(visibilityPolygon.get(i).x * Config.B2PIX, visibilityPolygon.get(i).y * Config.B2PIX);
      p.addPoint(visibilityPolygon.get(i + 1).x * Config.B2PIX, visibilityPolygon.get(i + 1).y * Config.B2PIX);
      p.addPoint(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX);

      g.fill(p);
    }
    Polygon p = new Polygon();
    p.addPoint(visibilityPolygon.get(0).x * Config.B2PIX, visibilityPolygon.get(0).y * Config.B2PIX);
    p.addPoint(visibilityPolygon.get(visibilityPolygon.size() - 1).x * Config.B2PIX, visibilityPolygon.get(visibilityPolygon.size() - 1).y * Config.B2PIX);
    p.addPoint(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX);
    g.fill(p);

    g.popTransform();
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

  private class BodiesInRangeCB implements QueryCallback {
    public final ArrayList<Body> bodiesInRange = new ArrayList<>();

    @Override
    public boolean reportFixture(Fixture fixture) {
      bodiesInRange.add(fixture.getBody());
      return true;
    }
  }

}
