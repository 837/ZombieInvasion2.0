package ch.redmonkeyass.zombieInvasion.worldmap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.util.Comparables;
import ch.redmonkeyass.zombieInvasion.worldmap.Node.CornerType;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.MapBodyBuilder;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.TiledMap;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.WorldMapLoader;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.WorldMapLoader.MAP_LOADER_TYPE;

public class WorldMap implements RenderableModul {
  private TiledMap tileMap;
  private float nodeSizeInMeter = 0;
  private float mapWidthInMeter = 0;
  private float mapHeightInMeter = 0;

  Array<Body> obstacles = new Array<>();

  private Logger logger = LogManager.getLogger(WorldMap.class);

  public float getNodeSizeInPixel() {
    return nodeSizeInMeter * Config.B2PIX;
  }


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
    return Arrays.copyOf(map, map.length);
  }


  ArrayList<Node> walkableNodes = new ArrayList<>();
  ArrayList<Node> unpassableNodes = new ArrayList<>();


  public ArrayList<Node> getAllWalkableNodes() {
    return walkableNodes;
  }

  public ArrayList<Node> getAllUnpassableNodes() {
    return unpassableNodes;
  }

  public void createWalkableAndUnpassableNodeLists() {
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        if (map[i][j].isWalkable()) {
          walkableNodes.add(map[i][j]);
        } else {
          unpassableNodes.add(map[i][j]);
        }
      }
    }
  }

  public Node getMapNodePos(Vector2 entityPos) {
    entityPos.scl(1f / WorldHandler.getWorldMap().getNodeSizeInMeter());
    return map[(int) entityPos.x][(int) entityPos.y];
  }

  private WorldMapLoader worldMapLoader = null;

  public WorldMapLoader getWorldMapLoader() {
    return worldMapLoader;
  }

  public WorldMap(String tmxFileName) {
    try {
      worldMapLoader = new WorldMapLoader(tmxFileName, MAP_LOADER_TYPE.RANDOM_ROOM_MAP);

      tileMap = worldMapLoader.getFinishedMap();
      // calculates tileWidth/tileHeight/mapSize/nodeSizeInMeter
      int tileHeigth = (int) tileMap.getTileHeight();
      int tileWidth = (int) tileMap.getTileWidth();
      int mapHeight = (int) tileMap.getHeight();
      int mapWidth = (int) tileMap.getWidth();
      setMapHeightInMeter((int) ((mapHeight * tileHeigth) / Config.B2PIX));
      setMapWidthInMeter((int) ((mapWidth * tileWidth) / Config.B2PIX));
      setNodeSizeInMeter((int) (tileWidth / Config.B2PIX));

      // create a nodemap
      map = new Node[mapWidth][mapHeight];

      Node[][] testBoxMap = new Node[mapWidth][mapHeight];

      // create the Nodes and create bodies if the second layer has elements
      createNodeMapAndDynamicObstacleBodies(map, tileWidth, tileMap);

      // for walls
      createNodeMapForBoxBodies(testBoxMap, tileWidth, tileMap);

      // remove inaccessible node bodies
      // removeInaccessibleBodiesFromSecondLayer(map);

      // creates box bodies from shape layer on tilemap //atm unused
      obstacles.addAll(MapBodyBuilder.buildShapes(tileMap));

      // creates areas used for wall calculation
      ArrayList<ArrayList<Node>> areas = getConnectedRegions(testBoxMap);


      // calculates box bodies for walls
      createBoxBodiesForAreas(areas);


      createWalkableAndUnpassableNodeLists();
    } catch (Exception e) {
      logger.error("Error while creating WorldMap.", e);
    }
  }

  boolean first = true;

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    int camX = (int) WorldHandler.getCamera().getPosition().x;
    int camY = (int) WorldHandler.getCamera().getPosition().y;
    int offsetX = (int) (camX % WorldHandler.getWorldMap().getNodeSizeInPixel());
    int offsetY = (int) (camY % WorldHandler.getWorldMap().getNodeSizeInPixel());
    int nodeSize = (int) WorldHandler.getWorldMap().getNodeSizeInPixel();
    worldMapLoader.getFinishedMap().render(camX - offsetX, camY - offsetY, camX / nodeSize,
        camY / nodeSize, Config.WIDTH / nodeSize + 3, Config.HEIGHT / nodeSize + 2);
  }

  /**
   * Destroys all Obstacles bodies
   */
  public void destroyAllObstacles() {
    obstacles.forEach(o -> WorldHandler.getB2World().destroyBody(o));
  }

  /**
   * kinda hacky, so that I don't have to check if x,y is in the array
   * 
   * @param x
   * @param y
   * @return Object or null
   */
  private Object returnObjectOrNull(int x, int y, Object[][] array) {
    Object n = null;
    try {
      n = array[x][y];
    } catch (Exception e) {
      // XXX kinda hacky, so that I don't have to check if x,y is in the array
    }
    return n;
  }

  /**
   * Hate this shitty looking function... creates bodies and nodes for stuff
   * 
   * @param map
   * @param tileWidth
   * @param tileMap
   */
  private void createNodeMapAndDynamicObstacleBodies(Node[][] map, int tileWidth,
      TiledMap tileMap) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        int xN = x;
        int yN = y;
        switch (tileMap.getTileId(x, y, 0)) {
          case 0:
            break;
          default:
            map[xN][yN] = new Node(xN, yN,
                createBodyForSecondLayer(FieldType.NOT_WALL, (int) (xN * getNodeSizeInMeter()),
                    (int) (yN * getNodeSizeInMeter()), getNodeSizeInMeter()),
                FieldType.NOT_WALL, tileWidth);
            // map[xN][yN + 1] = new Node(xN, yN + 1,
            // createBodyForSecondLayer(FieldType.NOT_WALL, (int) ((xN) * getNodeSizeInMeter()),
            // (int) ((yN + 1) * getNodeSizeInMeter()), getNodeSizeInMeter()),
            // FieldType.NOT_WALL, tileWidth / 2);
            // map[xN + 1][yN] = new Node(xN + 1, yN,
            // createBodyForSecondLayer(FieldType.NOT_WALL,
            // (int) ((xN + 1) * getNodeSizeInMeter()), (int) ((yN) * getNodeSizeInMeter()),
            // getNodeSizeInMeter()),
            // FieldType.NOT_WALL, tileWidth / 2);
            // map[xN + 1][yN + 1] = new Node(xN + 1, yN + 1,
            // createBodyForSecondLayer(FieldType.NOT_WALL,
            // (int) ((xN + 1) * getNodeSizeInMeter()),
            // (int) ((yN + 1) * getNodeSizeInMeter()), getNodeSizeInMeter()),
            // FieldType.NOT_WALL, tileWidth / 2);
            break;
        }
        switch (tileMap.getTileId(x, y, 1)) {
          case 0:
            break;
          default:
            map[xN][yN] = new Node(xN, yN,
                createBodyForSecondLayer(FieldType.DYNAMIC_OBSTACLE,
                    (int) (xN * getNodeSizeInMeter()), (int) (yN * getNodeSizeInMeter()),
                    getNodeSizeInMeter()),
                FieldType.DYNAMIC_OBSTACLE, tileWidth);
            // map[xN][yN + 1] = new Node(xN, yN + 1,
            // createBodyForSecondLayer(FieldType.DYNAMIC_OBSTACLE,
            // (int) ((xN) * getNodeSizeInMeter()), (int) ((yN + 1) * getNodeSizeInMeter()),
            // getNodeSizeInMeter()),
            // FieldType.DYNAMIC_OBSTACLE, tileWidth / 2);
            // map[xN + 1][yN] = new Node(xN + 1, yN,
            // createBodyForSecondLayer(FieldType.DYNAMIC_OBSTACLE,
            // (int) ((xN + 1) * getNodeSizeInMeter()), (int) ((yN) * getNodeSizeInMeter()),
            // getNodeSizeInMeter()),
            // FieldType.DYNAMIC_OBSTACLE, tileWidth / 2);
            // map[xN + 1][yN + 1] = new Node(xN + 1, yN + 1,
            // createBodyForSecondLayer(FieldType.DYNAMIC_OBSTACLE,
            // (int) ((xN + 1) * getNodeSizeInMeter()),
            // (int) ((yN + 1) * getNodeSizeInMeter()), getNodeSizeInMeter()),
            // FieldType.DYNAMIC_OBSTACLE, tileWidth / 2);
            break;
        }
        switch (tileMap.getTileId(x, y, 2)) {
          case 0:
            break;
          default:
            map[xN][yN] = new Node(xN, yN,
                createBodyForSecondLayer(FieldType.WALL, (int) (xN * getNodeSizeInMeter()),
                    (int) (yN * getNodeSizeInMeter()), getNodeSizeInMeter()),
                FieldType.WALL, tileWidth);
            // map[xN][yN + 1] = new Node(xN, yN + 1,
            // createBodyForSecondLayer(FieldType.WALL, (int) ((xN) * getNodeSizeInMeter()),
            // (int) ((yN + 1) * getNodeSizeInMeter()), getNodeSizeInMeter()),
            // FieldType.WALL, tileWidth / 2);
            //
            // map[xN + 1][yN] = new Node(xN + 1, yN,
            // createBodyForSecondLayer(FieldType.WALL, (int) ((xN + 1) * getNodeSizeInMeter()),
            // (int) ((yN) * getNodeSizeInMeter()), getNodeSizeInMeter()),
            // FieldType.WALL, tileWidth / 2);
            // map[xN + 1][yN + 1] = new Node(xN + 1, yN + 1,
            // createBodyForSecondLayer(FieldType.WALL, (int) ((xN + 1) * getNodeSizeInMeter()),
            // (int) ((yN + 1) * getNodeSizeInMeter()), getNodeSizeInMeter()),
            // FieldType.WALL, tileWidth / 2);
            break;
        }
      }
    }
  }

  /**
   * used for wall obstacle calculation, 1/4 of the actual nodes, but still correct^^
   * 
   * @param map
   * @param tileWidth
   * @param tileMap
   */
  private void createNodeMapForBoxBodies(Node[][] map, int tileWidth, TiledMap tileMap) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        switch (tileMap.getTileId(x, y, 0)) {
          case 0:
            break;
          default:
            map[x][y] = new Node(x, y, null, FieldType.NOT_WALL, tileWidth);
            break;
        }
        switch (tileMap.getTileId(x, y, 1)) {
          case 0:
            break;
          default:
            map[x][y] = new Node(x, y, null, FieldType.DYNAMIC_OBSTACLE, tileWidth);
            break;
        }
        switch (tileMap.getTileId(x, y, 2)) {
          case 0:
            break;
          default:
            map[x][y] = new Node(x, y, null, FieldType.WALL, tileWidth);

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

          Node x1 = (Node) returnObjectOrNull(current.x - 1, current.y, map);
          if (x1 != null)
            neighbours.add(x1);

          Node x2 = (Node) returnObjectOrNull(current.x + 1, current.y, map);
          if (x2 != null)
            neighbours.add(x2);

          Node y1 = (Node) returnObjectOrNull(current.x, current.y - 1, map);
          if (y1 != null)
            neighbours.add(y1);

          Node y2 = (Node) returnObjectOrNull(current.x, current.y + 1, map);
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

        Node x1 = (Node) returnObjectOrNull(x - 1, y, map);
        if (x1 != null)
          neighbours.add(x1);

        Node x2 = (Node) returnObjectOrNull(x + 1, y, map);
        if (x2 != null)
          neighbours.add(x2);

        Node y1 = (Node) returnObjectOrNull(x, y - 1, map);
        if (y1 != null)
          neighbours.add(y1);

        Node y2 = (Node) returnObjectOrNull(x, y + 1, map);
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

  /**
   * Creates rectangles based on Abi�s algorithm
   * 
   * @param areas
   */
  private void createBoxBodiesForAreas(ArrayList<ArrayList<Node>> areas) {
    for (int a = 0; a < areas.size(); a++) {
      ArrayList<Node> currentArea = areas.get(a);
      currentArea.sort(new Comparables.SortXThenYForNodes());

      ArrayList<NodeContainer> nodes = new ArrayList<>();
      currentArea.forEach(n -> nodes.add(new NodeContainer(n)));

      NodeContainer[][] test = new NodeContainer[nodes.get(nodes.size() - 1).node.x + 1][2000];
      nodes.forEach(nc -> test[nc.node.x][nc.node.y] = nc);

      ArrayList<ArrayList<NodeContainer>> rectangles = new ArrayList<>();
      while (!nodes.stream().filter(nc -> !nc.visited).collect(Collectors.toList()).isEmpty()) {
        NodeContainer currentStartNode = nodes.stream().filter(nc -> !nc.visited).findFirst().get();
        ArrayList<NodeContainer> rectangle1 = new ArrayList<>();
        ArrayList<NodeContainer> rectangle2 = new ArrayList<>();

        rectangle1.add(currentStartNode);
        rectangle2.add(currentStartNode);
        int xStartNode = currentStartNode.node.x;
        int yStartNode = currentStartNode.node.y;
        boolean isConnectedX1 = true;
        boolean isConnectedY1 = true;
        int x1 = xStartNode;
        int y1 = yStartNode;
        int size1 = 0;
        while (isConnectedX1) {
          NodeContainer cnc = (NodeContainer) returnObjectOrNull(x1, y1, test);
          if (cnc != null && !cnc.node.isWalkable()) {
            rectangle1.add(cnc);
            if (!cnc.visited) {
              size1++;
            }
            y1++;
          } else {
            isConnectedX1 = false;
            y1--;
          }
        }
        boolean breakOut1 = false;
        while (isConnectedY1) {
          NodeContainer cnc = (NodeContainer) returnObjectOrNull(x1, y1, test);
          if (cnc != null && !cnc.node.isWalkable()) {
            ArrayList<NodeContainer> tempUpNodes = new ArrayList<>();
            for (int i = y1; i >= yStartNode; i--) {
              NodeContainer cncUp = (NodeContainer) returnObjectOrNull(x1, i, test);
              if (cncUp == null) {
                breakOut1 = true;
                break;
              } else {
                tempUpNodes.add(cncUp);
              }
            }
            if (breakOut1) {
              break;
            } else {
              rectangle1.addAll(tempUpNodes);
            }
            rectangle1.add(cnc);
            if (!cnc.visited) {
              size1++;
            }
            x1++;
          } else {
            isConnectedY1 = false;
          }
        }

        boolean isConnectedX2 = true;
        boolean isConnectedY2 = true;
        int x2 = xStartNode;
        int y2 = yStartNode;
        int size2 = 0;
        while (isConnectedX2) {
          NodeContainer cnc = (NodeContainer) returnObjectOrNull(x2, y2, test);
          if (cnc != null && !cnc.node.isWalkable()) {
            rectangle2.add(cnc);
            if (!cnc.visited) {
              size2++;
            }
            x2++;
          } else {
            isConnectedX2 = false;
            x2--;
          }
        }
        boolean breakOut2 = false;
        while (isConnectedY2) {
          NodeContainer cnc = (NodeContainer) returnObjectOrNull(x2, y2, test);
          if (cnc != null && !cnc.node.isWalkable()) {
            ArrayList<NodeContainer> tempLeftNodes = new ArrayList<>();
            for (int i = x2; i >= xStartNode; i--) {
              NodeContainer cncLeft = (NodeContainer) returnObjectOrNull(i, y2, test);
              if (cncLeft == null) {
                breakOut2 = true;
                break;
              } else {
                tempLeftNodes.add(cncLeft);
              }
            }
            if (breakOut2) {
              break;
            } else {
              rectangle2.addAll(tempLeftNodes);
            }
            rectangle2.add(cnc);
            if (!cnc.visited) {
              size2++;
            }
            y2++;
          } else {
            isConnectedY2 = false;
          }
        }

        if (size1 > size2) {
          rectangle1.forEach(nc -> nc.setVisited(true));
          rectangles.add(rectangle1);
        } else {
          rectangle2.forEach(nc -> nc.setVisited(true));
          rectangles.add(rectangle2);
        }
      }

      // Create for all rectangle the bodies
      rectangles.stream().forEach(r -> {
        NodeContainer firstNC = r.get(0);
        NodeContainer lastNC = r.get(r.size() - 1);

        float width = (lastNC.node.getCornerInMeter(CornerType.BOTTOM_RIGHT).x - firstNC.node.x);
        float height = (lastNC.node.getCornerInMeter(CornerType.BOTTOM_RIGHT).y - firstNC.node.y);


        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyType.StaticBody;

        Body body2 = WorldHandler.getB2World().createBody(bodyDef2);

        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox((width * getNodeSizeInMeter()) / 2, (height * getNodeSizeInMeter()) / 2,
            firstNC.node.getCornerInMeter(CornerType.TOP_LEFT).cpy().add(width / 2, height / 2)
                .scl(getNodeSizeInMeter()),
            0);
        obstacles.add(body2);
        body2.createFixture(shape2, 1f);

      });
      // System.out.println("Bodies: " + WorldHandler.getB2World().getBodyCount());
    }
  }

  public class NodeContainer {
    private final Node node;
    private boolean visited = false;

    public NodeContainer(Node n) {
      node = n;
    }

    public void setVisited(boolean visited) {
      this.visited = visited;
    }
  }
}


