package ch.m837.zombieInvasion.entities;

import java.util.ArrayList;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.module.Module;
import ch.zombieInvasion.Eventhandling.Event;

public class EntityHandler {
  private ArrayList<Entity> entities = new ArrayList<>();

  public void addEntity(Entity entity) {
    entities.add(entity);
  }

  public void addModulToEntity(Module module) {
    entities.parallelStream().filter(entity -> entity.getID().equals(module.getEntityID())).findAny()
        .ifPresent(foundEntity -> foundEntity.addModul(module));
  }

  public ArrayList<Event> getEventsFrom(String fromID) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getEvents() : new ArrayList<>(0);
  }

  public Object getDataFrom(String fromID, DataType dataType) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getData(dataType) : DataType.DATA_NOT_FOUND;
  }

  public void UPDATE_ENTITIES() {
    entities.parallelStream().forEach(entity -> entity.UPDATE_ENTITY());
  }
}