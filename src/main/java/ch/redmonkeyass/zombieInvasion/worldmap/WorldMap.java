package ch.redmonkeyass.zombieInvasion.worldmap;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.MapBodyBuilder;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.TiledMap;

public class WorldMap implements RenderableModul {
  private TiledMap tileMap;
  private float nodeSizeInMeter = 0;
  private float mapWidthInMeter = 0;
  private float mapHeightInMeter = 0;

  Array<Body> obstacles = new Array<>();

  private Logger logger = LogManager.getLogger(WorldMap.class);

  public float getNodeSizeInMeter() {
    return nodeSizeInMeter;
  }

  public void setNodeSizeInMeter(float nodeSizeInMeter) {
    this.nodeSizeInMeter = nodeSizeInMeter;
  }

  enum FieldType {
    WALL(false), NOT_WALL(true), DYNAMIC_OBSTACLE(true);

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
      // loads the tiledMap
      tileMap = loadTiledMap(tmxFileName);

      // calculates tileWidth/tileHeight/mapSize/nodeSizeInMeter
      int tileHeigth = (int) tileMap.getTileHeight();
      int tileWidth = (int) tileMap.getTileWidth();
      int mapHeight = (int) tileMap.getHeight();
      int mapWidth = (int) tileMap.getWidth();
      setMapHeightInMeter((int) ((mapHeight * tileHeigth) / Config.B2PIX));
      setMapWidthInMeter((int) ((mapWidth * tileWidth) / Config.B2PIX));
      setNodeSizeInMeter((int) (tileWidth / Config.B2PIX));

      // create a Node[][]
      map = new Node[mapWidth][mapHeight];

      // create the Nodes and create bodies if the second layer has elements
      createNodeMapAndDynamicObstacleBodies(map, tileWidth, tileMap);

      // remove inaccessible node bodies
      // removeInaccessibleBodiesFromSecondLayer(map);

