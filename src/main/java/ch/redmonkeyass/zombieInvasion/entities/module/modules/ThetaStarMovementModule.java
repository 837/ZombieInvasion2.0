package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.GridCell;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.NavigationGrid;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.GridFinderOptions;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.finders.ThetaStarGridFinder;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.heuristics.EuclideanDistance;

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
  List<GridCell> pathToEnd = null;
  ThetaStarGridFinder<GridCell> finder = null;
  NavigationGrid<GridCell> navGrid = null;

  public ThetaStarMovementModule(String entityID) {
    super(entityID);
    // Creating custom GridFinderOptions. For standard options, hover "GridFinderOptions()".
    GridFinderOptions opt = new GridFinderOptions();
    opt.allowDiagonal = true;
    opt.dontCrossCorners = true;
    opt.heuristic = new EuclideanDistance();

    finder = new ThetaStarGridFinder<GridCell>(GridCell.class, opt);
    navGrid = new NavigationGrid<GridCell>(WorldHandler.getWorldMap().getCells(), true);
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
                  Vector2 moveToPos =
                      position.add(WorldHandler.getCamera().getPosition()).scl(Config.PIX2B).cpy();

                  // Asking the Entity for its position
                  WorldHandler.getEntityHandler()
                      .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
                      .ifPresent(entityPos -> {

                    // Calculating the GridCells on which the Entity and where to move to
                    GridCell[][] cells = WorldHandler.getWorldMap().getCells();
                    GridCell actualPos, goalPos;
                    actualPos = cells[(int) entityPos.x][(int) entityPos.y];
                    goalPos = cells[(int) moveToPos.x][(int) moveToPos.y];

                    // Calculating a path
                    List<GridCell> path = finder.findPath(actualPos, goalPos, navGrid);

                    // FIXME Copying the path to a new List, if you don't do that, some bad magic
                    // happens... can't explain it...
                    if (path != null) {
                      pathToEnd = new ArrayList<>(path);
                    }

                    LogManager.getLogger("zombie")
                        .trace("Entity: " + getEntityID() + " moveToPos: " + moveToPos.toString());

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
      case MOVE_TO_POS:
        return Optional.ofNullable(pathToEnd);
    }
    return Optional.empty();
  }
}
