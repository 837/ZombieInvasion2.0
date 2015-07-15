package ch.redmonkeyass.zombieInvasion.worldmap;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
  private float nodeSizeInMeter = 0;
  private float mapWidthInMeter = 0;
  private float mapHeightInMeter = 0;

  private Logger logger = LogManager.getLogger(WorldMap.class);

  public float getNodeSizeInMeter() {
    return nodeSizeInMeter;
  }

  public void setNodeSizeInMeter(float nodeSizeInMeter) {
    this.nodeSizeInMeter = nodeSizeInMeter;
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

  public float getMapHeightInMeter() {
    return mapHeightInMeter;
  }

  public float getMapWidthInMeter() {
    return mapWidthInMeter;
  }

  public void setMapHeightInMeter(float mapHeightInMeter) {
    this.mapHeightInMeter = mapHeightInMeter;
  }

  public void setMapWidthInMeter(float mapWidthInMeter) {
    this.mapWidthInMeter = mapWidthInMeter;
  }

  private Node[][] map = null;

  public Node[][] getMap() {
    return map;
  }

  public WorldMap(String tmxFileName) {
    try {
      tileMap = new TiledMap("res/tiledMap/" + tmxFileName + ".tmx");
      int tileHeigth = tileMap.getTileHeight();
      int tileWidth = tileMap.getTileWidth();
      map = new Node[tileMap.getWidth()][tileMap.getHeight()];

      setMapHeightInMeter((int) ((tileMap.getHeight() * tileHeigth) / Config.B2PIX));
      setMapWidthInMeter((int) ((tileMap.getWidth() * tileWidth) / Config.B2PIX));


      setNodeSizeInMeter((int) (tileWidth / Config.B2PIX));

      for (int x = 0; x < map.length; x++) {
        for (int y = 0; y < map[x].length; y++) {
          switch (tileMap.getTileId(x, y, 0)) {
            case 0:
              break;
            default:
              map[x][y] = new Node(x, y,
                  createBody(FieldType.NOT_WALL, (int) (x * getNodeSizeInMeter()),
                      (int) (y * getNodeSizeInMeter()), nodeSizeInMeter),
                  FieldType.NOT_WALL, tileWidth);
              break;
          }
          switch (tileMap.getTileId(x, y, 1)) {
            case 0:
              break;
            default:
              map[x][y] = new Node(x, y,
                  createBody(FieldType.WALL, (int) (x * getNodeSizeInMeter()),
                      (int) (y * getNodeSizeInMeter()), nodeSizeInMeter),
                  FieldType.WALL, tileWidth);
              break;
          }
        }
      }

      // Removes bodies of nodes, where the node has no connection to a walkable node
      for (int x = 0; x < map.length; x++) {
        for (int y = 0; y < map[x].length; y++) {
          if (map[x][y].isWalkable())
            continue;

          ArrayList<Node> neighbours = new ArrayList<>();

          Node x1 = returnNodeOrNull(x - 1, y);
          if (x1 != null)
            neighbours.add(x1);

          Node x2 = returnNodeOrNull(x + 1, y);
          if (x2 != null)
            neighbours.add(x2);

          Node y1 = returnNodeOrNull(x, y - 1);
          if (y1 != null)
            neighbours.add(y1);

          Node y2 = returnNodeOrNull(x, y + 1);
          if (y2 != null)
            neighbours.add(y2);

          for (int i = 0; i < neighbours.size(); i++) {
            if (neighbours.get(i).isWalkable()) {
              break;
            } else if (i == neighbours.size() - 1) {
              map[x][y].prepareForRemoval();
              logger.trace("Removed Body of Node: [" + x + ", " + y + "]");
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("Error while creating WorldMap.", e);
    }
  }

  /**
   * kinda hacky, so that I don't have to check if x,y is in the array
   * 
   * @param x
   * @param y
   * @return node or null
   */
  private Node returnNodeOrNull(int x, int y) {
    Node n = null;
    try {
      n = map[x][y];
    } catch (Exception e) {
      // XXX kinda hacky, so that I don't have to check if x,y is in the array
    }
    return n;
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
