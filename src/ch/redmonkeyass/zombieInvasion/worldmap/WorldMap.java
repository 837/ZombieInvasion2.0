package ch.redmonkeyass.zombieInvasion.worldmap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.WorldMap.FieldType;

public class WorldMap implements RenderableModul {
  enum FieldType {
    WALL, NOT_WALL
  }

  Field[][] map = new Field[Config.WORLDMAP_WIDTH][Config.WORLDMAP_HEIGHT];

  final int FIELD_SIZE = 1;

  public WorldMap() {
//    for (int x = 0; x < map.length; x++) {
//      for (int y = 0; y < map[x].length; y++) {
//        Body b;
//        if (new Random().nextInt(5) == 0) {
//          b = createBody(FieldType.WALL, x, y);
//          map[x][y] = new Field(FIELD_SIZE, x, y, b, FieldType.WALL);
//        } else {
//          b = createBody(FieldType.NOT_WALL, x, y);
//          map[x][y] = new Field(FIELD_SIZE, x, y, b, FieldType.NOT_WALL);
//        }
//      }
//    }
  }


  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
//    for (int x = 0; x < map.length; x++) {
//      for (int y = 0; y < map[x].length; y++) {
//        if (map[x][y].getType().equals(FieldType.WALL)) {
//          g.setColor(Color.black);
//        } else {
//          g.setColor(Color.green);
//        }
//
//        g.fillRect((x - FIELD_SIZE / 2) * Config.B2PIX, (y - FIELD_SIZE / 2) * Config.B2PIX,
//            FIELD_SIZE * Config.B2PIX, FIELD_SIZE * Config.B2PIX);
//      }
//    }
  }


  private Body createBody(FieldType type, int x, int y) {
    switch (type) {
      case NOT_WALL:
        // First we create a body definition
        BodyDef bodyDef1 = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it
        // to
        // StaticBody
        bodyDef1.type = BodyType.StaticBody;
        // Set our body's starting position in object space (meters)
        bodyDef1.position.set(x, y);

        // Create our body in the world using our body definition
        Body body1 = World.getB2World().createBody(bodyDef1);

        return body1;
      case WALL:
        // First we create a body definition
        BodyDef bodyDef2 = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it
        // to
        // StaticBody
        bodyDef2.type = BodyType.StaticBody;
        // Set our body's starting position in object space (meters)
        bodyDef2.position.set(x, y);

        // Create our body in the world using our body definition
        Body body2 = World.getB2World().createBody(bodyDef2);

        // Create a circle shape and set its radius to 6
        // PolygonShape shape = new PolygonShape();
        // shape.setAsBox(32, 32, new Vector2(16, 16), 0);
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(FIELD_SIZE / 2, FIELD_SIZE / 2);

        FixtureDef fixtureDef = new FixtureDef();
        // Create a fixture definition to apply our shape to
        fixtureDef.shape = shape2;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.0f;

        // Create our fixture and attach it to the body
        body2.createFixture(fixtureDef);

        return body2;
      default:
        return null;
    }

  }

}


class Field {
  private final Body body;
  private final FieldType type;

  public Field(int width, int x, int y, Body body, FieldType type) {
    this.body = body;
    this.type = type;
  }

  public Body getBody() {
    return body;
  }

  public FieldType getType() {
    return type;
  }
}
