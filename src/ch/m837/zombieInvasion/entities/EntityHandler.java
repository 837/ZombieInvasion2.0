package ch.m837.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.Random;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.modul.Modul;
import ch.zombieInvasion.Eventhandling.Event;
import ch.zombieInvasion.Eventhandling.EventDispatcher;
import ch.zombieInvasion.Eventhandling.EventType;

public class EntityHandler {
  private ArrayList<Entity> entities = new ArrayList<>();

  public void addEntity(Entity entity) {
    entities.add(entity);
  }

  public void addModulToEntity(Modul modul) {
    entities.parallelStream().filter(entity -> entity.getID().equals(modul.getEntityID())).findAny()
        .ifPresent(foundEntity -> foundEntity.addModul(modul));
  }

  public ArrayList<Event> getEventsFrom(String fromID) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getEvents() : new ArrayList<>(0);
  }

  public Object getDataFrom(String fromID, DataType dataType) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getData(dataType) : DataType.ENTITY_NOT_FOUND;
  }

  int i = 0;

  public void UPDATE_ENTITIES() {
    i++;
    entities.parallelStream().forEach(entity -> entity.UPDATE_ENTITY());
    switch (new Random().nextInt(3)) {
      case 0:
        EventDispatcher.createEvent(0, EventType.TESTEVENT, i, entities.get(0).getID(),
            entities.get(1).getID());
        break;
      case 1:
        EventDispatcher.createEvent(0, EventType.TESTEVENT, i, entities.get(2).getID(),
            entities.get(3).getID());
        break;
      case 2:
        EventDispatcher.createEvent(0, EventType.TESTEVENT, i, entities.get(1).getID(),
            entities.get(3).getID());
        break;

    }
  }
}
