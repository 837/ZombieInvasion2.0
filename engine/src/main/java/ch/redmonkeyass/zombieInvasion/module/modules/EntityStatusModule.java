package ch.redmonkeyass.zombieInvasion.module.modules;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.UpdatableModul;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Optional;

public class EntityStatusModule extends Module implements UpdatableModul {
  private Entity_Status status = Entity_Status.LIVING;

  public EntityStatusModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  public EntityStatusModule(String entityID, Entity_Status status) {
    super(entityID);
    this.status = status;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getEventsFrom(getEntityID())
        .ifPresent(events -> events.parallelStream()
            .filter(event -> event.getEvent().equals(EventType.KILL_ENTITY)).findAny()
            .ifPresent(e -> status = Entity_Status.DEAD));
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case ENTITY_STATUS:
        return Optional.ofNullable(status);
    }
    return Optional.empty();
  }

  public enum Entity_Status {
    LIVING, DEAD
  }

}
