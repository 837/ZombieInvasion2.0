package ch.redmonkeyass.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.Optional;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.eventhandling.Event;

public class EntityHandler {
  private ArrayList<Entity> entities = new ArrayList<>();

  public void addEntity(Entity entity) {
    entities.add(entity);
  }

  public void addModuleToEntity(Module module) {
    entities.parallelStream()
        .filter(entity -> entity.getID().equals(module.getEntityID()))
        .findAny()
        .ifPresent(foundEntity -> foundEntity.addModule(module));
  }

  public <T> void removeModuleFromEntity(Class<T> concreteModuleClazz,String entityID){
    entities.parallelStream()
        .filter(entity -> entity.getID().equals(entityID))
        .findAny()
        .ifPresent(foundEntity -> foundEntity.removeModule(concreteModuleClazz));
  }

  private void removeDeadEntities() {
    World.getEventDispatcher().getEvents().stream().sequential()
        .filter(event -> event.getReceiverID().equals("ENTITY_HANDLER"))
        .forEach(e -> {
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
   * @return ArrayList<Event> all_Events
   */
  public Optional<ArrayList<Event>> getEventsFrom(String fromID) {
    return entities.parallelStream()
        .filter(e -> e.getID().equals(fromID))
        .findAny()
        .flatMap(Entity::getEvents);
  }

  /**
   * Get Data from other Modules
   *
   * @return Optional<T> of supplied type
   */
  public <T> Optional<T> getDataFrom(String fromID, DataType dataType, Class<T> type) {
    return
        entities.parallelStream()
            .filter(e -> e.getID().equals(fromID))
            .findAny()
            .flatMap(entity1 -> entity1.getData(dataType))
            .map(type::cast);
  }

  public void UPDATE_ENTITYHANDLER() {
    removeDeadEntities();
    entities.stream().forEach(Entity::UPDATE_ENTITY);
  }
}
