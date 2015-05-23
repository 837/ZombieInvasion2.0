package ch.m837.zombieInvasion.entities;

import java.util.ArrayList;

import ch.zombieInvasion.Eventhandling.Event;

public class World {
  static private ArrayList<Entity> entities = new ArrayList<>();

  static public ArrayList<Event> getEventsFrom(String fromID) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getEvents() : new ArrayList<>(0);
  }

  static public Object getDataFrom(String fromID, DataType dataType) {
    Entity entity =
        entities.parallelStream().filter(e -> e.getID().equals(fromID)).findAny().orElse(null);
    return entity != null ? entity.getData(dataType) : DataType.ENTITY_NOT_FOUND;
  }

}
