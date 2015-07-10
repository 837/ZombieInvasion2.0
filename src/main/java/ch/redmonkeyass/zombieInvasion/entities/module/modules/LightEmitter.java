package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import ch.redmonkeyass.zombieInvasion.*;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    ArrayList<Vector2> nonVisible = new ArrayList<>();
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
        tltr.set(0, 0, ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X() * Config.PIX2B, 0);

        EdgeShape tlbl = new EdgeShape();
        tlbl.set(0, 0, 0, ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y() * Config.PIX2B);

        EdgeShape trbr = new EdgeShape();
        trbr.set(ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X() * Config.PIX2B,
                0,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X() * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y() * Config.PIX2B);
        EdgeShape blbr = new EdgeShape();
        blbr.set(0,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y() * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X() * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y() * Config.PIX2B);
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

    /*add the bottom left/right to the intersectionpoints
    intersectionPoints
        .add(new Vector2(ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X(),
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y()));
    intersectionPoints
        .add(new Vector2(0, ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y()));
  */
        debugDrawVisibilityLines(g, Color.red);
        debugDrawNotVisibleArea(g);
    }

    /**
     * requires intersectionpoints to be sorted by angle from the body's perspective!!!
     * colors non-visible areas black
     *
     * @param g jwgl Graphics context
     */
    private void debugDrawNotVisibleArea(Graphics g) {

        g.pushTransform();
        g.setDrawMode(Graphics.MODE_ALPHA_BLEND);


        sortByAngleFromBodysPerspecive();
        Rectangle screen = new Rectangle(
                ch.redmonkeyass.zombieInvasion.World.getCamera().getPosition().x,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getPosition().y,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X(),
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y());
        g.setColor(Color.black);
        //g.fill(screen);

        g.setColor(Color.red);


        for (int i = 0; i < intersectionPoints.size()-1; i++) {
            Polygon p = new Polygon();
            p.addPoint(intersectionPoints.get(i).x*Config.B2PIX,intersectionPoints.get(i).y*Config.B2PIX);
            p.addPoint(intersectionPoints.get(i+1).x*Config.B2PIX,intersectionPoints.get(i+1).y*Config.B2PIX);
            p.addPoint(mPosition.x*Config.B2PIX,mPosition.y*Config.B2PIX);
            g.fill(p);
        }
        Polygon p = new Polygon();
        p.addPoint(intersectionPoints.get(0).x*Config.B2PIX,intersectionPoints.get(0).y*Config.B2PIX);
        p.addPoint(intersectionPoints.get(intersectionPoints.size()-1).x * Config.B2PIX, intersectionPoints.get(intersectionPoints.size()-1).y * Config.B2PIX);
        p.addPoint(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX);
        g.fill(p);

        g.setDrawMode(Graphics.MODE_NORMAL);
        g.popTransform();
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
            g.drawLine(mPosition.x * Config.B2PIX, mPosition.y * Config.B2PIX, p.x * Config.B2PIX, p.y * Config.B2PIX);
        }
        g.setColor(previousColor);
    }

    private void sortByAngleFromBodysPerspecive() {
        //sort by angle from the emitting body
        // uses this formula to account for 360 degree view: (x > 0 ? x : (2*PI + x))
        intersectionPoints.sort((v0, v1) -> Double.compare(
                Math.atan2(v1.y - mPosition.y,
                        v1.x - mPosition.x),
                Math.atan2(v0.y - mPosition.y,
                        v0.x - mPosition.x)));
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
        Array<Body> bodies = new Array<>();

        BodiesInRangeCB cb = new BodiesInRangeCB();
        b2World.QueryAABB(cb,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getPosition().x * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getPosition().y * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X() * Config.PIX2B,
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y() * Config.PIX2B
        );


        cb.bodiesInRange.remove(mFixture.getBody());
        // TODO adjust precision maybe?
        final float maxDistVisibleOnScreen =
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()
                        * Config.PIX2B;

        cb.bodiesInRange.forEach(b -> {
            Vector2 bWorldPos = new Vector2();
            b.getFixtureList().forEach(f -> {

                if (f.getBody() != mFixture.getBody()) {
                    switch (f.getType()) {
                        case Circle:
                            break;
                        case Edge:

                EdgeShape e = (EdgeShape)f.getShape();
                            Vector2 tmp1 = new Vector2();
                            e.getVertex1(tmp1);
                            Vector2 worldpoint1 = b.getWorldPoint(tmp1).cpy();
                            vertices.add(worldpoint1.cpy());
                            vertices.add(createPointAt(0.000001f, worldpoint1).cpy().sub(mPosition).scl(maxDistVisibleOnScreen)
                                    .add(mPosition));
                            vertices.add(createPointAt(-0.000001f, worldpoint1).cpy().sub(mPosition)
                                    .scl(maxDistVisibleOnScreen).add(mPosition));
                            e.getVertex2(tmp1);
                            worldpoint1 = b.getWorldPoint(tmp1).cpy();
                            vertices.add(worldpoint1.cpy());
                            vertices.add(createPointAt(0.000001f, worldpoint1).cpy().sub(mPosition).scl(maxDistVisibleOnScreen)
                                    .add(mPosition));
                            vertices.add(createPointAt(-0.000001f, worldpoint1).cpy().sub(mPosition)
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
                                vertices.add(createPointAt(0.000001f, worldpoint).cpy().sub(mPosition).scl(maxDistVisibleOnScreen)
                                        .add(mPosition));
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
                nonVisible.add(b.closestIntersectionPoint.cpy());
            } else {
                //intersectionPoints.add(v);
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
    private class BodiesInRangeCB implements QueryCallback{
        public final ArrayList<Body> bodiesInRange = new ArrayList<>();
        @Override
        public boolean reportFixture(Fixture fixture) {
            bodiesInRange.add(fixture.getBody());
            return true;
        }
    }

}
