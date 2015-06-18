package ch.redmonkeyass.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.entities.Entity.EntityStatus;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.eventhandling.Event;

public class EntityHandler {
  private ArrayList<Entity> entities = new ArrayList<>();

  public void addEntity(Entity entity) {
    entities.add(entity);
  }

  public void addModulToEntity(Module module) {
    entities.parallelStream().filter(entity -> entity.getID().equals(module.getEntityID()))
        .findAny().ifPresent(foundEntity -> foundEntity.addModul(module));
  }

  private void removeDeadEntities() {
    entities.removeAll(entities.parallelStream()
        .filter(entity -> entity.getEntityStatus().equals(EntityStatus.DEAD))
        .collect(Collectors.toList()));
  }

  /**
   * Returns all Entities; Not sure if we need this
   * 
   * @return allEntities
   */
  public ArrayList<Entity> getAllEntities() {
    return entities;
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
   * Special cases: Vector2: returns a copy of vector, not the actual vector.
   * 
   * @param <T>
   * 
   * @param fromID
   * @param dataType
   * @return Optional<Object>
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getDataFrom(String fromID, DataType dataType, Class<T> type) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    if (entity != null) {
      Optional<Object> data = entity.getData(dataType);
      if (data.isPresent()) {
        if (data.get().getClass().equals(type)) {
          if (type.equals(Vector2.class)) {
            return (Optional<T>) Optional.ofNullable(((Vector2) data.get()).cpy());
          }
          return Optional.ofNullable(type.cast(data.get()));
        }
      }
    }
    return Optional.empty();
  }

  public void UPDATE_ENTITIES() {
    removeDeadEntities();
    entities.parallelStream().forEach(entity -> entity.UPDATE_ENTITY());
  }
}