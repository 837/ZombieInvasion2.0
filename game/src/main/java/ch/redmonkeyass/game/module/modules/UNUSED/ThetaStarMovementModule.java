package ch.redmonkeyass.game.module.modules.UNUSED;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.NavigationGrid;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.GridFinderOptions;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.ThetaStarGridFinder;
import com.badlogic.gdx.math.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This module allows selected entities to be moved with a right click on a tile.
 * <p>
 * Used path finding algorithm is <b>ThetaStarGridFinder</b>
 * <p>
 * <b>Needs:</b> <br>
 * DataType.IS_SELECTED,<br>
 * DataType.POSITION,<br>
 * EventType.RIGHT_CLICK<br>
 * 
 * @author Matthias
 *
 */
public class ThetaStarMovementModule extends Module implements UpdatableModul {
  List<Node> pathToEnd = null;
  ThetaStarGridFinder<Node> finder = null;
  NavigationGrid<Node> navGrid = null;

  public ThetaStarMovementModule(String entityID) {
    super(entityID);
    // Creating custom GridFinderOptions. For standard options, hover "GridFinderOptions()".
    GridFinderOptions opt = new GridFinderOptions();
    opt.allowDiagonal = false;
    opt.dontCrossCorners = true;
    opt.isYDown = true;
    opt.diagonalMovementCost = 1.414f;
   //opt.heuristic = new EuclideanDistance();

    finder = new ThetaStarGridFinder<Node>(Node.class, opt);
    navGrid = new NavigationGrid<Node>(WorldHandler.getWorldMap().getMap(), true);
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

                      // FIXME Copying the path to a new List, if you don't do that, some bad magic
                      // happens... can't explain it...
                      if (path != null) {
                        pathToEnd = new ArrayList<>(path);
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

  @Override
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case CALCULATED_PATH:
        return Optional.ofNullable(pathToEnd);
    }
    return Optional.empty();
  }
}
