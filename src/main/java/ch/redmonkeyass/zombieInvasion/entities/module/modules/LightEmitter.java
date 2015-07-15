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
import org.newdawn.slick.geom.Rectangle;
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
 * A point-light (shines in all directions) serves as baseclass to all light sources
 * <p>
 * Created by P on 18.06.2015.
 */
public class LightEmitter extends Module implements UpdatableModul, RenderableModul {
    World b2World = WorldHandler.getB2World();
    ArrayList<Vector2> line = new ArrayList<>();
    ArrayList<Vector2> visibilityPolygon = new ArrayList<>();
    ArrayList<Vector2> vertices = new ArrayList<>();
    Vector2 mPosition;
    Fixture mFixture;
    Body walls;

    public LightEmitter(String entityID) {
        super(entityID);
/*
        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.KinematicBody;
        wallsDef.position.set(new Vector2()); // top
        FixtureDef f = new FixtureDef();
        f.isSensor = true;
        f.density = 1;

     //create 4 boundry walls

        EdgeShape tltr = new EdgeShape();
        tltr.set(0, 0, ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_X() * Config.PIX2B, 0);

        EdgeShape tlbl = new EdgeShape();
        tlbl.set(0, 0, 0, ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y() * Config.PIX2B);

        EdgeShape trbr = new EdgeShape();
        trbr.set(ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_X() * Config.PIX2B,
                0,
                ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_X() * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y() * Config.PIX2B);
        EdgeShape blbr = new EdgeShape();
        blbr.set(0,
                ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y() * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_X() * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y() * Config.PIX2B);
        // :P
        walls = b2World.createBody(wallsDef);
        f.shape = tltr;
        walls.createFixture(f);
        f.shape = tlbl;
        walls.createFixture(f);
        f.shape = trbr;
        walls.createFixture(f);
        f.shape = blbr;
        walls.createFixture(f);
        */
    }

    @Override
    public void prepareModuleForRemoval() {
        super.prepareModuleForRemoval();
        b2World.destroyBody(walls);
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


        visibilityPolygon.clear();
        vertices.clear();
        vertices.ensureCapacity(b2World.getBodyCount() * 5);
        calculateVisibilityPolygon();

    }

    /*
   * for debugging
   */
    @Override
    public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {

    /*add the bottom left/right to the intersectionpoints
    visibilityPolygon
        .add(new Vector2(ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_X(),
                ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y()));
    visibilityPolygon
        .add(new Vector2(0, ch.redmonkeyass.zombieInvasion.WorldHandler.getCamera().getViewport_size_Y()));
  */
        debugDrawVisibilityLines(g, Color.red);
        //debugDrawNotVisibleArea(g);
      //  testLight(g);
    }

    /**
     * requires intersectionpoints to be sorted by angle from the body's perspective!!!
     * colors non-visible areas black
     *
     * @param g jwgl Graphics context
     */
    private void debugDrawNotVisibleArea(Graphics g) {

      Graphics gTemp = new Graphics();

      g.pushTransform();

        float range = 128;
        sortVisibilityPolygonByAngleFromBodysPerspective();
        Rectangle screen = new Rectangle(
                WorldHandler.getCamera().getPosition().x,
                WorldHandler.getCamera().getPosition().y,
                WorldHandler.getCamera().getViewport_size_X(),
                WorldHandler.getCamera().getViewport_size_Y());
        g.setColor(Color.black);
        //g.fill(screen);

        g.setColor(Color.red);


        for (int i = 0; i < visibilityPolygon.size() - 1; i++) {
            Polygon p = new Polygon();
            p.addPoint(visibilityPolygon.get(i).x * Config.B2PIX, visibilityPolygon.get(i).y * Config.B2PIX);
            p.addPoint(visibilityPolygon.get(i + 1).x * Config.B2PIX, visibilityPolygon.get(i + 1).y * Config.B2PIX);
            p.addPoint(mPosition.x*Config.B2PIX,mPosition.y*Config.B2PIX);

            g.fill(p);
        }
        Polygon p = new Polygon();
        p.addPoint(visibilityPolygon.get(0).x * Config.B2PIX, visibilityPolygon.get(0).y * Config.B2PIX);
        p.addPoint(visibilityPolygon.get(visibilityPolygon.size() - 1).x * Config.B2PIX, visibilityPolygon.get(visibilityPolygon.size() - 1).y * Config.B2PIX);
        p.addPoint(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX);
        g.fill(p);

        g.popTransform();
    }

