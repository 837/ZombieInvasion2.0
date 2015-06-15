package ch.redmonkeyass.zombieinvasion.entities;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.redmonkeyass.zombieinvasion.World;
import ch.redmonkeyass.zombieinvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieinvasion.entities.module.Module;
import ch.redmonkeyass.zombieinvasion.legacy.eventhandling.Event;
import ch.redmonkeyass.zombieinvasion.legacy.eventhandling.EventType;

public class Entity {
  public enum EntityStatus {
    LIVING, DEAD, INDESTRUCTIBLE
  }

  private final String ID;
  private EntityStatus entityStatus = EntityStatus.LIVING;
  private ArrayList<Event> events = new ArrayList<>();

  private ArrayList<Module> modules = new ArrayList<>();

  public Entity(String ID) {
    this.ID = ID;
  }

  public Entity(String ID, EntityStatus entityStatus) {
    this.ID = ID;
    this.entityStatus = entityStatus;
  }

  public void UPDATE_ENTITY() {
    events.clear();// XXX ned sicher ob ich das so möchti.. removes events every update
    events.addAll(World.getEventDispatcher().getEvents().parallelStream()
        .filter(event -> event.getReceiverID().equals(ID) || event.getReceiverID().equals("GLOBAL"))
        .collect(Collectors.toList()));
    
    
    
    events.parallelStream().filter(event -> event.getEvent().equals(EventType.KILL_ENTITY))
        .findAny().ifPresent(e -> {
          if (entityStatus.equals(EntityStatus.LIVING)) {
            entityStatus = EntityStatus.DEAD;
          }
        });
  }

  public String getID() {
    return ID;
  }

  public EntityStatus getEntityStatus() {
    return entityStatus;
  }

  public Optional<Object> getData(DataType dataType) {
    return modules
        .parallelStream()
        .filter(m -> m.getData(dataType) != null)
        .map(m -> Optional.ofNullable(m.getData(dataType)))
        .findAny()
        .orElse(Optional.empty());
  }

  public ArrayList<Event> getEvents() {
    return events;
  }

  public void receiveEvent(Event event) {
    events.add(event);
  }

  public void addModul(Module module) {
    modules.add(module);
  }
}
