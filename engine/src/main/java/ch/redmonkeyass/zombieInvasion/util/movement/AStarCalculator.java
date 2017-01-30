package ch.redmonkeyass.zombieInvasion.util.movement;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.NavigationGrid;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.AStarGridFinder;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.GridFinderOptions;

import java.util.ArrayList;
import java.util.List;

public class AStarCalculator {
  AStarGridFinder<Node> finder = null;
  NavigationGrid<Node> navGrid = null;
  Node[][] worldMap = WorldHandler.getWorldMap().getMap();

  public AStarCalculator() {
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

  public List<Node> calculatePath(Node startNode, Node targetNode) {
    // Calculating a path
    List<Node> path = finder.findPath(startNode, targetNode, navGrid);


    int breakOutCounter = 0;
    while (path == null) {
      breakOutCounter++;
      path = finder.findPath(startNode, targetNode, navGrid);
      if (breakOutCounter >= 2) {
        return new ArrayList<>();
      }
    }
    ArrayList<Node> finishedPath = new ArrayList<>();
    ArrayList<Node> restPath = new ArrayList<>();
    restPath.add(startNode);
    restPath.addAll(path);

    int reachableNodePos = 0;
    int lastNodePos = restPath.size() - 1;
    if (reachableNodePos == lastNodePos) {
      finishedPath.add(restPath.get(reachableNodePos));
    }
    while (reachableNodePos < lastNodePos) {
      Node start = restPath.get(reachableNodePos);
      reachableNodePos++;

      Node n = restPath.stream().skip(reachableNodePos - 1).filter(e -> existsDirectPath(start, e))
          .reduce((a, b) -> b).orElse(null);

      reachableNodePos = restPath.indexOf(n);
      if (reachableNodePos < 0) {
        return finishedPath;
      }
      finishedPath.add(restPath.get(reachableNodePos));
    }
    return finishedPath;
  }

  private boolean existsDirectPath(Node start, Node target) {
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
}