      obstacles.addAll(MapBodyBuilder.buildShapes(tileMap));

    } catch (Exception e) {
      logger.error("Error while creating WorldMap.", e);
    }
  }


  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    tileMap.render(0, 0);
    // for (int x = 0; x < map.length; x++) {
    // for (int y = 0; y < map[x].length; y++) {
    // if (map[x][y].isWalkable())
    // continue;
    // g.setColor(Color.darkGray);
    // g.fillRect(x * (Config.B2PIX * 2), y * (Config.B2PIX * 2), 64, 64);
    // g.setColor(Color.black);
    // g.drawRect(x * (Config.B2PIX * 2), y * (Config.B2PIX * 2), 64, 64);
    // g.drawString(map[x][y].getRegionGroup() + "", x * (Config.B2PIX * 2) + 32,
    // y * (Config.B2PIX * 2) + 32);
    // g.setColor(Color.green);
    // g.fillOval(map[x][y].getCornerInMeter(CornerType.TOP_LEFT).x * (Config.B2PIX * 2),
    // map[x][y].getCornerInMeter(CornerType.TOP_LEFT).y * (Config.B2PIX * 2), 4, 4);
    // g.setColor(Color.blue);
    // g.fillOval(map[x][y].getCornerInMeter(CornerType.TOP_RIGHT).x * (Config.B2PIX * 2),
    // map[x][y].getCornerInMeter(CornerType.TOP_RIGHT).y * (Config.B2PIX * 2), 4, 4);
    // g.setColor(Color.magenta);
    // g.fillOval(map[x][y].getCornerInMeter(CornerType.BOTTOM_LEFT).x * (Config.B2PIX * 2),
    // map[x][y].getCornerInMeter(CornerType.BOTTOM_LEFT).y * (Config.B2PIX * 2), 4, 4);
    // g.setColor(Color.red);
    // g.fillOval(map[x][y].getCornerInMeter(CornerType.BOTTOM_RIGHT).x * (Config.B2PIX * 2),
    // map[x][y].getCornerInMeter(CornerType.BOTTOM_RIGHT).y * (Config.B2PIX * 2), 4, 4);
    // }
    // }
  }

  /**
   * Destroys all WALL bodies
   */
  public void destroyAllObstacles() {
    obstacles.forEach(o -> WorldHandler.getB2World().destroyBody(o));
  }

  /**
   * loads the tiledmap and fixes a line in the tmx file
   * 
   * @param tmxFileName
   * @return TiledMap
   * @throws Exception
   */
  private TiledMap loadTiledMap(String tmxFileName) throws Exception {
    // Loads the mapFile into a string, adds width/height property to objectLayer, saves the file
    // again.
    String ref = new File("res/tiledMap/" + tmxFileName + ".tmx").getAbsolutePath();
    String loadFileContentForFix = new String(Files.readAllBytes(Paths.get(ref)));
    loadFileContentForFix =
        loadFileContentForFix.replaceAll("<objectgroup width=\"1\" height=\"1\"", "<objectgroup");
    loadFileContentForFix =
        loadFileContentForFix.replaceAll("<objectgroup", "<objectgroup width=\"1\" height=\"1\"");
    Path path = Paths.get(ref);
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      writer.write(loadFileContentForFix);
    }
    return new TiledMap(ref);
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

  private void createNodeMapAndDynamicObstacleBodies(Node[][] map, int tileWidth,
      TiledMap tileMap) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        switch (tileMap.getTileId(x, y, 0)) {
          case 0:
            break;
          default:
            map[x][y] = new Node(x, y,
                createBodyForSecondLayer(FieldType.NOT_WALL, (int) (x * getNodeSizeInMeter()),
                    (int) (y * getNodeSizeInMeter()), nodeSizeInMeter),
                FieldType.NOT_WALL, tileWidth);
            break;
        }
        switch (tileMap.getTileId(x, y, 1)) {
          case 0:
            break;
          default:
            map[x][y] = new Node(x, y,
                createBodyForSecondLayer(FieldType.DYNAMIC_OBSTACLE,
                    (int) (x * getNodeSizeInMeter()), (int) (y * getNodeSizeInMeter()),
                    nodeSizeInMeter),
                FieldType.DYNAMIC_OBSTACLE, tileWidth);
            break;
        }
        switch (tileMap.getTileId(x, y, 2)) {
          case 0:
            break;
          default:
            map[x][y] =
                new Node(x, y,
                    createBodyForSecondLayer(FieldType.WALL, (int) (x * getNodeSizeInMeter()),
                        (int) (y * getNodeSizeInMeter()), nodeSizeInMeter),
                    FieldType.WALL, tileWidth);
            break;
        }
      }
    }
  }


  /**
   * creates bodies for all dynamic_obstacle elements.
   * 
   * @param type
   * @param x
   * @param y
   * @param NODE_SIZE_BOX2D
   * @return
   */
  private Body createBodyForSecondLayer(FieldType type, int x, int y, final float NODE_SIZE_BOX2D) {
    switch (type) {
      // case WALL:
      // BodyDef bodyDef = new BodyDef();
      //
      // bodyDef.type = BodyType.StaticBody;
      // bodyDef.position.set(x + (NODE_SIZE_BOX2D / 2), y + (NODE_SIZE_BOX2D / 2));
      //
      // Body body = WorldHandler.getB2World().createBody(bodyDef);
      //
      // PolygonShape shape = new PolygonShape();
      // shape.setAsBox(NODE_SIZE_BOX2D / 2, NODE_SIZE_BOX2D / 2);
      //
      // body.createFixture(shape, 1f);
      // return body;

      case DYNAMIC_OBSTACLE:
        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyType.StaticBody;
        bodyDef2.position.set(x + (NODE_SIZE_BOX2D / 2), y + (NODE_SIZE_BOX2D / 2));

        Body body2 = WorldHandler.getB2World().createBody(bodyDef2);

        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(NODE_SIZE_BOX2D / 2, NODE_SIZE_BOX2D / 2);

        body2.createFixture(shape2, 1f);
        return body2;
      default:
        return null;
    }
  }


  /**
   * Helper function, might come in handy later. Returns connected tiles as a region and sets the
   * group ID inside the Nodes.
   * 
   * @param map
   * @return
   */
  private ArrayList<ArrayList<Node>> getConnectedRegions(Node[][] map) {
    ArrayDeque<Node> openNodes = new ArrayDeque<>();
    ArrayDeque<Node> visitedNodes = new ArrayDeque<>();

    ArrayList<ArrayList<Node>> outPut = new ArrayList<>();

    int groupID = 0;
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        Node first = map[x][y];

        if (visitedNodes.contains(first) || first.isWalkable())
          continue;

        groupID++;
        openNodes.push(first);
        visitedNodes.add(first);

        ArrayList<Node> region = new ArrayList<>();
        while (!openNodes.isEmpty()) {
          Node current = openNodes.pop();
          current.setRegionGroup(groupID);
          region.add(current);

          ArrayList<Node> neighbours = new ArrayList<>();

          Node x1 = returnNodeOrNull(current.x - 1, current.y);
          if (x1 != null)
            neighbours.add(x1);

          Node x2 = returnNodeOrNull(current.x + 1, current.y);
          if (x2 != null)
            neighbours.add(x2);

          Node y1 = returnNodeOrNull(current.x, current.y - 1);
          if (y1 != null)
            neighbours.add(y1);

          Node y2 = returnNodeOrNull(current.x, current.y + 1);
          if (y2 != null)
            neighbours.add(y2);

          for (int i = 0; i < neighbours.size(); i++) {
            if (!neighbours.get(i).isWalkable() && !visitedNodes.contains(neighbours.get(i))) {
              openNodes.push(neighbours.get(i));
              visitedNodes.push(neighbours.get(i));
            }
          }
        }
        outPut.add(region);
      }
    }
    return outPut;
  }

  private void removeInaccessibleBodiesFromSecondLayer(Node[][] map) {
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
  }
}