    private void testLight(Graphics g) {

        Graphics tmpG = new Graphics(WorldHandler.getCamera().getViewport_size_X(),
                WorldHandler.getCamera().getViewport_size_Y());

      Vector2 posOnScreen = mPosition.cpy().scl(Config.B2PIX);
        final float lightX = posOnScreen.x;
        final float lightY = posOnScreen.y;


        //g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
        byte[] innerColor = {(byte) 255, (byte) 255, (byte) 255};
        byte[] outerColor = {(byte) 255, (byte) 255, (byte) 255};

      g.setDrawMode(Graphics.MODE_ALPHA_MAP);


//        radialGradientCircle(lightX, lightY, innerColor, outerColor, 64, 300);
//        radialGradientCircle(lightX + 300, lightY + 300, innerColor, outerColor, 64, 300);
      GL11.glColorMask(false, false, false, true);
      //g.drawImage(Images.CIRCULAR_LIGHT.get(),lightX,lightY);
      GL11.glColorMask(true, true, true, true);

      GL11.glBegin(GL11.GL_QUADS);
      GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
      GL11.glColor3f(0, 0, 0);
      GL11.glVertex2f(WorldHandler.getCamera().getPosition().x, WorldHandler.getCamera().getPosition().y);
      GL11.glVertex2f(WorldHandler.getCamera().getPosition().x + WorldHandler.getCamera().getViewport_size_X(), WorldHandler.getCamera().getPosition().y);
      GL11.glVertex2f(WorldHandler.getCamera().getPosition().x+WorldHandler.getCamera().getViewport_size_X(), WorldHandler.getCamera().getPosition().y+WorldHandler.getCamera().getViewport_size_Y());
      GL11.glVertex2f(WorldHandler.getCamera().getPosition().x, WorldHandler.getCamera().getPosition().y + WorldHandler.getCamera().getViewport_size_Y());
      GL11.glEnd();

        g.setDrawMode(Graphics.MODE_NORMAL);

    }

    private void radialGradientCircle(float posX, float posY, byte[] innerColor, byte[] outerColor, int slices, float radius) {
        float incr = (float) (2 * Math.PI / slices);
      /*

draw whatever you want in the background
create texture with alpha gradient
lock color channels with glColorMask(GL_FALSE,GL_FALSE,GL_FALSE,GL_TRUE);
draw alpha gradient over background
unmask color channels with glColorMask(GL_TRUE,GL_TRUE,GL_TRUE,GL_TRUE);
draw 'black texture' with following blend options: glBlend(GL_DST_ALPHA, GL_ONE_MINUS_DST_ALPHA)
       */



      GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        //gl.glColor3fv(innerColor);
        GL11.glColor3ub(innerColor[0], innerColor[1], innerColor[2]);
        //start position - hub
        GL11.glVertex2f(posX, posY);

        GL11.glColor3ub(outerColor[0], outerColor[1], outerColor[2]);

        for (int i = 0; i < slices; i++) {
            float angle = incr * i;

            float x = (float) Math.cos(angle) * radius;
            float y = (float) Math.sin(angle) * radius;

            //vertices in counter clock wise order
            GL11.glVertex2f(x + posX, y + posY);
        }

        //closing vertice
        GL11.glVertex2f(radius + posX, posY);

        GL11.glEnd();


    }

    /**
     * draws the lines as conmputed by the emitToall function, useful for debugging
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

    private void sortVisibilityPolygonByAngleFromBodysPerspective() {
        //sort by angle from the emitting body
        // uses this formula to account for 360 degree view: (x > 0 ? x : (2*PI + x))
      visibilityPolygon.sort((v0, v1) -> Double.compare(
          Math.atan2(v1.y - mPosition.y,
              v1.x - mPosition.x),
          Math.atan2(v0.y - mPosition.y,
              v0.x - mPosition.x)));
    }

    /**
     * //TODO make more efficient by using AABB query first
     */
    private void calculateVisibilityPolygon() {
        Array<Body> bodies = new Array<>();
        b2World.getBodies(bodies);

        final float c = WorldHandler.getCamera().getViewport_size_X() * Config.PIX2B /4;

        BodiesInRangeCB cb = new BodiesInRangeCB();
        b2World.QueryAABB(cb,
                WorldHandler.getCamera().getPosition().x * Config.PIX2B - c,
                WorldHandler.getCamera().getPosition().y * Config.PIX2B - c,
                WorldHandler.getCamera().getPosition().x * Config.PIX2B + WorldHandler.getCamera().getViewport_size_X() * Config.PIX2B + c,
                WorldHandler.getCamera().getPosition().y * Config.PIX2B + WorldHandler.getCamera().getViewport_size_Y() * Config.PIX2B + c
        );



        cb.bodiesInRange.remove(mFixture.getBody());
        bodies.removeValue(mFixture.getBody(), true);
        // TODO adjust precision maybe?
        final float maxDistVisibleOnScreen =
                WorldHandler.getCamera().getViewport_size_X()
                        * Config.PIX2B;

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

    private Vector2 createVectorRotatedAroundCenter(float offsetinRadians, Vector2 point) {
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
    private class BodiesInRangeCB implements QueryCallback{
        public final ArrayList<Body> bodiesInRange = new ArrayList<>();
        @Override
        public boolean reportFixture(Fixture fixture) {
            bodiesInRange.add(fixture.getBody());
            return true;
        }
    }

}
