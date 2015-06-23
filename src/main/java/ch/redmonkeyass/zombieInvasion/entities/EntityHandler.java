package ch.redmonkeyass.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.Optional;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.World;
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
    World.getEventDispatcher().getEvents().stream().sequential()
        .filter(event -> event.getReceiverID().equals("ENTITY_HANDLER")).forEach(e -> {
          switch (e.getEvent()) {
            case REMOVE_ENTITY:
              entities.removeIf(entity -> entity.getID().equals(e.getSenderID()));
              break;
          }
        });
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
  public Optional<ArrayList<Event>> getEventsFrom(String fromID) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getEvents() : Optional.empty();
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

  public void UPDATE_ENTITYHANDLER() {
    removeDeadEntities();
    entities.stream().forEach(entity -> entity.UPDATE_ENTITY());
  }
}
