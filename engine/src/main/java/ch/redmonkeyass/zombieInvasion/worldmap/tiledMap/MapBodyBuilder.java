package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;

/**
 * creates box2d bodies and fixtures from all Objectlayer in the tmx map
 * 
 * @author Matthias
 *
 */
public class MapBodyBuilder {

  public static Array<Body> buildShapes(TiledMap map) {
    MapObjects objects = new MapObjects();

    int objectGroupCount = map.getObjectGroupCount();
    for (int gi = 0; gi < objectGroupCount; gi++) {
      int objectCount = map.getObjectCount(gi);
      for (int oi = 0; oi < objectCount; oi++) {
        int x = map.getObjectX(gi, oi);
        int y = map.getObjectY(gi, oi);
        int width = map.getObjectWidth(gi, oi);
        int height = map.getObjectHeight(gi, oi);
        if (map.getObjectType(gi, oi).equals("rectangle")) {
          objects.add(new RectangleMapObject((x + width / 2) * Config.PIX2B,
              (y + height / 2) * Config.PIX2B, width * Config.PIX2B, height * Config.PIX2B));
        } else if (map.getObjectType(gi, oi).equals("circle") && width == height) {
          objects.add(new CircleMapObject((x + width / 2) * Config.PIX2B,
              (y + height / 2) * Config.PIX2B, width * Config.PIX2B));
        }
      }
    }
    Array<Body> bodies = new Array<Body>();

    for (MapObject object : objects) {

      if (object instanceof TextureMapObject) {
        continue;
      }

      Shape shape;

      if (object instanceof RectangleMapObject) {
        shape = getRectangle((RectangleMapObject) object);
        // } else if (object instanceof PolygonMapObject) {
        // shape = getPolygon((PolygonMapObject) object);
        // } else if (object instanceof PolylineMapObject) {
        // shape = getPolyline((PolylineMapObject) object);
      } else if (object instanceof CircleMapObject) {
        shape = getCircle((CircleMapObject) object);
      } else {
        continue;
      }

      BodyDef bd = new BodyDef();
      bd.type = BodyType.StaticBody;
      Body body = WorldHandler.getB2World().createBody(bd);
      body.createFixture(shape, 1);

      bodies.add(body);

      shape.dispose();
    }
    return bodies;
  }

  private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
    Rectangle rectangle = rectangleObject.getRectangle();
    PolygonShape polygon = new PolygonShape();
    Vector2 pos = new Vector2(rectangle.x, rectangle.y);
    polygon.setAsBox(rectangle.width * 0.5f, rectangle.height * 0.5f, pos, 0.0f);
    return polygon;
  }

  private static CircleShape getCircle(CircleMapObject circleObject) {
    Circle circle = circleObject.getCircle();
    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(circle.radius);
    circleShape.setPosition(new Vector2(circle.x, circle.y));
    return circleShape;
  }



  // private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
  // PolygonShape polygon = new PolygonShape();
  // float[] vertices = polygonObject.getPolygon().getTransformedVertices();
  //
  // float[] worldVertices = new float[vertices.length];
  //
  // for (int i = 0; i < vertices.length; ++i) {
  // System.out.println(vertices[i]);
  // worldVertices[i] = vertices[i] / ppt;
  // }
  //
  // polygon.set(worldVertices);
  // return polygon;
  // }
  //
  // private static ChainShape getPolyline(PolylineMapObject polylineObject) {
  // float[] vertices = polylineObject.getPolyline().getTransformedVertices();
  // Vector2[] worldVertices = new Vector2[vertices.length / 2];
  //
  // for (int i = 0; i < vertices.length / 2; ++i) {
  // worldVertices[i] = new Vector2();
  // worldVertices[i].x = vertices[i * 2] / ppt;
  // worldVertices[i].y = vertices[i * 2 + 1] / ppt;
  // }
  //
  // ChainShape chain = new ChainShape();
  // chain.createChain(worldVertices);
  // return chain;
  // }
}
