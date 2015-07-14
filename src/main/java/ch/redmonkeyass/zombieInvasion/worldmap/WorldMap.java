package ch.redmonkeyass.zombieInvasion.worldmap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;

public class WorldMap implements RenderableModul {
  private TiledMap tileMap;
  private int NODE_SIZE_BOX2D = 1;

  public int getNODE_SIZE_BOX2D() {
    return NODE_SIZE_BOX2D;
  }

  enum FieldType {
    WALL(false), NOT_WALL;

    private FieldType() {}

    private FieldType(boolean isWalkable) {
      this.isWalkable = isWalkable;
    }

    private boolean isWalkable = true;

    public boolean isWalkable() {
      return isWalkable;
    }
  }

  private Node[][] map = new Node[Config.WORLDMAP_WIDTH][Config.WORLDMAP_HEIGHT];

  public Node[][] getMap() {
    return map;
  }

  public WorldMap() {
    try {
      tileMap = new TiledMap("res/tiledMap/64pxMapTest.tmx");
      int tileSize = tileMap.getTileHeight();
      NODE_SIZE_BOX2D = (int) (tileSize / Config.B2PIX);

      for (int x = 0; x < map.length; x++) {
        for (int y = 0; y < map[x].length; y++) {
          switch (tileMap.getTileId(x, y, 0)) {
            case 0:
              break;
            default:
              map[x][y] =
                  new Node(x, y, createBody(FieldType.NOT_WALL, x , y , NODE_SIZE_BOX2D),
                      FieldType.NOT_WALL, tileSize);
              break;
          }
          switch (tileMap.getTileId(x, y, 1)) {
            case 0:
              break;
            default:
              map[x][y] = new Node(x, y, createBody(FieldType.WALL, x , y , NODE_SIZE_BOX2D),
                  FieldType.WALL, tileSize);
              break;
          }
        }
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    tileMap.render(0, 0);
  }


  private Body createBody(FieldType type, int x, int y, final float NODE_SIZE_BOX2D) {
    switch (type) {
      case NOT_WALL:
        // First we create a body definition
        // BodyDef bodyDef1 = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it
        // to
        // StaticBody
        // bodyDef1.type = BodyType.StaticBody;
        // Set our body's starting position in object space (meters)
        // bodyDef1.position.set(x - (NODE_SIZE_BOX2D / 2), y - (NODE_SIZE_BOX2D / 2));

        // Create our body in the WorldHandler using our body definition
        // Body body1 = WorldHandler.getB2World().createBody(bodyDef1);

        return null;
      case WALL:
        // First we create a body definition
        BodyDef bodyDef2 = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it
        // to
        // StaticBody
        bodyDef2.type = BodyType.StaticBody;
        // Set our body's starting position in object space (meters)
        bodyDef2.position.set(x + (NODE_SIZE_BOX2D / 2), y + (NODE_SIZE_BOX2D / 2));

        // Create our body in the WorldHandler using our body definition
        Body body2 = WorldHandler.getB2World().createBody(bodyDef2);

        // Create a circle shape and set its radius to 6
        // PolygonShape shape = new PolygonShape();
        // shape.setAsBox(32, 32, new Vector2(16, 16), 0);
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(NODE_SIZE_BOX2D / 2, NODE_SIZE_BOX2D / 2);
        // CircleShape shape2 = new CircleShape();
        // shape2.setRadius(FIELD_SIZE / 2);

        FixtureDef fixtureDef = new FixtureDef();
        // Create a fixture definition to apply our shape to
        fixtureDef.shape = shape2;
        fixtureDef.density = 1.0f;

        // Create our fixture and attach it to the body
        body2.createFixture(fixtureDef);

        return body2;
      default:
        return null;
    }

  }
}
