package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;

public class MovementModule extends Module implements UpdatableModul {
  Vector2 moveToPos = null;

  public MovementModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
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
                  moveToPos = position.add(World.getCamera().getPosition()).cpy();
                  LogManager.getLogger("zombie")
                      .trace("Entity: " + getEntityID() + " moveToPos: " + moveToPos.toString());
                });
              }
            });
            break;
        }
      });
    });
  }

  @Override
  public Object getData(DataType dataType) {
    switch (dataType) {
      case MOVE_TO_POS:
        if (moveToPos != null) {
          return moveToPos.cpy();
        }
        break;
    }
    return null;
  }
}
