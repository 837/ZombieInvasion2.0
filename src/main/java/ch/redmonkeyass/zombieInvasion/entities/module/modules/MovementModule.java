package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;

public class MovementModule extends Module implements UpdatableModul {
  List<GridCell> pathToEnd = null;
  AStarGridFinder<GridCell> finder = null;
  NavigationGrid<GridCell> navGrid = null;

  public MovementModule(String entityID) {
    super(entityID);

    // or create your own pathfinder options:
    GridFinderOptions opt = new GridFinderOptions();
    opt.allowDiagonal = true;
    opt.dontCrossCorners = true;
    finder = new AStarGridFinder<GridCell>(GridCell.class, opt);
    navGrid = new NavigationGrid<GridCell>(World.getWorldMap().getCells(), false);
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID()).ifPresent(events -> {
      events.parallelStream().forEach(event -> {
        switch (event.getEvent()) {
          case RIGHT_CLICK:
            World.getEntityHandler().getDataFrom(getEntityID(), DataType.IS_SELECTED, Boolean.class)
                .ifPresent(isSelected -> {
              if (isSelected) {
                event.getAdditionalInfo(Vector2.class).ifPresent(position -> {
                  Vector2 moveToPos =
                      position.add(World.getCamera().getPosition()).scl(Config.PIX2B).cpy();
                  System.out.println("TargetPos: " + moveToPos);
                  World.getEntityHandler()
                      .getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
                      .ifPresent(pos -> {
                    Vector2 entityPos = pos.cpy();
                    System.out.println("PlayerPos: " + entityPos);


                    // GridCell[][] cells = World.getWorldMap().getCells();
                    //
                    // GridCell actualPos, goalPos;
                    // actualPos = cells[(int) entityPos.x][(int) entityPos.y];
                    // goalPos = cells[(int) moveToPos.x][(int) moveToPos.y];
                    //
                    // System.out.println(actualPos + " " + goalPos);



                    pathToEnd = finder.findPath((int) entityPos.x, (int) entityPos.y,
                        (int) moveToPos.x, (int) moveToPos.y, navGrid);

                    if (pathToEnd != null) {
                      pathToEnd.forEach(p -> System.out.println("Pos: " + new Vector2(p.x, p.y)));
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
