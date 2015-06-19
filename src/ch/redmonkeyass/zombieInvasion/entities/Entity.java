package ch.redmonkeyass.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.Optional;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.eventhandling.Event;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;

public class Entity {
  public enum EntityStatus {
    LIVING, DEAD, INDESTRUCTIBLE
  }

  private final String ID;
  private EntityStatus entityStatus = EntityStatus.LIVING;

  private ArrayList<Module> modules = new ArrayList<>();

  public Entity(String ID) {
    this.ID = ID;
  }

  public Entity(String ID, EntityStatus entityStatus) {
    this.ID = ID;
    this.entityStatus = entityStatus;
  }

  public void UPDATE_ENTITY() {

    World.getEntityHandler().getEventsFrom(ID).ifPresent(events -> {
      events.parallelStream().filter(event -> event.getEvent().equals(EventType.KILL_ENTITY))
          .findAny().ifPresent(e -> {
        if (entityStatus.equals(EntityStatus.LIVING)) {
          entityStatus = EntityStatus.DEAD;
        }
      });
    });
  }

  public String getID() {
    return ID;
  }

  public EntityStatus getEntityStatus() {
    return entityStatus;
  }

  public Optional<Object> getData(DataType dataType) {
    return modules.parallelStream().filter(m -> m.getData(dataType) != null)
        .map(m -> Optional.ofNullable(m.getData(dataType))).findAny().orElse(Optional.empty());
  }


  public void addModul(Module module) {
    modules.add(module);
  }

  @SuppressWarnings("unchecked")
  public Optional<ArrayList<Event>> getEvents() {
    return Optional.ofNullable(
        World.getEntityHandler().getDataFrom(ID, DataType.EVENTS, ArrayList.class).orElse(null));
  }
}
