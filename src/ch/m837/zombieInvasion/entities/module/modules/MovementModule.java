package ch.m837.zombieInvasion.entities.module.modules;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.m837.zombieInvasion.Config;
import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.module.Module;
import ch.m837.zombieInvasion.entities.module.UpdatableModul;

public class MovementModule extends Module implements UpdatableModul {
  Vector2 moveToPos = null;

  public MovementModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID()).parallelStream().forEach(event -> {
      switch (event.getEvent()) {
        case RIGHT_CLICK:
          World.getEntityHandler().getDataFrom(getEntityID(), DataType.IS_SELECTED, Boolean.class)
              .ifPresent(isSelected -> {
            if (isSelected) {
              event.getAdditionalInfo(Vector2.class).ifPresent(position -> {
                moveToPos = position.scl(Config.PIX2B).cpy();
                LogManager.getLogger("zombie")
                    .trace("Entity: " + getEntityID() + " moveToPos: " + moveToPos.toString());
              });
            }
          });
          break;
      }
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
