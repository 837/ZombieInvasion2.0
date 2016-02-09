package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.NavigationGrid;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.AStarGridFinder;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.GridFinderOptions;

/**
 * This module allows selected entities to be moved with a right click on a tile.
 * <p>
 * Used path finding algorithm is <b>AStarGridFinder</b>
 * <p>
 * <b>Needs:</b> <br>
 * DataType.IS_SELECTED,<br>
 * DataType.POSITION,<br>
 * EventType.RIGHT_CLICK<br>
 * 
 * @author Matthias
 *
 */
public class AStarMovementModule extends Module implements UpdatableModul {
  List<Node> pathToEnd = null;
  AStarGridFinder<Node> finder = null;
  NavigationGrid<Node> navGrid = null;
  Node[][] worldMap = WorldHandler.getWorldMap().getMap();

  public AStarMovementModule(String entityID) {
    super(entityID);
    // Creating custom GridFinderOptions. For standard options, hover "GridFinderOptions()".
    GridFinderOptions opt = new GridFinderOptions();
    opt.allowDiagonal = false;
    opt.dontCrossCorners = true;
    opt.diagonalMovementCost = 1.414f;
    opt.isYDown = true;
    // opt.heuristic = new EuclideanDistance();

    finder = new AStarGridFinder<Node>(Node.class, opt);
    navGrid = new NavigationGrid<Node>(worldMap, true);

  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getEventsFrom(getEntityID()).ifPresent(events -> {
      events.parallelStream().forEach(event -> {
        switch (event.getEvent()) {
          case RIGHT_CLICK:
            // Asking the Entity if it is selected
            WorldHandler.getEntityHandler()
                .getDataFrom(getEntityID(), DataType.IS_SELECTED, Boolean.class)
                .ifPresent(isSelected -> {

              if (isSelected) {
                // Getting the MouseClick Position from the RIGHT_CLICK event
                event.getAdditionalInfo(Vector2.class).ifPresent(position -> {

                  logger.trace("ENTITY isSelected: " + navGrid.toString());
                  // Calculating the click position and converting it to B2D coordinates
                  // Vector2 moveToPos =
                  // position.add(WorldHandler.getCamera().getPosition()).scl(Config.PIX2B).cpy();
                  WorldHandler.getEntityHandler()
                      .getDataFrom("MOUSE", DataType.MOUSE_SELECTED_NODE, Node.class)
                      .ifPresent(node -> {

                    // Asking the Entity for its position
                    WorldHandler.getEntityHandler()
                        .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
                        .ifPresent(entityPos -> {

                      Vector2 entityPosition =
                          entityPos.scl(1f / WorldHandler.getWorldMap().getNodeSizeInMeter());
                      // Calculating the GridCells on which the Entity and where to move to
                      Node[][] cells = WorldHandler.getWorldMap().getMap();
                      Node actualPos, goalPos;
                      actualPos = cells[(int) entityPosition.x][(int) entityPosition.y];
                      goalPos = node;


                      // Calculating a path
                      List<Node> path = finder.findPath(actualPos, goalPos, navGrid);



                      // checks if path is null, if yes, try three more times
                      int breakOutCounter = 0;
                      while (path == null) {
                        breakOutCounter++;
                        path = finder.findPath(actualPos, goalPos, navGrid);
                        if (breakOutCounter >= 2) {
                          break;
                        }
                      }
                      // XXX Copying the path to a new List, if you don't do that, some bad magic
                      // happens... can't explain it...
                      if (path != null) {

                        ArrayList<Node> finishedPath = new ArrayList<>();
                        ArrayList<Node> restPath = new ArrayList<>();
                        restPath.add(actualPos);
                        restPath.addAll(path);

                        int reachableNodePos = 0;
                        int lastNodePos = restPath.size() - 1;
                        if (reachableNodePos == lastNodePos) {
                          finishedPath.add(restPath.get(reachableNodePos));
                        }
                        while (reachableNodePos < lastNodePos) {
                          Node start = restPath.get(reachableNodePos);
                          reachableNodePos++;

                          Node n = restPath.stream().skip(reachableNodePos - 1)
                              .filter(e -> existsDirectPath(start, e)).reduce((a, b) -> b)
                              .orElse(null);

                          reachableNodePos = restPath.indexOf(n);
                          finishedPath.add(restPath.get(reachableNodePos));
                        }

                        pathToEnd = new ArrayList<>(finishedPath);
                      } else {
                        pathToEnd = new ArrayList<>();
                        // pathToEnd.add(goalPos);
                      }

                    });
                  });
                });
              }
            });
            break;
        }
      });
    });

  }

  public boolean existsDirectPath(Node start, Node target) {
    int minX = Math.min(target.x, start.x);
    int minY = Math.min(target.y, start.y);
    int maxX = Math.max(target.x, start.x);
    int maxY = Math.max(target.y, start.y);

    for (int i = minX; i < maxX + 1; i++) {
      for (int j = minY; j < maxY + 1; j++) {
        if (!worldMap[i][j].isWalkable()) {
          return false;
        }
      }
    }
    return true;
  }


  @Override
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case MOVE_TO_POS:
        return Optional.ofNullable(pathToEnd);
    }
    return Optional.empty();
  }
}
