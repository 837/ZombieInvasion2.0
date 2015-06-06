package ch.m837.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.Optional;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.module.Module;
import ch.zombieInvasion.Eventhandling.Event;

public class EntityHandler {
  private ArrayList<Entity> entities = new ArrayList<>();

  public void addEntity(Entity entity) {
    entities.add(entity);
  }

  public void addModulToEntity(Module module) {
    entities.parallelStream().filter(entity -> entity.getID().equals(module.getEntityID()))
        .findAny().ifPresent(foundEntity -> foundEntity.addModul(module));
  }

  /**
   * Returns all Events from Entity with matching ID
   * 
   * @param fromID
   * @return ArrayList<Event> all_Events
   */
  public ArrayList<Event> getEventsFrom(String fromID) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getEvents() : new ArrayList<>(0);
  }

  /**
   * Get Data from other Modules
   * 
   * World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION) .ifPresent(positionData
   * -> { Vector2 worldPosition = (Vector2) positionData; //TODO JUST AN EXAMPLE });
   * 
   * @param <T>
   * 
   * @param fromID
   * @param dataType
   * @return Optional<Object>
   */
  public <T> Optional<T> getDataFrom(String fromID, DataType dataType, Class<T> clazz) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    if (entity != null) {
      Optional<Object> data = entity.getData(dataType);
      if (data.isPresent()) {
        if (data.get().getClass().equals(clazz)) {
          return Optional.ofNullable(clazz.cast(data.get()));
        }
      }
    }
    return Optional.empty();
  }

  public void UPDATE_ENTITIES() {
    entities.parallelStream().forEach(entity -> entity.UPDATE_ENTITY());
  }
}
