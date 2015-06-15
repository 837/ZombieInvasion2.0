package ch.redmonkeyass.zombieinvasion.entities.module.modules;

import ch.redmonkeyass.zombieinvasion.Config;
import ch.redmonkeyass.zombieinvasion.World;
import ch.redmonkeyass.zombieinvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieinvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieinvasion.legacy.eventhandling.EventType;
import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieinvasion.entities.module.Module;

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
