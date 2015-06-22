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
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A point-light (shines in all directions)
 * serves as baseclass to all light sources
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

        BodyDef walls = new BodyDef();
        walls.type = BodyDef.BodyType.KinematicBody;
        walls.position.set(new Vector2()); // top

        FixtureDef f = new FixtureDef();
        f.isSensor = true;
        f.density = 1;
        EdgeShape tltr = new EdgeShape();
        tltr.set(new Vector2(),new Vector2(
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()* Config.PIX2B
                ,0));
        f.shape = tltr;

        topWall = b2World.createBody(walls);
        topWall.createFixture(f);
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
            mPosition = ch.redmonkeyass.zombieInvasion.World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class).get();
            mFixture = ch.redmonkeyass.zombieInvasion.World.getEntityHandler().getDataFrom(getEntityID(), DataType.COLLISION_FIXTURE, Fixture.class).get();
        } catch (NoSuchElementException e) {
        }


        intersectionPoints.clear();
        vertices.clear();
        vertices.ensureCapacity(b2World.getBodyCount() * 5);
        emitToAllFixtures();

    }

    @Override
    public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    /*
    for debugging
     */

        g.setColor(Color.yellow);

        final float b2pix = Config.B2PIX;
        for (Vector2 p : intersectionPoints) {
            g.drawLine(mPosition.x * b2pix, mPosition.y * b2pix, p.x * b2pix, p.y * b2pix);
        }
        for (int i = 0; i < intersectionPoints.size() -1; i++) {
            Polygon points= new Polygon();
            points.addPoint(mPosition.x *b2pix,
                    mPosition.y*b2pix);
            points.addPoint(intersectionPoints.get(i).x*b2pix,
                    intersectionPoints.get(i).y*b2pix);
            points.addPoint(intersectionPoints.get(i+1).x*b2pix,
                    intersectionPoints.get(i+1).y*b2pix);

            g.draw(points);
        }

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

    /**
     * //TODO make more efficient by using AABB query first
     */
    private void emitToAllFixtures() {
        Array<Body> bodies = new Array<>();
        b2World.getBodies(bodies);


        bodies.forEach(b -> {
            b.getFixtureList().forEach(f -> {
                if (f != mFixture) {
                    switch (f.getType()) {
                        case Circle:
                            break;
                        case Edge:
                            EdgeShape e = (EdgeShape)f.getShape();
                            Vector2 tmp1 = new Vector2();
                            e.getVertex1(tmp1);
                            vertices.add(tmp1.cpy());
                            vertices.add(createPointAt(0.001f, tmp1));
                            vertices.add(createPointAt(-0.001f,tmp1));
                            e.getVertex2(tmp1);
                            vertices.add(tmp1.cpy());
                            vertices.add(createPointAt(0.001f,tmp1));
                            vertices.add(createPointAt(-0.001f,tmp1));
                            break;
                        case Polygon:
                            PolygonShape p = (PolygonShape) f.getShape();
                            Vector2 tmp = new Vector2();
                            for (int i = 0; i < p.getVertexCount(); i++) {
                                p.getVertex(i, tmp);
                                tmp = tmp.cpy();//make le copy for not change shiat
                                tmp.add(b.getPosition());
                                vertices.add(tmp);
                                // TODO adjust precision maybe?
                                vertices.add(createPointAt(0.001f, tmp));
                                vertices.add(createPointAt(-0.001f, tmp));
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
            }
        });


    }

    private Vector2 createPointAt(float offsetinRadians, Vector2 point) {
        final float o = offsetinRadians;
        final Vector2 c = mPosition;
        final Vector2 p = point;

        Vector2 line = new Vector2(
                (float) ((p.x - c.x) * Math.cos(o) + (p.y - c.y) * Math.sin(o)) + c.x,
                (float) (-(p.x - c.x) * Math.sin(o) + (p.y - c.y) * Math.cos(o)) + c.y
        );

        return line;
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

            if (mFixture == fixture) { //
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
