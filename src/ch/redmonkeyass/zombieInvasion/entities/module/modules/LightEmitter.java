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
import org.lwjgl.util.vector.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
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
       // walls.position.set(new Vector2()); // top
        walls.position.set(ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()*0.5f*Config.PIX2B,
                0);

        FixtureDef f = new FixtureDef();
        f.isSensor = true;
        f.density = 1;
        /*EdgeShape tltr = new EdgeShape();
        tltr.set(new Vector2(),new Vector2(
                ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()* Config.PIX2B
                ,0));*/
        PolygonShape tltr = new PolygonShape();
        tltr.setAsBox(ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()*0.5f*Config.PIX2B,2);
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
  //          g.drawLine(mPosition.x * b2pix, mPosition.y * b2pix, p.x * b2pix, p.y * b2pix);
        }
        intersectionPoints.add(new Vector2(ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X(), ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y()));
        intersectionPoints.add(new Vector2(0, ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_Y()));
        intersectionPoints.sort((v0,v1) -> {
            return Double.compare(
                    Math.atan2(mFixture.getBody().getLocalPoint(v1.cpy()).y,
                            mFixture.getBody().getLocalPoint(v1.cpy()).x),
                    Math.atan2(mFixture.getBody().getLocalPoint(v0.cpy()).y,
                            mFixture.getBody().getLocalPoint(v0.cpy()).x));
        });

        for (int i = 0; i < intersectionPoints.size() -1; i++) {
            Polygon points= new Polygon();
            points.addPoint(mPosition.x *b2pix,
                    mPosition.y*b2pix);
            points.addPoint(intersectionPoints.get(i).x*b2pix,
                    intersectionPoints.get(i).y*b2pix);
            points.addPoint(intersectionPoints.get(i+1).x*b2pix,
                    intersectionPoints.get(i+1).y*b2pix);

            GradientFill fill = new GradientFill(
                    (intersectionPoints.get(i).x + (intersectionPoints.get(i+1).x-intersectionPoints.get(i).x)/2)*b2pix
                    ,(intersectionPoints.get(i).y + (intersectionPoints.get(i+1).y-intersectionPoints.get(i).y)/2)*b2pix
                    ,Color.transparent
                    ,mPosition.x*b2pix,mPosition.y*b2pix, Color.white);
            g.fill(points);
        }



        Polygon p = new Polygon();
        intersectionPoints.forEach( ip ->{
                    p.addPoint(ip.x*b2pix,ip.y*b2pix);
        });
        GradientFill filler = new GradientFill(mPosition.x*b2pix,mPosition.y*b2pix,Color.white,p.getCenterX(),p.getCenterY(),Color.transparent);
        //g.fill(p,filler);

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
        bodies.removeValue(mFixture.getBody(),true);

        bodies.forEach(b -> {
            Vector2 bWorldPos = new Vector2();
            b.getFixtureList().forEach(f -> {
                if (f.getBody() != mFixture.getBody()) {
                    switch (f.getType()) {
                        case Circle:
                            break;
                        case Edge:
/*                            EdgeShape e = (EdgeShape)f.getShape();
                            Vector2 tmp1 = new Vector2();
                            e.getVertex1(tmp1);
                            vertices.add(tmp1.cpy());
                            vertices.add(createPointAt(0.001f, tmp1));
                            vertices.add(createPointAt(-0.001f,tmp1));
                            e.getVertex2(tmp1);
                            vertices.add(tmp1.cpy());
                            vertices.add(createPointAt(0.001f,tmp1));
                            vertices.add(createPointAt(-0.001f,tmp1));*/
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
                                float camwidth = ch.redmonkeyass.zombieInvasion.World.getCamera().getViewport_size_X()*Config.PIX2B;
                               vertices.add(createPointAt(0.000001f,worldpoint).cpy().sub(mPosition).scl(camwidth).add(mPosition));
                               vertices.add(createPointAt(-0.000001f, worldpoint).cpy().sub(mPosition).scl(camwidth).add(mPosition));
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
            b2World.rayCast(b, mPosition,v);
            if (b.hasIntersection) {
                intersectionPoints.add(b.closestIntersectionPoint.cpy());
            }else {
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
                (float) (-(p.x - center.x) * Math.sin(o) + (p.y - center.y) * Math.cos(o)) + center.y
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
                hasIntersection = true;
                closestFixture = fixture;
                closestIntersectionPoint = intersectionPoint;
                return fraction;
        }
    }


}
