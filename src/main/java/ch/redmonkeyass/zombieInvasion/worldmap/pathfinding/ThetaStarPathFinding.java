package ch.redmonkeyass.zombieInvasion.worldmap.pathfinding;

import ch.redmonkeyass.zombieInvasion.util.SortedList;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

import java.util.ArrayList;
import java.util.Optional;

public class ThetaStarPathFinding {

  public ThetaStarPathFinding() {
    // TODO Auto-generated constructor stub
  }

  public static Optional<ArrayList<Node>> findPathWithThetaStar(ArrayList<Node> allNodes,
      Node startNode, Node endNode) {
    SortedList<Node> closedSet = new SortedList<>(); // The set of nodes already evaluated.
    SortedList<Node> openSet = new SortedList<>(); // The set of tentative nodes to be evaluated,
    openSet.add(startNode); // initially containing the start node
    ArrayList<Node> cameFrom = new ArrayList<>(); // The map of navigated nodes.

    ArrayList<Node> solution = null;
    while (!openSet.isEmpty()) {
      Node currentNode = openSet.pop();
      if (currentNode.isSame(endNode)) {
        endNode.setParent(currentNode);
        break;
      }

      //Generate each state node_successor that can come after node_current
     // ArrayList<Node> successors = currentNode.GetSuccessors ();

    }

    return Optional.empty();
  }


}
