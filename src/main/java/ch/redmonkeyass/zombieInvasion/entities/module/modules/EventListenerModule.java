package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.eventhandling.Event;

/**
 * EventListenerModule gives an Entity the ability to listen to events.
 * <p>
 * <b>All Events with the IDs:</b><br>
 * GLOBAL,<br>
 * ENTITY_ID,<br>
 * or when the ENTITY_ID starts with the EVENT_ID
 * 
 * @author Matthias
 *
 */
public class EventListenerModule extends Module implements UpdatableModul {
  private ArrayList<Event> events = new ArrayList<>();

  public EventListenerModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    events.clear();// XXX ned sicher ob ich das so möchti.. removes events every update

    events.addAll(WorldHandler.getEventDispatcher().getEvents().parallelStream()
        .filter(event -> event.getReceiverID().equals(getEntityID())
            || event.getReceiverID().equals("GLOBAL")
            || getEntityID().startsWith(event.getReceiverID()))
        .collect(Collectors.toList()));
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    switch (dataType) {
      case EVENTS:
        return Optional.ofNullable(events);
    }
    return Optional.empty();
  }
}
